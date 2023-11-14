package com.example.beautystore.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.beautystore.LoginActivity;
import com.example.beautystore.MainActivity;
import com.example.beautystore.R;
import com.example.beautystore.model.Customer;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class Fragment_editProfile extends Fragment {
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
    Button btnSave;
    FragmentManager fragmentManage;
    ActivityResultLauncher<Intent> cameraResultLauncher;
    boolean seletedImage = false;
    SpinKitView spinKitView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        setControl(view);
        //khoi tao firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Customer");

        //yeu cau quyen cho camera
        required_permissions = new String[]{
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.CAMERA
        };
        //lay uid cua nguoi dung dang dang nhap
        uid = mAuth.getCurrentUser().getUid();
        //lay thong tin nguoi dung dua tren uid
//        onResume();
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
//                updateAccountIntoFirebase(uid);
//                saveInfo();

                if (seletedImage == false) {

                    Toast.makeText(getContext(), "Chua co chon cai con me gi het " + imageUri, Toast.LENGTH_SHORT).show();
                    updateAccountIntoFirebaseWithoutImg();
                } else {
                    Toast.makeText(getContext(), "da chon hinh anh", Toast.LENGTH_SHORT).show();
                    saveInfo();
                    seletedImage = false;
                }
            }
        });
        return view;
    }

    private void saveInfo() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("imgProfile").child(uid);
        Log.d(TAG, "storage hinha nh: " + storageReference);
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
                Toast.makeText(getContext(), "co the" + imageUri, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "co the: " + imageUri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "khong the" + e, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "khong the: " + e);
            }
        });
    }

    private void setViewEnable(boolean b) {
        edtSDT.setEnabled(b);
        edtTen.setEnabled(b);
        edtDiaChi.setEnabled(b);
        btnSave.setEnabled(b);
        cvAnh.setEnabled(b);
    }

    private void updateAccountIntoFirebase() {
        name = edtTen.getText().toString();
        phoneNum = edtSDT.getText().toString();
        address = edtDiaChi.getText().toString();
        DatabaseReference databaseReference = database.getReference("Customer").child(uid);
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

                    Toast.makeText(getContext(), "Cap nhat thanh cong", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Toang", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Toang", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateAccountIntoFirebaseWithoutImg() {
        name = edtTen.getText().toString();
        phoneNum = edtSDT.getText().toString();
        address = edtDiaChi.getText().toString();
        spinKitView.setVisibility(View.VISIBLE);
        setViewEnable(false);
        DatabaseReference databaseReference = database.getReference("Customer").child(uid);
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
                    Toast.makeText(getContext(), "Cap nhat thanh cong", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Toang", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Toang", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(getContext());
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

    private String getFileExtention(Uri imageUri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));

    }

    private void pickImage() {
        Intent i = new Intent();

        if (Build.VERSION.SDK_INT > 30) {
            i = new Intent(MediaStore.ACTION_PICK_IMAGES);
            Toast.makeText(getContext(), "android 13", Toast.LENGTH_SHORT).show();
        } else {
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            Toast.makeText(getContext(), "android 10", Toast.LENGTH_SHORT).show();
        }
        resultLaucher.launch(i);
    }

    private void registerResult() {
        resultLaucher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                try {
                    imageUri = result.getData().getData();
                    imageView.setImageURI(imageUri);
                    seletedImage = true;
                    extention = getFileExtention(imageUri);
                    Log.d("TAG", "hinh anh: " + "." + extention);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "No img selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void getUser(String uid) {
        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Customer customer = snapshot.getValue(Customer.class);
                imageUri = Uri.parse(customer.getProfileImage());
                email = customer.getEmail();
                name = customer.getUsername();
                phoneNum = customer.getPhoneNumber();
                address = customer.getAddress();
                password = customer.getPassword();
                status = customer.getStatus();
                Glide.with(requireContext()).load(imageUri).into(imageView);
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

    private void getCameraResult() {
        cameraResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode() == RESULT_OK && o.getData() != null) {
                    imageView.setImageURI(imageUri);
                    Log.d(TAG, "uri hinh anh: " + imageUri);
                }
            }
        });
    }

    // camera laucher

    public void openCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Captured");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Captured image");
        imageUri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
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
                                imageView.setImageURI(imageUri);
                                seletedImage = true;
//                            Glide.with(requireContext()).load(imageUri).into(imageView);
                            }

                        }
                    });


//    code xin quyen
//    public void requestPermissionStorageImages() {
//        if (ContextCompat.checkSelfPermission(getContext(), required_permissions[0]) == PackageManager.PERMISSION_GRANTED) {
//            Log.d(TAG, required_permissions[0] + "Granted");
//            is_storage_image_permitted = true;
//            requestPermissionCameraAccress();
//        } else {
//            request_permission_laucher_storage_image.launch(required_permissions[0]);
//        }
//    }

//    private ActivityResultLauncher<String> request_permission_laucher_storage_image = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
//        if (isGranted) {
//            Log.d(TAG, required_permissions[0] + "Granted");
//            is_storage_image_permitted = true;
//        } else {
//            Log.d(TAG, required_permissions[0] + "not Granted");
//            is_storage_image_permitted = false;
//        }
//        requestPermissionCameraAccress();
//    });


    public void requestPermissionCameraAccress() {
        if (ContextCompat.checkSelfPermission(getContext(), required_permissions[1]) == PackageManager.PERMISSION_GRANTED) {
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


    private void setControl(View view) {
        cvAnh = view.findViewById(R.id.cvImage);
        imageView = view.findViewById(R.id.imgAnh);
        tvEmail = view.findViewById(R.id.tvEmailPF);
        edtTen = view.findViewById(R.id.edtFullnamePF);
        edtSDT = view.findViewById(R.id.edtPhonenumPF);
        edtDiaChi = view.findViewById(R.id.edtAddressPF);
        btnSave = view.findViewById(R.id.btnSavePF);
        spinKitView = view.findViewById(R.id.spin_kitPF);
    }
}
