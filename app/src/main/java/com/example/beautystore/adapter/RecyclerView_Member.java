package com.example.beautystore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.beautystore.R;
import com.example.beautystore.model.Members;
import com.example.beautystore.model.OrderStatus;

import java.util.ArrayList;

public class RecyclerView_Member extends RecyclerView.Adapter<RecyclerView_Member.ViewMember>{
    Context context;
    ArrayList<Members> data;

    public RecyclerView_Member(Context context,  ArrayList<Members> data) {
        this.context = context;
        this.data = data;
    }
    public void setFilterList(ArrayList<Members> filterlist) {
        this.data = filterlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewMember onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        CardView cardView = (CardView) LayoutInflater.from(context).inflate(viewType, parent, false);
//        return new ViewMember(cardView);
        LayoutInflater inflater = LayoutInflater.from(context);
        View orderDetailView = inflater.inflate(R.layout.layout_item_member, parent, false);
        return new ViewMember(orderDetailView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewMember holder, int position) {
        Members members = data.get(position);
        Glide.with(context).load(members.getProfileImage()).into(holder.ivMemberAvt);
        holder.tvMemberName.setText(members.getUsername());
        holder.tvMembermail.setText(members.getEmail());
        if (members.getStatus().equals("1")){
            holder.lnBackGround.setBackgroundResource(com.google.android.material.R.color.design_default_color_error);
        }
        else {
            holder.lnBackGround.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public  Members getData(int position){
        return data.get(position);
    }

    public static class ViewMember extends RecyclerView.ViewHolder {
        ImageView ivMemberAvt;
        TextView  tvMemberName, tvMembermail;
        LinearLayout lnBackGround;
        public ViewMember(@NonNull View itemView) {
            super(itemView);
            ivMemberAvt = itemView.findViewById(R.id.ivMemberAvt);
            tvMemberName = itemView.findViewById(R.id.tvMemberName);
            tvMembermail = itemView.findViewById(R.id.tvMemberMail);
            lnBackGround = itemView.findViewById(R.id.lnBackGround);
        }
    }
}
