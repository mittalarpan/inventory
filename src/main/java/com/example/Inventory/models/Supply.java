package com.example.Inventory.models;

import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class Supply implements Comparable<Supply> {

    int qty = 0;
    int price = 0;
    Date timestamp = new Date();
    @Id
    private CompositeKey id;

    public Supply(int qty, CompositeKey id, int price, Date timestamp) {
        this.qty = qty;
        this.id = id;
        this.price = price;
        this.timestamp = timestamp;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public CompositeKey getId() {
        return id;
    }

    public void setId(CompositeKey id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public int compareTo(Supply supply) {
        int price = supply.getPrice();
        return this.price - price;
    }

    public int compareDate(Supply supply) {
        if (getTimestamp() == null || supply.getTimestamp() == null) {
            return 0;
        }
        return (getTimestamp().compareTo(supply.getTimestamp()));
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
