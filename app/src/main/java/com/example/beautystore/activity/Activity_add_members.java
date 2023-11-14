package com.example.beautystore.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.beautystore.LoginActivity;
import com.example.beautystore.R;
import com.example.beautystore.fragments.Fragment_admin_employees;
import com.example.beautystore.fragments.Fragment_warehouse_list;
import com.example.beautystore.model.Members;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Activity_add_members extends AppCompatActivity {
    ImageView ivBack, ivImage;
    MaterialCardView cvImage;
    EditText edtFullname, edtPhonwNum, edtAddress, edtCCCD, edtEmail, edtPassword;
    Spinner spnRole;
    Uri uri;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference reference;
    Button btnAddEmployee, btnEditEmployee;
    String name, phoneNum, address, cccd, email, password, uid, role, emailAdmin, passAdmin, id;
    private static final int PICK_IMAGE_REQUEST = 1;
    ActivityResultLauncher<Intent> resultLaucher;
    String[] required_permissions;
    boolean is_camera_access_permitted = false;
    ActivityResultLauncher<Intent> cameraResultLauncher;
    StorageReference storageReference;
    boolean seletedImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);
        setControl();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARE_PREFS, MODE_PRIVATE);
        emailAdmin = sharedPreferences.getString("email", "");
        passAdmin = sharedPreferences.getString("pass", "");
        id = getIntent().getStringExtra("id");
