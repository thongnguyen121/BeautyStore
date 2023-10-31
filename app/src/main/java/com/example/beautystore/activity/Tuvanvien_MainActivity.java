package com.example.beautystore.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.beautystore.LoginActivity;
import com.example.beautystore.MainActivity;
import com.example.beautystore.R;
import com.google.firebase.auth.FirebaseAuth;

public class Tuvanvien_MainActivity extends AppCompatActivity {
    Button btnLogout;
    public static final String SHARE_PREFS = "sharedPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuvanvien_main);
        btnLogout = findViewById(R.id.btnConsultantLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("check", "");
                editor.apply();
                Intent intent = new Intent(Tuvanvien_MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}