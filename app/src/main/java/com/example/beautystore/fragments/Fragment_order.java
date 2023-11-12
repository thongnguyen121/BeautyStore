package com.example.beautystore.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.beautystore.R;
import com.example.beautystore.adapter.RecyclerViewCategories;
import com.example.beautystore.adapter.RecyclerViewOder_Customer;
import com.example.beautystore.model.Categories;
import com.example.beautystore.model.OrderStatus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Fragment_order extends Fragment {

    RecyclerViewOder_Customer recyclerViewOderCustomer;
    RecyclerView rcOrder_customer;
    View view;
    ArrayList<OrderStatus> data_OrderStatus = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order, container, false);
        setControl(view);
        getData_orderList();
        uid = FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        return view;
    }

    private void setControl(View view) {
        rcOrder_customer = view.findViewById(R.id.rcOrder_customer_fr);
    }

    private void getData_orderList() {
        recyclerViewOderCustomer = new RecyclerViewOder_Customer(requireContext(), R.layout.layout_items_orders_customer, data_OrderStatus);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getContext(), 1);
        layoutManager1.setOrientation(RecyclerView.VERTICAL);
        rcOrder_customer.setLayoutManager(layoutManager1);
        rcOrder_customer.setAdapter(recyclerViewOderCustomer);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference orderStatusReference = firebaseDatabase.getReference().child("OrderStatus");
        DatabaseReference orderReference = firebaseDatabase.getReference().child("Order");

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        orderStatusReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (data_OrderStatus != null) {
                    data_OrderStatus.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    OrderStatus orderStatus = dataSnapshot.getValue(OrderStatus.class);

                        if (orderStatus != null && !orderStatus.getStatus().equals("4") && !orderStatus.getStatus().equals("5")) {
                            // Lấy user_id từ bảng Order dựa trên order_id
                            String orderId = orderStatus.getOrder_id();
                            if (orderId != null) {
                                orderReference.child(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot orderSnapshot) {
                                        if (orderSnapshot.exists()) {
                                            String orderUserId = orderSnapshot.child("customer_id").getValue(String.class);

                                            if (orderUserId != null && orderUserId.equals(currentUserId)) {
                                                data_OrderStatus.add(orderStatus);
                                                recyclerViewOderCustomer.notifyDataSetChanged();
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Xử lý lỗi nếu cần
                                    }
                                });
                            }
                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }
}