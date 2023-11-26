package com.example.beautystore.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.beautystore.R;
import com.example.beautystore.activity.Activity_Product_Detail;
import com.example.beautystore.fragments.Fragment_home;
import com.example.beautystore.model.CartDetail;
import com.example.beautystore.model.Order;
import com.example.beautystore.model.OrderStatus;
import com.example.beautystore.model.Products;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RecyclerViewProducts extends RecyclerView.Adapter<RecyclerViewProducts.ViewProducts> {
    private Context context;
    private int resource;
    private ArrayList<Products> data;
    public static  String id ="";
    public RecyclerViewProducts(Context context, int resource, ArrayList<Products> data) {
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
        CardView cardViewItem = (CardView) LayoutInflater.from(context).inflate(viewType, parent, false);
        return new ViewProducts(cardViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewProducts.ViewProducts holder, int position) {
        Products products = data.get(position);

        String uid = FirebaseAuth.getInstance().getUid();
        String products_id = products.getProducts_id();
        checkOrderStatusForRating(products_id,uid, holder);

        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
        if(products.getProducts_name().length() < 13)
        {
            holder.tvproductName.setText(products.getProducts_name());
        }else {
            holder.tvproductName.setText(products.getProducts_name().substring(0,12) + "...");
        }

        holder.tvPrice.setText(decimalFormat.format(Integer.valueOf(products.getPrice().trim()))+ " Đ");
        if (products.getDescription().length() <= 40){
            holder.tvdescription.setText(products.getDescription());
        }
        else {
            holder.tvdescription.setText(products.getDescription().substring(0,40) + "...");
        }

        Glide.with(context).load(products.getImgProducts_1()).into(holder.imgProducts);
        id = products.getCategories_id();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Activity_Product_Detail.class);
                intent.putExtra("products_id", products.getProducts_id());
                intent.putExtra("categories_id", products.getCategories_id());
                context.startActivity(intent);
            }
        });


    }

    private void checkOrderStatusForRating(String productId, String uid, RecyclerViewProducts.ViewProducts holder) {
        DatabaseReference orderStatusRef = FirebaseDatabase.getInstance().getReference("OrderStatus");

        orderStatusRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot orderStatusSnapshot : snapshot.getChildren()) {
                    OrderStatus orderStatus = orderStatusSnapshot.getValue(OrderStatus.class);
                    if (orderStatus.getStatus().equals("4") || orderStatus.getStatus().equals("6")) {
                        checkOrderForRating(orderStatus.getOrder_id(), productId, uid, holder);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }
    private void checkOrderForRating(String order_id, String productId, String uid, RecyclerViewProducts.ViewProducts holder) {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Order");
        orderRef.child(order_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Order order = snapshot.getValue(Order.class);
                if (order != null && order.getCustomer_id().equals(uid)) {
                    if (order.getOrder_id() != null) {
                        for (CartDetail item : order.getItems()) {
                            if (item != null && item.getProduct_id().equals(productId)) {
                                holder.tvStatus.setVisibility(View.VISIBLE);
                            }
                        }
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
    public int getItemViewType(int position) {
        return resource;
    }
    @Override
    public int getItemCount() {
        return data.size();
    }


    public static class ViewProducts extends RecyclerView.ViewHolder {
        ImageView imgProducts;
        TextView tvproductName, tvPrice, tvStatus, tvdescription;
        public ViewProducts(@NonNull View itemView) {
            super(itemView);
            imgProducts = itemView.findViewById(R.id.imgPrducts);
            tvproductName = itemView.findViewById(R.id.tvProductname);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus_products);
            tvdescription = itemView.findViewById(R.id.tvDescription);

        }
    }
}
