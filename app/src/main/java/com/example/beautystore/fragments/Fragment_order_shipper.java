package com.example.beautystore.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.beautystore.MainActivity;
import com.example.beautystore.R;
import com.example.beautystore.activity.Shipper_MainActivity;
import com.example.beautystore.adapter.RecyclerViewOrder_queue;
import com.example.beautystore.model.OrderStatus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Fragment_order_shipper extends Fragment {

    RecyclerViewOrder_queue recyclerViewOrderQueue;
    RecyclerView rcOrderqueue_shipper;
    ArrayList<OrderStatus> data_OrderStatus = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    String uid;

    public static final String SHARE_PREFS = "sharedPrefs";
    Button btnLogout;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_shipper, container, false);

        setControl(view);
        getData_order();
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("check", "");
                editor.apply();
                Intent intent = new Intent(requireContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    private void setControl(View view) {
        btnLogout = view.findViewById(R.id.btnShipperLogout);
        rcOrderqueue_shipper = view.findViewById(R.id.rcOrder_shipper);

    }
    private void getData_order()
    {
        recyclerViewOrderQueue = new RecyclerViewOrder_queue(requireContext(), R.layout.layout_item_order_queue, data_OrderStatus);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getContext(), 1);
        layoutManager1.setOrientation(RecyclerView.VERTICAL);
        rcOrderqueue_shipper.setLayoutManager(layoutManager1);
        rcOrderqueue_shipper.setAdapter(recyclerViewOrderQueue);

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
                    if( orderStatus.getStatus().equals("2") || orderStatus.getStatus().equals("3"))
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


}