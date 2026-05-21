package org.example.repository;

import org.example.domain.item.Item;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ItemRepository {

    private static final String FILE_PATH =
            "data/items.csv";

    public List<Item> findAll() {

        List<Item> items =
                new ArrayList<>();

        try {

            BufferedReader br =
                    new BufferedReader(
                            new FileReader(FILE_PATH)
                    );

            String line;

            while ((line = br.readLine()) != null) {

                String[] data =
                        line.split(",");

                Item item =
                        new Item(
                                data[0],
                                data[1],
                                Integer.parseInt(data[2]),
                                Integer.parseInt(data[3])
                        );

                items.add(item);
            }

            br.close();

        } catch (IOException e) {

            System.out.println("상품 읽기 실패");
        }

        return items;
    }
}