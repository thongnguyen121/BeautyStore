package com.example.beautystore.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.example.beautystore.R;
import com.example.beautystore.adapter.RecyclerView_Rating;
import com.example.beautystore.adapter.RecyclerView_WishList;
import com.example.beautystore.model.WishList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class Activity_Wish_List extends AppCompatActivity {

    ImageView ivBackButton;
    RecyclerView wishlistRecyclerView;
    RecyclerView_WishList wishListAdapter;
    ArrayList<WishList> wishLists;
    FirebaseUser fuser;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        setScreenElement();
        fuser = FirebaseAuth.getInstance().getCurrentUser();

    }

    private void setScreenElement(){
        //Recyclerview:
//        wishlistRecyclerView = findViewById(R.id.rvWishList);
        wishLists = new ArrayList<>();
        createWishListList();
        wishListAdapter = new RecyclerView_WishList(wishLists, this);
        wishlistRecyclerView.setAdapter(wishListAdapter);
        wishlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ivBackButton = findViewById(R.id.ivWishListBackButton);
    }

    private void createWishListList(){
        wishLists.add(new WishList("1", "1", "1"));
        wishLists.add(new WishList("2", "2", "2"));
        wishLists.add(new WishList("3", "3", "3"));
        wishLists.add(new WishList("4", "4", "4"));
    }
}