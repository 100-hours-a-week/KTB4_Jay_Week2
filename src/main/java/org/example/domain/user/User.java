package org.example.domain.user;

public class User {

    private final String id;
    private final String password;

    // 주문 횟수
    private int orderCount;

    // 잔액
    private int balance;

    // 회원가입용 생성자
    public User(String id,
                String password,
                int balance) {

        this.id = id;
        this.password = password;

        // 처음 가입하면 주문횟수는 0
        this.orderCount = 0;

        this.balance = balance;
    }

    // CSV 복원용 생성자
    public User(String id,
                String password,
                int orderCount,
                int balance) {

        this.id = id;
        this.password = password;
        this.orderCount = orderCount;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public int getBalance() {
        return balance;
    }

    // 돈 차감
    public void deductBalance(int price) {

        // 돈 부족하면 예외
        if (balance < price) {
            throw new RuntimeException("잔액 부족");
        }

        // 잔액 차감
        this.balance -= price;
    }
    public void chargeBalance(int amount){
        if (amount <= 0){
            throw new RuntimeException("잘못된 금액입니다.");
        }
        this.balance += amount;
    }

    // 주문횟수 증가
    public void increaseOrderCount() {
        orderCount++;
    }

    // 회원 등급 반환
    public UserGrade getGrade() {

        if (orderCount >= 5) {
            return UserGrade.VIP;
        }

        return UserGrade.BASIC;
    }

    // 기존 유저 값 덮어쓰기
    public void copyFrom(User other) {

        this.orderCount = other.orderCount;
        this.balance = other.balance;
    }

    // csv 저장용 문자열
    public String toCsv() {

        return id + "," +
                password + "," +
                orderCount + "," +
                balance;
    }
}