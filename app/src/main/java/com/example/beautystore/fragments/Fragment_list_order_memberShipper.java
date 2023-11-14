package com.example.beautystore.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.beautystore.R;
import com.example.beautystore.adapter.RecyclerViewOrder_queue;
import com.example.beautystore.adapter.RecyclerView_order_shipper;
import com.example.beautystore.model.OrderStatus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Fragment_list_order_memberShipper extends Fragment {
    RecyclerView_order_shipper recyclerViewOrderShipper;
    RecyclerView rcOrderlist_shipper;
    ArrayList<OrderStatus> data_OrderStatus = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    String uid;
    Button btnback;
    View view;
    String member_id = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list_order_member_shipper, container, false);

        setControl(view);
        uid = FirebaseAuth.getInstance().getUid();
        getData_order();
        return view;
    }

    private void setControl(View view) {
        rcOrderlist_shipper =  view.findViewById(R.id.rcOrder_lít_memberShipper);

    }
    private void getData_order()
    {
        recyclerViewOrderShipper = new RecyclerView_order_shipper(requireContext(), R.layout.layout_item_order_being_received, data_OrderStatus);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getContext(), 1);
        layoutManager1.setOrientation(RecyclerView.VERTICAL);
        rcOrderlist_shipper.setLayoutManager(layoutManager1);
        rcOrderlist_shipper.setAdapter(recyclerViewOrderShipper);

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
                    member_id = orderStatus.getMember_id();
                    if(orderStatus.getStatus().equals("3"))
                    {
                        if (member_id.equals(uid))
                        {
                            data_OrderStatus.add(orderStatus);
                        }

                    }

                }
                recyclerViewOrderShipper.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}