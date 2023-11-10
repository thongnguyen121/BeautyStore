package com.example.beautystore.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beautystore.model.Order;
import com.example.beautystore.model.Products;

import java.util.ArrayList;

public class RecyclerViewOder_Customer extends RecyclerView.Adapter<RecyclerViewOder_Customer.OrderHolder> {
    private Context context;
    private int resource;
    private ArrayList<Order> data;
    @Override
    public RecyclerViewOder_Customer.OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewOder_Customer.OrderHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class OrderHolder extends RecyclerView.ViewHolder {
        public OrderHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
