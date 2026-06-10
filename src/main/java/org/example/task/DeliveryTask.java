package org.example.task;

import org.example.domain.order.Order;
import org.example.domain.order.OrderStatus;
import org.example.view.DeliveryView;

public class DeliveryTask implements Runnable {

    private final Order order;
    private final DeliveryView deliveryView;

    public DeliveryTask(Order order, DeliveryView deliveryView) {
        this.order = order;
        this.deliveryView = deliveryView;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
            order.setStatus(OrderStatus.PREPARING);
            deliveryView.showStatusChanged(OrderStatus.PREPARING);

            Thread.sleep(3000);
            order.setStatus(OrderStatus.DELIVERING);
            deliveryView.showStatusChanged(OrderStatus.DELIVERING);

            Thread.sleep(3000);
            order.setStatus(OrderStatus.COMPLETED);
            deliveryView.showStatusChanged(OrderStatus.COMPLETED);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
