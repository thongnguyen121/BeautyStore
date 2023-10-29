package com.example.beautystore.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.beautystore.R;
import com.example.beautystore.fragments.Fragment_home;
import com.example.beautystore.fragments.Fragment_warehouse_list;
import com.example.beautystore.model.Products;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RecyclerView_Products_WH extends RecyclerView.Adapter<RecyclerView_Products_WH.HolderProducts_WH> {

    private Fragment_warehouse_list context;
    private int resource;
    private ArrayList<Products> data;

    public RecyclerView_Products_WH(Fragment_warehouse_list context, int resource, ArrayList<Products> data) {
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView_Products_WH.HolderProducts_WH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardViewItem = (CardView) context.getLayoutInflater().
                inflate(viewType, parent, false);
        return new RecyclerView_Products_WH.HolderProducts_WH(cardViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView_Products_WH.HolderProducts_WH holder, int position) {

        Products products = data.get(position);

        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");

        holder.tvProducts_name.setText(products.getProducts_name().substring(0,12) + "...");
        holder.tvProducts_price.setText(decimalFormat.format(Integer.valueOf(products.getPrice().trim()))+ " Đ");
        holder.tvProducts_id.setText(products.getProducts_id());
        Glide.with(context).load(products.getImgProducts_1()).into(holder.imgProducs);

    }
    @Override
    public int getItemViewType(int position) {
        return resource;
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class HolderProducts_WH extends RecyclerView.ViewHolder {
        ImageView imgProducs;
        TextView tvProducts_name, tvProducts_price, tvProducts_quantity, tvProducts_id;
        Button btnEdit, btnDelete;
        public HolderProducts_WH(@NonNull View itemView) {
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
