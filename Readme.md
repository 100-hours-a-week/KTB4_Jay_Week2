## 주제: 배달 CLI 프로그램에 스레드를 만들어보자

## 목차 
[1. 어떤 스레드를 만들까?](#어떤-스레드를-만들까)
[2. 상호작용하는 쓰레드](#스레드끼리-상호작용-만들기)
---
### 어떤 스레드를 만들까?
> 지난 1주차까지 만든 CLI 프로그램은 
> 1. 회원가입 후 로그인
> 2. 로그인 후 주문
> 
> 이였습니다. 이에 주문 스레드를 만들기로 했습니다.

지금까지는 main thread 안에서 주문을 한 뒤에 `주문 완료`만 출력하였습니다.  

그래서 주문이 완료가 되면 `배달 상태`를 나타낼 수 있는 enum 클래스를 만들었습니다.  

```java
package org.example.domain.order;

public enum OrderStatus {
    ORDERED,
    PREPARING,
    DELIVERING,
    COMPLETED
}
```

해당 상태를 통해서 시간이 지날 수록 상태가 변경되게 해주었습니다.

`org.example` 안에 `task` package를 만들어 `DeliveryTask`를 만들어 주었습니다.  

```java
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

```

이 안에 해당 코드를 넣어서 주문이 완료됨과 동시에 
1. 상품 준비
2. 상품 배달
3. 상품 배달 완료  

를 현재 쓰레드 이름과 함께 출력하였습니다.

<img width="360" height="70" alt="Image" src="https://github.com/user-attachments/assets/ad9ac9c1-e68e-4d7f-a5e4-a079ba5d09c3" />  

먼저 상품을 주문하고 주문에 성공하면 현재 main thread의 이름을 출력하고 `주문 완료!!` 를 출력합니다.  
그리고 주문을 동시에 두개를 하게 되면 각각 3초마다 배달 상태가 변하게 됩니다.  

<img width="418" height="290" alt="Image" src="https://github.com/user-attachments/assets/b9f1f942-0a6d-4ee1-b48a-f62762d04b68" />  

---

### 스레드끼리 상호작용 만들기

여기서는 어떤 상호작용을 만들어볼까 고민했습니다.  
위에서 배달 상태를 만들었으니 main thread에서 해당 배달 상태를 조회할 수 있는 칸을 만들고자 했습니다.  
> worker thread에서 배달 상태를 바꾼다  
> main thread에서 해당 배달 상태를 조회할 수 있다(바뀔 때마다 main에서도 바뀌는지 보기)  

하지만 문제가 생겼습니다.  
main 쓰레드에서 `orderservice`의 `order()`함수가 끝나면 main은 더이상 참조할 수가 없습니다.  

따라서 `main`과 `worker`스레드가 둘다 참조 가능한 배열을 만들게 되었습니다.