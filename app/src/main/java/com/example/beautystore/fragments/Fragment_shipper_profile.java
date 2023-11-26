package com.example.beautystore.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.beautystore.MainActivity;
import com.example.beautystore.R;
import com.example.beautystore.activity.ChangePasswordShipperActivity;
import com.example.beautystore.activity.EditProfileShipperActivity;
import com.example.beautystore.activity.HistoryTransactionShipperActivity;
import com.example.beautystore.model.Members;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_shipper_profile extends Fragment {
    ImageView imageView;
    TextView tvMailShipper, tvNameShipper;
    Button btnEditProfile, btnChangePassword, btnHistoryTransaction, btnLogout;
FirebaseDatabase database;
DatabaseReference databaseReference;
    public static final String SHARE_PREFS = "sharedPrefs";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_shipper_profile, container, false);
        setControl(view);
        setEvent();
        return view;
    }

    private void setEvent() {
        loadDataShipper();
        btnHistoryTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), HistoryTransactionShipperActivity.class);
                startActivity(intent);
            }
        });
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), EditProfileShipperActivity.class);
                startActivity(intent);
            }
        });
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), ChangePasswordShipperActivity.class);
                startActivity(intent);

            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                databaseReference = database.getReference("Member").child(FirebaseAuth.getInstance().getUid()).child("fcmToken");
                databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            FirebaseAuth.getInstance().signOut();
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("check", "");
                            editor.apply();
                            Intent intent = new Intent(requireContext(), MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }
                });
            }
        });
    }

    private void loadDataShipper() {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Member").child(FirebaseAuth.getInstance().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Members members = snapshot.getValue(Members.class);
                tvMailShipper.setText(members.getEmail());
                tvNameShipper.setText(members.getUsername());
                Glide.with(getContext().getApplicationContext()).load(members.getProfileImage()).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setControl(View view) {
        imageView = view.findViewById(R.id.imgAnhShipper);
        tvNameShipper = view.findViewById(R.id.tvNameShipper);
        tvMailShipper = view.findViewById(R.id.tvEmailShipper);
        btnEditProfile = view.findViewById(R.id.btnEditProfileShipper);
        btnChangePassword = view.findViewById(R.id.btnEditProfileChangePassword);
        btnHistoryTransaction = view.findViewById(R.id.btnHistoryTransactionShipper);
        btnLogout = view.findViewById(R.id.btnLogoutShipper);
    }
}