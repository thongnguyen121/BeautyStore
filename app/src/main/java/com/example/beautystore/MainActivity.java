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


import android.graphics.drawable.Drawable;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beautystore.fragments.Fragment_cart;
import com.example.beautystore.fragments.Fragment_editProfile;
import com.example.beautystore.fragments.Fragment_home;
import com.example.beautystore.fragments.Fragment_order;
import com.example.beautystore.fragments.Fragment_profile;
import com.example.beautystore.fragments.Fragment_transaction_history;
import com.example.beautystore.fragments.Fragment_wishlist;
import com.example.beautystore.model.Customer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

   public static BottomNavigationView bottomNavigationView;

    TextView tvProfile_name, tvProfile_email;
    DrawerLayout drawerLayout;
   public static Toolbar toolbar;
    FragmentManager fragmentManager;

    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    String email="", name="";
    private final int Fragment_home = 3;
    private final int Fragment_profile = 2;
    private final int Fragment_order = 1;
    private final int Fragment_wishlist = 4;
    private final int Fragment_cart = 5;
    private final int Fragment_editprofile = 6;
    private final int Fragment_transaction_history = 7;

    private int currentFragment = Fragment_home;
    private Menu menu;
    private MenuItem menuItem;
    public static final String SHARE_PREFS = "sharedPrefs";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.idDrawer);
        toolbar = findViewById(R.id.toolbar);
        firebaseDatabase = FirebaseDatabase.getInstance();

        bottomNavigationView = findViewById(R.id.idbottomNavigation);
        if (isUserLoggedin()) {
            Toast.makeText(this, "dax dang nhap", Toast.LENGTH_SHORT).show();
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "Chuaw dang nhap", Toast.LENGTH_SHORT).show();
            bottomNavigationView.setVisibility(View.GONE);
        }


        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView = findViewById(R.id.id_navigationview);

        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        tvProfile_name = header.findViewById(R.id.tvProfile_name_drawer);
        tvProfile_email = header.findViewById(R.id.tvEmail_drawer);
        bottomNavigationView.setBackground(null);



        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int idItem = item.getItemId();
                if (idItem == R.id.appBar_home) {
                    replaceFragment_home();
                    setTitle();
                    refreshMenuItem_back(R.id.appBar_home);
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    bottomNavigationView.getMenu().findItem(R.id.menu_tap3).setChecked(true);
                    navigationView.getMenu().findItem(R.id.Edit_profile).setChecked(false);
                    navigationView.getMenu().findItem(R.id.Transaction_history).setChecked(false);
                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.framelayout);
                    if (currentFragment instanceof Fragment_home) {
                        ((Fragment_home) currentFragment).refreshFragment();
                    }

                }
                return true;
            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int idItem = item.getItemId();
                if (idItem == R.id.menu_tap1) {
                    replaceFragment_orders();
                    navigationView.getMenu().findItem(R.id.Edit_profile).setChecked(false);
                    navigationView.getMenu().findItem(R.id.Transaction_history).setChecked(false);
                    hideMenuItem(R.id.appBar_home);
                    setTitle();
                    return true;
                } else if (idItem == R.id.menu_tap2) {
                    replaceFragment_profile();
                    navigationView.getMenu().findItem(R.id.Edit_profile).setChecked(false);
                    navigationView.getMenu().findItem(R.id.Transaction_history).setChecked(false);
                    hideMenuItem(R.id.appBar_home);
                    setTitle();
                    return true;
                } else if (idItem == R.id.menu_tap3) {
                    replaceFragment_home();
                    navigationView.getMenu().findItem(R.id.Edit_profile).setChecked(false);
                    navigationView.getMenu().findItem(R.id.Transaction_history).setChecked(false);
                    displayMenuItem(R.id.appBar_home);
                    setTitle();
                    return true;
                } else if (idItem == R.id.menu_tap4) {
                    replaceFragment_wishlist();
                    navigationView.getMenu().findItem(R.id.Edit_profile).setChecked(false);
                    navigationView.getMenu().findItem(R.id.Transaction_history).setChecked(false);
                    hideMenuItem(R.id.appBar_home);
                    setTitle();
                    return true;
                } else if (idItem == R.id.menu_tap5) {
                    replaceFragment_cart();
                    navigationView.getMenu().findItem(R.id.Edit_profile).setChecked(false);
                    navigationView.getMenu().findItem(R.id.Transaction_history).setChecked(false);
                    hideMenuItem(R.id.appBar_home);
                    setTitle();

                    return true;
                }


                return false;
            }
        });

        setTitle();
        toolbar.getMenu().findItem(R.id.appBar_home).setVisible(false);
        fragmentManager = getSupportFragmentManager();
        openFragment(new Fragment_home());

        bottomNavigationView.setSelectedItemId(R.id.menu_tap3);


    }
