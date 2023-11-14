package com.example.beautystore.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.beautystore.R;
import com.example.beautystore.activity.Activity_Order;
import com.example.beautystore.adapter.RecyclerView_Cart_Detail;
import com.example.beautystore.model.Cart;
import com.example.beautystore.model.CartDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Fragment_cart extends Fragment {

    public static TextView tvTotalMoney;
    Button btnOrder;
    RecyclerView_Cart_Detail cartDetailAdapter;
    private RecyclerView cartDetaiRecyclerView;
    private ArrayList<CartDetail> cartDetails = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Cart");
        setControl(view);
        getDataCart();
        RecyclerView_Cart_Detail.getTotal();
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_Order.class);
                startActivity(intent);
            }
        });
        return view;
    }


    private void getDataCart() {
        cartDetailAdapter = new RecyclerView_Cart_Detail(cartDetails,this, R.layout.layout_item_cart_detail);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        cartDetaiRecyclerView.setLayoutManager(layoutManager);
        cartDetaiRecyclerView.setAdapter(cartDetailAdapter);
        databaseReference.child(FirebaseAuth.getInstance().getUid()).child("items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartDetails.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CartDetail item = dataSnapshot.getValue(CartDetail.class);
                    cartDetails.add(item);
                }
                cartDetailAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void setControl(View view) {
        cartDetaiRecyclerView = view.findViewById(R.id.CartDetailList);
//        createCartDetailsList();
        tvTotalMoney = view.findViewById(R.id.tvCartDetailTotalMoney);
        btnOrder = view.findViewById(R.id.btnCartDetailOrderButton);
    }
}