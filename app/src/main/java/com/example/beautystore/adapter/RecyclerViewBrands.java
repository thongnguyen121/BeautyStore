package com.example.beautystore.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.beautystore.R;
import com.example.beautystore.fragments.Fragment_home;
import com.example.beautystore.model.Brands;
import com.example.beautystore.model.Categories;
import com.example.beautystore.model.Products;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class RecyclerViewBrands extends RecyclerView.Adapter<RecyclerViewBrands.ViewBrands> {
    private Fragment_home context;
    private int resource;
    private ArrayList<Brands> data;
    public static String id = "";
    public  static String brand_id = "";
    public static int selectedPosition_brands = -1;
    public static  String categories_id_brands = "";
    public RecyclerViewBrands(Fragment_home context, int resource, ArrayList<Brands> data) {
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerViewBrands.ViewBrands onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) context.getLayoutInflater().
                inflate(viewType, parent, false);
        return new RecyclerViewBrands.ViewBrands(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewBrands.ViewBrands holder, int position) {
        Brands brands = data.get(position);
        holder.tvBrands_name.setText(brands.getBrands_name());
        Glide.with(context).load(brands.getImg_brands()).into(holder.imgBrands);

        if (position == selectedPosition_brands) {

            holder.tvBrands_name.setTextColor(ContextCompat.getColor(context.getContext(), R.color.pink));

            holder.cardView.setStrokeColor(ContextCompat.getColor(context.getContext(), R.color.pink));

//            RecyclerViewCategories.selectedPosition_cate = -1;


        } else  {
            if (RecyclerViewCategories.brands_id_cate.equals(brand_id))
            {
                holder.tvBrands_name.setTextColor(ContextCompat.getColor(context.getContext(), R.color.pink));

                holder.cardView.setStrokeColor(ContextCompat.getColor(context.getContext(), R.color.pink));
            }
            holder.tvBrands_name.setTextColor(ContextCompat.getColor(context.getContext(), R.color.gray));
            holder.cardView.setStrokeColor(Color.TRANSPARENT);
//            holder.tvCategories_name.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = brands.getBrands_id();
                int previousSelectedPosition = selectedPosition_brands;
                selectedPosition_brands = holder.getAdapterPosition();
                RecyclerViewCategories.selectedPosition_cate = -1;

                // Thông báo Adapter cập nhật lại giao diện để vẽ lại màu nền và màu chữ
                notifyItemChanged(previousSelectedPosition);
                notifyItemChanged(selectedPosition_brands);
                notifyItemChanged(selectedPosition_brands);

                filterProductsAndCategories_BransID(id);

            }
        });
    }

    private void filterProductsAndCategories_BransID(String id) {
        DatabaseReference databaseReferenceProducts = FirebaseDatabase.getInstance().getReference("Products");
        DatabaseReference databaseReferenceCategories = FirebaseDatabase.getInstance().getReference("Categories");

        Query queryProducts = databaseReferenceProducts.orderByChild("brands_id").equalTo(id);

        queryProducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Fragment_home.data_products.clear();
                Fragment_home.data_categories.clear();
                HashSet<String> categoryIds = new HashSet<>(); // lưu trữ ID loại sản phẩm

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Products products = dataSnapshot.getValue(Products.class);
                    String id_categories = products.getCategories_id();

                    // lay id cua categories khi loc prodcucts theo hang
                    categories_id_brands =  products.getCategories_id();
                    //lay brand_id khi loc products theo hang
                    brand_id = products.getBrands_id();
                    Fragment_home.data_products.add(products);

                    categoryIds.add(id_categories); // Thêm ID loại sản phẩm vào HashSet
                }

                // Lặp qua danh sách duy nhất các ID loại sản phẩm
                for (String categoryId : categoryIds) {
                    databaseReferenceCategories.child(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Categories categories = dataSnapshot.getValue(Categories.class);
                            if (categories != null) {
                                Fragment_home.data_categories.add(categories);
                                Fragment_home.recylerViewCategories.notifyDataSetChanged();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                Fragment_home.recyclerViewProducts.notifyDataSetChanged();
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

    public static class ViewBrands extends RecyclerView.ViewHolder {
        TextView tvBrands_name;
        ImageView imgBrands;
        MaterialCardView cardView;
        public ViewBrands(@NonNull View itemView) {
            super(itemView);
            tvBrands_name = itemView.findViewById(R.id.tvBrand);
            imgBrands = itemView.findViewById(R.id.imgBrand);
            cardView = itemView.findViewById(R.id.img_card_brands);
        }
    }
}
