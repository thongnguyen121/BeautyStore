package com.example.beautystore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beautystore.R;
import com.example.beautystore.model.CartDetail;
import com.example.beautystore.model.Products;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RecyclerView_Order extends RecyclerView.Adapter<RecyclerView_Order.OrderDetailViewHolder> {
    private ArrayList<CartDetail> data;
    private Context context;
    private  int resource;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public RecyclerView_Order(ArrayList<CartDetail> data, Context context,int resource) {
        this.data = data;
        this.context = context;
        this.resource = resource;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
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
        CartDetail cartDetail = data.get(position);
//        holder.tvProductName.setText(orderDetail.getProduct_id()); //Fix replace product name with firebase
//        holder.tvProductQty.setText(orderDetail.getProduct_id()); //Fix replace product name with firebase
//        holder.tvProductPrice.setText(orderDetail.getProduct_id()); //Fix replace product name with firebase
//        OrderDetail cartDetail = data.get(position);
        String product_id = cartDetail.getProduct_id();
        loadDataProduct(holder, product_id, cartDetail.getQty());
    }

    private void loadDataProduct(OrderDetailViewHolder holder, String productId, String qty) {
        DatabaseReference productReference = databaseReference.child("Products").child(productId);
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
        productReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Products products = snapshot.getValue(Products.class);
                    if (products != null) {
                        holder.tvProductName.setText(products.getProducts_name());
//                        holder.tvProductPrice.setText(products.getPrice());
                        holder.tvProductQty.setText(qty);
                        holder.tvProductPrice.setText(decimalFormat.format(Integer.valueOf(products.getPrice().trim()))+ " Đ");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
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
