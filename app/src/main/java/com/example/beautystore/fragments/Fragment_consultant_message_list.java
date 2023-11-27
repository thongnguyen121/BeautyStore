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
import com.example.beautystore.adapter.RecyclerView_Consultant_Message;
import com.example.beautystore.model.ChatGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Fragment_consultant_message_list extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RecyclerView messageRecyclerView;
    RecyclerView_Consultant_Message messageAdapter;
    ArrayList<ChatGroup> chatGroups;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_consultant_message_list, container, false);

        chatGroups = new ArrayList<>();
        messageRecyclerView = view.findViewById(R.id.rvConsultantMessageList);
        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        messageAdapter = new RecyclerView_Consultant_Message(chatGroups, getContext());

        messageRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(false);
        messageRecyclerView.setLayoutManager(linearLayoutManager);
        messageRecyclerView.setAdapter(messageAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("ChatGroup");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatGroups.clear();
                if (dataSnapshot.exists()){

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        ChatGroup chatGroup = snapshot.getValue(ChatGroup.class);
                        chatGroups.add(chatGroup);

                    }
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    return view;
    }
}