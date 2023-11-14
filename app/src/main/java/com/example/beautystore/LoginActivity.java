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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beautystore.activity.Admin_MainActivity;
import com.example.beautystore.activity.Shipper_MainActivity;
import com.example.beautystore.activity.Tuvanvien_MainActivity;
import com.example.beautystore.model.Members;
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
    public static final String SHARE_PREFS = "sharedPrefs";

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
                login();
            }
        });
//        rememberLogin();
    }

//    private void rememberLogin() {
//    SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
//    String check = sharedPreferences.getString("check","");
//    if (check.equals("true")){
//        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//        startActivity(intent);
//        finish();
//    } else if (check.equals("0")) {
//        Intent intent = new Intent(LoginActivity.this, Admin_MainActivity.class);
//        startActivity(intent);
//        finish();
//    } else if (check.equals("1")) {
//        Intent intent = new Intent(LoginActivity.this, Shipper_MainActivity.class);
//        startActivity(intent);
//        finish();
//    } else if (check.equals("2")) {
//        Intent intent = new Intent(LoginActivity.this, Tuvanvien_MainActivity.class);
//        startActivity(intent);
//        finish();
//    }
//    }

    private void login() {


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
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("check","true");

                                editor.apply();
                                Toast.makeText(LoginActivity.this, "Khach", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Khong phai khach", Toast.LENGTH_SHORT).show();
                                        checkRole(uid);
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

    private void checkRole(String uid) {
        databaseReference.child("Member").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Members members = snapshot.getValue(Members.class);
                    if (members.getRole().equals("1")) {
                        if (members.getStatus().equals("1")){
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(LoginActivity.this, "Tài khoản của bạn đã bị khóa, hãy liên hệ với shủ shọp đẹp chai để lấy lại nick", Toast.LENGTH_LONG).show();
                        }
                        else{
                            SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("check","1");
                            editor.apply();
                            Toast.makeText(LoginActivity.this, "sp", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, Shipper_MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

                    } else if (members.getRole().equals("2")) {
                        if (members.getStatus().equals("1")){
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(LoginActivity.this, "Tài khoản của bạn đã bị khóa, hãy liên hệ với shủ shọp đẹp chai để lấy lại nick", Toast.LENGTH_LONG).show();
                        }
                        else {
                            SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("check", "2");
                            editor.apply();
                            Toast.makeText(LoginActivity.this, "tv", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, Tuvanvien_MainActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        if (members.getStatus().equals("1")){
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(LoginActivity.this, "Tài khoản của bạn đã bị khóa, hãy liên hệ với shủ shọp đẹp chai để lấy lại nick", Toast.LENGTH_LONG).show();
                        }
                        else {
                            SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("check", "0");
                            editor.putString("email", email);
                            editor.putString("pass", pass);
                            editor.apply();
                            Toast.makeText(LoginActivity.this, "am", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(LoginActivity.this, Admin_MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkStatus() {
        databaseReference.child("Member").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Members members = snapshot.getValue(Members.class);
                    Log.d("TAG", "status cua mem ber la: ." + members.getStatus());
                    if (members.getStatus().equals("1")){
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(LoginActivity.this, "Lmao tài khoản của bạn đã bị khóa", Toast.LENGTH_SHORT).show();
                    }
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
        ivShowHidePassword.setImageResource(R.drawable.view);
    }

    private void show(@NonNull EditText edtPass, ImageView ivShowHidePassword) {
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