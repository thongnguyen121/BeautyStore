package com.example.beautystore.model;

public class WishList {
    private String customer_id;
    private String product_id;
    private String order_id;

    public WishList() {
    }

    public WishList(String customer_id, String product_id, String order_id) {
        this.customer_id = customer_id;
        this.product_id = product_id;
        this.order_id = order_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}
