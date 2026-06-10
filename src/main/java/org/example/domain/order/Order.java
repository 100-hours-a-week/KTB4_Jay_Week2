package org.example.domain.order;

import org.example.domain.item.Item;
import org.example.domain.user.User;

import java.util.UUID;

public class Order {

    private final String orderId;

    private final String userId;
    private final String itemId;

    private final int price;
    private final int discountPrice;
    private final int finalPrice;
    private volatile OrderStatus status;
    private final String itemName;

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
        this.itemName = item.getName();
    }

    public OrderStatus getStatus() {
        return status;
    }
    public String getUserId(){
        return userId;
    }
    public String getItemId(){
        return itemId;
    }

    public String getName(){
        return itemName;
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