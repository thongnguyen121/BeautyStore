package com.example.beautystore.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.beautystore.MainActivity;
import com.example.beautystore.R;
import com.example.beautystore.adapter.RecyclerViewCategories;
import com.example.beautystore.adapter.RecyclerViewOder_Customer;
import com.example.beautystore.model.Categories;
import com.example.beautystore.model.OrderStatus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Fragment_order extends Fragment {

    RecyclerViewOder_Customer recyclerViewOderCustomer;
    RecyclerView rcOrder_customer;
    View view;
    ArrayList<OrderStatus> data_OrderStatus = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    SearchView searchView;
    String uid;
    private boolean isSearchViewExpanded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order, container, false);
        setControl(view);
        getData_orderList();

        setSearchView();
        uid = FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        return view;
    }

    private void setControl(View view) {
        rcOrder_customer = view.findViewById(R.id.rcOrder_customer_fr);
        searchView = view.findViewById(R.id.idsearchview_order_customer);
    }

    private void getData_orderList() {
        recyclerViewOderCustomer = new RecyclerViewOder_Customer(requireContext(), R.layout.layout_items_orders_customer, data_OrderStatus);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getContext(), 1);
        layoutManager1.setOrientation(RecyclerView.VERTICAL);
        rcOrder_customer.setLayoutManager(layoutManager1);
        rcOrder_customer.setAdapter(recyclerViewOderCustomer);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference orderStatusReference = firebaseDatabase.getReference().child("OrderStatus");
        DatabaseReference orderReference = firebaseDatabase.getReference().child("Order");

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        orderStatusReference.orderByChild("status").startAt("0").endAt("3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    OrderStatus orderStatus = dataSnapshot.getValue(OrderStatus.class);
                    Log.d("TAG", "onDataChange: "+orderStatus.getOrder_id());
                        if (orderStatus != null ) {
                            data_OrderStatus.clear();
                            String orderId = orderStatus.getOrder_id();
                            if (orderId != null) {
                                orderReference.child(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot orderSnapshot) {
                                        if (orderSnapshot.exists()) {
                                            String orderUserId = orderSnapshot.child("customer_id").getValue(String.class);

                                            if (orderUserId != null && orderUserId.equals(currentUserId)) {

                                                data_OrderStatus.add(orderStatus);

                                            }

                                        }
                                        recyclerViewOderCustomer.notifyDataSetChanged();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Xử lý lỗi nếu cần
                                    }
                                });
                            }
                        }
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void setSearchView()
    {
            searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.bottomNavigationView.setVisibility(View.GONE);
                    MainActivity.toolbar.setVisibility(View.GONE);
                    isSearchViewExpanded = true;

                }
            });

            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {

                    if (isSearchViewExpanded) {
                        MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
                        MainActivity.toolbar.setVisibility(View.VISIBLE);
                    }
                    return false;
                }
            });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                filterList(newText);

                return false;

            }
        });
    }


    private void filterList(String text) {
        ArrayList<OrderStatus> filteredlist = new ArrayList<>();
        for (OrderStatus item : data_OrderStatus) {
            if (item.getOrder_id().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }

        recyclerViewOderCustomer.setFilterList(filteredlist);
    }
}