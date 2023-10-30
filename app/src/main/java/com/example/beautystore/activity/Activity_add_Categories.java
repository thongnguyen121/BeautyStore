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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.beautystore.R;
import com.example.beautystore.fragments.Fragment_warehouse_list;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity_add_Categories extends AppCompatActivity {
    ImageView ivBack, imgCategory;
    EditText edtNameCate;
    Button btnAddCate, btnEditCate;
    ActivityResultLauncher<Intent> resultLauncher;
    Uri imageURI;
    String id_cate = "",autoId_category , nameCate,oldImageURI;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, reference;
    boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_categories);
        setControl();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        status = Fragment_warehouse_list.statusCate;
        id_cate = getIntent().getStringExtra("id_category");
        setEvent();

    }

    private void loadCategory(String id_cate) {
        databaseReference.child("Categories").child(id_cate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Categories categories = snapshot.getValue(Categories.class);
                edtNameCate.setText(categories.getCategories_name());
                Glide.with(Activity_add_Categories.this).load(categories.getImg_categories()).into(imgCategory);
                oldImageURI = categories.getImg_categories();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        Toast.makeText(this, "id la " + id_cate, Toast.LENGTH_SHORT).show();
    }

    private void setEvent() {
        getIDCategories();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (status) {
            btnEditCate.setVisibility(View.GONE);
            Log.d("TAG", "id cua loai " + id_cate);
        } else {
            btnAddCate.setVisibility(View.GONE);
            Log.d("TAG", "id cua loai " + id_cate);
        }
        registerResult();
        imgCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                if (Build.VERSION.SDK_INT > 30) {
                    i = new Intent(MediaStore.ACTION_PICK_IMAGES);
                    Toast.makeText(Activity_add_Categories.this, "android 13", Toast.LENGTH_SHORT).show();
                } else {
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);
                }
                resultLauncher.launch(i);
            }
        });
        if (status == false) {
            loadCategory(id_cate);
        }
        btnAddCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageURI == null || TextUtils.isEmpty(edtNameCate.getText())) {
                    Toast.makeText(Activity_add_Categories.this, "Vui long cung cap day du thong tin", Toast.LENGTH_SHORT).show();
                } else {
                    reference = FirebaseDatabase.getInstance().getReference("Categories/" + autoId_category);
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("imgProducts").child(autoId_category);
                    storageReference.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete()) ;
                            imageURI = uriTask.getResult();
                            Categories categories = new Categories(autoId_category, edtNameCate.getText().toString(), imageURI.toString());
                            reference.setValue(categories);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Activity_add_Categories.this, "ko ther", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Toast.makeText(Activity_add_Categories.this, "dep chai" + autoId_category, Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnEditCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameCate = edtNameCate.getText().toString();
                reference = FirebaseDatabase.getInstance().getReference("Categories").child(id_cate);
                if (imageURI == null ){
                    Toast.makeText(Activity_add_Categories.this, "ko co hinh", Toast.LENGTH_SHORT).show();
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("categories_name", nameCate);
                                reference.updateChildren(updates);
                                Toast.makeText(Activity_add_Categories.this, "Cap nhat thanh cong", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Activity_add_Categories.this, "Ko dc", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(Activity_add_Categories.this, "co hinh", Toast.LENGTH_SHORT).show();
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("imgProducts").child(id_cate);
                    storageReference.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete()) ;
                            imageURI = uriTask.getResult();
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        Map<String, Object> updatesCategory = new HashMap<>();
                                        updatesCategory.put("categories_name", nameCate);
                                        updatesCategory.put("img_categories",imageURI.toString());
                                        reference.updateChildren(updatesCategory);
                                        Toast.makeText(Activity_add_Categories.this, "update thanh cong", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(Activity_add_Categories.this, "ko dc", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Activity_add_Categories.this, "ko ther", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    private void registerResult() {
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                try {
                    imageURI = o.getData().getData();
                    imgCategory.setImageURI(imageURI);
                    Log.d("TAG", "hinh anh: " + imageURI.getLastPathSegment());
                } catch (Exception e) {
                    Toast.makeText(Activity_add_Categories.this, "no img selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getIDCategories() {
        databaseReference.child("Categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> dsUser = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dsUser.add(dataSnapshot.getKey());
                }
                String[] temp = dsUser.get(dsUser.size() - 1).split("CT");
                String id = "";
                if (Integer.parseInt(temp[1]) < 10) {
                    id = "CT0" + (Integer.parseInt(temp[1]) + 1);
                } else {
                    id = "CT" + (Integer.parseInt(temp[1]) + 1);
                }
                autoId_category = id;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setControl() {
        ivBack = findViewById(R.id.ivBack);
        imgCategory = findViewById(R.id.ivCategory);
        edtNameCate = findViewById(R.id.edtCateName);
        btnAddCate = findViewById(R.id.btnAddCate);
        btnEditCate = findViewById(R.id.btnEditCate);
    }
}