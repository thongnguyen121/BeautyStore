package com.example.beautystore.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;

import com.example.beautystore.R;
import com.example.beautystore.adapter.RecyclerViewOrder_queue;
import com.example.beautystore.adapter.RecyclerViewProgress_order;
import com.example.beautystore.model.OrderStatus;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Fragment_order_queue extends Fragment {

    RecyclerViewOrder_queue recyclerViewOrderQueue;
    RecyclerView rcOrderqueue_admin;
    View view;
    ArrayList<OrderStatus> data_OrderStatus = new ArrayList<>();
    ImageView imgFilter;
    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_order_queue, container, false);

       setControl(view);
       getData_order();

       imgFilter.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               showPopupMenu(v);
           }
       });
       setSearchView();

       return view;
    }

    private void setControl(View view) {
        rcOrderqueue_admin = view.findViewById(R.id.rcOrder_admin_queue_fr);
        imgFilter = view.findViewById(R.id.btnFileter_orderQueue);
        searchView = view.findViewById(R.id.idsearchview_order_queue);
    }
    private void getData_order()
    {
        recyclerViewOrderQueue = new RecyclerViewOrder_queue(requireContext(), R.layout.layout_item_order_queue, data_OrderStatus);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getContext(), 1);
        layoutManager1.setOrientation(RecyclerView.VERTICAL);
        rcOrderqueue_admin.setLayoutManager(layoutManager1);
        rcOrderqueue_admin.setAdapter(recyclerViewOrderQueue);

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
                    if( orderStatus.getStatus().equals("2") || orderStatus.getStatus().equals("3") || orderStatus.getStatus().equals("4") || orderStatus.getStatus().equals("5") )
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
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_orders_queue, popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.All_order){
                   getData_order();
                }
                else if (id == R.id.Order_awaiting_shipping)
                {
                   filterOrderMenu("2");
                }
                else if (id == R.id.Order_being_received)
                {
                    filterOrderMenu("3");
                }
                else if (id == R.id.Order_canceled)
                {
                    filterOrderMenu("5");
                }
                else if (id == R.id.Order_received)
                {
                    filterOrderMenu("4");
                }

                return true;
            }
        });
    }
    private void filterOrderMenu(String status)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference orderStatusReference = firebaseDatabase.getReference().child("OrderStatus");
        Query query = orderStatusReference.orderByChild("status").equalTo(status);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data_OrderStatus.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    OrderStatus orderStatus = dataSnapshot.getValue(OrderStatus.class);
                    data_OrderStatus.add(orderStatus);
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