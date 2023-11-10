package com.example.beautystore.model;

public class Rating {

    private String product_id;
    private String customer_id;
    private String comment;
    private String startNumber;
    private String create_at;

    public Rating() {
    }

    public Rating(String product_id, String customer_id, String comment, String startNumber, String create_at) {
        this.product_id = product_id;
        this.customer_id = customer_id;
        this.comment = comment;
        this.startNumber = startNumber;
        this.create_at = create_at;
    }



    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStartNumber() {
        return startNumber;
    }

    public void setStartNumber(String startNumber) {
        this.startNumber = startNumber;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }
}
