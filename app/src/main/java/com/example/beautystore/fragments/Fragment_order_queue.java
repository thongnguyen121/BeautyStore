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
import com.example.beautystore.adapter.RecyclerViewOrder_queue;
import com.example.beautystore.adapter.RecyclerViewProgress_order;
import com.example.beautystore.model.OrderStatus;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Fragment_order_queue extends Fragment {

    RecyclerViewOrder_queue recyclerViewOrderQueue;
    RecyclerView rcOrderqueue_admin;
    View view;
    ArrayList<OrderStatus> data_OrderStatus = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    String uid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_order_queue, container, false);

       setControl(view);
       getData_order();

       return view;
    }

    private void setControl(View view) {
        rcOrderqueue_admin = view.findViewById(R.id.rcOrder_admin_queue_fr);
    }
    private void getData_order()
    {
        recyclerViewOrderQueue = new RecyclerViewOrder_queue(requireContext(), R.layout.layout_item_order_queue, data_OrderStatus);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getContext(), 1);
        layoutManager1.setOrientation(RecyclerView.VERTICAL);
        rcOrderqueue_admin.setLayoutManager(layoutManager1);
        rcOrderqueue_admin.setAdapter(recyclerViewOrderQueue);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference orderStatusReference = firebaseDatabase.getReference().child("OrderStatus");
        orderStatusReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (data_OrderStatus != null) {
                    data_OrderStatus.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    OrderStatus orderStatus = dataSnapshot.getValue(OrderStatus.class);
                    if( orderStatus.getStatus().equals("2") || orderStatus.getStatus().equals("3") || orderStatus.getStatus().equals("4") || orderStatus.getStatus().equals("5") )
                    {
                        data_OrderStatus.add(orderStatus);
                    }

                }
                recyclerViewOrderQueue.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}