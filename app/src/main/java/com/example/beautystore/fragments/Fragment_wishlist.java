package com.example.beautystore.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.beautystore.R;
import com.example.beautystore.activity.Activity_Messenger;
import com.example.beautystore.adapter.RecyclerView_Messages;
import com.example.beautystore.adapter.RecyclerView_WishList;
import com.example.beautystore.model.Chat;
import com.example.beautystore.model.WishList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Fragment_wishlist extends Fragment {

    String UID;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    RecyclerView wishlistRecyclerView;
    RecyclerView_WishList wishlistAdapter;
    ArrayList<WishList> wishLists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        wishLists = new ArrayList<>();
        wishlistRecyclerView = view.findViewById(R.id.rvFragmentWishList);
        wishlistAdapter = new RecyclerView_WishList(wishLists, getContext());
        wishlistRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        wishlistRecyclerView.setLayoutManager(linearLayoutManager);
        wishlistRecyclerView.setAdapter(wishlistAdapter);


        databaseReference = FirebaseDatabase.getInstance().getReference("WishList");
        databaseReference.child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                wishLists.clear();
                if (dataSnapshot.exists()){

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        WishList wishList = snapshot.getValue(WishList.class);
                        wishLists.add(wishList);

                    }

                }
                wishlistAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return view;
    }
}