package com.example.beautystore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.beautystore.R;
import com.example.beautystore.model.OrderDetail;
import com.example.beautystore.model.WishList;

import java.util.ArrayList;

public class RecyclerView_Order extends RecyclerView.Adapter<RecyclerView_Order.OrderDetailViewHolder> {
    private ArrayList<OrderDetail> data;
    private Context context;

    public RecyclerView_Order(ArrayList<OrderDetail> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View orderDetailView = inflater.inflate(R.layout.layout_item_order, parent, false);
        return new OrderDetailViewHolder(orderDetailView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        OrderDetail orderDetail = data.get(position);
        holder.tvProductName.setText(orderDetail.getProduct_id()); //Fix replace product name with firebase
        holder.tvProductQty.setText(orderDetail.getProduct_id()); //Fix replace product name with firebase
        holder.tvProductPrice.setText(orderDetail.getProduct_id()); //Fix replace product name with firebase
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class OrderDetailViewHolder extends RecyclerView.ViewHolder{
        TextView tvProductName, tvProductQty, tvProductPrice;

        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvOrderDetailProductName);
            tvProductQty = itemView.findViewById(R.id.tvOrderDetailProductQty);
            tvProductPrice = itemView.findViewById(R.id.tvOrderDetailProductPrice);
        }
    }
}
