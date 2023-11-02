package com.example.beautystore.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.beautystore.R;
import com.example.beautystore.adapter.ListView_Order;
import com.example.beautystore.model.OrderDetail;

import java.util.ArrayList;

public class Activity_Order extends AppCompatActivity {

    EditText edtUserName, edtAddress, edtPhoneNumber;
    TextView tvTotalPrice;
    Spinner spPaymentMethod;
    Button btnOrder;
    ArrayList<OrderDetail> orderDetails;
    ListView orderListView;
    ListView_Order orderListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


    }
}