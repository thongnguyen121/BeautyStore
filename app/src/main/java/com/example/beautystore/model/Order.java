package com.example.beautystore.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private String order_id;
    private String customer_id;
    private String phuongThucThanhToan;
    private String create_at;
    private String total_amount;
    private String address;
    private String phoneNumber;
    private String name;
    private List<CartDetail> items = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Order() {
    }

    public Order(String order_id, String customer_id, String phuongThucThanhToan, String create_at, String total_amount, String address, String phoneNumber, String name, List<CartDetail> items) {
        this.order_id = order_id;
        this.customer_id = customer_id;
        this.phuongThucThanhToan = phuongThucThanhToan;
        this.create_at = create_at;
        this.total_amount = total_amount;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.items = items;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(String phuongThucThanhToan) {
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<CartDetail> getItems() {
        return items;
    }

    public void setItems(List<CartDetail> items) {
        this.items = items;
    }
}
