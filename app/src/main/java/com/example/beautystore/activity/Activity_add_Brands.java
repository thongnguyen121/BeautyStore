package com.example.beautystore.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.beautystore.R;

public class Activity_add_Brands extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_brands);

        ImageView imageView = findViewById(R.id.btnNut);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                Toast.makeText(Activity_add_Brands.this, "bam ddc", Toast.LENGTH_SHORT).show();
            }
        });
    }
}