package com.example.beautystore.model;

public class OrderStatus {
    private String order_id, status, member_id;

    public OrderStatus() {
    }

    public OrderStatus(String order_id, String status, String member_id) {
        this.order_id = order_id;
        this.status = status;
        this.member_id = member_id;
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

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }
}
