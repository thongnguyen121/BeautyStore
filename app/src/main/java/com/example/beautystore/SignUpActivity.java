package com.example.beautystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.beautystore.model.Customer;
import com.example.beautystore.model.Members;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    Button btnBack, btnSignUp;
    EditText edtTen, edtMail, edtSDT, edtPass;
    ImageView ivShowHidePass;
    String name, email, phoneNum, pass;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        setControl();
        setEvent();
    }

    private void setEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ivShowHidePass.setImageResource(R.drawable.hide);
        ivShowHidePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtPass.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    show(edtPass, ivShowHidePass);
                    edtPass.setSelection(edtPass.getText().length());
                }
                else{
                    hide(edtPass, ivShowHidePass);
                    edtPass.setSelection(edtPass.getText().length());
                }
            }
        });
        checkEditText();
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Signup();
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
             name = edtTen.getText().toString();
             email = edtMail.getText().toString();
             phoneNum = edtSDT.getText().toString();
             pass = edtPass.getText().toString();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(pass)) {
                // Nếu một trong các trường văn bản trống, disable nút đăng kí
                btnSignUp.setEnabled(false);
                btnSignUp.setBackgroundResource(R.drawable.btn_login_bg_mt); // Thay đổi background màu xám (ví dụ)
            } else {
                // Nếu tất cả các trường đã được điền đầy đủ, enable nút đăng kí
                btnSignUp.setEnabled(true);
                btnSignUp.setBackgroundResource(R.drawable.btn_login_bg); // Thay đổi background màu mặc định
            }
        }
    };
        edtTen.addTextChangedListener(textWatcher);
        edtMail.addTextChangedListener(textWatcher);
        edtSDT.addTextChangedListener(textWatcher);
        edtPass.addTextChangedListener(textWatcher);
    }

    private void Signup() {
        name = edtTen.getText().toString();
        email = edtMail.getText().toString();
        phoneNum = edtSDT.getText().toString();
        pass = edtPass.getText().toString();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtMail.setError("Vui lòng cung cấp đúng email!");
            edtMail.requestFocus();
            return;
        }
        if (pass.length() < 6) {
            edtPass.setError("Độ dài tối thiểu của mật khẩu là 6");
            edtPass.requestFocus();
            return;
        }
        if (phoneNum.length() < 10){
            edtSDT.setError("Độ dài tối thiểu của số điện thoại là 8");
            edtSDT.requestFocus();
            return;
        }if (phoneNum.length() >10){
            edtSDT.setError("Độ dài tối đa của số điện thoại là 10");
            edtSDT.requestFocus();
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("password", pass);
        editor.putString("phoneNum", phoneNum);
        editor.apply();
        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this, "Mail xác thực đã được gửi đến địa chỉ mail của bạn, hãy xác thực trước khi đăng nhập", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
//                                finish();
                            } else {
                                // Failed to send email verification
                                Toast.makeText(SignUpActivity.this, "Gửi mail thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, "Mail đã đăng kí", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hide(EditText edtPass, ImageView ivShowHidePass) {
        edtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        ivShowHidePass.setImageResource(R.drawable.view);
    }

    private void show(EditText edtPass, ImageView ivShowHidePass) {
        edtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        ivShowHidePass.setImageResource(R.drawable.hide);
    }

    private void setControl() {
        btnBack = findViewById(R.id.btnBack);
        btnSignUp = findViewById(R.id.btnSignUp);
        edtMail = findViewById(R.id.edtEmailDK);
        edtTen = findViewById(R.id.edtFullname);
        edtSDT = findViewById(R.id.edtPhoneNum);
        edtPass = findViewById(R.id.edtPassDK);
        ivShowHidePass = findViewById(R.id.ivShowHideSignupPass);
    }
}