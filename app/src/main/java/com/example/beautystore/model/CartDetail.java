package com.example.beautystore.model;

public class CartDetail {
    private String product_id;
    private String price;
    private String qty;

    public CartDetail() {
    }

    public CartDetail(String product_id, String price, String qty) {
        this.product_id = product_id;
        this.price = price;
        this.qty = qty;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public String setQty(String qty) {
        this.qty = qty;
        return qty;
    }
}
