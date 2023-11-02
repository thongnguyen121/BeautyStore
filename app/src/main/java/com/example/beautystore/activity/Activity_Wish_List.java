package com.example.beautystore.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import com.example.beautystore.R;
import com.example.beautystore.adapter.RecyclerView_WishList;
import com.example.beautystore.model.WishList;

import java.util.ArrayList;

public class Activity_Wish_List extends AppCompatActivity {

    Button btnBack;
    RecyclerView wishlistRecyclerView;
    RecyclerView_WishList wishListRecyclerView;
    ArrayList<WishList> wishLists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);


    }
}