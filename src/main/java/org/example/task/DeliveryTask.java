package org.example.task;

import org.example.domain.order.Order;
import org.example.domain.order.OrderStatus;

public class DeliveryTask implements  Runnable{

    private Order order;

    public DeliveryTask(Order order){
        this.order = order;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);

            order.setStatus(OrderStatus.PREPARING);

            System.out.println(Thread.currentThread().getName() + ": 상품을 준비 중입니다!");

            Thread.sleep(3000);

            order.setStatus(OrderStatus.DELIVERING);

            System.out.println(Thread.currentThread().getName() + ": 상품을 배달 중입니다!");

            Thread.sleep(3000);

            order.setStatus(OrderStatus.COMPLETED);

            System.out.println(Thread.currentThread().getName() + ": 상품 배달이 완료되었습니다!");


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
