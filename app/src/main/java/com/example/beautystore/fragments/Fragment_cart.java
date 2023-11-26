package com.example.beautystore.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beautystore.R;
import com.example.beautystore.activity.Activity_Order;
import com.example.beautystore.activity.Activity_Product_Detail;
import com.example.beautystore.adapter.RecyclerView_Cart_Detail;
import com.example.beautystore.model.Cart;
import com.example.beautystore.model.CartDetail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Fragment_cart extends Fragment {

    public static TextView tvTotalMoney;
    Button btnOrder;
    RecyclerView_Cart_Detail cartDetailAdapter;
    private RecyclerView cartDetaiRecyclerView;
    private ArrayList<CartDetail> cartDetails = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Cart").child(FirebaseAuth.getInstance().getUid());
        DatabaseReference productRef = firebaseDatabase.getReference("Products");
        setControl(view);
        getDataCart();
        RecyclerView_Cart_Detail.getTotal();
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Cart cart = snapshot.getValue(Cart.class);
                        List<CartDetail> item = cart.getItems();
                        Iterator<CartDetail> iterator = item.iterator();
                        final boolean[] check = {false};
                        while (iterator.hasNext()){
                            CartDetail detail = iterator.next();
                            productRef.child(detail.getProduct_id()).child("quantity").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String qty = snapshot.getValue(String.class);
                                    if (qty.equals("0")){
                                        check[0] = true;
                                        item.remove(detail);
                                        Log.d("TAG", "san pham: " + item);
                                        Activity_Product_Detail.updateTotalPrice(databaseReference, FirebaseAuth.getInstance().getUid());

                                        databaseReference.child("items").setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(getContext(), "xoa thanh cong", Toast.LENGTH_SHORT).show();
                                                    RecyclerView_Cart_Detail.getTotal();

                                                }
                                            }
                                        });
                                    }
                                    else if (Integer.parseInt(detail.getQty())>Integer.parseInt(qty)){
                                        Log.d("TAG", "san pham : " + detail.getProduct_id());

                                        detail.setQty(qty);
                                        Log.d("TAG", "san pham : " + detail.getProduct_id() + detail.getQty());
                                        item.remove(detail);
                                        item.add(detail);
                                        Log.d("TAG", "san pham : " + item);
                                        Activity_Product_Detail.updateTotalPrice(databaseReference, FirebaseAuth.getInstance().getUid());

                                        databaseReference.child("items").setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(getContext(), "xoa thanh cong", Toast.LENGTH_SHORT).show();
                                                    RecyclerView_Cart_Detail.getTotal();

                                                }
                                            }
                                        });
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getActivity(), Activity_Order.class);
                                startActivity(intent);
                            }
                        }, 500);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        return view;
    }


    private void getDataCart() {
        cartDetailAdapter = new RecyclerView_Cart_Detail(cartDetails,getContext(), R.layout.layout_item_cart_detail);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        cartDetaiRecyclerView.setLayoutManager(layoutManager);
        cartDetaiRecyclerView.setAdapter(cartDetailAdapter);
        databaseReference.child("items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartDetails.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CartDetail item = dataSnapshot.getValue(CartDetail.class);
                    cartDetails.add(item);
                }
                cartDetailAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void setControl(View view) {
        cartDetaiRecyclerView = view.findViewById(R.id.CartDetailList);
//        createCartDetailsList();
        tvTotalMoney = view.findViewById(R.id.tvCartDetailTotalMoney);
        btnOrder = view.findViewById(R.id.btnCartDetailOrderButton);
    }
}