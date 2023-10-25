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
import com.example.beautystore.model.Brands;
import com.example.beautystore.model.Categories;

import java.util.ArrayList;

public class RecyclerViewBrands extends RecyclerView.Adapter<RecyclerViewBrands.ViewBrands> {
    private Fragment_home context;
    private int resource;
    private ArrayList<Brands> data;

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
        holder.imgBrands.setImageResource(brands.getImg_brands());
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
        public ViewBrands(@NonNull View itemView) {
            super(itemView);
            tvBrands_name = itemView.findViewById(R.id.tvBrand);
            imgBrands = itemView.findViewById(R.id.imgBrand);
        }
    }
}
