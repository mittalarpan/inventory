package com.example.Inventory;

public class ViewSupply {
    private Product product ;
    private Vendor vendor ;
    int qty ;
    int price ;
    public ViewSupply() {
    }

    public ViewSupply(Product product, Vendor vendor, int qty , int price) {
        this.product = product;
        this.vendor = vendor;
        this.qty = qty;
        this.price=price ;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
