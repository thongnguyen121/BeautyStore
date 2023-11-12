package com.example.beautystore.activity;

import androidx.appcompat.app.AppCompatActivity;
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
import com.example.beautystore.fragments.Fragment_order_shipper;
import com.google.firebase.auth.FirebaseAuth;

public class Shipper_MainActivity extends AppCompatActivity {
Button btnLogout;
    public static final String SHARE_PREFS = "sharedPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipper_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new Fragment_order_shipper());
        fragmentTransaction.commit();
    }

}