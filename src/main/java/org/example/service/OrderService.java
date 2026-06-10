package org.example.service;

import org.example.domain.discount.DiscountPolicy;
import org.example.domain.discount.DiscountPolicyFactory;
import org.example.domain.item.Item;
import org.example.domain.order.Order;
import org.example.domain.user.User;
import org.example.repository.ItemRepository;
import org.example.repository.OrderMemoryRepository;
import org.example.repository.OrderRepository;
import org.example.repository.UserRepository;
import org.example.task.OrderTask;
import org.example.service.result.OrderError;
import org.example.service.result.OrderResult;

import java.util.List;

public class OrderService {

    private UserRepository userRepository;

    private ItemRepository itemRepository;

    private OrderRepository orderRepository;

    private OrderMemoryRepository orderMemoryRepository;

    private DiscountPolicyFactory discountPolicyFactory;

    private DeliveryService deliveryService;

    public OrderService(UserRepository userRepository,
                        ItemRepository itemRepository,
                        OrderRepository orderRepository,
                        OrderMemoryRepository orderMemoryRepository,
                        DiscountPolicyFactory discountPolicyFactory,
                        DeliveryService deliveryService) {

        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
        this.orderMemoryRepository = orderMemoryRepository;
        this.discountPolicyFactory = discountPolicyFactory;
        this.deliveryService = deliveryService;
    }

    // 주문
    public OrderResult order(User user,
                        String itemId) {
        System.out.println("주문 thread: " + Thread.currentThread().getName());
        Item item = findItem(itemId);

        // 상품 없으면 실패
        if (item == null) {
            return OrderResult.fail(OrderError.ITEM_NOT_FOUND);
        }

        int price = item.getPrice();

        DiscountPolicy policy = discountPolicyFactory.getPolicy(user.getGrade());

        int discount = policy.discount(price);

        int finalPrice = price - discount;

        // 돈 부족
        if (user.getBalance() < finalPrice) {
            return OrderResult.fail(OrderError.INSUFFICIENT_BALANCE);

        }
        try {
            item.decreaseStock();
        } catch (RuntimeException e) {
            return OrderResult.fail(OrderError.OUT_OF_STOCK);
        }

        // 유저 상태 변경
        user.deductBalance(finalPrice);

        user.increaseOrderCount();

        // 유저 저장
        userRepository.save(user);

        // 주문 생성
        Order order =
                new Order(
                        user,
                        item,
                        price,
                        discount,
                        finalPrice
                );

        // 주문 저장
        orderRepository.save(order);
        orderMemoryRepository.add(order);

        deliveryService.startDelivery(order);
        return OrderResult.success(finalPrice, user.getBalance());


    }
    public List<Order> getorders(){
        return orderMemoryRepository.findAllStatus();
    }

    public void waitForDeliveries() {
        deliveryService.waitForDeliveries();
    }

    // 상품 찾기
    private Item findItem(String itemId) {

        List<Item> items =
                itemRepository.findAll();

        for (Item item : items) {

            if (item.getId().equals(itemId)) {

                return item;
            }
        }

        return null;
    }

    public String testOrder ( User user, Item item){
        try {
            item.decreaseStock();

            return user.getId() + ": 주문 성공!!";
        } catch (Exception e) {
            return user.getId() + ": 주문 실패: " + e.getMessage();
        }
    }

    public void testStockRace(){
        Item item = new Item(
                "1", "치킨",18000, 3
        );
        User user1 = new User(
                    "user1", "1234", 100000
        );
        User user2 = new User(
                "user2", "1234", 100000
        );

        OrderTask task1 = new OrderTask(this, user1, item);
        OrderTask task2 = new OrderTask(this, user2, item);

        Thread t1 = new Thread(task1);
        Thread t2 = new Thread(task2);

        t1.start();
        t2.start();

        try{
            t1.join();
            t2.join();
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            return;
        }

        System.out.println("최종 재고: " + item.getStock());
    }
}
