package com.example.beautystore.adapter;

import android.content.Context;
import android.util.Log;
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
import com.example.beautystore.model.Order;
import com.example.beautystore.model.OrderStatus;
import com.example.beautystore.model.Products;
import com.example.beautystore.model.Rating;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RecyclerViewRating_admin extends RecyclerView.Adapter<RecyclerViewRating_admin.RatingHolder> {

    private Context context;
    private int resource;
    private ArrayList<Products> data;
    FirebaseDatabase database;
    DatabaseReference databaseReference;


    public RecyclerViewRating_admin(Context context, int resource, ArrayList<Products> data) {
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerViewRating_admin.RatingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardViewItem = (CardView) LayoutInflater.from(context).inflate(viewType, parent, false);
        return new RecyclerViewRating_admin.RatingHolder(cardViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewRating_admin.RatingHolder holder, int position) {
        Products products = data.get(position);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        String products_id = products.getProducts_id();
        if(products.getProducts_name().length() < 13)
        {
            holder.tvProductsname.setText(products.getProducts_name());
        }else {
            holder.tvProductsname.setText(products.getProducts_name().substring(0,12) + "...");
        }
        holder.tvProductsID.setText(products.getProducts_id());
        Glide.with(context).load(products.getImgProducts_1()).into(holder.imgProducts);
        calculateAverageRating(holder, products_id);
    }


    private void calculateAverageRating(RecyclerViewRating_admin.RatingHolder holder, String productId) {
        databaseReference.child("Rating").child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    float totalStars = 0;
                    int totalRatings = 0;

                    for (DataSnapshot ratingSnapshot : snapshot.getChildren()) {
                        Rating rating = ratingSnapshot.getValue(Rating.class);
                        if (rating != null) {
                            totalStars += Float.parseFloat(rating.getStartNumber());
                            totalRatings++;
                        }
                    }
                    if (totalRatings > 0) {
                        float averageRating = totalStars / totalRatings;
                        holder.rbRating.setRating(averageRating);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    @Override
    public int getItemViewType(int position) {
        return resource;
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class RatingHolder extends RecyclerView.ViewHolder {
        ImageView imgProducts;
        TextView tvProductsID, tvProductsname;
        RatingBar rbRating;
        public RatingHolder(@NonNull View itemView) {
            super(itemView);
            imgProducts = itemView.findViewById(R.id.imgProduct_rating_admin);
            tvProductsID = itemView.findViewById(R.id.tv_productsID_rating_admin);
            tvProductsname = itemView.findViewById(R.id.tv_products_name_rating_admin);
            rbRating = itemView.findViewById(R.id.rbRating_admin);
        }
    }
}
