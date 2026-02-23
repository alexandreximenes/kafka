package com.learnavro.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;


public class CoffeeOrder implements Serializable {
    private String id;
    private String name;
    private String nickName;
    private Store store;
    private List<OrderLineItem> orderLineItems;
    private LocalDateTime orderedTime;
    private String status;

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNickName() { return nickName; }
    public void setNickName(String nickName) { this.nickName = nickName; }

    public Store getStore() { return store; }
    public void setStore(Store store) { this.store = store; }

    public List<OrderLineItem> getOrderLineItems() { return orderLineItems; }
    public void setOrderLineItems(List<OrderLineItem> orderLineItems) { this.orderLineItems = orderLineItems; }

    public LocalDateTime getOrderedTime() { return orderedTime; }
    public void setOrderedTime(LocalDateTime orderedTime) { this.orderedTime = orderedTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public static CoffeeOrder builder(int count) {
        Random random = new Random();
        String[] sizes = {"SMALL", "MEDIUM", "LARGE"};
        String randomSize = sizes[ThreadLocalRandom.current().nextInt(sizes.length)];

        CoffeeOrder order = new CoffeeOrder();
        order.setId(UUID.randomUUID().toString());
        order.setName("Alexandre Ximenes " + count);
        order.setNickName("xyymenes");
        order.setOrderedTime(LocalDateTime.now());
        order.setStatus("NEW");

        // Store e Address
        Address address = new Address();
        address.setAddressLine1("1234 Address Line " + random.nextInt());
        address.setCity("Curitiba");
        address.setStateProvince("PR");
        address.setCountry("BR");
        address.setZip("12345");

        Store store = new Store();
        store.setId(205);
        store.setAddress(address);

        order.setStore(store);

        // OrderLineItems
        OrderLineItem item = new OrderLineItem();
        item.setName("Caffe Latte");
        item.setSize(randomSize);
        item.setQuantity(1);
        item.setCost(BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 10)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

        order.setOrderLineItems(new ArrayList<>());
        order.getOrderLineItems().add(item);

        return order;
    }

    @Override
    public String toString() {
        return "CoffeeOrder{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", nickName='" + nickName + '\'' +
                ", store=" + store +
                ", orderLineItems=" + orderLineItems +
                ", orderedTime=" + orderedTime +
                ", status='" + status + '\'' +
                '}';
    }
}