package com.example.Inventory.models;

public class ViewReport implements Comparable<ViewReport>{
    private Product product;
    private String vendorName;
    private int qty;
    private String userId;
    private String userName;
    private String timestamp;

    public ViewReport() {
    }

    public ViewReport(Product product, String vendorName, int qty, String userId, String userName, String timestamp) {
        this.product = product;
        this.vendorName = vendorName;
        this.qty = qty;
        this.userId = userId;
        this.userName = userName;
        this.timestamp = timestamp;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public int compareTo(ViewReport viewReport) {
        int currentPrice = viewReport.getProduct().getPrice() ;
        return this.getProduct().getPrice() - currentPrice ;
    }
}
