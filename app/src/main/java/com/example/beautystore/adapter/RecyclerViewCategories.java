package com.example.beautystore.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.beautystore.R;
import com.example.beautystore.fragments.Fragment_home;
import com.example.beautystore.model.Categories;
import com.example.beautystore.model.Products;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RecyclerViewCategories extends RecyclerView.Adapter<RecyclerViewCategories.Viewcategories> {
    private Fragment_home context;
    private int resource;
    private ArrayList<Categories> data;
    public static  String id ="";
    private String currentCategorie;
    private View.OnClickListener categoryClickListener;
    public static String brands_id_cate = "";
    public static String cate_id = "";
    public static int selectedPosition_cate = -1;
    public RecyclerViewCategories(Fragment_home context, int resource, ArrayList<Categories> data) {
        this.context = context;
        this.resource = resource;
        this.data = data;
    }
    public void setFilterList(ArrayList<Categories> filterlist) {
        this.data = filterlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewCategories.Viewcategories onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) context.getLayoutInflater().
                inflate(viewType, parent, false);
        return new RecyclerViewCategories.Viewcategories(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewCategories.Viewcategories holder, int position) {
        Categories categories = data.get(position);
        holder.tvCategories_name.setText(categories.getCategories_name() + "\n");

        Glide.with(context).load(categories.getImg_categories()).into(holder.imgCategories);
        if (position == selectedPosition_cate) {

            holder.tvCategories_name.setTextColor(ContextCompat.getColor(context.getContext(), R.color.pink));
            holder.cardView.setStrokeColor(ContextCompat.getColor(context.getContext(), R.color.pink));

//            RecyclerViewBrands.selectedPosition_brands = -1;
//            Fragment_home.recyclerViewBands.notifyDataSetChanged();

        } else {

            holder.tvCategories_name.setTextColor(ContextCompat.getColor(context.getContext(), R.color.gray));
            holder.cardView.setStrokeColor(Color.TRANSPARENT);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = categories.getCategories_id();
                if (RecyclerViewBrands.brand_id == null){
                    filterProducts_CategoriesID(id);
                }
                else {
                    filterProductsAndCategories_BransID(id, RecyclerViewBrands.brand_id);
                }
                int previousSelectedPosition = selectedPosition_cate;
                selectedPosition_cate = holder.getAdapterPosition();
//                RecyclerViewBrands.selectedPosition_brands = -1;

                // Thông báo Adapter cập nhật lại giao diện để vẽ lại màu nền và màu chữ
                notifyItemChanged(previousSelectedPosition);
                notifyItemChanged(selectedPosition_cate);
                notifyDataSetChanged();



//                    filterProducts(id, RecyclerViewBrands.brand_id);
//                    filterProducts_CategoriesID(id);



                Log.e("TAG", "layidbrand: "+ RecyclerViewBrands.brand_id );
                Log.e("TAG", "layidbcate: "+ id );
                Fragment_home.recyclerViewBands.notifyDataSetChanged();

            }
        });

    }



    private void filterProductsAndCategories_BransID(String id, String brandId) {
        DatabaseReference databaseReferenceProducts = FirebaseDatabase.getInstance().getReference("Products");


        Query queryProducts = databaseReferenceProducts.orderByChild("categories_id").equalTo(id);

        queryProducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Fragment_home.data_products.clear();


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Products products = dataSnapshot.getValue(Products.class);
                    cate_id = products.getCategories_id();
                    Log.d("TAG", "du lieu product: " + products.getBrands_id());
                    brands_id_cate = products.getBrands_id();
                    if (products.getCategories_id().equals(id)){
                        if (products.getBrands_id().equals(brandId)) {

                            Fragment_home.data_products.add(products);

                        }
                        else if(brandId.isEmpty())
                        {
                            Fragment_home.data_products.add(products);
                        }

                    }


                }



                Fragment_home.recyclerViewProducts.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void filterProducts_CategoriesID(String id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        DatabaseReference databaseReference_brands = FirebaseDatabase.getInstance().getReference("Brands");
        Query query = databaseReference.orderByChild("categories_id").equalTo(id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Fragment_home.data_products.clear();


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Products products = dataSnapshot.getValue(Products.class);


                        Fragment_home.data_products.add(products);


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

    public static class Viewcategories extends RecyclerView.ViewHolder {
        TextView tvCategories_name;
        ImageView imgCategories;
        MaterialCardView cardView;
        public Viewcategories(@NonNull View itemView) {
            super(itemView);
            tvCategories_name = itemView.findViewById(R.id.tvCategoriesname);
            imgCategories = itemView.findViewById(R.id.imgCategories);
            cardView = itemView.findViewById(R.id.img_card_categories);
        }
    }
}
