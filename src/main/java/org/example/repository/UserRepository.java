package org.example.repository;

import org.example.domain.user.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private static final String FILE_PATH =
            "data/users.csv";

    // 회원 저장
    public void save(User user) {

        List<User> users = findAll();

        boolean found = false;

        // 기존 회원 수정
        for (User u : users) {

            if (u.getId().equals(user.getId())) {

                u.copyFrom(user);

                found = true;
            }
        }

        // 신규 회원
        if (!found) {
            users.add(user);
        }

        saveAll(users);
    }

    // 전체 저장
    public void saveAll(List<User> users) {

        try {

            BufferedWriter bw =
                    new BufferedWriter(
                            new FileWriter(FILE_PATH)
                    );

            for (User user : users) {

                bw.write(user.toCsv());

                bw.newLine();
            }

            bw.close();

        } catch (IOException e) {

            System.out.println("저장 실패");
        }
    }

    // 전체 조회
    public List<User> findAll() {

        List<User> users =
                new ArrayList<>();

        try {

            BufferedReader br =
                    new BufferedReader(
                            new FileReader(FILE_PATH)
                    );

            String line;

            while ((line = br.readLine()) != null) {

                // 빈 줄 스킵
                if (line.isBlank()) continue;

                String[] data =
                        line.split(",");

                User user =
                        new User(
                                data[0],
                                data[1],
                                Integer.parseInt(data[2]),
                                Integer.parseInt(data[3])
                        );

                users.add(user);
            }

            br.close();

        } catch (IOException e) {

            System.out.println("파일 읽기 실패");
        }

        return users;
    }

}