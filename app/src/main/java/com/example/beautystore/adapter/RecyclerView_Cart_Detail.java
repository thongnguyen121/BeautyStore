package com.example.beautystore.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.beautystore.R;
import com.example.beautystore.activity.Activity_Order;
import com.example.beautystore.activity.Activity_Product_Detail;
import com.example.beautystore.fragments.Fragment_cart;
import com.example.beautystore.model.Cart;
import com.example.beautystore.model.CartDetail;
import com.example.beautystore.model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RecyclerView_Cart_Detail extends RecyclerView.Adapter<RecyclerView_Cart_Detail.CartDetailViewHolder> {
    private ArrayList<CartDetail> data;
    private Fragment_cart context;
    int resource;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    public static boolean isDeleteCartItem = false;
    int maxQty;

    public RecyclerView_Cart_Detail(ArrayList<CartDetail> data, Fragment_cart context, int resource) {
        this.data = data;
        this.context = context;
        this.resource = resource;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @NonNull
    @Override
    public CartDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context.getContext());
        View cartDetailView = inflater.inflate(R.layout.layout_item_cart_detail, parent, false);
//        CardView cardView = (CardView) context.getLayoutInflater().inflate(viewType, parent, false);
        return new CartDetailViewHolder(cartDetailView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartDetailViewHolder holder, int position) {
        CartDetail cartDetail = data.get(position);
        String product_id = cartDetail.getProduct_id();
        int qty = Integer.parseInt(cartDetail.getQty());
        loadDataProduct(holder, product_id, cartDetail.getQty());
        holder.ivDeceaseQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qty == 1){
                    holder.ivDeceaseQty.setEnabled(false);
                }else{
                    holder.ivDeceaseQty.setEnabled(true);
                    updateQty(product_id, qty-1);
                }
            }
        });
        holder.ivIncreaseQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qty == maxQty){
                    holder.ivIncreaseQty.setEnabled(false);
                }else{
                    holder.ivIncreaseQty.setEnabled(true);
                    updateQty(product_id, qty+1);
                }
            }
        });
        holder.ivClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCartItem(product_id);
            }
        });
    }

    private void deleteCartItem(String productId) {
        DatabaseReference reference = firebaseDatabase.getReference("Cart").child(FirebaseAuth.getInstance().getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Cart cart = snapshot.getValue(Cart.class);
                    if (cart != null) {
                        List<CartDetail> items = cart.getItems();
                        for (CartDetail detail : items) {
                            if (detail.getProduct_id().equals(productId)) {
                                items.remove(detail);
                                break;
                            }
                        }
                        Activity_Product_Detail.updateTotalPrice(reference, FirebaseAuth.getInstance().getUid());
                        reference.setValue(cart).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    getTotal();
                                }

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateQty(String productId, int i) {
        DatabaseReference reference = firebaseDatabase.getReference("Cart").child(FirebaseAuth.getInstance().getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Cart cart = snapshot.getValue(Cart.class);
                    if (cart != null) {
                        List<CartDetail> items = cart.getItems();
                        for (CartDetail detail : items) {
                            if (detail.getProduct_id().equals(productId)) {
                                Log.d("TAG", "onDataChange: " + detail.getQty());
                                detail.setQty(String.valueOf(i));
                                break;
                            }
                        }
                        Activity_Product_Detail.updateTotalPrice(reference, FirebaseAuth.getInstance().getUid());
                        reference.setValue(cart).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    getTotal();
                                }

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d("TAG", "so luong: " + i);
    }

    public static void getTotal() {
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Cart").child(FirebaseAuth.getInstance().getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String format = snapshot.child("total").getValue(String.class);
//                holder.tvPrice.setText(decimalFormat.format(Integer.valueOf(products.getPrice().trim()))+ " Đ");
                if (snapshot.exists()){
                    Fragment_cart.tvTotalMoney.setText(decimalFormat.format(Integer.valueOf(format.trim())) + " Đ");
                }
                else {
                    Fragment_cart.tvTotalMoney.setText("0 Đ");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void loadDataProduct(CartDetailViewHolder holder, String productId, String qty) {
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
                        maxQty = Integer.parseInt(products.getQuantity());
                        Glide.with(context).load(products.getImgProducts_1()).into(holder.ivProductImage);
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

    public static class CartDetailViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProductImage;
        TextView tvProductName;
        TextView tvProductPrice;
        ImageView ivClearBtn;
        ImageView ivDeceaseQty;
        TextView tvProductQty;
        ImageView ivIncreaseQty;

        public CartDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivCartDetailProductImage);
            tvProductName = itemView.findViewById(R.id.tvCartDetailProductName);
            tvProductPrice = itemView.findViewById(R.id.tvCartDetailProductPrice);
            ivClearBtn = itemView.findViewById(R.id.ivCartDetailClearBtn);
            ivDeceaseQty = itemView.findViewById(R.id.ivCartDetailDecreaseQty);
            ivIncreaseQty = itemView.findViewById(R.id.ivCartDetailIncreaseQty);
            tvProductQty = itemView.findViewById(R.id.tvCartDetailProductQty);
        }
    }
}
