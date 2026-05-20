package org.example.domain.order;

import org.example.domain.item.Item;
import org.example.domain.user.User;

import java.util.UUID;

public class Order {

    private String orderId;

    private String userId;
    private String itemId;

    private int price;
    private int discountPrice;
    private int finalPrice;
    private OrderStatus status;

    public Order(User user,
                 Item item,
                 int price,
                 int discountPrice,
                 int finalPrice) {

        // 랜덤 주문번호 생성
        this.orderId = UUID.randomUUID().toString();

        this.userId = user.getId();
        this.itemId = item.getId();

        this.price = price;
        this.discountPrice = discountPrice;
        this.finalPrice = finalPrice;
        this.status = OrderStatus.ORDERED;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status){
        this.status = status;
    }

    public String toCsv() {

        return orderId + "," +
                userId + "," +
                itemId + "," +
                price + "," +
                discountPrice + "," +
                finalPrice;
    }
}