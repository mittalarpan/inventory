package com.example.Inventory.models;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.UUID;

public class Order implements Comparable<Order> {

    Date timestamp;
    @Id
    private String orderId = "";
    private String userId = "";
    private String prodId = "";
    private String vendorId = "";
    private int q;

    public Order() {
    }

    public Order(String userId, String prodId, String vendorId, int q, Date timestamp) {
        this.orderId = UUID.randomUUID().toString() ;;
        this.userId = userId;
        this.prodId = prodId;
        this.vendorId = vendorId;
        this.q = q;
        this.timestamp = timestamp;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public int getQ() {
        return q;
    }

    public void setQ(int q) {
        this.q = q;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(Order order) {
        if (getTimestamp() == null || order.getTimestamp() == null) {
            return 0;
        }
        return (getTimestamp().compareTo(order.getTimestamp()));
    }
}
