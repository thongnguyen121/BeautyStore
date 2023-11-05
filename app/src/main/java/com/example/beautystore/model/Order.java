package com.example.beautystore.model;

public class Order {
    private String order_id;
    private String customer_id;
    private String nhanVienDuyet;
    private String nhanVienGiao;
    private String phuongThucThanhToan;
    private String create_at;
    private String total_amount;

    public Order() {
    }

    public Order(String order_id, String customer_id, String nhanVienDuyet, String nhanVienGiao, String phuongThucThanhToan, String create_at, String total_amount) {
        this.order_id = order_id;
        this.customer_id = customer_id;
        this.nhanVienDuyet = nhanVienDuyet;
        this.nhanVienGiao = nhanVienGiao;
        this.phuongThucThanhToan = phuongThucThanhToan;
        this.create_at = create_at;
        this.total_amount = total_amount;
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

    public String getNhanVienDuyet() {
        return nhanVienDuyet;
    }

    public void setNhanVienDuyet(String nhanVienDuyet) {
        this.nhanVienDuyet = nhanVienDuyet;
    }

    public String getNhanVienGiao() {
        return nhanVienGiao;
    }

    public void setNhanVienGiao(String nhanVienGiao) {
        this.nhanVienGiao = nhanVienGiao;
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
}
