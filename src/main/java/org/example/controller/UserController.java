package org.example.controller;

import org.example.domain.user.User;
import org.example.service.UserService;

import java.util.Scanner;

public class UserController {

    private UserService userService;

    private Scanner sc =
            new Scanner(System.in);

    public UserController(UserService userService){

        this.userService = userService;
    }

    // 회원가입
    public void register(){

        System.out.print("아이디 입력: ");
        String id = sc.nextLine();

        System.out.print("비밀번호 입력: ");
        String pw = sc.nextLine();

        // 서비스한테 회원가입 요청
        boolean result =
                userService.register(id, pw);

        if (!result){
            System.out.println("회원가입 실패!!");
        }
    }

    // 로그인
    public User login(){

        System.out.print("아이디 입력: ");
        String id = sc.nextLine();

        System.out.print("비밀번호 입력: ");
        String pw = sc.nextLine();

        User user =
                userService.login(id, pw);

        // 로그인 실패
        if (user == null){

            System.out.println("로그인 실패!!");
        }

        // 성공한 user 반환
        return user;
    }

    public void charge(User user, int amount){
        userService.charge(user, amount);
    }
}