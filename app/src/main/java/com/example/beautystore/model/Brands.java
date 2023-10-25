package com.example.beautystore.model;

public class Brands {
    String Brands_id,Brands_name;
    String Img_brands;

    public Brands() {
    }

    public Brands(String brands_id, String brands_name, String img_brands) {
        Brands_id = brands_id;
        Brands_name = brands_name;
        Img_brands = img_brands;
    }

    public String getBrands_id() {
        return Brands_id;
    }

    public void setBrands_id(String brands_id) {
        Brands_id = brands_id;
    }

    public String getBrands_name() {
        return Brands_name;
    }

    public void setBrands_name(String brands_name) {
        Brands_name = brands_name;
    }

    public String getImg_brands() {
        return Img_brands;
    }

    public void setImg_brands(String img_brands) {
        Img_brands = img_brands;
    }
}
