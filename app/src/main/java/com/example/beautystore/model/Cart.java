package com.example.beautystore.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private String user_id;
    private List<CartDetail> items = new ArrayList<>();
    private String total;

    public Cart() {
    }

    public Cart(String user_id, List<CartDetail> items, String total) {
        this.user_id = user_id;
        this.items = items;
        this.total = total;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<CartDetail> getItems() {
        return items;
    }

    public void setItems(List<CartDetail> items) {
        this.items = items;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
