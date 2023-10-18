package com.example.beautystore.model;

public class Categories {
    String Categories_name;
    int Img_categories;

    public Categories(String categories_name, int img_categories) {
        Categories_name = categories_name;
        Img_categories = img_categories;
    }

    public Categories() {
    }

    public String getCategories_name() {
        return Categories_name;
    }

    public void setCategories_name(String categories_name) {
        Categories_name = categories_name;
    }

    public int getImg_categories() {
        return Img_categories;
    }

    public void setImg_categories(int img_categories) {
        Img_categories = img_categories;
    }
}
