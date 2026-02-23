package com.learnavro.domain;

import java.io.Serializable;

public class Store implements Serializable {
    private int id;
    private Address address;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", address=" + address +
                '}';
    }
}
