package org.example.repository;

import org.example.domain.order.Order;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class OrderRepository {

    private static final String FILE_PATH =
            "data/orders.csv";

    public void save(Order order) {

        try {

            BufferedWriter bw =
                    new BufferedWriter(
                            new FileWriter(FILE_PATH, true)
                    );

            bw.write(order.toCsv());

            bw.newLine();

            bw.close();

        } catch (IOException e) {

            System.out.println("주문 저장 실패");
        }
    }

}