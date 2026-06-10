package org.example.view;

import org.example.controller.OrderController;
import org.example.controller.UserController;
import org.example.domain.item.Item;
import org.example.domain.order.Order;
import org.example.domain.order.OrderStatus;
import org.example.domain.user.User;
import org.example.service.result.OrderError;
import org.example.service.result.OrderResult;

import java.util.List;
import java.util.Scanner;

public class OrderView {

    private Scanner sc =
            new Scanner(System.in);

    private InputReader inputReader = new InputReader(sc);

    private OrderController orderController;
    private UserController userController;

    public OrderView(OrderController orderController,
                     UserController userController){

        this.orderController = orderController;
        this.userController = userController;
    }
    private String getOrderErrorMessage(OrderError error) {
        if (error == OrderError.ITEM_NOT_FOUND) {
            return "상품이 존재하지 않습니다.";
        }

        if (error == OrderError.INSUFFICIENT_BALANCE) {
            return "잔액 부족";
        }

        if (error == OrderError.OUT_OF_STOCK) {
            return "재고 부족";
        }

        return "주문 실패";
    }

    public void start(User user){

        while (true){

            System.out.println("===== 주문 화면 =====");
            System.out.println("1. 정보 확인");
            System.out.println("2. 주문");
            System.out.println("3. 현재 배달 정보 확인");
            System.out.println("4. 금액 충전");
            System.out.println("5. 재고 충돌 테스트");
            System.out.println("6. 로그아웃");


            int choice = inputReader.readMenuChoice(1, 6);


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
                List<Item> items = orderController.showItems();

                System.out.println("===== 상품 목록 =====");
                for (Item item : items) {
                    System.out.println(item.getId() + " / " + item.getName() + " / " + item.getPrice());
                }

                System.out.print("상품 ID 입력: ");

                String itemId =
                        sc.nextLine();

                // 주문 요청
                OrderResult result = orderController.order(user, itemId);
                System.out.println(getOrderErrorMessage(result.getError()));
            }

            // 로그아웃
            else if (choice == 3){
                System.out.println("현재 음식들 배달 상태는 다음과 같습니다. ");
                List<Order> orders = orderController.getorders();
                boolean found = false;
                for (Order order: orders) {
                    if (order.getStatus() != OrderStatus.COMPLETED) {
                        found = true;
                        System.out.println(order.getUserId() + "님이 시키신 " + order.getName() + "의 배달상태: " + order.getStatus());
                    }
                }
                if (!found){
                    System.out.println("현재 배달 중인 음식이 없습니다.");
                }
            }
            else if (choice == 4){
                System.out.println("충전할 금액 입력: ");
                int amount = inputReader.readPositiveInt();
                userController.charge(user, amount);
                System.out.println("충전 후 잔액: " + user.getBalance());
            }
            else if (choice == 5){
                orderController.testStockRace();
            }
            else if (choice == 6){

                System.out.println("로그아웃");

                break;
            }
        }
    }
}
