package org.example.view;

import org.example.controller.OrderController;
import org.example.domain.user.User;

import java.util.Scanner;

public class OrderView {

    private Scanner sc =
            new Scanner(System.in);

    private OrderController orderController;

    public OrderView(OrderController orderController){

        this.orderController = orderController;
    }

    public void start(User user){

        while (true){

            System.out.println("===== 주문 화면 =====");
            System.out.println("1. 정보 확인");
            System.out.println("2. 주문");
            System.out.println("3. 로그아웃");

            int choice =
                    sc.nextInt();

            sc.nextLine();

            if (choice == 1){
                System.out.println("===== 내 정보 =====");
                System.out.println("ID: "+ user.getId());
                System.out.println("남은 잔액: "+ user.getBalance());
                System.out.println("주문 횟수: "+ user.getOrderCount());
                System.out.println("회원 등급: "+ user.getGrade());

            }
            // 주문
            if (choice == 2){

                // 상품 목록 출력
                orderController.showItems();

                System.out.print("상품 ID 입력: ");

                String itemId =
                        sc.nextLine();

                // 주문 요청
                String result =
                        orderController.order(user, itemId);

                System.out.println(result);
            }

            // 로그아웃
            else if (choice == 3){

                System.out.println("로그아웃");

                break;
            }
        }
    }
}