package com.example.beautystore.model;

public class Cart {
    private String cart_id;
    private String customer_id;

    public Cart() {
    }

    public Cart(String cart_id, String customer_id) {
        this.cart_id = cart_id;
        this.customer_id = customer_id;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }
}
