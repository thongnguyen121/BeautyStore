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

import com.bumptech.glide.Glide;
import com.example.beautystore.R;
import com.example.beautystore.activity.Activity_Messenger;
import com.example.beautystore.activity.Activity_Product_Detail;
import com.example.beautystore.model.ChatNoti;
import com.example.beautystore.model.Members;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerView_ChatNotification extends RecyclerView.Adapter<RecyclerView_ChatNotification.ChatNotificationViewHolder> {
    private ArrayList<ChatNoti> data;
    private Context context;

    public RecyclerView_ChatNotification(ArrayList<ChatNoti> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatNotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View chatNotiView = inflater.inflate(R.layout.layout_item_chat_notification, parent, false);
        return new ChatNotificationViewHolder(chatNotiView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatNotificationViewHolder holder, int position) {
        ChatNoti chatNoti = data.get(position);
        String consultantId = chatNoti.getConsultant_id();
        DatabaseReference memberRef = FirebaseDatabase.getInstance().getReference().child("Member").child(consultantId);
        memberRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Members members = snapshot.getValue(Members.class);
                holder.tvConsultantName.setText(members.getUsername());
                Glide.with(context.getApplicationContext()).load(members.getProfileImage()).into(holder.ivConsultantImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Activity_Messenger.class);
                intent.putExtra("chatId", chatNoti.getChatId());
                context.startActivity(intent);
                DatabaseReference chatNotiRef = FirebaseDatabase.getInstance().getReference().child("ChatNoti").child(consultantId);
                Map<String, Object> seen = new HashMap<>();
                seen.put("seen","1");
                chatNotiRef.updateChildren(seen);
            }
        });
        holder.tvMessage.setText(chatNoti.getMessage());
        holder.tvDate.setText(chatNoti.getDate());
        if (chatNoti.getSeen().equals("0")){
            holder.chatNotiItem.setBackgroundResource(R.drawable.bg_chat_noti_unseen);
        }
        else {
            holder.chatNotiItem.setBackgroundResource(R.drawable.bg_chat_noti_seen);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ChatNotificationViewHolder extends RecyclerView.ViewHolder{

        TextView tvConsultantName, tvMessage, tvDate;
        ImageView ivConsultantImage;
        LinearLayout chatNotiItem;
        public ChatNotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvConsultantName = itemView.findViewById(R.id.consultantName);
            tvMessage = itemView.findViewById(R.id.consultantMessage);
            tvDate = itemView.findViewById(R.id.consultantMessageDate);
            ivConsultantImage = itemView.findViewById(R.id.ivConsultantImage);
            chatNotiItem = itemView.findViewById(R.id.chatNotiItem);
        }
    }
}
