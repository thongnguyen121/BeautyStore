package com.example.beautystore.model;

public class OrderDetail {
    private String id;
    private String order_id;
    private String product_id;
    private String qty;

    public OrderDetail() {
    }

    public OrderDetail(String id, String order_id, String product_id, String qty) {
        this.id = id;
        this.order_id = order_id;
        this.product_id = product_id;
        this.qty = qty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
