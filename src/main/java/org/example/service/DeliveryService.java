package org.example.service;

import org.example.domain.order.Order;
import org.example.task.DeliveryTask;
import org.example.view.DeliveryView;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DeliveryService {

    private final DeliveryView deliveryView;
    private final List<Thread> deliveryThreads = new CopyOnWriteArrayList<>();

    public DeliveryService(DeliveryView deliveryView) {
        this.deliveryView = deliveryView;
    }

    public void startDelivery(Order order) {
        Runnable task = new DeliveryTask(order, deliveryView);
        Thread thread = new Thread(task);
        deliveryThreads.add(thread);
        thread.start();
    }

    public void waitForDeliveries() {
        for (Thread thread : deliveryThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}

