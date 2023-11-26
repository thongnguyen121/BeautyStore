package com.example.beautystore.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.beautystore.R;
import com.example.beautystore.model.Customer;
import com.example.beautystore.model.Members;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
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

public class EditProfileShipperActivity extends AppCompatActivity {
    MaterialCardView cvAnh;
    ImageView imageView;
    TextView tvEmail;
    EditText edtTen, edtSDT, edtDiaChi;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference reference;
    StorageReference storageReference;
    String uid = "", email = "", name = "", phoneNum = "", address = "", password = "", status = "", extention = "";
    Uri imageUri = Uri.parse("");
    private static final int PICK_IMAGE_REQUEST = 1;
    private ProgressBar progressBar;
    ActivityResultLauncher<Intent> resultLaucher;
    String[] required_permissions;
    boolean is_storage_image_permitted = false;
    boolean is_camera_access_permitted = false;
    String TAG = "Permission";
    Button btnSave, btnBack;
    FragmentManager fragmentManage;
    ActivityResultLauncher<Intent> cameraResultLauncher;
    boolean seletedImage = false;
    SpinKitView spinKitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_shipper);
        setControll();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Member");

        required_permissions = new String[]{
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.CAMERA
        };
        uid = mAuth.getCurrentUser().getUid();
        getUser(uid);
        registerResult();
        getCameraResult();
        cvAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seletedImage == false) {

                    updateAccountIntoFirebaseWithoutImg();
                } else {
                    saveInfo();
                    seletedImage = false;
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void saveInfo() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("imgProfile").child(uid);
        spinKitView.setVisibility(View.VISIBLE);
        setViewEnable(false);
        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                imageUri = uriTask.getResult();
                updateAccountIntoFirebase();
                spinKitView.setVisibility(View.GONE);
                setViewEnable(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "khong the: " + e);
            }
        });
    }

    private void updateAccountIntoFirebase() {
        name = edtTen.getText().toString();
        phoneNum = edtSDT.getText().toString();
        address = edtDiaChi.getText().toString();
        DatabaseReference databaseReference = database.getReference("Member").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Tạo đối tượng chứa các trường cần cập nhật
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("username", name);
                    updates.put("address", address);
                    updates.put("phoneNumber", phoneNum);
                    updates.put("profileImage", imageUri.toString());

                    // Cập nhật dữ liệu
                    databaseReference.updateChildren(updates);

                    Toast.makeText(EditProfileShipperActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    onBackPressed();

                } else {
                    Toast.makeText(EditProfileShipperActivity.this, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileShipperActivity.this, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateAccountIntoFirebaseWithoutImg() {
        name = edtTen.getText().toString();
        phoneNum = edtSDT.getText().toString();
        address = edtDiaChi.getText().toString();
        spinKitView.setVisibility(View.VISIBLE);
        setViewEnable(false);
        DatabaseReference databaseReference = database.getReference("Member").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Tạo đối tượng chứa các trường cần cập nhật
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("username", name);
                    updates.put("address", address);
                    updates.put("phoneNumber", phoneNum);

                    // Cập nhật dữ liệu
                    databaseReference.updateChildren(updates);
                    spinKitView.setVisibility(View.GONE);
                    setViewEnable(true);
                    Toast.makeText(EditProfileShipperActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    onBackPressed();

                } else {
                    Toast.makeText(EditProfileShipperActivity.this, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileShipperActivity.this, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setViewEnable(boolean b) {
        edtSDT.setEnabled(b);
        edtTen.setEnabled(b);
        edtDiaChi.setEnabled(b);
        btnSave.setEnabled(b);
        cvAnh.setEnabled(b);
        btnBack.setEnabled(b);
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(EditProfileShipperActivity.this);
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

    private void requestPermissionCameraAccress() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), required_permissions[1]) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, required_permissions[1] + "Granted");
            is_camera_access_permitted = true;
        } else {
            request_permission_laucher_camera_access.launch(required_permissions[1]);
        }
    }
    private ActivityResultLauncher<String> request_permission_laucher_camera_access = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            Log.d(TAG, required_permissions[1] + "Granted");
            is_camera_access_permitted = true;
        } else {
            Log.d(TAG, required_permissions[1] + "not Granted");
            is_camera_access_permitted = false;
        }
    });
    private void openCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Captured");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Captured image");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        laucher_for_camera.launch(intent);
    }
    private ActivityResultLauncher<Intent> laucher_for_camera =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == RESULT_OK) {
                                imageView.setImageURI(imageUri);
                                seletedImage = true;
                            }

                        }
                    });
    private void pickImage() {
        Intent i = new Intent();

        if (Build.VERSION.SDK_INT > 30) {
            i = new Intent(MediaStore.ACTION_PICK_IMAGES);
        } else {
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
        }
        resultLaucher.launch(i);
    }

    private void getCameraResult() {
        cameraResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode() == RESULT_OK && o.getData() != null) {
                    imageView.setImageURI(imageUri);
                }
            }
        });
    }

    private void registerResult() {
        resultLaucher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                try {
                    imageUri = result.getData().getData();
                    imageView.setImageURI(imageUri);
                    seletedImage = true;
                } catch (Exception e) {
                }
            }
        });
    }

    private void getUser(String uid) {
        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Members members = snapshot.getValue(Members.class);
                imageUri = Uri.parse(members.getProfileImage());
                email = members.getEmail();
                name = members.getUsername();
                phoneNum = members.getPhoneNumber();
                address = members.getAddress();
                Glide.with(getApplicationContext()).load(imageUri).into(imageView);
                tvEmail.setText(email);
                edtTen.setText(name);
                edtSDT.setText(phoneNum);
                edtDiaChi.setText(address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setControll() {
        cvAnh = findViewById(R.id.cvImageMember);
        imageView = findViewById(R.id.imgAnhMember);
        tvEmail = findViewById(R.id.tvEmailMember);
        edtTen = findViewById(R.id.edtFullnameMember);
        edtSDT = findViewById(R.id.edtPhonenumMember);
        edtDiaChi = findViewById(R.id.edtAddressMember);
        btnSave = findViewById(R.id.btnSaveMember);
        spinKitView = findViewById(R.id.spin_kitMember);
        btnBack = findViewById(R.id.btnBackMember);
    }
}