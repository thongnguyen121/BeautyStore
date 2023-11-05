package com.example.beautystore.model;

public class CartDetail {
    private String id;
    private String product_id;
    private String cart_id;
    private String qty;

    public CartDetail() {
    }

    public CartDetail(String id, String product_id, String cart_id, String qty) {
        this.id = id;
        this.product_id = product_id;
        this.cart_id = cart_id;
        this.qty = qty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
