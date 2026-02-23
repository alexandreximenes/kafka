package com.learnavro.domain;

import java.io.Serializable;

public class OrderLineItem implements Serializable {
    private String name;
    private String size;
    private int quantity;
    private double cost;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }

    @Override
    public String toString() {
        return "OrderLineItem{" +
                "name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", quantity=" + quantity +
                ", cost=" + cost +
                '}';
    }
}
