package com.example.beautystore.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beautystore.R;
import com.example.beautystore.activity.Activity_Messenger;
import com.example.beautystore.model.ChatGroup;
import com.example.beautystore.model.Customer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class RecyclerView_Consultant_Message extends RecyclerView.Adapter<RecyclerView_Consultant_Message.ConsultantMessageViewHolder> {
    private ArrayList<ChatGroup> data;
    private Context context;

    public RecyclerView_Consultant_Message(ArrayList<ChatGroup> data, Context context) {
        this.data = data;
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
        ChatGroup chatGroup = data.get(position);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference customerRef = firebaseDatabase.getReference().child("Customer");
        customerRef.child(chatGroup.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Customer customer = snapshot.getValue(Customer.class);
                holder.tvCustomerName.setText(customer.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.tvDateReceived.setText(chatGroup.getDate());
        holder.tvMessageContent.setText(chatGroup.getLatestMessage());
        if (chatGroup.getStatus().equals("1")){
            holder.tvStatus.setText("Chưa tiếp nhận");
            holder.chatGroupItem.setBackgroundResource(R.drawable.bg_consultant_messages_unseen);
        }else if(chatGroup.getStatus().equals("2")) {
            holder.tvStatus.setText("Đang tư vấn");
            holder.chatGroupItem.setBackgroundResource(R.drawable.bg_consultant_messages_seen);
        }else {
            holder.tvStatus.setText("Đã tiếp nhận");
            holder.chatGroupItem.setBackgroundResource(R.drawable.bg_consultant_messages_seen);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Chat group status: 1: not in conversation, 2: in conversation
                Intent intent = new Intent(v.getContext(), Activity_Messenger.class);
                intent.putExtra("chatId", chatGroup.getId());
                context.startActivity(intent);
                chatGroup.setStatus("2");
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ConsultantMessageViewHolder extends RecyclerView.ViewHolder{

        TextView tvCustomerName, tvDateReceived, tvMessageContent, tvStatus;
        ImageView ivButtonGoMessenger;
        LinearLayout chatGroupItem;

        public ConsultantMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView = itemView;
            tvCustomerName = itemView.findViewById(R.id.tvMessageConsultantCustomerName);
            tvDateReceived = itemView.findViewById(R.id.tvMessageConsultantDateReceived);
            tvMessageContent = itemView.findViewById(R.id.tvMessageConsultantMessageContent);
            tvStatus = itemView.findViewById(R.id.tvMessageConsultantReceivedStatus);
            ivButtonGoMessenger = itemView.findViewById(R.id.ivConsultantGoMessenger);
            chatGroupItem = itemView.findViewById(R.id.chatGroupItem);
        }
    }
}
