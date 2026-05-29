package org.example.domain.discount;

public class VipDiscountPolicy
        implements DiscountPolicy {

    @Override
    public int discount(int price) {

        // VIP 10% 할인
        return price/10;
    }
}