//    private void actionMenuItem(int itemId) {
//        if (menu != null) {
//            MenuItem itemToHide = menu.findItem(itemId); // Tìm mục cần ẩn
//            if (itemToHide != null) {
//                itemToHide.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER); // Ẩn mục cụ thể
//            }
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_appbar, menu);

        this.menu = menu;
        if (isUserLoggedin()) {
            navigationView.getMenu().findItem(R.id.Login).setVisible(false);
            navigationView.getMenu().findItem(R.id.Signup).setVisible(false);
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            databaseReference = firebaseDatabase.getReference("Customer");
            databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Customer customer = snapshot.getValue(Customer.class);
                    name = customer.getUsername();
                    email = customer.getEmail();

                    if (customer != null) {
                        Log.e("TAG", "onDataChange: "+customer.getUsername() );
                        tvProfile_name.setVisibility(View.VISIBLE);
                        tvProfile_name.setText(name);
                        tvProfile_email.setVisibility(View.VISIBLE);
                        tvProfile_email.setText(email);
                    }
                    else{
                            tvProfile_name.setText("Guess");
                            tvProfile_email.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "cant", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            navigationView.getMenu().findItem(R.id.Edit_profile).setVisible(false);
            navigationView.getMenu().findItem(R.id.Transaction_history).setVisible(false);
            navigationView.getMenu().findItem(R.id.Logout).setVisible(false);
        }
        return true;
    }

    private void hideMenuItem(int itemId) {
        if (menu != null) {
            MenuItem itemToHide = menu.findItem(itemId); // Tìm mục cần ẩn
            if (itemToHide != null) {
                itemToHide.setVisible(false); // Ẩn mục cụ thể
            }
        }
    }

    private void displayMenuItem(int itemId) {
        if (menu != null) {
            MenuItem itemToHide = menu.findItem(itemId); // Tìm mục cần ẩn
            if (itemToHide != null) {
                itemToHide.setVisible(true); // Ẩn mục cụ thể
            }
        }
    }
    private void refreshMenuItem(int itemId) {

        if (menu != null) {
            MenuItem itemToHide = menu.findItem(itemId); // Tìm mục cần ẩn
            if (itemToHide != null) {
                itemToHide.setIcon(R.drawable.baseline_home_24);
            }
        }
    }
    private void refreshMenuItem_back(int itemId) {

        if (menu != null) {
            MenuItem itemToHide = menu.findItem(itemId); // Tìm mục cần ẩn
            if (itemToHide != null) {
                itemToHide.setIcon(R.drawable.baseline_refresh_24);
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int idItem = item.getItemId();
        if (idItem == R.id.Edit_profile) {
            replaceFragment_editprofile();
            bottomNavigationView.setVisibility(View.GONE);
            displayMenuItem(R.id.appBar_home);
            refreshMenuItem(R.id.appBar_home);

        } else if (idItem == R.id.Transaction_history) {
            replaceFragment_transaction_history();
            bottomNavigationView.setVisibility(View.GONE);
            displayMenuItem(R.id.appBar_home);
            refreshMenuItem(R.id.appBar_home);
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
        if (idItem == R.id.Login) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);

            startActivity(intent);

        } else if (idItem == R.id.Signup) {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        }


        setTitle();
        drawerLayout.closeDrawer(GravityCompat.START);


        return true;
    }

    private void replaceFragment_home() {
        if (currentFragment != Fragment_home) {
            openFragment(new Fragment_home());
            currentFragment = Fragment_home;


        }

    }

    private void replaceFragment_profile() {
        if (currentFragment != Fragment_profile) {
            openFragment(new Fragment_profile());
            currentFragment = Fragment_profile;
            if (currentFragment ==  Fragment_profile)
            {
                hideMenuItem(R.id.appBar_home);
            }
        }
    }

    private void replaceFragment_orders() {
        if (currentFragment != Fragment_order) {
            openFragment(new Fragment_order());
            currentFragment = Fragment_order;
        }
    }

    private void replaceFragment_wishlist() {
        if (currentFragment != Fragment_wishlist) {
            openFragment(new Fragment_wishlist());
            currentFragment = Fragment_wishlist;
        }
    }

    private void replaceFragment_cart() {
        if (currentFragment != Fragment_cart) {
            openFragment(new Fragment_cart());
            currentFragment = Fragment_cart;
        }
    }

    private void replaceFragment_editprofile() {
        if (currentFragment != Fragment_editprofile) {
            openFragment(new Fragment_editProfile());
            currentFragment = Fragment_editprofile;

        }
    }

    private void replaceFragment_transaction_history() {
        if (currentFragment != Fragment_transaction_history) {
            openFragment(new Fragment_transaction_history());
            currentFragment = Fragment_transaction_history;
        }
    }


//    private void backToHome() {
//        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//            getSupportFragmentManager().popBackStack();
//            bottomNavigationView.setVisibility(View.VISIBLE);
//        } else {
//            finish();
//        }
//    }

    private void setTitle() {
        String title = "";
        switch (currentFragment) {
            case Fragment_home:
                title = getString(R.string.fragment_home);
                break;
            case Fragment_profile:
                title = getString(R.string.fragment_profile);
                break;
            case Fragment_order:
                title = getString(R.string.fragment_orders);
                break;
            case Fragment_wishlist:
                title = getString(R.string.fragment_wishlist);
                break;
            case Fragment_cart:
                title = getString(R.string.fragment_cart);
                break;
            case Fragment_editprofile:
                title = getString(R.string.fragment_edit_profile);
                break;
            case Fragment_transaction_history:
                title = getString(R.string.fragment_transaction_history);
                break;
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);

        }


    }


    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }

    private void stackFragment_home(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.framelayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private boolean isUserLoggedin() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS, Context.MODE_PRIVATE);
        String check = sharedPreferences.getString("check", "");
        return check.equals("true");
    }

    ;
}