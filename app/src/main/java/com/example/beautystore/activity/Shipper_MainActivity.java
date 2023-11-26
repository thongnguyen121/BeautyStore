package com.example.beautystore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.beautystore.LoginActivity;
import com.example.beautystore.MainActivity;
import com.example.beautystore.R;
import com.example.beautystore.fragments.Fragment_list_order_memberShipper;
import com.example.beautystore.fragments.Fragment_order_shipper;
import com.example.beautystore.model.OrderStatus;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Shipper_MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    AppBarConfiguration appBarConfiguration;
    NavController navController;
    public static final String SHARE_PREFS = "sharedPrefs";
    int counterOrder, counterReceivedOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipper_main);
        bottomNavigationView = findViewById(R.id.idBottomNavigationShipper);
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_container_shipper);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        getCounterOrderList();
        getCounterReceivedOrder();


    }

    private void getCounterReceivedOrder() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference orderStatusReference = firebaseDatabase.getReference().child("OrderStatus");
        orderStatusReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    counterReceivedOrder = 0;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        OrderStatus orderStatus = dataSnapshot.getValue(OrderStatus.class);
                        if (orderStatus.getMember_id().equals(FirebaseAuth.getInstance().getUid())){
                        if( orderStatus.getStatus().equals("3"))
                        {
                            counterReceivedOrder ++;
                        }
                        }

                    }
                    if (counterReceivedOrder>0){
                        bottomNavigationView.getOrCreateBadge(R.id.fragment_list_order_memberShipper).setNumber(counterReceivedOrder);
                    }
                    else {
                        bottomNavigationView.removeBadge(R.id.fragment_list_order_memberShipper);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getCounterOrderList() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference orderStatusReference = firebaseDatabase.getReference().child("OrderStatus");
        orderStatusReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    counterOrder = 0;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String status = dataSnapshot.child("status").getValue(String.class);
                        if (status.equals("2")) {
                            counterOrder++;
                        }

                    }
                    if (counterOrder > 0) {
                        bottomNavigationView.getOrCreateBadge(R.id.fragment_order_shipper).setNumber(counterOrder);
                    }
                    else{
                        bottomNavigationView.removeBadge(R.id.fragment_order_shipper);
                    }
                }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}