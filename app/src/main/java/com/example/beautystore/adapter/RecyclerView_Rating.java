package com.example.beautystore.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.beautystore.R;
import com.example.beautystore.activity.Activity_Product_Detail;
import com.example.beautystore.activity.Activity_add_Brands;
import com.example.beautystore.fragments.Fragment_warehouse_list;
import com.example.beautystore.model.Customer;
import com.example.beautystore.model.Rating;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecyclerView_Rating extends RecyclerView.Adapter<RecyclerView_Rating.RatingViewHolder> {
    private ArrayList<Rating> data;
    private Context context;

    private int resource;
    private DatabaseReference databaseReference;

    public RecyclerView_Rating(ArrayList<Rating> data, Context context, int resource) {
        this.data = data;
        this.context = context;
        this.resource = resource;
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
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
        String userid = rating.getCustomer_id();
        loadInformation_user(holder, userid);
        holder.tvComment.setText(rating.getComment());
        holder.tvCreateAt.setText(rating.getCreate_at());
        holder.rbRating.setRating(Float.parseFloat(rating.getStartNumber().trim()));

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopupMenu(v);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void loadInformation_user(RatingViewHolder holder, String user_id)
    {
        databaseReference.child("Customer").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Customer customer = snapshot.getValue(Customer.class);
                    if (customer != null) {
                        holder.tvUserName.setText(customer.getUsername());
                        Glide.with(context).load(customer.getProfileImage()).into(holder.ivUserAvatar);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_function_edit_delete, popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.Edit) {

                } else if (id == R.id.Delete) {

                }


                return true;
            }
        });


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
