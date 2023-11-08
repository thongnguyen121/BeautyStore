package com.example.beautystore.activity;

import static java.security.AccessController.getContext;

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
import com.example.beautystore.adapter.RecyclerViewProducts;
import com.example.beautystore.adapter.RecyclerView_Rating;
import com.example.beautystore.adapter.RecyclerView_search_products;
import com.example.beautystore.model.Products;
import com.example.beautystore.model.Rating;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Activity_Product_Detail extends AppCompatActivity {

    String productId = "", cate_id = "", imgProduct1,imgProduct2,imgProduct3;

    int productQty =1;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private RecyclerViewProducts recyclerViewProducts;
    Button btnAddCart, btnBuyNow;
    ImageView ivComment, ivMessenger, ivDecreaseQty, ivIncreaseQty, ivAddWishList, ivBack, ivProductBig, ivProductSmall1, ivProductSmall2, ivProductSmall3;
    TextView tvProductName, tvProductPrice, tvProductQty, tvProductDesc;
    RatingBar rbProductRating, rbUserRating;
    EditText edtComment;
    RecyclerView_Rating ratingAdapter; //Adapter
    private RecyclerView ratingRecyclerView, rcDSlienquan;
    private ArrayList<Rating> ratings;
    private ArrayList<Products> data_products = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        setScreenElement();
        setScreenData();
        changeBigProductImage();
        increaseProductQty();
        decreaseProductQty();
        reView_products();
        productId = getIntent().getStringExtra("products_id");
        cate_id = getIntent().getStringExtra("categories_id");
        Toast.makeText(this, "cate_id"+cate_id, Toast.LENGTH_SHORT).show();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Products");
        getDataFromFireBase(productId);
        getData_DSLienquan(cate_id);
        Log.d("TAG", "onCreate: " + productId);
        //intent_getData(productId);
        ivMessenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(v.getContext(), Activity_Product_Detail.class);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void getDataFromFireBase(String productId){
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
        databaseReference.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Products products = snapshot.getValue(Products.class);
                    tvProductName.setText(products.getProducts_name());
                    tvProductDesc.setText(products.getDescription());
                    tvProductPrice.setText(decimalFormat.format(Integer.valueOf(products.getPrice().trim()))+ " Đ");
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

        rcDSlienquan = findViewById(R.id.rcDSSlienquan);
    }

    private void reView_products()
    {
        rbUserRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

            }
        });
    }

    private void getData_DSLienquan(String categories_id)
    {
        recyclerViewProducts = new RecyclerViewProducts(this, R.layout.layout_item_products, data_products);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcDSlienquan.setLayoutManager(layoutManager);
        rcDSlienquan.setAdapter(recyclerViewProducts);
        Query queryProducts = databaseReference.orderByChild("categories_id").equalTo(categories_id);
        queryProducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (data_products != null) {
                        data_products.clear();
                    }
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Products products = dataSnapshot.getValue(Products.class);
                        if (!products.getProducts_id().equals(productId))
                        {
                            data_products.add(products);
                        }

                    }
                    recyclerViewProducts.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}