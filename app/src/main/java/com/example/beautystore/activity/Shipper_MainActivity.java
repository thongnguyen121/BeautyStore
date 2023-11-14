package com.example.beautystore.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.firebase.auth.FirebaseAuth;

public class Shipper_MainActivity extends AppCompatActivity {
    Button btnDSDH, btnDSDH_shipper;;
    public static final String SHARE_PREFS = "sharedPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipper_main);

        setControl();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new Fragment_order_shipper());
        fragmentTransaction.commit();


        btnDSDH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new Fragment_order_shipper());
            }
        });

        btnDSDH_shipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new Fragment_list_order_memberShipper());
            }
        });

    }

    private void setControl() {
        btnDSDH = findViewById(R.id.DSDH);
        btnDSDH_shipper = findViewById(R.id.DSDH_shipper);
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

}