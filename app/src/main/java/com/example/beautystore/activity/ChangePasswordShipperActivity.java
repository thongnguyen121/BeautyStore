package com.example.beautystore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.beautystore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePasswordShipperActivity extends AppCompatActivity {
    EditText edtNewPassword;
    Button btnSaveChange, btnBack;
    String newPassword;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_shipper);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Member").child(FirebaseAuth.getInstance().getUid());
        auth = FirebaseAuth.getInstance();
        setControl();
        checkEditText();
        setEvent();
    }

    private void setEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnSaveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPassword = edtNewPassword.getText().toString();
                auth.getCurrentUser().updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            databaseReference.child("password").setValue(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(ChangePasswordShipperActivity.this, "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                            });
                        }
                        else{
                            Toast.makeText(ChangePasswordShipperActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }

    private void checkEditText() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                newPassword = edtNewPassword.getText().toString();
                if (TextUtils.isEmpty(newPassword)) {
                    // Nếu một trong các trường văn bản trống, disable nút đăng kí
                    btnSaveChange.setEnabled(false);
                    btnSaveChange.setBackgroundResource(R.drawable.btn_login_bg_mt); // Thay đổi background màu xám (ví dụ)
                } else {
                    // Nếu tất cả các trường đã được điền đầy đủ, enable nút đăng kí
                    btnSaveChange.setEnabled(true);
                    btnSaveChange.setBackgroundResource(R.drawable.btn_login_bg); // Thay đổi background màu mặc định
                }
            }
        };
        edtNewPassword.addTextChangedListener(textWatcher);
    }

    private void setControl() {
        edtNewPassword = findViewById(R.id.edtNewPassword);
        btnBack = findViewById(R.id.btnChangePasswordBack);
        btnSaveChange = findViewById(R.id.btnSaveChangePassword);
    }
}