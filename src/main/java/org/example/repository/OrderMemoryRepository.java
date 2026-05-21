package org.example.repository;

import org.example.domain.order.Order;
import java.util.ArrayList;
import java.util.List;

public class OrderMemoryRepository {
    private List<Order> orders = new ArrayList<>();

    public void add(Order order){
        orders.add(order);
    }
    public List<Order> findAllStatus(){
        return orders;
    }
}
