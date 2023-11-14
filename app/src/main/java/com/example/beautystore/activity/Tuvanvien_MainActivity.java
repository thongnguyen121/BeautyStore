package com.example.beautystore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.beautystore.LoginActivity;
import com.example.beautystore.MainActivity;
import com.example.beautystore.R;
import com.example.beautystore.model.Members;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Tuvanvien_MainActivity extends AppCompatActivity {
    Button btnLogout;
    public static final String SHARE_PREFS = "sharedPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuvanvien_main);
        checkStatus();
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

    private void checkStatus() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Member");
        reference.child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    Members members = snapshot.getValue(Members.class);
                    if (members.getStatus().equals("1")){
                        FirebaseAuth.getInstance().signOut();
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("check", "");
                        editor.apply();
                        Intent intent = new Intent(Tuvanvien_MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}