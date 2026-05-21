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
**1. main thread에서 배달 상태 확인 가능하게 하기**  

여기서는 어떤 상호작용을 만들어볼까 고민했습니다.  
위에서 배달 상태를 만들었으니 main thread에서 해당 배달 상태를 조회할 수 있는 칸을 만들고자 했습니다.  
> worker thread에서 배달 상태를 바꾼다  
> main thread에서 해당 배달 상태를 조회할 수 있다(바뀔 때마다 main에서도 바뀌는지 보기)  

하지만 문제가 생겼습니다.  
main 쓰레드에서 `orderservice`의 `order()`함수가 끝나면 main은 더이상 참조할 수가 없습니다.  

따라서 `main`과 `worker`스레드가 둘다 참조 가능한 배열을 만들게 되었습니다.  
이에 `repository` 에 `OrderMemoryRepository` 를 만들어 main thread가 참조할 수 있게 하였습니다.  

```java
package org.example.repository;

import org.example.domain.order.Order;
import org.example.domain.order.OrderStatus;

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
```

해당 코드를 통해 주문이 완료되면 `MemoryRepository` 에도 넣어서 main thread도 참조할 수 있도록 하였습니다.  
<img width="441" height="267" alt="Image" src="https://github.com/user-attachments/assets/c6b15264-d027-4f3e-bc6b-0aebf8a2f07f" />  

주문 상태가 `COMPLETED`, 즉 완료 상태를 제외한 현재 배달 중인 모든 상태를 출력하도록 하였습니다.

**2. 충돌하는 재고관리 스레드 실행해보기**  
두번째로 만든 스레드는 상품 재고를 추가하여 스레드를 동시에 실행하게 해보았습니다.  
배달 현환 확인은 쓰여진 값을 읽기만 하기 때문에 충돌할 일이 없어서 충돌이 날 수 있는 스레드를 실행시켰습니다.  

그렇게 하기 위해서 `OrderService` 안에 `testOrder`, `testStockRace` 두 개의 메서드를 생성하였습니다.  

```java
public void testStockRace(){
        Item item = new Item(
                "1", "치킨",18000, 1
        );
        User user1 = new User(
                    "user1", "1234", 100000
        );
        User user2 = new User(
                "user2", "1234", 100000
        );

        OrderTask task1 = new OrderTask(this, user1, item);
        OrderTask task2 = new OrderTask(this, user2, item);

        Thread t1 = new Thread(task1);
        Thread t2 = new Thread(task2);

        t1.start();
        t2.start();

        try{
            t1.join();
            t2.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        System.out.println("최종 재고: " + item.getStock());
    }
```

해당 코드를 `orderService` 에 넣었고 임의로 제품 한개와 서로다른 user 두명을 만들어서 충돌 테스트를 진행해보았습니다.  
하지만 코드를 수행해보니  
<img width="448" height="266" alt="Image" src="https://github.com/user-attachments/assets/85270620-885e-4aaa-9820-a173f327c710" />  

예상과는 달리 충돌은 없었고 재고가 3개에서 1개로 알맞게 줄었습니다.  
여기서 첫번째 스레드가 stock이 0이하인지 확인하고 -1 하는 과정이 너무 짧아서 충돌이 안발생한 것으로 가정하고  
0이하인지 확인 부터 실제로 줄이는 작업 사이에 `sleep(2000)` 을 넣어보았습니다.  
<img width="510" height="316" alt="Image" src="https://github.com/user-attachments/assets/413887cc-229c-42ad-b1b8-597d5157bcb5" />  

그리고 여러번 시도 끝에 원자성이 깨지는 현상을 발견했습니다.  
이는 첫번째 스레드가 변경된 값을 쓰기 전에 두번째 쓰레드가 읽어서 문제가 발생한 것입니다.  
이에 락을 사용해보았습니다.

다음과 같이 `synchronized` 를 이용해서 stock-- 코드를 변경하였습니다.  
```java
public synchronized void decreaseStock(){
        if (stock <= 0){
            throw new RuntimeException("재고 부족!!");
        }
        System.out.println(Thread.currentThread().getName() + " 재고 확인 완료!!");

        try{
            Thread.sleep(1000);
        }catch (Exception e){
            e.getStackTrace();
        }
        stock --;

        System.out.println(Thread.currentThread().getName() + " 감소 후 재고 : " + stock);
    }
```

바꾼 결과 재고확인 부터 감소후 재고까지 첫번째 쓰레드가 완료가 된 후에 두번째 쓰레드가 실행되는 것이 확인되었습니다.  
<img width="346" height="180" alt="Image" src="https://github.com/user-attachments/assets/19b9a6a4-cc61-4be8-a91d-b32da8ea4eb3" />  

이로써 synchronized가 필요한 이유를 깨달았습니다.  
원자성 문제: 서로 다른 스레드가 공유자원을 접근할 때 문제가 발생 -> 락을 걸어서 한번에 하나의 스레드만 실행 가능하도록 함.  

