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
import com.example.beautystore.adapter.RecyclerViewOder_Customer;
import com.example.beautystore.model.OrderStatus;
import com.example.beautystore.model.Products;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Fragment_transaction_history extends Fragment {


    RecyclerView rcTransaction_history;
    RecyclerViewOder_Customer recyclerViewOderCustomer;

    SearchView searchView;
    View view;
    ArrayList<OrderStatus> data_OrderStatus = new ArrayList<>();
    private boolean isSearchViewExpanded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_transaction_history, container, false);

        setControl(view);
        getData_orderList();
        setSearchView();
        return view;
    }

    private void setControl(View view) {

        rcTransaction_history = view.findViewById(R.id.rcTransaction_history_customer);
        searchView = view.findViewById(R.id.idsearchview_transaction_history_customer);

    }
    private void getData_orderList() {
        recyclerViewOderCustomer = new RecyclerViewOder_Customer(requireContext(), R.layout.layout_items_orders_customer, data_OrderStatus);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getContext(), 1);
        layoutManager1.setOrientation(RecyclerView.VERTICAL);
        rcTransaction_history.setLayoutManager(layoutManager1);
        rcTransaction_history.setAdapter(recyclerViewOderCustomer);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference orderStatusReference = firebaseDatabase.getReference().child("OrderStatus");
        DatabaseReference orderReference = firebaseDatabase.getReference().child("Order");

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                orderStatusReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (data_OrderStatus != null) {
                    data_OrderStatus.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    OrderStatus orderStatus = dataSnapshot.getValue(OrderStatus.class);

                        if (orderStatus != null && orderStatus.getStatus().equals("4") || orderStatus.getStatus().equals("6")) {

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
                                            recyclerViewOderCustomer.notifyDataSetChanged();

                                        }
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
                // Xử lý lỗi nếu cần
            }
        });

    }


    private void setSearchView() {
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
        if(filteredlist.isEmpty())
        {

        }
        else
        {
            recyclerViewOderCustomer.setFilterList(filteredlist);
        }


    }
}