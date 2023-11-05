package com.example.beautystore.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.beautystore.LoginActivity;
import com.example.beautystore.MainActivity;
import com.example.beautystore.R;
import com.example.beautystore.model.Customer;
import com.example.beautystore.model.Members;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin_MainActivity extends AppCompatActivity{
    NavController navController;
    AppBarConfiguration appBarConfiguration;
    Menu menu;
    TextView tvProfile_name;
    ImageView ivProfileImg;
    String name, uri;
    public static BottomNavigationView bottomNavigationView;
    public static  MaterialToolbar toolbar;
    public static final String SHARE_PREFS = "sharedPrefs";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationAdminMenu);
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_container);
        navController = navHostFragment.getNavController();

        toolbar = findViewById(R.id.toolbarAdmin);
        setSupportActionBar(toolbar);
        NavigationView navigation = findViewById(R.id.id_navigationviewAdmin);


        View header = navigation.getHeaderView(0);
        tvProfile_name = header.findViewById(R.id.tvProfile_name_drawer);
        ivProfileImg = header.findViewById(R.id.ivProfileImg);
        DrawerLayout drawerLayout = findViewById(R.id.dwLayout);

        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("TAG", "UID cua admin: " + UID);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Member");
        databaseReference.child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Members members = snapshot.getValue(Members.class);
                name = members.getUsername();
                uri = members.getProfileImage();
                if (members != null){
                    tvProfile_name.setVisibility(View.VISIBLE);
                    tvProfile_name.setText(name);
                    Glide.with(Admin_MainActivity.this).load(uri).into(ivProfileImg);
                }else{
                    tvProfile_name.setText("Guess");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Admin_MainActivity.this, "Cant", Toast.LENGTH_SHORT).show();
            }
            });

        appBarConfiguration = new AppBarConfiguration.Builder(R.id.fragment_admin_home, R.id.fragment_admin_employees,R.id.fragment_admin_statistic, R.id.fragment_warehouse_list, R.id.fragment_admin_transaction ).setOpenableLayout(drawerLayout).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        navController.addOnDestinationChangedListener((controller, navDestination, bundle) -> {
        if (navDestination.getId() == R.id.fragment_customer_list){
            bottomNavigationView.setVisibility(View.GONE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        } else{
            bottomNavigationView.setVisibility(View.VISIBLE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        });

        NavigationUI.setupWithNavController(navigation, navController);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int idItem = item.getItemId();
                if (idItem == R.id.AdminLogout) {
                    FirebaseAuth.getInstance().signOut();
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("check", "");
                    editor.apply();
                    Intent intent = new Intent(Admin_MainActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    Toast.makeText(Admin_MainActivity.this, "da logout", Toast.LENGTH_SHORT).show();
                }
                else if (idItem == R.id.fragment_customer_list){
                    navController.navigate(R.id.fragment_customer_list);
                    return  true;
                }
                    return false;


            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
//        navController.navigateUp();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_container);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

}