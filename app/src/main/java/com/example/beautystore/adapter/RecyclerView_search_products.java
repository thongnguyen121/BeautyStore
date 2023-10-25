package com.example.beautystore.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.beautystore.R;
import com.example.beautystore.fragments.Fragment_home;
import com.example.beautystore.model.Products;

import java.util.ArrayList;

public class RecyclerView_search_products extends RecyclerView.Adapter<RecyclerView_search_products.ViewHolder_Search> {
    private Fragment_home context;
    private int resource;
    private ArrayList<Products> data;

    public RecyclerView_search_products(Fragment_home context, int resource, ArrayList<Products> data) {
        this.context = context;
        this.resource = resource;
        this.data = data;
    }
    public void setFilterList(ArrayList<Products> filterlist) {
        this.data = filterlist;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView_search_products.ViewHolder_Search onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardViewItem = (CardView) context.getLayoutInflater().
                inflate(viewType, parent, false);
        return new RecyclerView_search_products.ViewHolder_Search(cardViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView_search_products.ViewHolder_Search holder, int position) {
        Products products = data.get(position);
        Glide.with(context).load(products.getImgProducts_1()).into(holder.imgHinh);
        holder.tvProducts_name.setText(products.getProducts_name());

    }
    @Override
    public int getItemViewType(int position) {
        return resource;
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder_Search extends RecyclerView.ViewHolder {
        ImageView imgHinh;
        TextView tvProducts_name;
        public ViewHolder_Search(@NonNull View itemView) {
            super(itemView);
            imgHinh = itemView.findViewById(R.id.img_search);
            tvProducts_name = itemView.findViewById(R.id.tvproducts_name_search);
        }
    }
}
