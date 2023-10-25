package com.example.beautystore.model;

public class Products {
    String products_id, products_name, categories_id, brands_id, quantity, price, description, create_at;
    String imgProducts_1, imgProducts_2, imgProducts_3;

    public Products() {
    }

    public Products(String products_id, String products_name, String categories_id, String brands_id, String quantity,
                    String price, String description, String create_at, String imgProducts_1, String imgProduct_2, String imgProduct_3) {
        this.products_id = products_id;
        this.products_name = products_name;
        this.categories_id = categories_id;
        this.brands_id = brands_id;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
        this.create_at = create_at;
        this.imgProducts_1 = imgProducts_1;
        this.imgProducts_2 = imgProduct_2;
        this.imgProducts_3 = imgProduct_3;
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

    public String getCategories_id() {
        return categories_id;
    }

    public void setCategories_id(String categories_id) {
        this.categories_id = categories_id;
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

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getImgProducts_1() {
        return imgProducts_1;
    }

    public void setImgProducts_1(String imgProducts_1) {
        this.imgProducts_1 = imgProducts_1;
    }

    public String getImgProducts_2() {
        return imgProducts_2;
    }

    public void setImgProducts_2(String imgProduct_2) {
        this.imgProducts_2 = imgProduct_2;
    }

    public String getImgProduct_3() {
        return imgProducts_3;
    }

    public void setImgProduct_3(String imgProduct_3) {
        this.imgProducts_3 = imgProduct_3;
    }
}
