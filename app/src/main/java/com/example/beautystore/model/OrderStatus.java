package com.example.beautystore.model;

public class OrderStatus {
    private String order_id, status, shipper_id;

    public OrderStatus() {
    }

    public OrderStatus(String order_id, String status, String shipper_id) {
        this.order_id = order_id;
        this.status = status;
        this.shipper_id = shipper_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShipper_id() {
        return shipper_id;
    }

    public void setShipper_id(String shipper_id) {
        this.shipper_id = shipper_id;
    }
}
