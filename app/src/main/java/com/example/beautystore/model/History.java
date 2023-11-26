package com.example.beautystore.model;

public class History {
    String history_id, status, order_id, shipper_id, create_at;

    public History() {
    }

    public History(String history_id, String status, String order_id, String shipper_id, String create_at) {
        this.history_id = history_id;
        this.status = status;
        this.order_id = order_id;
        this.shipper_id = shipper_id;
        this.create_at = create_at;
    }

    public String getHistory_id() {
        return history_id;
    }

    public void setHistory_id(String history_id) {
        this.history_id = history_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getShipper_id() {
        return shipper_id;
    }

    public void setShipper_id(String shipper_id) {
        this.shipper_id = shipper_id;
    }


    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }
}
