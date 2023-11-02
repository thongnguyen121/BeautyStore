package com.example.beautystore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.beautystore.R;
import com.example.beautystore.adapter.RecyclerView_Rating;
import com.example.beautystore.model.Products;
import com.example.beautystore.model.Rating;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_Product_Detail extends AppCompatActivity {

    String productId = "";

    Button btnAddCart, btnBuyNow;
    ImageView ivComment, ivMessenger, ivDecreaseQty, ivIncreaseQty, ivAddWishList, ivBack, ivProductBig, ivProductSmall1, ivProductSmall2, ivProductSmall3;
    TextView tvProductName, tvProductPrice, tvProductQty, tvProductDesc;
    RatingBar rbProductRating, rbUserRating;
    EditText edtComment;
//    RecyclerView_Rating recyclerViewRating;
//    private RecyclerView ratingList;
//    private ArrayList<Rating> ratings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        //productId = getIntent().getStringExtra("products_id");

        setScreenElement();

        //changeBigProductImage();

        //intent_getData(productId);

        ivMessenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(v.getContext(), Activity_Product_Detail.class);
            }
        });

    }

    private void intent_getData(String products_id) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Products");
        databaseReference.child(products_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Products products = snapshot.getValue(Products.class);
                tvProductName.setText(products.getProducts_name());
                tvProductQty.setText("1");
                tvProductPrice.setText(products.getPrice());
                tvProductDesc.setText(products.getDescription());
                Glide.with(Activity_Product_Detail.this).load(products.getImgProducts_1()).into(ivProductBig);
                Glide.with(Activity_Product_Detail.this).load(products.getImgProducts_1()).into(ivProductSmall1);
                Glide.with(Activity_Product_Detail.this).load(products.getImgProducts_2()).into(ivProductSmall2);
                Glide.with(Activity_Product_Detail.this).load(products.getImgProduct_3()).into(ivProductSmall3);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    protected void setScreenData(){
//        ivProductBig.setImageDrawable();
//    }

    protected void changeBigProductImage(){
        ivProductSmall1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivProductBig.setImageDrawable(ivProductSmall1.getDrawable());
            }
        });

        ivProductSmall2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivProductBig.setImageDrawable(ivProductSmall2.getDrawable());
            }
        });

        ivProductSmall3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivProductBig.setImageDrawable(ivProductSmall3.getDrawable());
            }
        });
    }

    protected void addOrRemoveProductToWishList(){

    }

    protected void setScreenElement(){
//        recyclerViewRating = new RecyclerView_Rating( R.layout.activity_product_detail, ratings, this);
//        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
//        layoutManager.setOrientation(RecyclerView.VERTICAL);
//        ratingList.setLayoutManager(layoutManager);
//        ratingList.setAdapter(recyclerViewRating);

        ivBack = findViewById(R.id.ivProductDetailBack);
        ivProductBig = findViewById(R.id.ivProductDetailBigProduct);
        ivProductSmall1 = findViewById(R.id.ivProductDetailSmallProduct1);
        ivProductSmall2 = findViewById(R.id.ivProductDetailSmallProduct2);
        ivProductSmall3 = findViewById(R.id.ivProductDetailSmallProduct3);

        tvProductName = findViewById(R.id.tvProductDetailProductName);
        ivAddWishList = findViewById(R.id.ivProductDetailAddWishList);

        tvProductPrice = findViewById(R.id.tvProductDetailProductPrice);
        ivDecreaseQty = findViewById(R.id.ivProductDetailDecreaseQty);
        tvProductQty = findViewById(R.id.tvProductDetailProductQty);
        ivIncreaseQty = findViewById(R.id.ivProductDetailIncreaseQty);

        rbProductRating = findViewById(R.id.rbProductDetailProductRating);

        tvProductDesc = findViewById(R.id.tvProductDetailProductDesc);

        btnAddCart = findViewById(R.id.btnProductDetailAddCart);
        btnBuyNow = findViewById(R.id.btnProductDetailBuyNow);

        rbUserRating = findViewById(R.id.rbProductDetailUserRating);

        ivComment = findViewById(R.id.ivProductDetailAddComment);
        edtComment = findViewById(R.id.edtProductDetailComment);

        ivMessenger = findViewById(R.id.ivProductDetailMessenger);
    }
}