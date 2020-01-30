package com.example.Inventory;

import org.springframework.data.annotation.Id;

public class Order {

    @Id
    private String orderId ;
    private String userId ;
    private String prodId ;
    private String vendorId ;
    private int q ;

    public Order() {
    }

    public Order(String orderId, String userId, String prodId, String vendorId, int q) {
        this.orderId = orderId;
        this.userId = userId;
        this.prodId = prodId;
        this.vendorId = vendorId;
        this.q = q;
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
}
