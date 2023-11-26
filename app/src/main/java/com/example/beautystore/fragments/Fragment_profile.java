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
import com.example.beautystore.adapter.RecyclerView_ChatNotification;
import com.example.beautystore.model.ChatNoti;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Fragment_profile extends Fragment {

    RecyclerView chatNotiRecyclerView;
    RecyclerView_ChatNotification chatNotiAdapter;
    ArrayList<ChatNoti> chatNotis;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        chatNotiRecyclerView = view.findViewById(R.id.rvFragmentNotification);

        chatNotis = new ArrayList<>();
        DatabaseReference chatNotiRef = FirebaseDatabase.getInstance().getReference().child("ChatNoti");
        chatNotiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    chatNotis.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        ChatNoti chatNoti = dataSnapshot.getValue(ChatNoti.class);
                        chatNotis.add(chatNoti);
                        chatNotiAdapter = new RecyclerView_ChatNotification(chatNotis, getContext());
                        chatNotiRecyclerView.setAdapter(chatNotiAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        chatNotiRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        chatNotiRecyclerView.setLayoutManager(linearLayoutManager);
        return view;
    }
}