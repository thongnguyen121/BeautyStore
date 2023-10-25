package com.example.beautystore.model;

public class Categories {
    String Categories_id,Categories_name;
    String Img_categories;

    public Categories(String categories_id, String categories_name, String img_categories) {
        Categories_id = categories_id;
        Categories_name = categories_name;
        Img_categories = img_categories;
    }

    public Categories() {
    }

    public String getCategories_id() {
        return Categories_id;
    }

    public void setCategories_id(String categories_id) {
        Categories_id = categories_id;
    }

    public String getCategories_name() {
        return Categories_name;
    }

    public void setCategories_name(String categories_name) {
        Categories_name = categories_name;
    }

    public String getImg_categories() {
        return Img_categories;
    }

    public void setImg_categories(String img_categories) {
        Img_categories = img_categories;
    }
}
