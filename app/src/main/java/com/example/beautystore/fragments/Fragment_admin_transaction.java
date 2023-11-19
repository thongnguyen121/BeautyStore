package com.example.beautystore.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.SearchView;

import com.example.beautystore.R;
import com.example.beautystore.adapter.RecyclerViewOrder_queue;
import com.example.beautystore.model.OrderStatus;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Fragment_admin_transaction extends Fragment {

    View view;
    RecyclerView rcTransaction_history;
    RecyclerViewOrder_queue recyclerViewOrderQueue;
    SearchView searchView;
    RadioButton rdoAll, rdoDelivered, rdoCancled;
    ArrayList<OrderStatus> data_OrderStatus = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin_transaction, container, false);

        setControl(view);
        getData_All();
        setSearchView();
        rdoAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                   getData_All();
                }

            }
        });
        rdoDelivered.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    getData_Delivered();
                }
            }
        });
        rdoCancled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                   getData_Canceled();
                }

            }
        });
        return view;
    }

    private void setControl(View view) {
        rcTransaction_history = view.findViewById(R.id.rcTransaction_history_admin);
        searchView = view.findViewById(R.id.idsearchview_transaction_history_admin);
        rdoAll = view.findViewById(R.id.rdoAll_history_admin);
        rdoDelivered = view.findViewById(R.id.rdoDelivered);
        rdoCancled = view.findViewById(R.id.rdoCancel);
    }
    private void getData_All()
    {
        recyclerViewOrderQueue = new RecyclerViewOrder_queue(requireContext(), R.layout.layout_item_order_queue, data_OrderStatus);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getContext(), 1);
        layoutManager1.setOrientation(RecyclerView.VERTICAL);
        rcTransaction_history.setLayoutManager(layoutManager1);
        rcTransaction_history.setAdapter(recyclerViewOrderQueue);

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
                    if( orderStatus.getStatus().equals("6") || orderStatus.getStatus().equals("7")  )
                    {
                        data_OrderStatus.add(orderStatus);
                    }

                }
                recyclerViewOrderQueue.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getData_Delivered()
    {
        recyclerViewOrderQueue = new RecyclerViewOrder_queue(requireContext(), R.layout.layout_item_order_queue, data_OrderStatus);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getContext(), 1);
        layoutManager1.setOrientation(RecyclerView.VERTICAL);
        rcTransaction_history.setLayoutManager(layoutManager1);
        rcTransaction_history.setAdapter(recyclerViewOrderQueue);

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
                    if( orderStatus.getStatus().equals("6") )
                    {
                        data_OrderStatus.add(orderStatus);
                    }

                }
                recyclerViewOrderQueue.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getData_Canceled()
    {
        recyclerViewOrderQueue = new RecyclerViewOrder_queue(requireContext(), R.layout.layout_item_order_queue, data_OrderStatus);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getContext(), 1);
        layoutManager1.setOrientation(RecyclerView.VERTICAL);
        rcTransaction_history.setLayoutManager(layoutManager1);
        rcTransaction_history.setAdapter(recyclerViewOrderQueue);

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
                    if( orderStatus.getStatus().equals("7") )
                    {
                        data_OrderStatus.add(orderStatus);
                    }

                }
                recyclerViewOrderQueue.notifyDataSetChanged();
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

        recyclerViewOrderQueue.setFilterList(filteredlist);
    }
}