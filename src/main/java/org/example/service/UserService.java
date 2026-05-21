package org.example.service;

import org.example.domain.user.User;
import org.example.repository.UserRepository;

import java.util.List;

public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    // 회원가입
    public boolean register(String id,
                         String pw) {

        List<User> users =
                userRepository.findAll();

        // 중복 아이디 체크
        for (User user : users) {

            if (user.getId().equals(id)) {

                System.out.println("이미 존재하는 ID");

                return false;
            }
        }

        User user =
                new User(id, pw, 10000);

        userRepository.save(user);

        System.out.println("회원가입 성공");
        return true;
    }

    // 로그인
    public User login(String id,
                      String pw) {

        List<User> users =
                userRepository.findAll();

        for (User user : users) {

            if (user.getId().equals(id)
                    && user.getPassword().equals(pw)) {

                return user;
            }
        }

        return null;
    }
    public void charge(User user, int amount){
        user.chargeBalance(amount);

        userRepository.save(user);
    }
}