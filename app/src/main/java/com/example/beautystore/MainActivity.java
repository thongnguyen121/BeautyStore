package com.example.beautystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import android.graphics.drawable.Drawable;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.beautystore.activity.Admin_MainActivity;
import com.example.beautystore.activity.Shipper_MainActivity;
import com.example.beautystore.activity.Tuvanvien_MainActivity;
import com.example.beautystore.fragments.Fragment_cart;
import com.example.beautystore.fragments.Fragment_editProfile;
import com.example.beautystore.fragments.Fragment_home;
import com.example.beautystore.fragments.Fragment_order;
import com.example.beautystore.fragments.Fragment_profile;
import com.example.beautystore.fragments.Fragment_transaction_history;
import com.example.beautystore.fragments.Fragment_warehouse_list;
import com.example.beautystore.fragments.Fragment_wishlist;
import com.example.beautystore.model.Customer;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    public static BottomNavigationView bottomNavigationView;

    TextView tvProfile_name, tvProfile_email;
    DrawerLayout drawerLayout;
    public static MaterialToolbar toolbar;
    FragmentManager fragmentManager;

    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    //    String email="", name="";
    String email = "", name = "", uri = "";
    private Menu menu;
    private MenuItem menuItem;
    ImageView ivProfileImg;
    NavController controller;
    AppBarConfiguration configuration;
    public static final String SHARE_PREFS = "sharedPrefs";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rememberLogin();
        drawerLayout = findViewById(R.id.idDrawer);
        toolbar = findViewById(R.id.toolbar);
        firebaseDatabase = FirebaseDatabase.getInstance();
        navigationView = findViewById(R.id.id_navigationview);
        bottomNavigationView = findViewById(R.id.idbottomNavigation);
        NavHostFragment hostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_container_user);
        controller = hostFragment.getNavController();
        setSupportActionBar(toolbar);
        configuration = new AppBarConfiguration.Builder(R.id.fragment_cart, R.id.fragment_home, R.id.fragment_profile, R.id.fragment_wishlist, R.id.fragment_order).setOpenableLayout(drawerLayout).build();
        NavigationUI.setupActionBarWithNavController(this, controller, configuration);
        controller.addOnDestinationChangedListener((controller, navDestination, bundle) -> {
            if (navDestination.getId() == R.id.fragment_editProfile){
                bottomNavigationView.setVisibility(View.GONE);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            } else{
                bottomNavigationView.setVisibility(View.VISIBLE);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        });
        NavigationUI.setupWithNavController(navigationView, controller);
        NavigationUI.setupWithNavController(bottomNavigationView, controller);
        if (isUserLoggedin()) {
            Toast.makeText(this, "dax dang nhap", Toast.LENGTH_SHORT).show();
            bottomNavigationView.setVisibility(View.VISIBLE);
            navigationView.getMenu().findItem(R.id.Login).setVisible(false);
            navigationView.getMenu().findItem(R.id.Signup).setVisible(false);
            String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            databaseReference = firebaseDatabase.getReference("Customer");
            databaseReference.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Customer customer = snapshot.getValue(Customer.class);
                    name = customer.getUsername();
                    email = customer.getEmail();
                    uri = customer.getProfileImage();
                    if (customer != null){
                        tvProfile_name.setVisibility(View.VISIBLE);
                        tvProfile_name.setText(name);
                        tvProfile_email.setVisibility(View.VISIBLE);
                        tvProfile_email.setText(email);
                        Glide.with(MainActivity.this).load(uri).into(ivProfileImg);
                    }else{
                        tvProfile_name.setText("Guess");
                        tvProfile_email.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Cant", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Chuaw dang nhap", Toast.LENGTH_SHORT).show();
            bottomNavigationView.setVisibility(View.GONE);
            navigationView.getMenu().findItem(R.id.fragment_editProfile).setVisible(false);
            navigationView.getMenu().findItem(R.id.fragment_transaction_history).setVisible(false);
            navigationView.getMenu().findItem(R.id.Logout).setVisible(false);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int idItem = item.getItemId();
                if (idItem == R.id.Login) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else if (idItem == R.id.Logout) {
                    FirebaseAuth.getInstance().signOut();
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("check", "");
                    editor.apply();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (idItem == R.id.fragment_editProfile){
                    controller.navigate(R.id.fragment_editProfile);
                    return  true;
                }
                return false;
            }
        });
        View header = navigationView.getHeaderView(0);
        tvProfile_name = header.findViewById(R.id.tvProfile_name_drawer);
        tvProfile_email = header.findViewById(R.id.tvEmail_drawer);
        ivProfileImg = header.findViewById(R.id.ivProfileImg);
    }

    @Override
    public boolean onSupportNavigateUp() {
        controller = Navigation.findNavController(this, R.id.nav_host_fragment_container_user);
        return NavigationUI.navigateUp(controller, configuration) || super.onSupportNavigateUp();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.appBar_home){
                // Thực hiện việc tải lại fragment tại đây
                // Sử dụng FragmentManager để thay thế fragment hiện tại bằng một instance mới của nó
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void rememberLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
        String check = sharedPreferences.getString("check", "");
        if (check.equals("0")) {
            Intent intent = new Intent(MainActivity.this, Admin_MainActivity.class);
            startActivity(intent);
            finish();
        } else if (check.equals("1")) {
            Intent intent = new Intent(MainActivity.this, Shipper_MainActivity.class);
            startActivity(intent);
            finish();
        } else if (check.equals("2")) {
            Intent intent = new Intent(MainActivity.this, Tuvanvien_MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean isUserLoggedin() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS, Context.MODE_PRIVATE);
        String check = sharedPreferences.getString("check", "");
        return check.equals("true");
    }


}