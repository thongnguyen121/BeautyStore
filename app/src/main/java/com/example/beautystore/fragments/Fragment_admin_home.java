package com.example.beautystore.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.AutoScrollHelper;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beautystore.R;
import com.example.beautystore.adapter.RecyclerViewRating_admin;
import com.example.beautystore.adapter.RecyclerView_Brands_WH;
import com.example.beautystore.model.Brands;
import com.example.beautystore.model.CartDetail;
import com.example.beautystore.model.Order;
import com.example.beautystore.model.OrderStatus;
import com.example.beautystore.model.Products;
import com.example.beautystore.model.Rating;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Fragment_admin_home extends Fragment {

    TextView tvDoanhthu, tvNhansu, tvTongsp, tvDonhanghuy, tvDonhangmoi, tvXuatkho;
    View view;
    long totalRevenue = 0;
    public static final String SHARE_PREFS = "sharedPrefs";

    private RecyclerViewRating_admin recyclerViewRatingAdmin;
    RecyclerView rcRating;
    ArrayList<Products> data_ratingProducts = new ArrayList<>();
    AutoScrollHelper autoScrollHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        setControl(view);
//        getDataRating();
        checkOrderStatusForRating();
        getNew_order("0");
        getOrder_cancel();
//        getOrder_Exportwarehouse();
        getTotalProductQuantity();
        getTotal_member();
//        getTotalAmount();
        getTotalAmount();
        getExport_Warehouse();


        return view;
    }

    private void setControl(View view) {
        tvDoanhthu = view.findViewById(R.id.tvDoanhthu);
        tvNhansu = view.findViewById(R.id.tvNhansu);
        tvTongsp = view.findViewById(R.id.tvTongsanpham);
        tvDonhanghuy = view.findViewById(R.id.tvDonhangbihuy);
        tvDonhangmoi = view.findViewById(R.id.tvDonhangmoi);
        tvXuatkho = view.findViewById(R.id.tvXuatkho);
        rcRating = view.findViewById(R.id.rcNew_rating);
    }


    private void checkOrderStatusForRating() {
        DatabaseReference ratingRef = FirebaseDatabase.getInstance().getReference().child("Rating");

        ratingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ratingSnapshot : snapshot.getChildren()) {
                    String products_id = ratingSnapshot.getKey();
                    getRatingData(products_id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    private void getRatingData(String products_id) {
        recyclerViewRatingAdmin = new RecyclerViewRating_admin(requireContext(), R.layout.layout_item_rating_admin, data_ratingProducts);
        GridLayoutManager layoutManager2 = new GridLayoutManager(getContext(), 1);
        layoutManager2.setOrientation(RecyclerView.HORIZONTAL);
        rcRating.setLayoutManager(layoutManager2);
        rcRating.setAdapter(recyclerViewRatingAdmin);
       

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Products");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Products products = dataSnapshot.getValue(Products.class);
                    if (products != null) {
                        if (products.getProducts_id().equals(products_id))
                        {
                            data_ratingProducts.add(products);

                        }
                        recyclerViewRatingAdmin.notifyDataSetChanged();

                        Log.d("data", "Data: " + dataSnapshot.getKey());
                    }
                }


            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    private void getNew_order(String stattus) {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("OrderStatus");
        Query query = ordersRef.orderByChild("status").equalTo(stattus);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                String countString = String.valueOf(count);
                tvDonhangmoi.setText(countString);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }

    private void getOrder_cancel() {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("OrderStatus");
        Query query = ordersRef.orderByChild("status");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String statusValue = snapshot.child("status").getValue(String.class);
                    if (statusValue != null && (statusValue.equals("5") || statusValue.equals("7"))) {
                        count++;
                    }
                }

                String countString = String.valueOf(count);
                tvDonhanghuy.setText(countString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }

    private void getOrder_Exportwarehouse() {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("OrderStatus");
        Query query = ordersRef.orderByChild("status");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String statusValue = snapshot.child("status").getValue(String.class);
                    if (statusValue != null && (statusValue.equals("4") || statusValue.equals("6"))) {
                        count++;
                    }
                }

                String countString = String.valueOf(count);
                tvXuatkho.setText(countString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }

    private void getTotalProductQuantity() {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long totalQuantity = 0;

                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot quantitySnapshot = productSnapshot.child("quantity");
                    if (quantitySnapshot.exists()) {
                        Object quantityValue = quantitySnapshot.getValue();
                        if (quantityValue instanceof Long) {
                            long quantity = (long) quantityValue;
                            totalQuantity += quantity;
                        } else if (quantityValue instanceof String) {
                            long quantity = Long.parseLong((String) quantityValue);
                            totalQuantity += quantity;
                        }
                    }
                }

                String totalQuantityString = String.valueOf(totalQuantity);
                tvTongsp.setText(totalQuantityString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }

    private void getTotal_member() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Member");
        Query query = databaseReference.orderByChild("role").startAt("1").endAt("2");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                String countString = String.valueOf(count);
                tvNhansu.setText(countString);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }

    private void getTotalAmount() {
        DatabaseReference orderStatusRef = FirebaseDatabase.getInstance().getReference().child("OrderStatus");

        orderStatusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {

                List<Long> amounts = new ArrayList<>();
                data_ratingProducts.clear();
                for (DataSnapshot orderStatusSnapshot : snapshot.getChildren()) {
                    OrderStatus orderStatus = orderStatusSnapshot.getValue(OrderStatus.class);

                    if (orderStatus != null && (orderStatus.getStatus().equals("4") || orderStatus.getStatus().equals("6"))) {
                        String orderId = orderStatus.getOrder_id();
                        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Order").child(orderId);
                        orderRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                long totalAmount = 0;
                                String totalAmountString = dataSnapshot.child("total_amount").getValue(String.class);
                                if (totalAmountString != null) {
                                    long amount = Long.parseLong(totalAmountString);
                                    amounts.add(amount);
                                    Log.d("as", "amount: " + amount);
                                }
                                DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
                                totalAmount = calculateTotal(amounts);
                                String totalAmountString_1 = String.valueOf(totalAmount);
                                tvDoanhthu.setText(decimalFormat.format(Integer.valueOf(totalAmountString_1.trim())) + " Đ");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
            }
        });
    }

    private long calculateTotal(List<Long> amounts) {
        long total = 0;
        for (long amount : amounts) {
            total += amount;
        }
        return total;
    }
    private void getExport_Warehouse() {
        DatabaseReference orderStatusRef = FirebaseDatabase.getInstance().getReference().child("OrderStatus");

        orderStatusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {

                List<Long> amounts = new ArrayList<>();
                data_ratingProducts.clear();
                for (DataSnapshot orderStatusSnapshot : snapshot.getChildren()) {
                    OrderStatus orderStatus = orderStatusSnapshot.getValue(OrderStatus.class);

                    if (orderStatus != null && (orderStatus.getStatus().equals("4") || orderStatus.getStatus().equals("6"))) {
                        String orderId = orderStatus.getOrder_id();
                        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Order").child(orderId);
                        orderRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                long totalAmount = 0;

                                Order order = dataSnapshot.getValue(Order.class);

                                    if (order.getOrder_id() != null) {
                                        for (CartDetail item : order.getItems()) {
                                            String totalAmountString = item.getQty();
                                            if (totalAmountString != null) {
                                                long amount = Long.parseLong(totalAmountString);
                                                amounts.add(amount);
                                                Log.d("as", "amount: " + amount);
                                            }
                                        }
                                        totalAmount = calculateTotal(amounts);
                                        String totalAmountString_1 = String.valueOf(totalAmount);
                                        tvXuatkho.setText(totalAmountString_1);
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
            }
        });
    }
}