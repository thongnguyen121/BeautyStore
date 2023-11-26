package com.example.beautystore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.beautystore.MainActivity;
import com.example.beautystore.R;
import com.example.beautystore.model.Members;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin_MainActivity extends AppCompatActivity {
    NavController navController;
    AppBarConfiguration appBarConfiguration;
    Menu menu;
    TextView tvProfile_name;
    ImageView ivProfileImg;
    String name, uri;
    NavigationView navigation;
    public static BottomNavigationView bottomNavigationView;
    public static MaterialToolbar toolbar;
    public static final String SHARE_PREFS = "sharedPrefs";
    public static int counterOrderInProgess = 0, counterOrderPacking = 0, counterOrderInQueue;
    public static TextView tvCounterOrderProgress, tvCounterPackingOrder, tvCounterOrderQueue;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
//        FirebaseAuth.getInstance().signOut();
        bottomNavigationView = findViewById(R.id.bottomNavigationAdminMenu);
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_container);
        navController = navHostFragment.getNavController();

        toolbar = findViewById(R.id.toolbarAdmin);
        setSupportActionBar(toolbar);
        navigation = findViewById(R.id.id_navigationviewAdmin);


        View header = navigation.getHeaderView(0);
        tvProfile_name = header.findViewById(R.id.tvProfile_name_drawer);
        ivProfileImg = header.findViewById(R.id.ivProfileImg);
        DrawerLayout drawerLayout = findViewById(R.id.dwLayout);

        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Member");
        databaseReference.child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Members members = snapshot.getValue(Members.class);
                name = members.getUsername();
                uri = members.getProfileImage();
                if (members != null) {
                    tvProfile_name.setVisibility(View.VISIBLE);
                    tvProfile_name.setText(name);
                    Glide.with(getApplicationContext()).load(uri).into(ivProfileImg);
                } else {
                    tvProfile_name.setText("Guess");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Admin_MainActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });

        appBarConfiguration = new AppBarConfiguration.Builder(R.id.fragment_admin_home, R.id.fragment_admin_employees, R.id.fragment_admin_statistic, R.id.fragment_warehouse_list, R.id.fragment_admin_transaction).setOpenableLayout(drawerLayout).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        navController.addOnDestinationChangedListener((controller, navDestination, bundle) -> {
           if (navDestination.getId() == R.id.fragment_order_in_progress) {
                bottomNavigationView.setVisibility(View.GONE);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            } else if (navDestination.getId() == R.id.fragment_packing_order) {
                bottomNavigationView.setVisibility(View.GONE);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            } else if (navDestination.getId() == R.id.fragment_order_queue) {
                bottomNavigationView.setVisibility(View.GONE);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            } else {
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
                    databaseReference = firebaseDatabase.getReference("Member").child(FirebaseAuth.getInstance().getUid()).child("fcmToken");
                    databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                FirebaseAuth.getInstance().signOut();
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("check", "");
                                editor.apply();
                                Intent intent = new Intent(Admin_MainActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

                } else if (idItem == R.id.fragment_order_in_progress) {
                    navController.navigate(R.id.fragment_order_in_progress);
                    return true;
                } else if (idItem == R.id.fragment_packing_order) {
                    navController.navigate(R.id.fragment_packing_order);
                    return true;
                } else if (idItem == R.id.fragment_order_queue) {
                    navController.navigate(R.id.fragment_order_queue);
                    return true;
                }
                return false;


            }
        });
        getCounterOrderInProgress();
        getCounterOrderPacking();
        getCoutneOrderQueue();
//        counterOrderInProgess = 2;
        LayoutInflater layoutInflater = LayoutInflater.from(Admin_MainActivity.this);
        tvCounterOrderProgress = (TextView) layoutInflater.inflate(R.layout.counter_order_in_progress, null);
        tvCounterPackingOrder = (TextView) layoutInflater.inflate(R.layout.counter_order_packing, null);
        tvCounterOrderQueue = (TextView) layoutInflater.inflate(R.layout.counter_order_queue, null);

//    bottomNavigationView.getOrCreateBadge(R.id.fragment_admin_employees).setNumber(12);


    }

    private void getCoutneOrderQueue() {
        DatabaseReference reference = firebaseDatabase.getReference("OrderStatus");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    counterOrderInQueue = 0;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String status = dataSnapshot.child("status").getValue(String.class);
                        if (status.equals("2") || status.equals("3") || status.equals("4") || status.equals("5")) {
                            counterOrderInQueue++;
                            navigation.getMenu().findItem(R.id.fragment_order_queue).setActionView(tvCounterOrderQueue);
                            Show_Counter_Order_Queue(counterOrderInQueue);
                        }


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getCounterOrderPacking() {
        DatabaseReference reference = firebaseDatabase.getReference("OrderStatus");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    boolean hasStatus = false;
                    counterOrderPacking = 0;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String status = dataSnapshot.child("status").getValue(String.class);
                        if (status.equals("1")) {
                            hasStatus = true;
                            counterOrderPacking++;

                        }
                        if (hasStatus) {
                            navigation.getMenu().findItem(R.id.fragment_packing_order).setActionView(tvCounterPackingOrder);
                            Show_Counter_PackingOrder(counterOrderPacking);
                        } else {
                            counterOrderPacking = 0;
                            navigation.getMenu().findItem(R.id.fragment_packing_order).setActionView(tvCounterPackingOrder);
                            Show_Counter_PackingOrder(counterOrderPacking);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getCounterOrderInProgress() {

        DatabaseReference reference = firebaseDatabase.getReference("OrderStatus");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    counterOrderInProgess = 0;
                    boolean hasStatus = false;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String status = dataSnapshot.child("status").getValue(String.class);
                        if (status.equals("0")) {
                            hasStatus = true;
                            counterOrderInProgess++;
//                            navigation.getMenu().findItem(R.id.fragment_order_in_progress).setActionView(tvCounterOrderProgress);
//                            Show_Counter_ProgressOrder(counterOrderInProgess);
                        }
                        if (hasStatus) {
                            navigation.getMenu().findItem(R.id.fragment_order_in_progress).setActionView(tvCounterOrderProgress);
                            Show_Counter_ProgressOrder(counterOrderInProgess);
                        } else {
                            counterOrderInProgess = 0;
                            navigation.getMenu().findItem(R.id.fragment_order_in_progress).setActionView(tvCounterOrderProgress);
                            Show_Counter_ProgressOrder(counterOrderInProgess);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void Show_Counter_ProgressOrder(int counter) {
        if (counter > 0) {
            tvCounterOrderProgress.setText(counter + "");
        } else {
            tvCounterOrderProgress.setText("");
        }
    }

    public static void Show_Counter_PackingOrder(int counter) {
        if (counter > 0) {
            tvCounterPackingOrder.setText(counter + "");
        } else {
            tvCounterPackingOrder.setText("");
        }
    }

    public static void Show_Counter_Order_Queue(int counter) {
        if (counter > 0) {
            tvCounterOrderQueue.setText(counter + "");
        } else {
            tvCounterOrderQueue.setText("");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
//        navController.navigateUp();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_container);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

}