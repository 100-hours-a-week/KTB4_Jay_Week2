package org.example.task;

import org.example.domain.item.Item;
import org.example.domain.user.User;
import org.example.service.OrderService;

public class OrderTask implements Runnable{
    private OrderService orderService;
    private User user;
    private Item item;

    public OrderTask(OrderService orderService,
                     User user,
                     Item item){
        this.orderService = orderService;
        this.user = user;
        this.item = item;
    }
    @Override
    public void run() {
        String result = orderService.testOrder(user, item);

        System.out.println(Thread.currentThread().getName() + " : " + result);
    }
}
