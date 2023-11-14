package com.example.beautystore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.beautystore.R;
import com.example.beautystore.adapter.RecyclerView_Rating;
import com.example.beautystore.model.Products;
import com.example.beautystore.model.Rating;
import com.example.beautystore.model.WishList;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_Product_Detail extends AppCompatActivity {

    Boolean isOnWishList;

    String productId = "", imgProduct1,imgProduct2,imgProduct3;

    int productQty =1;

    String UID;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Button btnAddCart, btnBuyNow;
    ImageView ivComment, ivMessenger, ivDecreaseQty, ivIncreaseQty, ivAddWishList, ivBack, ivProductBig, ivProductSmall1, ivProductSmall2, ivProductSmall3;
    TextView tvProductName, tvProductPrice, tvProductQty, tvProductDesc;
    RatingBar rbProductRating, rbUserRating;
    EditText edtComment;
    RecyclerView_Rating ratingAdapter; //Adapter
    private RecyclerView ratingRecyclerView;
    private ArrayList<Rating> ratings;
    private ArrayList<WishList> wishLists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        setScreenElement();

        setAddWishListButton();
        setScreenData();
        changeBigProductImage();
        increaseProductQty();
        decreaseProductQty();
        productId = getIntent().getStringExtra("products_id");
        firebaseDatabase = FirebaseDatabase.getInstance();
        getDataFromFireBase(productId);
        Log.d("TAG", "onCreate: " + productId);

        UID = FirebaseAuth.getInstance().getUid();

        addOrRemoveProductToWishList();

        //intent_getData(productId);

        ivMessenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Activity_Messenger.class);
                intent.putExtra("products_id", productId);
                intent.putExtra("user_id", UID);
                startActivity(intent);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setAddWishListButton(){
        wishLists = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("WishList");
        databaseReference.child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(productId).exists()){
                    Log.d("TAG", "onDataChange: sp co trong wishlist");
                    isOnWishList = true;
                    ivAddWishList.setImageResource(R.drawable.baseline_favorite_24);
                }
                else {
                    isOnWishList = false;
                    ivAddWishList.setImageResource(R.drawable.baseline_favorite_border_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDataFromFireBase(String productId){
        databaseReference = firebaseDatabase.getReference("Products");
        databaseReference.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Products products = snapshot.getValue(Products.class);
                    tvProductName.setText(products.getProducts_name());
                    tvProductDesc.setText(products.getDescription());
                    tvProductPrice.setText(products.getPrice());
                    imgProduct1 = products.getImgProducts_1();
                    imgProduct2 = products.getImgProducts_2();
                    imgProduct3 = products.getImgProducts_3();
                    Glide.with(Activity_Product_Detail.this)
                            .load(products.getImgProducts_1())
                            .into(ivProductBig);
                    Glide.with(Activity_Product_Detail.this)
                            .load(products.getImgProducts_1())
                            .into(ivProductSmall1);
                    Glide.with(Activity_Product_Detail.this)
                            .load(products.getImgProducts_2())
                            .into(ivProductSmall2);
                    Glide.with(Activity_Product_Detail.this)
                            .load(products.getImgProducts_3())
                            .into(ivProductSmall3);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void increaseProductQty(){
        ivIncreaseQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productQty++;
                tvProductQty.setText(String.valueOf(productQty));
            }
        });
    }
    private void decreaseProductQty(){
        ivDecreaseQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productQty <= 1 ){
                    ivDecreaseQty.setEnabled(false);

                }
                productQty--;
                tvProductQty.setText(String.valueOf(productQty));
            }
        });
    }

    private void createRatingsList(){
        ratings.add(new Rating("1", "1", "1", "Dep", "4", "12/12/2023"));
        ratings.add(new Rating("2", "2", "2", "OK", "3", "12/12/2023"));
        ratings.add(new Rating("3", "3", "3", "Tot", "5", "12/12/2023"));
        ratings.add(new Rating("4", "4", "4", "Xau", "4", "12/12/2023"));
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
                Glide.with(Activity_Product_Detail.this).load(products.getImgProducts_3()).into(ivProductSmall3);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    protected void setScreenData(){
        tvProductQty.setText(String.valueOf(productQty));
    }

    protected void changeBigProductImage(){
        ivProductSmall1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ivProductBig.setImageDrawable(ivProductSmall1.getDrawable());
                Glide.with(Activity_Product_Detail.this).load(imgProduct1).into(ivProductBig);
            }
        });

        ivProductSmall2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ivProductBig.setImageDrawable(ivProductSmall2.getDrawable());
                Glide.with(Activity_Product_Detail.this).load(imgProduct2).into(ivProductBig);
            }
        });

        ivProductSmall3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivProductBig.setImageDrawable(ivProductSmall3.getDrawable());
                Glide.with(Activity_Product_Detail.this).load(imgProduct3).into(ivProductBig);
            }
        });
    }

    protected void addOrRemoveProductToWishList(){
        ivAddWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOnWishList){
                    databaseReference = firebaseDatabase.getReference().child("WishList");
                    WishList wishList = new WishList(UID, productId, "");
                    databaseReference.child(UID).child(productId).setValue(wishList).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Activity_Product_Detail.this, "Add Wish List Successfully!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    isOnWishList = true;
                    ivAddWishList.setImageResource(R.drawable.baseline_favorite_24);
                }
                else {
                    isOnWishList = false;
                    ivAddWishList.setImageResource(R.drawable.baseline_favorite_border_24);

                }
            }
        });
    }

    protected void setScreenElement(){

        //Recyclerview:
        ratingRecyclerView = findViewById(R.id.reviewList);
        ratings = new ArrayList<>();
        createRatingsList();
        ratingAdapter = new RecyclerView_Rating(ratings, this);
        ratingRecyclerView.setAdapter(ratingAdapter);
        ratingRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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