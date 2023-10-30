package com.example.beautystore.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.beautystore.R;
import com.example.beautystore.activity.Activity_Add_Products;
import com.example.beautystore.fragments.Fragment_warehouse_list;
import com.example.beautystore.model.Products;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RecyclerViewProducts_WH extends RecyclerView.Adapter<RecyclerViewProducts_WH.ViewHolder_WH> {

    private Fragment_warehouse_list context;
    private int resource;
    private ArrayList<Products> data;
    public static String products_id = "";
    public RecyclerViewProducts_WH(Fragment_warehouse_list context, int resource, ArrayList<Products> data) {
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
    public RecyclerViewProducts_WH.ViewHolder_WH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardViewItem = (CardView) context.getLayoutInflater().
                inflate(viewType, parent, false);
        return new RecyclerViewProducts_WH.ViewHolder_WH(cardViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewProducts_WH.ViewHolder_WH holder, int position) {

        Products products = data.get(position);

        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");

        holder.tvProducts_name.setText(products.getProducts_name().substring(0,12) + "...");
        holder.tvProducts_price.setText(decimalFormat.format(Integer.valueOf(products.getPrice().trim()))+ " Đ");
        holder.tvProducts_id.setText(products.getProducts_id());
        Glide.with(context).load(products.getImgProducts_1()).into(holder.imgProducs);
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), Activity_Add_Products.class);
                intent.putExtra("products_id", products.getProducts_id());
                intent.putExtra("categories_id", products.getCategories_id());
                intent.putExtra("brands_id", products.getBrands_id());
                context.startActivity(intent);
                Fragment_warehouse_list.statusProducts = false;
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

    public static class ViewHolder_WH extends RecyclerView.ViewHolder {
        ImageView imgProducs;
        TextView tvProducts_name, tvProducts_price, tvProducts_quantity, tvProducts_id;
        ImageButton btnEdit, btnDelete;
        public ViewHolder_WH(@NonNull View itemView) {
            super(itemView);
            imgProducs = itemView.findViewById(R.id.imgProduct_wh);
            tvProducts_name = itemView.findViewById(R.id.tv_products_name_wh);
            tvProducts_id = itemView.findViewById(R.id.tv_products_id_wh);
            tvProducts_price = itemView.findViewById(R.id.tv_products_price_wh);
            tvProducts_quantity = itemView.findViewById(R.id.tv_products_quantity_wh);
            btnEdit = itemView.findViewById(R.id.imb_Edit_products_wh);
            btnDelete = itemView.findViewById(R.id.imb_Delete_products_wh);
        }
    }
}
