package com.example.beautystore.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.beautystore.R;
import com.example.beautystore.activity.Activity_add_Brands;
import com.example.beautystore.fragments.Fragment_home;
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
import java.util.HashSet;

public class RecyclerView_Brands_WH extends RecyclerView.Adapter<RecyclerView_Brands_WH.HolderBrand_WH> {
    private Fragment_warehouse_list context;
    private int resource;
    private ArrayList<Brands> data;
    public static String brand_id_wh = "";
    public static int selectedPosition_brands = -1;
    public static String categories_id_brands_wh = "";

    public RecyclerView_Brands_WH(Fragment_warehouse_list context, int resource, ArrayList<Brands> data) {
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView_Brands_WH.HolderBrand_WH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) context.getLayoutInflater().
                inflate(viewType, parent, false);
        return new RecyclerView_Brands_WH.HolderBrand_WH(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView_Brands_WH.HolderBrand_WH holder, int position) {
        Brands brands = data.get(position);
        holder.tvBrands_name.setText(brands.getBrands_name());
        Glide.with(context).load(brands.getImg_brands()).into(holder.imgBrands);

        if (position == selectedPosition_brands) {
            holder.materialCardView.setStrokeColor(ContextCompat.getColor(context.getContext(), R.color.blue));

        } else {
            if (RecyclerView_cate_WH.brands_id_cate_wh.equals(brand_id_wh)) {

                holder.materialCardView.setStrokeColor(ContextCompat.getColor(context.getContext(), R.color.blue));
            }

            holder.materialCardView.setStrokeColor(Color.TRANSPARENT);
//
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brand_id_wh = brands.getBrands_id();
                int previousSelectedPosition = selectedPosition_brands;
                selectedPosition_brands = holder.getAdapterPosition();
                RecyclerView_cate_WH.selectedPosition_cate = -1;
                // Thông báo Adapter cập nhật lại giao diện để vẽ lại màu nền và màu chữ
                notifyItemChanged(previousSelectedPosition);
                notifyItemChanged(selectedPosition_brands);
                notifyItemChanged(selectedPosition_brands);

                filterProductsAndCategories_BransID(brand_id_wh);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                brand_id_wh = brands.getBrands_id();
                showPopupMenu(v);
                return false;
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
                    Intent intent = new Intent(view.getContext(), Activity_add_Brands.class);
                    intent.putExtra("brands_id", brand_id_wh);
                    context.startActivity(intent);
                    Fragment_warehouse_list.statusBrands = false;
                } else if (id == R.id.Delete) {

                }


                return true;
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
                Fragment_warehouse_list.data_products.clear();
                Fragment_warehouse_list.data_categories.clear();
                HashSet<String> categoryIds = new HashSet<>(); // lưu trữ ID loại sản phẩm

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Products products = dataSnapshot.getValue(Products.class);
                    String id_categories = products.getCategories_id();
                    // lay id cua categories khi loc prodcucts theo hang
                    categories_id_brands_wh = products.getCategories_id();
                    //lay brand_id khi loc products theo hang
                    brand_id_wh = products.getBrands_id();
                    Fragment_warehouse_list.data_products.add(products);

                    categoryIds.add(id_categories); // Thêm ID loại sản phẩm vào HashSet
                }

                // Lặp qua danh sách duy nhất các ID loại sản phẩm
                for (String categoryId : categoryIds) {
                    databaseReferenceCategories.child(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Categories categories = dataSnapshot.getValue(Categories.class);
                            if (categories != null) {
                                Fragment_warehouse_list.data_categories.add(categories);
                                Fragment_warehouse_list.recyclerView_cate_wh.notifyDataSetChanged();

                            }else {
                                Fragment_warehouse_list.data_categories.add(categories);
                                Fragment_warehouse_list.recyclerView_cate_wh.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                Fragment_warehouse_list.recyclerView_cate_wh.notifyDataSetChanged();

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

    public static class HolderBrand_WH extends RecyclerView.ViewHolder {
        ImageView imgBrands;
        TextView tvBrands_name;
        MaterialCardView materialCardView;

        public HolderBrand_WH(@NonNull View itemView) {
            super(itemView);
            imgBrands = itemView.findViewById(R.id.imgBrand_wh);
            tvBrands_name = itemView.findViewById(R.id.tvBrand_wh);
            materialCardView = itemView.findViewById(R.id.img_card_brands_wh);
        }
    }
}
