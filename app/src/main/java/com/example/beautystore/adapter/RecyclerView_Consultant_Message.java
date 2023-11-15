package com.example.beautystore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beautystore.R;
import com.example.beautystore.model.Chat;

import java.util.ArrayList;

public class RecyclerView_Consultant_Message extends RecyclerView.Adapter<RecyclerView_Consultant_Message.ConsultantMessageViewHolder> {
    private ArrayList<Chat> data;
    private Context context;

    public RecyclerView_Consultant_Message(ArrayList<Chat> data, Context context) {
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
        Chat chat = data.get(position);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ConsultantMessageViewHolder extends RecyclerView.ViewHolder{

        TextView tvCustomerName, tvDateReceived, tvMessageContent, tvStatus;
        ImageView ivButtonGoMessenger;

        public ConsultantMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvMessageConsultantCustomerName);
            tvDateReceived = itemView.findViewById(R.id.tvMessageConsultantDateReceived);
            tvMessageContent = itemView.findViewById(R.id.tvMessageConsultantMessageContent);
            tvStatus = itemView.findViewById(R.id.tvMessageConsultantReceivedStatus);
            ivButtonGoMessenger = itemView.findViewById(R.id.ivConsultantGoMessenger);
        }
    }
}
