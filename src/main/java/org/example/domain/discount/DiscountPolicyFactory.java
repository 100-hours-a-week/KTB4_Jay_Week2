package org.example.domain.discount;

import org.example.domain.user.UserGrade;

import java.util.Map;

public class DiscountPolicyFactory {

    private final Map<UserGrade, DiscountPolicy> policies = Map.of(
            UserGrade.BASIC, new BasicDiscountPolicy(),
            UserGrade.VIP, new VipDiscountPolicy()
    );

    public DiscountPolicy getPolicy(UserGrade grade){
        return policies.get(grade);
    }
}