//        adminUid = admin.getUid();
        reference = database.getReference("Member");
        storageReference = FirebaseStorage.getInstance().getReference("imgProfile");
        required_permissions = new String[]{
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.CAMERA
        };
        uid = mAuth.getInstance().getUid();
        if (Fragment_admin_employees.statusEmployee) {
            btnEditEmployee.setVisibility(View.GONE);

        } else {
            btnAddEmployee.setVisibility(View.GONE);
            Log.d("TAG", "sua nhan vien: " + id);
            edtEmail.setEnabled(false);
            edtPassword.setEnabled(false);
            getDataMember(id);
//            intent_getData(products_id);
        }
        registerResult();
        getCameraResult();
        checkEditText();
        cvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        btnAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Activity_add_members.this, "asdasdasdasfbb", Toast.LENGTH_SHORT).show();
                SaveDataMember();

            }
        });
        btnEditEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seletedImage) {
                    updateDataMember();
                    seletedImage = false;
                } else {
                    updateDataMemberWithoutImage();
                    Toast.makeText(Activity_add_members.this, "chua chom amj", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void updateDataMemberWithoutImage() {
        name = edtFullname.getText().toString();
        phoneNum = edtPhonwNum.getText().toString();
        address = edtAddress.getText().toString();
        cccd = edtCCCD.getText().toString();
        role = spnRole.getSelectedItem().toString();
        if (role.equals("Shipper")) {
            role = "1";
        } else {
            role = "2";
        }
        reference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Members members = snapshot.getValue(Members.class);
                    Map<String, Object> update = new HashMap<>();
                    update.put("username", name);
                    update.put("phoneNumber", phoneNum);
                    update.put("address", address);
                    update.put("cccd", cccd);
                    update.put("role", role);
                    reference.child(id).updateChildren(update);
                    Toast.makeText(Activity_add_members.this, "cap nhat thanh cong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Activity_add_members.this, "cap nhat khong thanh cong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDataMember() {

        Log.d("TAG", "updateDataMember: " + id);
        storageReference.child(id).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;
                uri = uriTask.getResult();
                updateIntoFirebaseRealTime();
                Toast.makeText(Activity_add_members.this, "hinh anh thanh cong", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Activity_add_members.this, "khonf thanh cong " + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateIntoFirebaseRealTime() {
        name = edtFullname.getText().toString();
        phoneNum = edtPhonwNum.getText().toString();
        address = edtAddress.getText().toString();
        cccd = edtCCCD.getText().toString();
        role = spnRole.getSelectedItem().toString();
        if (role.equals("Shipper")) {
            role = "1";
        } else {
            role = "2";
        }
        reference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Members members = snapshot.getValue(Members.class);
                    Map<String, Object> update = new HashMap<>();
                    update.put("username", name);
                    update.put("phoneNumber", phoneNum);
                    update.put("address", address);
                    update.put("cccd", cccd);
                    update.put("role", role);
                    update.put("profileImage", uri.toString());
                    reference.child(id).updateChildren(update);
                    Toast.makeText(Activity_add_members.this, "cap nhat thanh cong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Activity_add_members.this, "cap nhat khong thanh cong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataMember(String id) {
        reference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Members members = snapshot.getValue(Members.class);
                edtAddress.setText(members.getAddress());
                edtCCCD.setText(members.getCccd());
                edtEmail.setText(members.getEmail());
                edtFullname.setText(members.getUsername());
                edtPassword.setText(members.getPassword());
                edtPhonwNum.setText(members.getPhoneNumber());
                Glide.with(Activity_add_members.this).load(members.getProfileImage()).into(ivImage);
                if (members.getRole().equals("1")) {
                    spnRole.setSelection(0);
                } else {
                    spnRole.setSelection(1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SaveDataMember() {
        name = edtFullname.getText().toString();
        phoneNum = edtPhonwNum.getText().toString();
        address = edtAddress.getText().toString();
        cccd = edtCCCD.getText().toString();
        email = edtEmail.getText().toString();
        password = edtPassword.getText().toString();
        role = spnRole.getSelectedItem().toString();
        if (role.equals("Shipper")) {
            role = "1";
        } else {
            role = "2";
        }
        if (uri == null) {
            Toast.makeText(this, "hay cap nhat hinh anh " + role, Toast.LENGTH_SHORT).show();
        } else {

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String id = mAuth.getCurrentUser().getUid();
                        Members members = new Members(id, name, email, password, phoneNum, "", cccd, "0", role, address);

                        storageReference.child(id).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uriTask.isComplete()) ;
                                uri = uriTask.getResult();
                                members.setProfileImage(String.valueOf(uri));
                                reference.child(mAuth.getCurrentUser().getUid()).setValue(members).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(Activity_add_members.this, " da them nhan vien", Toast.LENGTH_SHORT).show();
                                        Log.d("TAG", "dang xuat: " + mAuth.getCurrentUser().getUid());
                                        mAuth.signOut();
                                        loginAdminAccount();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Activity_add_members.this, "khong the them", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        });
                    }
                }
            });

        }
    }

    private void loginAdminAccount() {
        mAuth.signInWithEmailAndPassword(emailAdmin, passAdmin).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Activity_add_members.this, "thanh cong", Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "dang nhap lai admin thanh cong: " + task.getResult().getUser().getUid());
                } else {
                    Toast.makeText(Activity_add_members.this, "ko thanh cong", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Activity_add_members.this, "ko thanh cong " + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(Activity_add_members.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_popup_layout);

        LinearLayout lvLayoutGallery = dialog.findViewById(R.id.layoutGallery);
        LinearLayout lvLayoutCamera = dialog.findViewById(R.id.layoutCamera);

        lvLayoutGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
                dialog.cancel();
            }
        });
        lvLayoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_camera_access_permitted) {
                    openCamera();
                } else {
                    requestPermissionCameraAccress();
                }
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialoAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    public void openCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Captured");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Captured image");
        uri = Activity_add_members.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        laucher_for_camera.launch(intent);
    }

    private ActivityResultLauncher<Intent> laucher_for_camera =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            //now android 13 no request
                            if (result.getResultCode() == RESULT_OK) {
                                //set uri in here
                                ivImage.setImageURI(uri);
                                seletedImage = true;
//                            Glide.with(requireContext()).load(imageUri).into(imageView);
                            }

                        }
                    });

    private void pickImage() {
        Intent i = new Intent();

        if (Build.VERSION.SDK_INT > 30) {
            i = new Intent(MediaStore.ACTION_PICK_IMAGES);
            Toast.makeText(Activity_add_members.this, "android 13", Toast.LENGTH_SHORT).show();
        } else {
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            Toast.makeText(Activity_add_members.this, "android 10", Toast.LENGTH_SHORT).show();
        }
        resultLaucher.launch(i);
    }

    public void requestPermissionCameraAccress() {
        if (ContextCompat.checkSelfPermission(Activity_add_members.this, required_permissions[1]) == PackageManager.PERMISSION_GRANTED) {
            Log.d("TAG", required_permissions[1] + "Granted");
            is_camera_access_permitted = true;
        } else {
            request_permission_laucher_camera_access.launch(required_permissions[1]);
        }
    }

    private ActivityResultLauncher<String> request_permission_laucher_camera_access = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            Log.d("TAG", required_permissions[1] + "Granted");
            is_camera_access_permitted = true;
        } else {
            Log.d("TAG", required_permissions[1] + "not Granted");
            is_camera_access_permitted = false;
        }
    });

    private void getCameraResult() {
        cameraResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode() == RESULT_OK && o.getData() != null) {
                    ivImage.setImageURI(uri);
                    Log.d("TAG", "uri hinh anh: " + uri);
                }
            }
        });
    }

    private void registerResult() {
        resultLaucher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                try {
                    uri = result.getData().getData();
                    ivImage.setImageURI(uri);
                    seletedImage = true;
                } catch (Exception e) {
                    Toast.makeText(Activity_add_members.this, "No img selected", Toast.LENGTH_SHORT).show();
                }
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
                name = edtFullname.getText().toString();
                phoneNum = edtPhonwNum.getText().toString();
                address = edtAddress.getText().toString();
                cccd = edtCCCD.getText().toString();
                email = edtEmail.getText().toString();
                password = edtPassword.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(address) || TextUtils.isEmpty(cccd) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    btnAddEmployee.setEnabled(false);
//                    btnEditEmployee.setEnabled(false);
                    btnAddEmployee.setBackgroundResource(R.drawable.btn_add_employee_bg_mt);
                    btnEditEmployee.setBackgroundResource(R.drawable.btn_add_employee_bg_mt);
                } else {
                    btnAddEmployee.setEnabled(true);
                    btnEditEmployee.setEnabled(true);
                    btnAddEmployee.setBackgroundResource(R.drawable.btn_add_employee_bg);
                    btnEditEmployee.setBackgroundResource(R.drawable.btn_add_employee_bg);
                }
            }
        };
        edtFullname.addTextChangedListener(textWatcher);
        edtPhonwNum.addTextChangedListener(textWatcher);
        edtAddress.addTextChangedListener(textWatcher);
        edtCCCD.addTextChangedListener(textWatcher);
        edtEmail.addTextChangedListener(textWatcher);
        edtPassword.addTextChangedListener(textWatcher);
    }

    private void setControl() {
        ivBack = findViewById(R.id.ivBackE);
        ivImage = findViewById(R.id.imgAnhE);
        cvImage = findViewById(R.id.cvImageE);
        edtFullname = findViewById(R.id.edtFullnameE);
        edtPhonwNum = findViewById(R.id.edtPhoneNumE);
        edtAddress = findViewById(R.id.edtAdddressE);
        edtCCCD = findViewById(R.id.edtCCCDE);
        edtEmail = findViewById(R.id.edtEmailE);
        edtPassword = findViewById(R.id.edtPassE);
        spnRole = findViewById(R.id.spnRoleE);
        btnAddEmployee = findViewById(R.id.btnAddEmployee);
        btnEditEmployee = findViewById(R.id.btnEditEmployee);
    }
}