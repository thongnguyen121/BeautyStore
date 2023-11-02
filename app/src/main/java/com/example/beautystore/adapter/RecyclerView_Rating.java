package com.example.beautystore.adapter;

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
    private int resource;
    private ArrayList<Rating> data;
    private Activity_Product_Detail context;

    public RecyclerView_Rating(int resource, ArrayList<Rating> data, Activity_Product_Detail context) {
        this.resource = resource;
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) context.getLayoutInflater().inflate(viewType,parent, false);
        return new RecyclerView_Rating.RatingViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingViewHolder holder, int position) {

//        Rating rating = data.get(position);
////        Glide.with(context).load(rating.get)
//        holder.ivUserAvatar.setImageResource(R.drawable.abc);
//        holder.tvUserName.setText("Quynh Anh");
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
