package org.example.domain.item;

public class Item {

    private final String id;
    private final String name;
    private final int price;
    private int stock;

    public Item(String id,
                String name,
                int price,
                int stock) {

        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStock(){
        return stock;
    }

    public synchronized void decreaseStock(){
        if (stock <= 0){
            throw new RuntimeException("재고 부족!!");
        }
        System.out.println(Thread.currentThread().getName() + " 재고 확인 완료!!");

        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            throw new RuntimeException("재고 감소 중 인터럽트가 발생했습니다.", e);
        }
        stock --;

        System.out.println(Thread.currentThread().getName() + " 감소 후 재고 : " + stock);
    }
}
