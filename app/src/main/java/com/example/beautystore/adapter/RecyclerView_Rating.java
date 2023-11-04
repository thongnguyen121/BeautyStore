package com.example.beautystore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.beautystore.R;
import com.example.beautystore.activity.Activity_Product_Detail;
import com.example.beautystore.model.Rating;

import java.util.ArrayList;

public class RecyclerView_Rating extends RecyclerView.Adapter<RecyclerView_Rating.RatingViewHolder> {
    private ArrayList<Rating> data;
    private Context context;

    public RecyclerView_Rating(ArrayList<Rating> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View ratingView = inflater.inflate(R.layout.layout_item_review, parent, false);
        return new RatingViewHolder(ratingView);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingViewHolder holder, int position) {

        Rating rating = data.get(position);
        Glide.with(context)
                .load(R.drawable.abc)
                .into(holder.ivUserAvatar);
        holder.tvUserName.setText(rating.getCustomer_id());
        holder.tvComment.setText(rating.getComment());
        holder.tvCreateAt.setText(rating.getCreate_at());
        holder.rbRating.setRating(Float.parseFloat(rating.getStartNumber()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class RatingViewHolder extends RecyclerView.ViewHolder{

        ImageView ivUserAvatar;
        TextView tvUserName;
        TextView tvCreateAt;
        TextView tvComment;
        RatingBar rbRating;
        public RatingViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserAvatar = itemView.findViewById(R.id.ivUserAvatar);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvCreateAt = itemView.findViewById(R.id.tvCreateAt);
            tvComment = itemView.findViewById(R.id.tvUserComment);
            rbRating = itemView.findViewById(R.id.rbUserRating);
        }

    }
}
