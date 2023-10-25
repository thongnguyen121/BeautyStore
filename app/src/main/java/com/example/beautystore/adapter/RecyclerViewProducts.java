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

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RecyclerViewProducts extends RecyclerView.Adapter<RecyclerViewProducts.ViewProducts> {
    private Fragment_home context;
    private int resource;
    private ArrayList<Products> data;
    public static  String id ="";
    public RecyclerViewProducts(Fragment_home context, int resource, ArrayList<Products> data) {
        this.context = context;
        this.resource = resource;
        this.data = data;
    }
    public void setFilterList_Products(ArrayList<Products> filterlist) {
        this.data = filterlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewProducts.ViewProducts onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardViewItem = (CardView) context.getLayoutInflater().
                inflate(viewType, parent, false);
        return new ViewProducts(cardViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewProducts.ViewProducts holder, int position) {
        Products products = data.get(position);

        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");

        holder.tvproductName.setText(products.getProducts_name().substring(0,12) + "...");

        holder.tvPrice.setText(decimalFormat.format(Integer.valueOf(products.getPrice().trim()))+ " Đ");
        holder.tvdescription.setText(products.getDescription().substring(0,40) + "...");
        Glide.with(context).load(products.getImgProducts_1()).into(holder.imgProducts);
        id = products.getCategories_id();

    }
    @Override
    public int getItemViewType(int position) {
        return resource;
    }
    @Override
    public int getItemCount() {
        return data.size();
    }


    public static class ViewProducts extends RecyclerView.ViewHolder {
        ImageView imgProducts;
        TextView tvproductName, tvPrice, tvRating, tvdescription;
        public ViewProducts(@NonNull View itemView) {
            super(itemView);
            imgProducts = itemView.findViewById(R.id.imgPrducts);
            tvproductName = itemView.findViewById(R.id.tvProductname);
            tvPrice = itemView.findViewById(R.id.tvPrice);
//            tvRating = itemView.findViewById(R.id.tvTotalRating);
            tvdescription = itemView.findViewById(R.id.tvDescription);
        }
    }
}
