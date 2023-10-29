package com.example.beautystore.adapter;

import android.graphics.Color;
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
import com.example.beautystore.fragments.Fragment_warehouse_list;
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

public class RecyclerView_cate_WH extends RecyclerView.Adapter<RecyclerView_cate_WH.Vholder_cate> {
    private Fragment_warehouse_list context;
    private int resource;
    private ArrayList<Categories> data;
    public static String brands_id_cate_wh = "";
    public static String cate_id_wh = "";
    public static int selectedPosition_cate = -1;

    public RecyclerView_cate_WH(Fragment_warehouse_list context, int resource, ArrayList<Categories> data) {
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView_cate_WH.Vholder_cate onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) context.getLayoutInflater().
                inflate(viewType, parent, false);
        return new RecyclerView_cate_WH.Vholder_cate(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView_cate_WH.Vholder_cate holder, int position) {
        Categories categories = data.get(position);
        holder.tvCate_name.setText(categories.getCategories_name() + "\n");
        Glide.with(context).load(categories.getImg_categories()).into(holder.imgCate);

        if (position == selectedPosition_cate) {

            holder.materialCardView.setStrokeColor(ContextCompat.getColor(context.getContext(), R.color.blue));

        } else {
            holder.materialCardView.setStrokeColor(Color.TRANSPARENT);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cate_id_wh = categories.getCategories_id();
                selectedPosition_cate = holder.getAdapterPosition();
//                RecyclerViewBrands.selectedPosition_brands = -1;
                int previousSelectedPosition = selectedPosition_cate;
                // Thông báo Adapter cập nhật lại giao diện để vẽ lại màu nền và màu chữ
                notifyItemChanged(previousSelectedPosition);
                notifyItemChanged(selectedPosition_cate);
                notifyDataSetChanged();
                filterProductsAndCategories_BransID(cate_id_wh, RecyclerView_Brands_WH.brand_id_wh);
            }
        });
    }

    private void filterProductsAndCategories_BransID(String id, String brandId) {
        DatabaseReference databaseReferenceProducts = FirebaseDatabase.getInstance().getReference("Products");


        Query queryProducts = databaseReferenceProducts.orderByChild("categories_id").equalTo(id);

        queryProducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Fragment_warehouse_list.data_products.clear();


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Products products = dataSnapshot.getValue(Products.class);
                    brands_id_cate_wh = products.getBrands_id();
                    if (products.getCategories_id().equals(id)) {
                        if (products.getBrands_id().equals(brandId)) {

                            Fragment_warehouse_list.data_products.add(products);

                        } else if (brandId.isEmpty()) {
                            Fragment_warehouse_list.data_products.add(products);
                        }

                    }


                }


                Fragment_warehouse_list.recyclerViewProductsWh.notifyDataSetChanged();
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

    public static class Vholder_cate extends RecyclerView.ViewHolder {
        ImageView imgCate;
        TextView tvCate_name;
        MaterialCardView materialCardView;

        public Vholder_cate(@NonNull View itemView) {
            super(itemView);
            imgCate = itemView.findViewById(R.id.imgCategories_wh);
            tvCate_name = itemView.findViewById(R.id.tvCategories_wh);
            materialCardView = itemView.findViewById(R.id.img_card_categories_wh);
        }
    }
}
