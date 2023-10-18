package com.example.beautystore.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beautystore.R;
import com.example.beautystore.fragments.Fragment_home;
import com.example.beautystore.model.Categories;
import com.example.beautystore.model.Products;

import java.util.ArrayList;

public class RecyclerViewCategories extends RecyclerView.Adapter<RecyclerViewCategories.Viewcategories> {
    private Fragment_home context;
    private int resource;
    private ArrayList<Categories> data;

    public RecyclerViewCategories(Fragment_home context, int resource, ArrayList<Categories> data) {
        this.context = context;
        this.resource = resource;
        this.data = data;
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
        holder.tvCategories_name.setText(categories.getCategories_name());
        holder.imgCategories.setImageResource(categories.getImg_categories());

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
        public Viewcategories(@NonNull View itemView) {
            super(itemView);
            tvCategories_name = itemView.findViewById(R.id.tvCategoriesname);
            imgCategories = itemView.findViewById(R.id.imgCategories);
        }
    }
}
