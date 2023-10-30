package com.example.beautystore.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.beautystore.R;
import com.example.beautystore.fragments.Fragment_warehouse_list;
import com.example.beautystore.model.Brands;
import com.example.beautystore.model.Categories;
import com.example.beautystore.model.Products;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Activity_Add_Products extends AppCompatActivity {

    private ArrayAdapter<String> adapter_categories;
    private ArrayAdapter<String> adapter_brands;

    private ArrayList<String> data_categories = new ArrayList<>();
    private ArrayList<String> data_brands = new ArrayList<>();
    private Spinner spinner_categories, spinner_brands;
    private ImageView img_products_1, img_products_2, img_products_3, img_gallery;
    private EditText edt_products_name, edt_products_price, edt_products_quantity, edt_products_description;
    private Button btn_add, btn_edit;
    private ImageView imgBack;
    private MaterialCardView cardView_products_name, cardView_products_price, cardView_products_quantity,
            cardView_products_description, cardView_cate, cardView_brands;
    private TextView tv_products_name, tv_products_price, tv_products_quantity, tv_products_description,
            tv_categories, tv_brands;
    private ScrollView scrollView;
    ActivityResultLauncher<Intent> resultLaucher_1, resultLauncher_2, resultLauncher_3;
    Uri imageUri_1, imageUri_2, imageUri_3;
    Uri [] uris = new Uri[3];
    private String products_id ="", categories_id ="", brands_id = "", autoId_products;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        setControl();
        getSpinnerCategories();
        getSpinnerBrands();
        condition_edtProducts_price();
        condition_edtProducts_quantity();
        condition_edtProducts_description();
        condition_edtProducts_name();
        setFocus_spinner();
        focusOut();
        getImage();
        products_id = getIntent().getStringExtra("products_id");
        categories_id = getIntent().getStringExtra("categories_id");
        brands_id =  getIntent().getStringExtra("brands_id");
        if (Fragment_warehouse_list.statusProducts){
            btn_edit.setVisibility(View.GONE);

        }
        else {
            btn_add.setVisibility(View.GONE);
            intent_getData(products_id);

        }



        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    private void setControl() {
        imgBack = findViewById(R.id.img_back_addProducts_screen);
        spinner_categories = findViewById(R.id.spn_Cate_name);
        spinner_brands = findViewById(R.id.spn_Brands_name);
        edt_products_name = findViewById(R.id.edt_products_name);
        edt_products_quantity = findViewById(R.id.edt_products_quatity);
        edt_products_price = findViewById(R.id.edt_products_price);
        edt_products_description = findViewById(R.id.edt_products_descirption);
        img_products_1 = findViewById(R.id.img_products_add_1);
        img_products_2 = findViewById(R.id.img_products_add_2);
        img_products_3 = findViewById(R.id.img_products_add_3);

        btn_add = findViewById(R.id.btnAdd_products);
        btn_edit = findViewById(R.id.btnEdit_products);
        tv_products_name = findViewById(R.id.tv_edt_products_name);
        tv_products_price = findViewById(R.id.tv_edit_products_price);
        tv_products_quantity = findViewById(R.id.tv_edit_products_quantity);
        tv_products_description = findViewById(R.id.tv_edit_products_description);
        tv_categories = findViewById(R.id.tv_spn_cate);
        tv_brands = findViewById(R.id.tv_spn_brands);
        cardView_products_name = findViewById(R.id.mcv_products_name);
        cardView_products_price = findViewById(R.id.mcv_products_price);
        cardView_products_quantity = findViewById(R.id.mcv_products_quantity);
        cardView_products_description = findViewById(R.id.mcv_products_description);
        scrollView = findViewById(R.id.id_scrollView);

    }
    private void getSpinnerCategories() {

        adapter_categories = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data_categories);
        adapter_categories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_categories.setAdapter(adapter_categories);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Categories");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (data_categories != null) {
                    data_categories.clear();
                    data_categories.add(0,"chọn danh mục loại");
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Categories categories = dataSnapshot.getValue(Categories.class);
                    String cate_name = categories.getCategories_name();

                    data_categories.add(cate_name);
                    int pos = adapter_categories.getPosition(categories.getCategories_name());
                    if(categories.getCategories_id().equals(categories_id))
                    {
                        spinner_categories.setSelection(pos);
                    }

                }

                adapter_categories.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi (nếu cần)
            }
        });
    }
    private void getSpinnerBrands() {

        adapter_brands = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data_brands);
        adapter_brands.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_brands.setAdapter(adapter_brands);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Brands");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (data_brands != null) {
                    data_brands.clear();
                    data_brands.add(0,"chọn danh mục hãng");
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Brands brands = dataSnapshot.getValue(Brands.class);
                    String brands_name = brands.getBrands_name();
                    data_brands.add(brands_name);
                    int pos = adapter_brands.getPosition(brands.getBrands_name());
                    if(brands.getBrands_id().equals(brands_id))
                    {
                        spinner_brands.setSelection(pos);
                    }
                }

                adapter_brands.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi (nếu cần)
            }
        });
    }
    private void focusOut()
    {
        scrollView.setOnTouchListener(new View.OnTouchListener() {
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

    private void getImage()
    {
        registerResult_img_1();
        registerResult_img_2();
        registerResult_img_3();
        img_products_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              pickImage_1();
            }
        });
        img_products_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage_2();
            }
        });
        img_products_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage_3();
            }
        });
    }

    private void condition_edtProducts_price() {
        edt_products_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputText = s.toString().trim();
                if (!inputText.isEmpty()) {
                    if (inputText.length() > 7) {
                        cardView_products_price.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.red));
                    } else {
                        cardView_products_price.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.blue));
                        if (!edt_products_price.hasFocus()) {
                            cardView_products_price.setStrokeColor(Color.TRANSPARENT);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 7) {
                    cardView_products_price.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.pink));
                    tv_products_price.setVisibility(View.VISIBLE);
                    tv_products_price.setText("Bạn đã nhập quá 7 kí tự");
                } else {
                    if (!edt_products_price.hasFocus()) {
                        cardView_products_price.setStrokeColor(Color.TRANSPARENT);
                    }
                    tv_products_price.setVisibility(View.GONE);
                }
            }
        });

        edt_products_price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String inputText = edt_products_price.getText().toString().trim();
                if (!hasFocus) {
                    if (inputText.isEmpty()) {
                        cardView_products_price.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.red));
                        tv_products_price.setVisibility(View.VISIBLE);
                        tv_products_price.setText("Bạn cần nhập giá của sản phẩm");
                    } else if (inputText.length() > 7) {
                        cardView_products_price.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.red));
                    } else {
                        cardView_products_price.setStrokeColor(Color.TRANSPARENT);
                    }
                } else {
                    if (inputText.length() > 7) {
                        cardView_products_price.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.red));
                    } else {
                        cardView_products_price.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.blue));
                        tv_products_price.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
    private void condition_edtProducts_quantity() {
        edt_products_quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputText = s.toString().trim();
                if (!inputText.isEmpty()) {
                    if (inputText.length() > 2) {
                        cardView_products_quantity.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.red));
                    } else {
                        cardView_products_quantity.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.blue));
                        if (!edt_products_quantity.hasFocus()) {
                            cardView_products_quantity.setStrokeColor(Color.TRANSPARENT);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 2) {
                    cardView_products_quantity.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.pink));
                    tv_products_quantity.setVisibility(View.VISIBLE);
                    tv_products_quantity.setText("Bạn đã nhập quá 2 kí tự");
                } else {
                    if (!edt_products_quantity.hasFocus()) {
                        cardView_products_quantity.setStrokeColor(Color.TRANSPARENT);
                    }
                    tv_products_quantity.setVisibility(View.GONE);
                }
            }
        });

        edt_products_quantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String inputText = edt_products_quantity.getText().toString().trim();
                if (!hasFocus) {
                    if (inputText.isEmpty()) {
                        cardView_products_quantity.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.red));
                        tv_products_quantity.setVisibility(View.VISIBLE);
                        tv_products_quantity.setText("Bạn cần nhập giá của sản phẩm");
                    } else if (inputText.length() > 2) {
                        cardView_products_quantity.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.red));
                    } else {
                        cardView_products_quantity.setStrokeColor(Color.TRANSPARENT);
                    }
                } else {
                    if (inputText.length() > 2) {
                        cardView_products_quantity.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.red));
                    } else {
                        cardView_products_quantity.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.blue));
                        tv_products_quantity.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
    private void condition_edtProducts_name() {
        edt_products_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputText = s.toString().trim();
                if (!inputText.isEmpty()) {
                    if (inputText.length() > 200) {
                        cardView_products_name.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.red));
                    } else {
                        cardView_products_name.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.blue));
                        if (!edt_products_name.hasFocus()) {
                            cardView_products_name.setStrokeColor(Color.TRANSPARENT);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 200) {
                    cardView_products_name.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.pink));
                    tv_products_name.setVisibility(View.VISIBLE);
                    tv_products_name.setText("Bạn đã nhập quá 200 kí tự");
                } else {
                    if (!edt_products_name.hasFocus()) {
                        cardView_products_name.setStrokeColor(Color.TRANSPARENT);
                    }
                    tv_products_name.setVisibility(View.GONE);
                }
            }
        });

        edt_products_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String inputText = edt_products_name.getText().toString().trim();
                if (!hasFocus) {
                    if (inputText.isEmpty()) {
                        cardView_products_name.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.red));
                        tv_products_name.setVisibility(View.VISIBLE);
                        tv_products_name.setText("Bạn cần nhập giá của sản phẩm");
                    } else if (inputText.length() > 200) {
                        cardView_products_name.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.red));
                    } else {
                        cardView_products_name.setStrokeColor(Color.TRANSPARENT);
                    }
                } else {
                    if (inputText.length() > 200) {
                        cardView_products_name.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.red));
                    } else {
                        cardView_products_name.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.blue));
                        tv_products_name.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
    private void condition_edtProducts_description() {
        edt_products_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputText = s.toString().trim();
                if (!inputText.isEmpty()) {
                    if (inputText.length() > 500) {
                        cardView_products_description.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.red));
                    } else {
                        cardView_products_description.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.blue));
                        if (!edt_products_description.hasFocus()) {
                            cardView_products_description.setStrokeColor(Color.TRANSPARENT);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 500) {
                    cardView_products_description.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.pink));
                    tv_products_description.setVisibility(View.VISIBLE);
                    tv_products_description.setText("Bạn đã nhập quá 500 kí tự");
                } else {
                    if (!edt_products_description.hasFocus()) {
                        cardView_products_description.setStrokeColor(Color.TRANSPARENT);
                    }
                    tv_products_description.setVisibility(View.GONE);
                }
            }
        });

        edt_products_description.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String inputText = edt_products_description.getText().toString().trim();
                if (!hasFocus) {
                    if (inputText.isEmpty()) {
                        cardView_products_description.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.red));
                        tv_products_description.setVisibility(View.VISIBLE);
                        tv_products_description.setText("Bạn cần nhập giá của sản phẩm");
                    } else if (inputText.length() > 500) {
                        cardView_products_description.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.red));
                    } else {
                        cardView_products_description.setStrokeColor(Color.TRANSPARENT);
                    }
                } else {
                    if (inputText.length() > 500) {
                        cardView_products_description.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.red));
                    } else {
                        cardView_products_description.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.blue));
                        tv_products_description.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
    private void setFocus_spinner(){
        spinner_categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Khi bạn chọn một mục trong Spinner, gọi clearFocus trên EditText
                edt_products_quantity.clearFocus();
                edt_products_name.clearFocus();
                edt_products_price.clearFocus();
                edt_products_price.clearFocus();
                spinner_categories.requestFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Để implement theo yêu cầu cụ thể của bạn, nếu cần.
            }
        });
        spinner_brands.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Khi bạn chọn một mục trong Spinner, gọi clearFocus trên EditText
                edt_products_quantity.clearFocus();
                edt_products_name.clearFocus();
                edt_products_price.clearFocus();
                edt_products_price.clearFocus();
                spinner_brands.requestFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Để implement theo yêu cầu cụ thể của bạn, nếu cần.
            }
        });
    }
    private void registerResult_img_1() {
        resultLaucher_1 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                try {
                    imageUri_1 = result.getData().getData();
                    img_products_1.setImageURI(imageUri_1);
                } catch (Exception e) {

                    Toast.makeText(Activity_Add_Products.this, "No image select", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void registerResult_img_2() {
        resultLauncher_2 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                try {
                    imageUri_2 = result.getData().getData();
                    img_products_2.setImageURI(imageUri_2);
                } catch (Exception e) {

                    Toast.makeText(Activity_Add_Products.this, "No image select", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void registerResult_img_3() {
        resultLauncher_3 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                try {
                    imageUri_3 = result.getData().getData();
                    img_products_3.setImageURI(imageUri_3);
                } catch (Exception e) {

                    Toast.makeText(Activity_Add_Products.this, "No image select", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void pickImage_1() {
        Intent i = new Intent();

        if (Build.VERSION.SDK_INT > 30) {
            i = new Intent(MediaStore.ACTION_PICK_IMAGES);

        } else {
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);

        }
        resultLaucher_1.launch(i);
    }
    private void pickImage_2() {
        Intent i = new Intent();

        if (Build.VERSION.SDK_INT > 30) {
            i = new Intent(MediaStore.ACTION_PICK_IMAGES);

        } else {
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);

        }
        resultLauncher_2.launch(i);
    }
    private void pickImage_3() {
        Intent i = new Intent();

        if (Build.VERSION.SDK_INT > 30) {
            i = new Intent(MediaStore.ACTION_PICK_IMAGES);

        } else {
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);

        }
        resultLauncher_3.launch(i);
    }
    private void intent_getData(String products_id)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Products");
        databaseReference.child(products_id).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                Products products = snapshot.getValue(Products.class);
                edt_products_name.setText(products.getProducts_name());
                edt_products_quantity.setText(products.getQuantity());
                edt_products_price.setText(products.getPrice());
                edt_products_description.setText(products.getDescription());
                Glide.with(Activity_Add_Products.this).load(products.getImgProducts_1()).into(img_products_1);
                Glide.with(Activity_Add_Products.this).load(products.getImgProducts_2()).into(img_products_2);
                Glide.with(Activity_Add_Products.this).load(products.getImgProducts_3()).into(img_products_3);
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });
    }
    private void add_DataProducts(){
        getIDProducts();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri_1 == null ||imageUri_2 == null || imageUri_3 == null || TextUtils.isEmpty(edt_products_name.getText()) || TextUtils.isEmpty(edt_products_price.getText())
                        || TextUtils.isEmpty(edt_products_quantity.getText()) || TextUtils.isEmpty(edt_products_description.getText())) {
                    Toast.makeText(Activity_Add_Products.this, "Vui long cung cap day du thong tin", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products/" + autoId_products);
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("imgProducts").child(autoId_products);
                    String selectedCategory = spinner_categories.getSelectedItem().toString();
                    String selectedBrands = spinner_brands.getSelectedItem().toString();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Date currentDate = new Date();
                    String currentDateString = dateFormat.format(currentDate);
                    Products products = new Products(autoId_products, edt_products_name.getText().toString(), selectedCategory, selectedBrands,
                            edt_products_quantity.getText().toString(), edt_products_price.getText().toString(), edt_products_description.getText().toString()
                            ,currentDateString, null, null, null);

                    databaseReference.setValue(products);

                    // Tải lên hình ảnh vào Firebase Storage
                    // Sử dụng imageUri_1, imageUri_2, và imageUri_3 để tải lên hình ảnh

                    // StorageReference cho hình ảnh 1
                    StorageReference imageRef1 = storageReference.child("image_1.jpg");
                    imageRef1.putFile(imageUri_1).addOnSuccessListener(taskSnapshot -> {
                        // Hình ảnh 1 đã được tải lên thành công, lấy đường dẫn và cập nhật đối tượng Products
                        imageRef1.getDownloadUrl().addOnSuccessListener(uri -> {
                            products.setImgProducts_1(uri.toString());
                            databaseReference.setValue(products);
                        });
                    });

                    StorageReference imageRef2 = storageReference.child("image_2.jpg");
                    imageRef2.putFile(imageUri_2).addOnSuccessListener(taskSnapshot -> {
                        // Hình ảnh 2 đã được tải lên thành công, lấy đường dẫn và cập nhật đối tượng Products
                        imageRef2.getDownloadUrl().addOnSuccessListener(uri -> {
                            products.setImgProducts_2(uri.toString());
                            databaseReference.setValue(products);
                        });
                    });

                    StorageReference imageRef3 = storageReference.child("image_3.jpg");
                    imageRef3.putFile(imageUri_3).addOnSuccessListener(taskSnapshot -> {
                        // Hình ảnh 3 đã được tải lên thành công, lấy đường dẫn và cập nhật đối tượng Products
                        imageRef3.getDownloadUrl().addOnSuccessListener(uri -> {
                            products.setImgProducts_3(uri.toString());
                            databaseReference.setValue(products);
                        });
                    });

                }
            }
        });
    }
    public void getIDProducts() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                ArrayList<String> dsUser = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dsUser.add(dataSnapshot.getKey());
                }
                String[] temp = dsUser.get(dsUser.size() - 1).split("SP");
                String id = "";
                if (Integer.parseInt(temp[1]) < 10) {
                    id = "SP0" + (Integer.parseInt(temp[1]) + 1);
                } else {
                    id = "SP" + (Integer.parseInt(temp[1]) + 1);
                }
                autoId_products = id;
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });
    }
}
