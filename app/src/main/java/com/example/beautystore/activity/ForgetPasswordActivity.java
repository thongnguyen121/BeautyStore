package com.example.beautystore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.beautystore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    EditText edtMail;
    Button btnSend, btnBack;
    String email;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        setControl();
        checkEditText();
        setEvent();
    }

    private void setEvent() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth =FirebaseAuth.getInstance();
                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ForgetPasswordActivity.this, "Hãy kiểm tra mail của bạn", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    }
                });

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setControl() {
        edtMail = findViewById(R.id.edtEmailFP);
        btnSend = findViewById(R.id.btnSendResetMail);
        btnBack = findViewById(R.id.btnBackFP);
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
                email = edtMail.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    // Nếu một trong các trường văn bản trống, disable nút đăng kí
                    btnSend.setEnabled(false);
                    btnSend.setBackgroundResource(R.drawable.btn_login_bg_mt); // Thay đổi background màu xám (ví dụ)
                } else {
                    // Nếu tất cả các trường đã được điền đầy đủ, enable nút đăng kí
                    btnSend.setEnabled(true);
                    btnSend.setBackgroundResource(R.drawable.btn_login_bg); // Thay đổi background màu mặc định
                }
            }
        };
        edtMail.addTextChangedListener(textWatcher);
    }
}