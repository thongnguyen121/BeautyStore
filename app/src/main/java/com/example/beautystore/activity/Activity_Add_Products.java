package com.example.beautystore.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.beautystore.R;
import com.example.beautystore.model.Brands;
import com.example.beautystore.model.Categories;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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
        img_gallery = findViewById(R.id.img_Gallery);
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
                    if (inputText.length() > 7) {
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
                if (s.length() > 7) {
                    cardView_products_quantity.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.pink));
                    tv_products_quantity.setVisibility(View.VISIBLE);
                    tv_products_quantity.setText("Bạn đã nhập quá 7 kí tự");
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
                    } else if (inputText.length() > 7) {
                        cardView_products_quantity.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.red));
                    } else {
                        cardView_products_quantity.setStrokeColor(Color.TRANSPARENT);
                    }
                } else {
                    if (inputText.length() > 7) {
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
                    if (inputText.length() > 7) {
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
                if (s.length() > 7) {
                    cardView_products_name.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.pink));
                    tv_products_name.setVisibility(View.VISIBLE);
                    tv_products_name.setText("Bạn đã nhập quá 7 kí tự");
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
                    } else if (inputText.length() > 7) {
                        cardView_products_name.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.red));
                    } else {
                        cardView_products_name.setStrokeColor(Color.TRANSPARENT);
                    }
                } else {
                    if (inputText.length() > 7) {
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
                    if (inputText.length() > 7) {
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
                if (s.length() > 7) {
                    cardView_products_description.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.pink));
                    tv_products_description.setVisibility(View.VISIBLE);
                    tv_products_description.setText("Bạn đã nhập quá 7 kí tự");
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
                    } else if (inputText.length() > 7) {
                        cardView_products_description.setStrokeColor(ContextCompat.getColor(Activity_Add_Products.this, R.color.red));
                    } else {
                        cardView_products_description.setStrokeColor(Color.TRANSPARENT);
                    }
                } else {
                    if (inputText.length() > 7) {
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



}
