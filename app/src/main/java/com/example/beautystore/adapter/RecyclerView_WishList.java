package com.example.beautystore.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beautystore.R;
import com.example.beautystore.activity.Activity_Wish_List;
import com.example.beautystore.model.WishList;

import java.util.ArrayList;

public class RecyclerView_WishList extends RecyclerView.Adapter<RecyclerView_WishList.WishListViewHolder> {

    private int resource;
    private ArrayList<WishList> data;
    private Activity_Wish_List context;

    public RecyclerView_WishList(int resource, ArrayList<WishList> data, Activity_Wish_List context) {
        this.resource = resource;
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public WishListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) context.getLayoutInflater().inflate(viewType, parent, false);
        return new WishListViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class WishListViewHolder extends RecyclerView.ViewHolder{

        ImageView ivProductImage;
        TextView tvProductName;
        ImageView ivAddWishList;
        public WishListViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivWishListProductImage);
            tvProductName = itemView.findViewById(R.id.tvWishListProductName);
            ivAddWishList = itemView.findViewById(R.id.ivWishListHeartButton);
        }
    }
}
