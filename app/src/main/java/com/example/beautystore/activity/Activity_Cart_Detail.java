package com.example.beautystore.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.beautystore.R;
import com.example.beautystore.adapter.RecyclerView_Cart_Detail;
import com.example.beautystore.model.CartDetail;

import java.util.ArrayList;

public class Activity_Cart_Detail extends AppCompatActivity {

    TextView tvTotalMoney;
    Button btnOrder;
    RecyclerView_Cart_Detail cartDetailAdapter;
    private RecyclerView cartDetaiRecyclerView;
    private ArrayList<CartDetail> cartDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_detail);
        setScreenElement();
    }

    private void setScreenElement(){
        //Set RecyclerView
        cartDetaiRecyclerView = findViewById(R.id.CartDetailList);
        cartDetails = new ArrayList<>();
//        createCartDetailsList();
//        cartDetailAdapter = new RecyclerView_Cart_Detail(cartDetails, this);
//        cartDetaiRecyclerView.setAdapter(cartDetailAdapter);
//        cartDetaiRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        tvTotalMoney = findViewById(R.id.tvCartDetailTotalMoney);
        btnOrder = findViewById(R.id.btnCartDetailOrderButton);
    }

    private void createCartDetailsList(){
//        cartDetails.add(new CartDetail("1", "1", "1", "1"));
//        cartDetails.add(new CartDetail("2", "2", "2", "2"));
//        cartDetails.add(new CartDetail("3", "3", "3", "3"));
//        cartDetails.add(new CartDetail("4", "4", "4", "4"));
    }
}