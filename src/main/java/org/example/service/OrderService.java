package org.example.service;

import org.example.domain.discount.BasicDiscountPolicy;
import org.example.domain.discount.DiscountPolicy;
import org.example.domain.discount.VipDiscountPolicy;
import org.example.domain.item.Item;
import org.example.domain.order.Order;
import org.example.domain.user.User;
import org.example.repository.ItemRepository;
import org.example.repository.OrderRepository;
import org.example.repository.UserRepository;
import org.example.task.DeliveryTask;

import java.util.List;

public class OrderService {

    private UserRepository userRepository;

    private ItemRepository itemRepository;

    private OrderRepository orderRepository;

    public OrderService(UserRepository userRepository,
                        ItemRepository itemRepository,
                        OrderRepository orderRepository) {

        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
    }

    // 주문
    public String order(User user,
                        String itemId) {
        System.out.println("주문 thread: " + Thread.currentThread().getName());
        Item item = findItem(itemId);

        // 상품 없으면 실패
        if (item == null) {
            return "상품이 존재하지 않습니다.";
        }

        int price = item.getPrice();

        // 할인정책 선택
        DiscountPolicy policy;

        if (user.getGrade().name().equals("VIP")) {

            policy = new VipDiscountPolicy();

        } else {

            policy = new BasicDiscountPolicy();
        }

        int discount =
                policy.discount(price);

        int finalPrice =
                price - discount;

        // 돈 부족
        if (user.getBalance() < finalPrice) {
            return "잔액 부족";
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

        Runnable task = new DeliveryTask(order);
        Thread thread = new Thread(task);
        thread.start();
        return "주문 완료 / 결제 금액 : " + finalPrice + " 남은 금액: " + user.getBalance();


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
}