package com.example.beautystore.model;

public class Brands {
    String Brands_name;
    int Img_brands;

    public Brands() {
    }

    public Brands(String brands_name, int img_brands) {
        Brands_name = brands_name;
        Img_brands = img_brands;
    }

    public String getBrands_name() {
        return Brands_name;
    }

    public void setBrands_name(String brands_name) {
        Brands_name = brands_name;
    }

    public int getImg_brands() {
        return Img_brands;
    }

    public void setImg_brands(int img_brands) {
        Img_brands = img_brands;
    }
}
