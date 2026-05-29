package org.example.service.result;

public class OrderResult {

    private final boolean success;
    private final OrderError error;
    private final int finalPrice;
    private final int remainingBalance;

    private OrderResult(boolean success,
                        OrderError error,
                        int finalPrice,
                        int remainingBalance) {
        this.success = success;
        this.error = error;
        this.finalPrice = finalPrice;
        this.remainingBalance = remainingBalance;
    }

    public static OrderResult success(int finalPrice,
                                      int remainingBalance) {
        return new OrderResult(true, null, finalPrice, remainingBalance);
    }

    public static OrderResult fail(OrderError error) {
        return new OrderResult(false, error, 0, 0);
    }

    public boolean isSuccess() {
        return success;
    }

    public OrderError getError() {
        return error;
    }

    public int getFinalPrice() {
        return finalPrice;
    }

    public int getRemainingBalance() {
        return remainingBalance;
    }
}