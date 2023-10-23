package com.example.beautystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    TextView tvForgotPass, tvSignUp;
    Button btnLogin;
    ImageView ivShowHidePassword;
    EditText edtEmail, edtPass;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String email, pass, uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        setControl();
        setEvent();
    }

    private void setEvent() {
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        ivShowHidePassword.setImageResource(R.drawable.view);
        ivShowHidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtPass.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    show(edtPass, ivShowHidePassword);
                    edtPass.setSelection(edtPass.getText().length());
                } else {
                    hide(edtPass, ivShowHidePassword);
                    edtPass.setSelection(edtPass.getText().length());
                }
            }
        });
        checkEditText();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = edtEmail.getText().toString().trim();
                pass = edtPass.getText().toString().trim();
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            uid = firebaseAuth.getCurrentUser().getUid();
                            databaseReference.child("Customer").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChild(uid)) {
                                        Toast.makeText(LoginActivity.this, "Khach", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Khong phai khach", Toast.LENGTH_SHORT).show();
//                                        checkRole(uid);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, "Khong thanh cong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    private void checkRole(String uid) {
        databaseReference.child("NhanSu").child(uid).child("role").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String a = snapshot.getValue(String.class);
                if (a.equals("Shipper")) {
                    Toast.makeText(LoginActivity.this, "sp", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (a.equals("tuvan")) {
                    Toast.makeText(LoginActivity.this, "tv", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "am", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                email = edtEmail.getText().toString();
                pass = edtPass.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
                    // Nếu một trong các trường văn bản trống, disable nút đăng kí
                    btnLogin.setEnabled(false);
                    btnLogin.setBackgroundResource(R.drawable.btn_login_bg_mt); // Thay đổi background màu xám (ví dụ)
                } else {
                    // Nếu tất cả các trường đã được điền đầy đủ, enable nút đăng kí
                    btnLogin.setEnabled(true);
                    btnLogin.setBackgroundResource(R.drawable.btn_login_bg); // Thay đổi background màu mặc định
                }
            }
        };
        edtEmail.addTextChangedListener(textWatcher);
        edtPass.addTextChangedListener(textWatcher);
    }

    private void hide(EditText edtPass, ImageView ivShowHidePassword) {
        edtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        ivShowHidePassword.setImageResource(R.drawable.hide);
    }

    private void show(EditText edtPass, ImageView ivShowHidePassword) {
        edtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        ivShowHidePassword.setImageResource(R.drawable.hide);

    }

    private void setControl() {
        tvForgotPass = findViewById(R.id.tvForgetPassword);
        tvSignUp = findViewById(R.id.tvSignUP);
        ivShowHidePassword = findViewById(R.id.ivShowHideLoginPass);
        btnLogin = findViewById(R.id.btnLogin);
        edtEmail = findViewById(R.id.edtEmailDN);
        edtPass = findViewById(R.id.edtPassDN);
    }
}