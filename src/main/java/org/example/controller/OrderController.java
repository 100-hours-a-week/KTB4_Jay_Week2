package org.example.controller;

import org.example.domain.item.Item;
import org.example.domain.order.Order;
import org.example.domain.user.User;
import org.example.service.ItemService;
import org.example.service.OrderService;
import org.example.service.result.OrderResult;

import java.util.List;

public class OrderController {

    private OrderService orderService;

    private ItemService itemService;

    public OrderController(OrderService orderService,
                           ItemService itemService) {

        this.orderService = orderService;
        this.itemService = itemService;
    }

    // 상품 목록 출력
    // 컨트롤러가 서비스를 부르는 단계인데 사실 show items 넣는것도 맞는지는 모르겠다.
    public List<Item> showItems() {

        return itemService.getItems();
    }

    // 주문 요청 -> 여기서 굳이 컨트롤러를 거쳐서 갈 필요가 있을까? 어차피 order 함수를 실행하는건데
    public OrderResult order(User user,
                             String itemId) {

        return orderService.order(user, itemId);
    }

    public List<Order> getorders() {
        return orderService.getorders();
    }

    public void testStockRace(){
        orderService.testStockRace();
    }
}