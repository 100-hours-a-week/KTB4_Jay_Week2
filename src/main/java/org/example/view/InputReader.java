package org.example.view;

import java.util.Scanner;

public class InputReader {

    private final Scanner sc;

    public InputReader(Scanner sc) {
        this.sc = sc;
    }

    public int readMenuChoice(int min, int max) {
        while (true) {
            int input = readInt();

            if (input >= min && input <= max) {
                return input;
            }

            System.out.println(min + "부터 " + max + "까지의 숫자를 입력해주세요.");
        }
    }

    public int readPositiveInt() {
        while (true) {
            int input = readInt();

            if (input > 0) {
                return input;
            }

            System.out.println("0보다 큰 숫자를 입력해주세요.");
        }
    }

    private int readInt() {
        while (true) {
            String input = sc.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("값을 입력해주세요.");
                continue;
            }

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력해주세요.");
            }
        }
    }
}
