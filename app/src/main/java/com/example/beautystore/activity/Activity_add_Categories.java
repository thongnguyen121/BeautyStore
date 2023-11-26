package com.example.beautystore.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.beautystore.R;
import com.example.beautystore.fragments.Fragment_warehouse_list;
import com.example.beautystore.model.Categories;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
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
    private MaterialCardView cardView;
    private TextView tvCondition_cate, tvTile;
    private ConstraintLayout linearLayout;
    boolean status;
    SpinKitView spinKitView;

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
        condition_cate_name();
        focusOut();

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

        } else {
            btnAddCate.setVisibility(View.GONE);
        }
        registerResult();
        imgCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                if (Build.VERSION.SDK_INT > 30) {
                    i = new Intent(MediaStore.ACTION_PICK_IMAGES);
                } else {
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);
                }
                resultLauncher.launch(i);
                edtNameCate.clearFocus();
                imgCategory.requestFocus();
            }
        });
        if (status == false) {
            loadCategory(id_cate);
        }
        btnAddCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEnable(false);
                spinKitView.setVisibility(View.VISIBLE);
                if (imageURI == null || TextUtils.isEmpty(edtNameCate.getText())) {
                    Toast.makeText(Activity_add_Categories.this, "Vui lòng cung cấp đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    if (edtNameCate.length() > 50)
                    {
                        Toast.makeText(Activity_add_Categories.this, "Tên loại quá dài", Toast.LENGTH_SHORT).show();
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
                                setEnable(true);
                                spinKitView.setVisibility(View.GONE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Activity_add_Categories.this, "Không thể thêm loại", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
        btnEditCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameCate = edtNameCate.getText().toString();
                reference = FirebaseDatabase.getInstance().getReference("Categories").child(id_cate);
                setEnable(false);
                spinKitView.setVisibility(View.VISIBLE);
                if(edtNameCate.length()> 50)
                {
                    Toast.makeText(Activity_add_Categories.this, "Tên loại quá dài", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (imageURI == null ){
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put("categories_name", nameCate);
                                    reference.updateChildren(updates);
                                    Toast.makeText(Activity_add_Categories.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                    setEnable(true);
                                    spinKitView.setVisibility(View.GONE);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(Activity_add_Categories.this, "Không thể cập nhật", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
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
                                            Toast.makeText(Activity_add_Categories.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                            setEnable(true);
                                            spinKitView.setVisibility(View.GONE);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(Activity_add_Categories.this, "Không thể cập nhật", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Activity_add_Categories.this, "Không thể cập nhật", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
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
                } catch (Exception e) {
                    Log.e("TAG", "onActivityResult: ",e );
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
                int idNumber = Integer.parseInt(temp[1])+1;
                if (idNumber < 10) {
                    id = "CT0" + idNumber;
                } else {
                    id = "CT" + idNumber;
                }
                autoId_category = id;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setEnable(boolean b) {
        ivBack.setEnabled(b);
        btnAddCate.setEnabled(b);
        btnEditCate.setEnabled(b);
        edtNameCate.setEnabled(b);
        imgCategory.setEnabled(b);
    }

    private void setControl() {
        ivBack = findViewById(R.id.ivBack);
        imgCategory = findViewById(R.id.ivCategory);
        edtNameCate = findViewById(R.id.edtCateName);
        btnAddCate = findViewById(R.id.btnAddCate);
        btnEditCate = findViewById(R.id.btnEditCate);
        linearLayout = findViewById(R.id.liner_add_cate);
        cardView = findViewById(R.id.card_add_cate_screen);
        tvCondition_cate = findViewById(R.id.tv_addCate_screen);
        tvTile = findViewById(R.id.tvTitle_add_cate_screen);
        spinKitView = findViewById(R.id.spin_kitCate);

    }
    private void condition_cate_name() {
        edtNameCate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputText = s.toString().trim();
                if (!inputText.isEmpty()) {
                    if (inputText.length() > 50) {
                        cardView.setStrokeColor(ContextCompat.getColor(Activity_add_Categories.this, R.color.red));
                    } else {
                        cardView.setStrokeColor(ContextCompat.getColor(Activity_add_Categories.this, R.color.blue));
                        if (!edtNameCate.hasFocus()) {
                            cardView.setStrokeColor(Color.TRANSPARENT);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 50) {
                    cardView.setStrokeColor(ContextCompat.getColor(Activity_add_Categories.this, R.color.red));
                    tvCondition_cate.setVisibility(View.VISIBLE);
                    tvCondition_cate.setText("Bạn đã nhập quá 7 kí tự");
                } else {
                    if (!edtNameCate.hasFocus()) {
                        cardView.setStrokeColor(Color.TRANSPARENT);
                    }
                    tvCondition_cate.setVisibility(View.GONE);
                }
            }
        });
        edtNameCate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String inputText = edtNameCate.getText().toString().trim();
                if (!hasFocus) {
                    if (inputText.isEmpty()) {
                        cardView.setStrokeColor(ContextCompat.getColor(Activity_add_Categories.this, R.color.red));
                        tvCondition_cate.setVisibility(View.VISIBLE);
                        tvCondition_cate.setText("Bạn cần nhập giá của sản phẩm");
                    } else if (inputText.length() > 50) {
                        cardView.setStrokeColor(ContextCompat.getColor(Activity_add_Categories.this, R.color.red));
                    } else {
                        cardView.setStrokeColor(Color.TRANSPARENT);
                    }
                } else {
                    if (inputText.length() > 50) {
                        cardView.setStrokeColor(ContextCompat.getColor(Activity_add_Categories.this, R.color.red));
                    } else {
                        cardView.setStrokeColor(ContextCompat.getColor(Activity_add_Categories.this, R.color.blue));
                        tvCondition_cate.setVisibility(View.GONE);
                    }
                }
            }
        });

    }
    private void focusOut() {
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    View focusedView = getCurrentFocus();
                    if (focusedView instanceof EditText) {
                        Rect outRect = new Rect();
                        focusedView.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                            focusedView.clearFocus();
                        }
                    }
                }
                return false;
            }
        });
    }
}