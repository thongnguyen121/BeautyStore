package com.example.beautystore;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;

import com.example.beautystore.fragments.Fragment_cart;
import com.example.beautystore.fragments.Fragment_editProfile;
import com.example.beautystore.fragments.Fragment_home;
import com.example.beautystore.fragments.Fragment_order;
import com.example.beautystore.fragments.Fragment_profile;
import com.example.beautystore.fragments.Fragment_transaction_history;
import com.example.beautystore.fragments.Fragment_wishlist;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    FragmentManager fragmentManager;

    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    private final int Fragment_home = 3;
    private final int Fragment_profile = 2;
    private final int Fragment_order = 1;
    private final int Fragment_wishlist = 4;
    private final int Fragment_cart = 5;
    private final int Fragment_editprofile = 6;
    private final int Fragment_transaction_history = 7;

    private int currentFragment = Fragment_home;
    private Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.idDrawer);
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.idbottomNavigation);



        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView = findViewById(R.id.id_navigationview);

        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView.setBackground(null);

        hideMenuItem(R.id.appBar_home);


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int idItem = item.getItemId();
                if (idItem == R.id.appBar_home) {
                    replaceFragment_home();
                    setTitle();
                    hideMenuItem(R.id.appBar_home);
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    bottomNavigationView.getMenu().findItem(R.id.menu_tap3).setChecked(true);
                    navigationView.getMenu().findItem(R.id.Edit_profile).setChecked(false);
                    navigationView.getMenu().findItem(R.id.Transaction_history).setChecked(false);


                } else if (idItem == R.id.appBar_notification) {
                    Toast.makeText(MainActivity.this, "Notification", Toast.LENGTH_SHORT).show();
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
                    hideMenuItem(R.id.appBar_home);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_appbar, menu);

        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.appBar_home) {
            item.setVisible(false); // Ẩn mục có ID "appbar"
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int idItem = item.getItemId();
        if (idItem == R.id.Edit_profile) {
            replaceFragment_editprofile();
            bottomNavigationView.setVisibility(View.GONE);
            displayMenuItem(R.id.appBar_home);

        } else if (idItem == R.id.Transaction_history) {
            replaceFragment_transaction_history();
            bottomNavigationView.setVisibility(View.GONE);
            displayMenuItem(R.id.appBar_home);
        } else if (idItem == R.id.Logout) {
            finish();
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
}