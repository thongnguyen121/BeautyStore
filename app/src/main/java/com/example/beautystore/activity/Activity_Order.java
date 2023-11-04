package com.example.beautystore.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.beautystore.R;
import com.example.beautystore.adapter.RecyclerView_Order;
import com.example.beautystore.adapter.RecyclerView_Rating;
import com.example.beautystore.model.OrderDetail;

import java.util.ArrayList;

public class Activity_Order extends AppCompatActivity {

    EditText edtUserName, edtAddress, edtPhoneNumber;
    TextView tvTotalPrice;
    Spinner spPaymentMethod;
    Button btnOrder;
    ArrayList<OrderDetail> orderDetails;
    RecyclerView orderDetailRecyclerView;
    RecyclerView_Order orderDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        setScreenElement();
    }

    private void setScreenElement(){

        //Recyclerview:
        orderDetailRecyclerView = findViewById(R.id.rvOrderDetail);
        orderDetails = new ArrayList<>();
        createOrderDetailList();
        orderDetailAdapter = new RecyclerView_Order(orderDetails, this);
        orderDetailRecyclerView.setAdapter(orderDetailAdapter);
        orderDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        edtUserName = findViewById(R.id.edtOrderUserName);
        edtAddress = findViewById(R.id.edtOrderAddress);
        edtPhoneNumber = findViewById(R.id.edtOrderPhoneNumber);

        tvTotalPrice = findViewById(R.id.tvOrderTotalMoney);
        spPaymentMethod = findViewById(R.id.spOrderPaymentMethod);
        btnOrder = findViewById(R.id.btnOrderOrderButton);
    }

    private void createOrderDetailList(){
        orderDetails.add(new OrderDetail("1", "1", "1", "1"));
        orderDetails.add(new OrderDetail("2", "2", "2", "2"));
        orderDetails.add(new OrderDetail("3", "3", "3", "3"));
        orderDetails.add(new OrderDetail("4", "4", "4", "4"));
    }
}