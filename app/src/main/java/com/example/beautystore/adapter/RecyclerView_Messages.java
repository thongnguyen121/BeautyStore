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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class RecyclerView_Messages extends RecyclerView.Adapter<RecyclerView_Messages.MessageViewHolder> {
    public static  final int MSG_TYPE_LEFT = 0;
    public static  final int MSG_TYPE_RIGHT = 1;

    private Context context;
    private ArrayList<Chat> data;
    FirebaseUser fuser;

    public RecyclerView_Messages(Context context, ArrayList<Chat> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_item_chat_right, parent, false);
            return new MessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_item_chat_left, parent, false);
            return new MessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Chat chat = data.get(position);

        holder.messageContent.setText(chat.getMessage());

//        Glide.with(mContext).load(imageurl).into(holder.profile_image);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (data.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        ImageView messageUserImage;
        TextView messageContent;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageUserImage = itemView.findViewById(R.id.messageProfileImage);
            messageContent = itemView.findViewById(R.id.messageContent);
        }
    }
}
