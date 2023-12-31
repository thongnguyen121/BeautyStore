package com.example.beautystore.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.beautystore.R;
import com.example.beautystore.adapter.RecyclerViewProgress_order;
import com.example.beautystore.model.OrderStatus;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Fragment_packing_order extends Fragment {

    RecyclerViewProgress_order recyclerViewOderprogress;
    RecyclerView rcOrderpacking_admin;
    View view;
    ArrayList<OrderStatus> data_OrderStatus = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    String uid;
    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_packing_order, container, false);

       setControl(view);
       getData_orderPacking();
       setSearchView();
       return view;
    }

    private void setControl(View view) {
        rcOrderpacking_admin = view.findViewById(R.id.rcOrder_admin_packing);
        searchView = view.findViewById(R.id.idsearchview_orderPacking_admin);
    }
    private void getData_orderPacking()
    {
        recyclerViewOderprogress = new RecyclerViewProgress_order(requireContext(), R.layout.layout_item_order_progress, data_OrderStatus);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getContext(), 1);
        layoutManager1.setOrientation(RecyclerView.VERTICAL);
        rcOrderpacking_admin.setLayoutManager(layoutManager1);
        rcOrderpacking_admin.setAdapter(recyclerViewOderprogress);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference orderStatusReference = firebaseDatabase.getReference().child("OrderStatus");
        orderStatusReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (data_OrderStatus != null) {
                    data_OrderStatus.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    OrderStatus orderStatus = dataSnapshot.getValue(OrderStatus.class);
                    if(orderStatus.getStatus().equals("1"))
                    {
                        data_OrderStatus.add(orderStatus);
                    }

                }
                recyclerViewOderprogress.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setSearchView()
    {
//            searchView.setOnSearchClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    isSearchViewExpanded = true;
//                }
//            });
//
//            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//                @Override
//                public boolean onClose() {
//
//                    if (isSearchViewExpanded) {
//
//                    }
//                    return false;
//                }
//            });
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
            recyclerViewOderprogress.setFilterList(filteredlist);
        }


    }

}