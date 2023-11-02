package com.example.beautystore.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.beautystore.R;
import com.example.beautystore.adapter.RecyclerView_Cart_Detail;
import com.example.beautystore.model.CartDetail;

import java.util.ArrayList;

public class Activity_Cart_Detail extends AppCompatActivity {

    EditText edtTotalMoney;
    Button btnOrder;
    RecyclerView_Cart_Detail recyclerViewCartDetail;
    private RecyclerView cartDetailList;
    private ArrayList<CartDetail> cartDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_detail);

        recyclerViewCartDetail = new RecyclerView_Cart_Detail(R.layout.activity_cart_detail, cartDetails, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        cartDetailList.setLayoutManager(layoutManager);
        cartDetailList.setAdapter(recyclerViewCartDetail);
    }
}