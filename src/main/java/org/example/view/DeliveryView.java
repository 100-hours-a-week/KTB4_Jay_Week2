package org.example.view;

import org.example.domain.order.OrderStatus;

public class DeliveryView {

    public void showStatusChanged(OrderStatus status) {
        if (status == OrderStatus.PREPARING) {
            System.out.println(Thread.currentThread().getName() + ": 상품을 준비 중입니다!");
        } else if (status == OrderStatus.DELIVERING) {
            System.out.println(Thread.currentThread().getName() + ": 상품을 배달 중입니다!");
        } else if (status == OrderStatus.COMPLETED) {
            System.out.println(Thread.currentThread().getName() + ": 상품 배달이 완료되었습니다!");
        }
    }
}