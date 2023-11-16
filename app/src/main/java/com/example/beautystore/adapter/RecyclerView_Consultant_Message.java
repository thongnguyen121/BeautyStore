package com.example.beautystore.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beautystore.R;
import com.example.beautystore.activity.Activity_Messenger;
import com.example.beautystore.activity.Activity_Product_Detail;
import com.example.beautystore.model.ChatList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecyclerView_Consultant_Message extends RecyclerView.Adapter<RecyclerView_Consultant_Message.ConsultantMessageViewHolder> {
    private ArrayList<String> data;
    private Context context;

    public RecyclerView_Consultant_Message(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ConsultantMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View chatView = inflater.inflate(R.layout.layout_item_message_consultant, parent, false);
        return new ConsultantMessageViewHolder(chatView);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultantMessageViewHolder holder, int position) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference chatListReference = firebaseDatabase.getReference().child("ChatList");
        chatListReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String chatID = snapshot.getKey();
                    holder.tvCustomerName.setText(chatID);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), Activity_Messenger.class);
                            intent.putExtra("chatId", chatID);
                            context.startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public static class ConsultantMessageViewHolder extends RecyclerView.ViewHolder{

        TextView tvCustomerName, tvDateReceived, tvMessageContent, tvStatus;
        ImageView ivButtonGoMessenger;

        public ConsultantMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView = itemView;
            tvCustomerName = itemView.findViewById(R.id.tvMessageConsultantCustomerName);
            tvDateReceived = itemView.findViewById(R.id.tvMessageConsultantDateReceived);
            tvMessageContent = itemView.findViewById(R.id.tvMessageConsultantMessageContent);
            tvStatus = itemView.findViewById(R.id.tvMessageConsultantReceivedStatus);
            ivButtonGoMessenger = itemView.findViewById(R.id.ivConsultantGoMessenger);
        }
    }
}
