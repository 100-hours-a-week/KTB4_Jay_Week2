package org.example.view;

import org.example.controller.UserController;
import org.example.domain.user.User;

import java.util.Scanner;

public class UserView {
    private Scanner sc = new Scanner(System.in);
    private UserController userController;
    private OrderView orderView;

    public UserView(UserController userController, OrderView orderView){
        this.userController = userController;
        this.orderView = orderView;
    }
    public void start() {

        while (true) {

            System.out.println("===== 배달 앱 =====");
            System.out.println("1. 회원가입");
            System.out.println("2. 로그인");
            System.out.println("3. 종료");

            int choice = sc.nextInt();
            sc.nextLine();

            // 회원가입 시 유저 컨트롤러의 register 함수를 실행시킨다.
            if (choice == 1) {
                userController.register();
            }
            else if (choice == 2){
                User user = userController.login();

                if (user != null){

                    System.out.println("로그인 성공!!");
                    // 로그인 성공 시에만 다음 주문 orderView의 start 실행
                    orderView.start(user);
                }
            }
            // 종료
            else if (choice == 3) {
                break;
            }
        }
    }
}