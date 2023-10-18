package com.example.beautystore.model;

public class Products {
    String products_id, products_name, prototypes_id, brands_id, quantity, price, description;
    int imgProducts;

    public Products() {
    }

    public Products(String products_id, String products_name, String prototypes_id, String brands_id, String quantity, String price, String description, int imgProducts) {
        this.products_id = products_id;
        this.products_name = products_name;
        this.prototypes_id = prototypes_id;
        this.brands_id = brands_id;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
        this.imgProducts = imgProducts;
    }

    public String getProducts_id() {
        return products_id;
    }

    public void setProducts_id(String products_id) {
        this.products_id = products_id;
    }

    public String getProducts_name() {
        return products_name;
    }

    public void setProducts_name(String products_name) {
        this.products_name = products_name;
    }

    public String getPrototypes_id() {
        return prototypes_id;
    }

    public void setPrototypes_id(String prototypes_id) {
        this.prototypes_id = prototypes_id;
    }

    public String getBrands_id() {
        return brands_id;
    }

    public void setBrands_id(String brands_id) {
        this.brands_id = brands_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImgProducts() {
        return imgProducts;
    }

    public void setImgProducts(int imgProducts) {
        this.imgProducts = imgProducts;
    }
}
