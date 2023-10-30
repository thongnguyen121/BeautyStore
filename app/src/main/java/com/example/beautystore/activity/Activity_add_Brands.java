package com.example.beautystore.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.beautystore.R;
import com.example.beautystore.fragments.Fragment_warehouse_list;
import com.example.beautystore.model.Brands;
import com.example.beautystore.model.Categories;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity_add_Brands extends AppCompatActivity {

    private ImageView imgBack, imgBrand;
    private TextView tvBrand_name;
    private Button btnAdd, btnEdit;
    private EditText edtBrand_name;
    ActivityResultLauncher<Intent> resultLaucher;
    Uri imageUri_Brands;
    String brands_id = "", autoId_brands,  nameBrand;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_brands);

        setControl();
        registerResult();
        add_Brands();
        edit_Brands();
        brands_id = getIntent().getStringExtra("brands_id");
        Toast.makeText(this, "id" + brands_id, Toast.LENGTH_SHORT).show();
        if (Fragment_warehouse_list.statusBrands) {
            btnEdit.setVisibility(View.GONE);
        } else {
            btnAdd.setVisibility(View.GONE);
            edit_getData(brands_id);

        }
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void setControl() {
        imgBrand = findViewById(R.id.img_Add_Brand_screen);
        imgBack = findViewById(R.id.img_back_brand);
        tvBrand_name = findViewById(R.id.tvBrand);
        btnAdd = findViewById(R.id.btnAdd_Brands_screeh);
        btnEdit = findViewById(R.id.btnEdit_Brands_screen);
        edtBrand_name = findViewById(R.id.edt_add_brand_screen);
    }

    private void edit_getData(String brand_id) {
       FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
       DatabaseReference databaseReference = firebaseDatabase.getReference().child("Brands");
        databaseReference.child(brand_id).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Brands brands = snapshot.getValue(Brands.class);
                edtBrand_name.setText(brands.getBrands_name());
                Glide.with(Activity_add_Brands.this).load(brands.getImg_brands()).into(imgBrand);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void add_Brands() {


        getIDBrands();
        imgBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri_Brands == null || TextUtils.isEmpty(edtBrand_name.getText())) {
                    Toast.makeText(Activity_add_Brands.this, "Vui long cung cap day du thong tin", Toast.LENGTH_SHORT).show();
                } else {
                   DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Brands/" + autoId_brands);
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("imgProducts").child(autoId_brands);
                    storageReference.putFile(imageUri_Brands).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete()) ;
                            imageUri_Brands = uriTask.getResult();
                            Brands brands = new Brands(autoId_brands, edtBrand_name.getText().toString(), imageUri_Brands.toString());
                            databaseReference.setValue(brands);
                            onBackPressed();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Activity_add_Brands.this, "ko ther", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Toast.makeText(Activity_add_Brands.this, "dep chai" + autoId_brands, Toast.LENGTH_SHORT).show();


                }
            }
        });

    }
    private void edit_Brands(){

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameBrand = edtBrand_name.getText().toString();

                DatabaseReference databaseReference;
                databaseReference = FirebaseDatabase.getInstance().getReference("Brands").child(brands_id);
                if (imageUri_Brands == null ){
                    Toast.makeText(Activity_add_Brands.this, "ko co hinh", Toast.LENGTH_SHORT).show();
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("brands_name", nameBrand);
                                databaseReference.updateChildren(updates);
                                Toast.makeText(Activity_add_Brands.this, "Cap nhat thanh cong", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Activity_add_Brands.this, "Ko dc", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(Activity_add_Brands.this, "co hinh", Toast.LENGTH_SHORT).show();
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("imgProducts").child(brands_id);
                    storageReference.putFile(imageUri_Brands).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete()) ;
                            imageUri_Brands = uriTask.getResult();
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        Map<String, Object> updatesCategory = new HashMap<>();
                                        updatesCategory.put("brands_name", nameBrand);
                                        updatesCategory.put("img_brands",imageUri_Brands.toString());
                                        databaseReference.updateChildren(updatesCategory);
                                        Toast.makeText(Activity_add_Brands.this, "update thanh cong", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(Activity_add_Brands.this, "ko dc", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Activity_add_Brands.this, "ko ther", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

    }


    private void registerResult() {
        resultLaucher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                try {
                    imageUri_Brands = result.getData().getData();
                    imgBrand.setImageURI(imageUri_Brands);
                    Log.d("TAG", "hinh anh: " + "." + imageUri_Brands.getLastPathSegment());
                } catch (Exception e) {

                    Toast.makeText(Activity_add_Brands.this, "No image select", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void pickImage() {
        Intent i = new Intent();

        if (Build.VERSION.SDK_INT > 30) {
            i = new Intent(MediaStore.ACTION_PICK_IMAGES);
            Toast.makeText(Activity_add_Brands.this, "android 13", Toast.LENGTH_SHORT).show();
        } else {
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            Toast.makeText(Activity_add_Brands.this, "android 10", Toast.LENGTH_SHORT).show();
        }
        resultLaucher.launch(i);
    }

    public void getIDBrands() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Brands");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> dsUser = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dsUser.add(dataSnapshot.getKey());
                }
                String[] temp = dsUser.get(dsUser.size() - 1).split("BD");
                String id = "";
                if (Integer.parseInt(temp[1]) < 10) {
                    id = "BD0" + (Integer.parseInt(temp[1]) + 1);
                } else {
                    id = "BD" + (Integer.parseInt(temp[1]) + 1);
                }
                autoId_brands = id;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}