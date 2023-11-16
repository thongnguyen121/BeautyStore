package com.example.beautystore.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.beautystore.R;
import com.example.beautystore.activity.Activity_Product_Detail;
import com.example.beautystore.activity.Activity_Wish_List;
import com.example.beautystore.model.CartDetail;
import com.example.beautystore.model.Products;
import com.example.beautystore.model.WishList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecyclerView_WishList extends RecyclerView.Adapter<RecyclerView_WishList.WishListViewHolder> {

    private ArrayList<WishList> data;
    private Context context;

    public RecyclerView_WishList(ArrayList<WishList> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public WishListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View wishListView = inflater.inflate(R.layout.layout_item_wish_list, parent, false);
        return new WishListViewHolder(wishListView);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListViewHolder holder, int position) {
        WishList wishList = data.get(position);
        String productId = wishList.getProduct_id();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Products");
        DatabaseReference wishlistRef = firebaseDatabase.getReference().child("WishList");
        databaseReference.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Products products = snapshot.getValue(Products.class);
                holder.tvProductName.setText(products.getProducts_name());
                Glide.with(context).load(products.getImgProducts_1()).into(holder.ivProductImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Activity_Product_Detail.class);
                intent.putExtra("products_id", productId);
                context.startActivity(intent);
            }
        });
        holder.ivAddWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wishlistRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(productId).removeValue();
            }
        });
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
