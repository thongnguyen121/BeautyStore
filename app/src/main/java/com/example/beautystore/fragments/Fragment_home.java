package com.example.beautystore.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.AnimationTypes;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.beautystore.MainActivity;
import com.example.beautystore.R;
import com.example.beautystore.adapter.RecyclerViewBrands;
import com.example.beautystore.adapter.RecyclerViewCategories;
import com.example.beautystore.adapter.RecyclerViewProducts;
import com.example.beautystore.adapter.RecyclerView_search_products;
import com.example.beautystore.model.Brands;
import com.example.beautystore.model.Categories;
import com.example.beautystore.model.Products;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Pattern;


public class Fragment_home extends Fragment {

    public static RecyclerViewProducts recyclerViewProducts;
    public static RecyclerViewBrands recyclerViewBands;
    public static RecyclerViewCategories recylerViewCategories;
    RecyclerView_search_products recyclerViewSearchProducts;

    public static RecyclerView rcProducts, rcCategories, rcBrans, rcSearch;
    SearchView searchView;
//    ViewFlipper viewFlipper;
    ImageView btnFilter;
    ImageSlider imageSlider;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    public static ArrayList<Products> data_products = new ArrayList<>();
    public static ArrayList<Categories> data_categories = new ArrayList<>();
    public static ArrayList<Brands> data_brands = new ArrayList<>();
    TextView tvTitle_brands, tvTitle_categories, tvTitle_products;
    private boolean isSearchViewExpanded = false;
    boolean isPopupMenuOpen = false;
    public static final String SHARE_PREFS = "sharedPrefs";
    SpinKitView spinKitView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        setControl(view);
        RunWiewLipper();
        getProducts();
        setSearchView();
        getData_brands();
        getData_categories();
        getFilter_price(view);
        
        RecyclerViewBrands.selectedPosition_brands= -1;
        RecyclerViewCategories.selectedPosition_cate= -1;
        Fragment_home.recylerViewCategories.notifyDataSetChanged();
        Fragment_home.recyclerViewBands.notifyDataSetChanged();
        return view;

    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_appbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setControl(View view) {

        btnFilter = view.findViewById(R.id.btnFileter);
        rcProducts = view.findViewById(R.id.rcProducts);
        rcBrans = view.findViewById(R.id.rcBrand);
        rcCategories = view.findViewById(R.id.rcCategories);
//        viewFlipper = view.findViewById(R.id.viewflipper);\
        imageSlider = view.findViewById(R.id.imgSlider);
        searchView = view.findViewById(R.id.idsearchview_Home_user);
        rcSearch = view.findViewById(R.id.rc_search);
        tvTitle_products = view.findViewById(R.id.title_products);
        tvTitle_categories = view.findViewById(R.id.title_categories);
        tvTitle_brands = view.findViewById(R.id.title_brands);
        spinKitView = view.findViewById(R.id.spin_kit);
    }

    private void getProducts() {
        //Display products list
        recyclerViewProducts = new RecyclerViewProducts(requireContext(), R.layout.layout_item_products, data_products);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcProducts.setLayoutManager(layoutManager);
        rcProducts.setAdapter(recyclerViewProducts);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Products");

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (data_products != null) {
                    data_products.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    spinKitView.setVisibility(View.VISIBLE);
                    Products products = dataSnapshot.getValue(Products.class);
                    data_products.add(products);
                }
                recyclerViewProducts.notifyDataSetChanged();
                spinKitView.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setSearchView() {

//        recyclerViewSearchProducts = new RecyclerView_search_products(this, R.layout.layout_item_search, data_products);
//        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
//        layoutManager.setOrientation(RecyclerView.VERTICAL);
//        rcSearch.setLayoutManager(layoutManager);
//        rcSearch.setAdapter(recyclerViewSearchProducts);
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Products");
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (data_products != null) {
//                    data_products.clear();
//                }
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Products products = dataSnapshot.getValue(Products.class);
//                    data_products.add(products);
//                }
//                recyclerViewProducts.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setGonehome();
                isSearchViewExpanded = true;
            }
        });

//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//
//                if (isSearchViewExpanded) {
//                    setVisibityhome();
//                    getData_brands();
//                    getData_categories();
//                    getProducts();
//                    rcSearch.setVisibility(View.GONE);
//                    isSearchViewExpanded = false;
//                }
//                return false;
//            }
//        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                removeDiacritics(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                String searchTerm = removeDiacritics(newText);

                filterList(newText);

                return false;

            }
        });
    }
    public static String removeDiacritics(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("").toLowerCase();
    }

    public void getData_categories() {
        //Display categories list

        recylerViewCategories = new RecyclerViewCategories(this, R.layout.layout_items_categories, data_categories);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getContext(), 1);
        layoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        rcCategories.setLayoutManager(layoutManager1);
        rcCategories.setAdapter(recylerViewCategories);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Categories");

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (data_categories != null) {
                    data_categories.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Categories categories = dataSnapshot.getValue(Categories.class);
                    data_categories.add(categories);
                }
                recylerViewCategories.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.appBar_home){
            getProducts();
            getData_brands();
            getData_categories();
            RecyclerViewBrands.selectedPosition_brands= -1;
            RecyclerViewCategories.selectedPosition_cate= -1;
            rcBrans.setVisibility(View.VISIBLE);
            rcCategories.setVisibility(View.VISIBLE);
            tvTitle_categories.setVisibility(View.VISIBLE);
            tvTitle_brands.setVisibility(View.VISIBLE);
            imageSlider.setVisibility(View.VISIBLE);
            Fragment_home.recylerViewCategories.notifyDataSetChanged();
            Fragment_home.recyclerViewBands.notifyDataSetChanged();
            searchView.setIconified(true);
            searchView.clearFocus();
            searchView.setIconified(true);
            if (isUserLoggedin()){
                MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
            }
            else {
                MainActivity.bottomNavigationView.setVisibility(View.GONE);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData_brands() {
        //Display brands list
        recyclerViewBands = new RecyclerViewBrands(this, R.layout.layout_items_brans, data_brands);
        GridLayoutManager layoutManager2 = new GridLayoutManager(getContext(), 1);
        layoutManager2.setOrientation(RecyclerView.HORIZONTAL);
        rcBrans.setLayoutManager(layoutManager2);
        rcBrans.setAdapter(recyclerViewBands);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Brands");

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (data_brands != null) {
                    data_brands.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Brands brands = dataSnapshot.getValue(Brands.class);
                    data_brands.add(brands);
                }
                recyclerViewBands.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void refreshFragment() {
        getProducts();
        getData_brands();
        getData_categories();
        RecyclerViewBrands.selectedPosition_brands = -1;
        RecyclerViewCategories.selectedPosition_cate = -1;
        RecyclerViewBrands.brand_id = "";
        rcBrans.setVisibility(View.VISIBLE);
        rcCategories.setVisibility(View.VISIBLE);
        tvTitle_categories.setVisibility(View.VISIBLE);
        tvTitle_brands.setVisibility(View.VISIBLE);
        imageSlider.setVisibility(View.VISIBLE);

    }

    private void RunWiewLipper() {


        ArrayList<SlideModel> litsviewlippers = new ArrayList<>();
        litsviewlippers.add(new SlideModel("https://intphcm.com/data/upload/banner-my-pham-dep.jpg", ScaleTypes.FIT));
        litsviewlippers.add(new SlideModel("https://thegioidohoa.com/wp-content/uploads/2018/12/thi%E1%BA%BFt-k%E1%BA%BF-banner-m%E1%BB%B9-ph%E1%BA%A9m-6.png", ScaleTypes.FIT));
        litsviewlippers.add(new SlideModel("https://topprint.vn/wp-content/uploads/2021/07/banner-my-pham-dep-10.png", ScaleTypes.FIT));
        litsviewlippers.add(new SlideModel("https://youthvietnam.vn/wp-content/uploads/2021/05/banner-my-pham-noi-tieng.jpg", ScaleTypes.FIT));
        imageSlider.setImageList(litsviewlippers, ScaleTypes.FIT);
        imageSlider.setSlideAnimation(AnimationTypes.ZOOM_OUT);
//        litsviewlippers.add("https://intphcm.com/data/upload/banner-my-pham-dep.jpg");
//        for (int i = 0; i < litsviewlippers.size(); i++) {
//            ImageView imageView = new ImageView(getContext());
//            Picasso.get().load(litsviewlippers.get(i)).into(imageView);
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            viewFlipper.addView(imageView);
//        }
//        viewFlipper.setFlipInterval(3000);
//        viewFlipper.setAutoStart(true);
//
//        viewFlipper.setInAnimation(getContext(), android.R.anim.slide_in_left);
//        viewFlipper.setOutAnimation(getContext(), android.R.anim.slide_out_right);

    }

    private void getFilter_price(View view)
    {
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    rcBrans.setVisibility(View.GONE);
                    rcCategories.setVisibility(View.GONE);
                    tvTitle_categories.setVisibility(View.GONE);
                    tvTitle_brands.setVisibility(View.GONE);
                    imageSlider.setVisibility(View.GONE);
                    searchView.setIconified(true);
                    searchView.clearFocus();
                    searchView.setIconified(true);
                    getProducts();
                    showPopupMenu(v);


            }
        });


    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_price, popupMenu.getMenu());
        popupMenu.show();
//        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
//            @Override
//            public void onDismiss(PopupMenu menu) {
//                isPopupMenuOpen = false; // Đặt trạng thái menu popup về "đã đóng"
//                rcBrans.setVisibility(View.VISIBLE);
//                rcCategories.setVisibility(View.VISIBLE);
//                tvTitle_categories.setVisibility(View.VISIBLE);
//                tvTitle_brands.setVisibility(View.VISIBLE);
//            }
//        });
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.id_price_1){
                    filterProductsByPriceRange("0","100000");
                }
                else if (id == R.id.id_price_2)
                {
                    filterProductsByPriceRange("100000","300000");
                }
                else if (id == R.id.id_price_3)
                {
                    filterProductsByPriceRange("300000","500000");
                }
                else if (id == R.id.id_price_4)
                {
                    filterProductsByPriceRange("500000","800000");
                }
                else if (id == R.id.id_price_5)
                {
                    filterProductsByPriceRange("800000","1000000");
                }

                return true;
            }
        });


    }


    private void filterProductsByPriceRange(String minPriceStr, String maxPriceStr) {
        int minPrice = Integer.parseInt(minPriceStr);
        int maxPrice = Integer.parseInt(maxPriceStr);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Products");
        Query query = databaseReference.orderByChild("price");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data_products.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Products products = dataSnapshot.getValue(Products.class);

                    int productPrice = Integer.parseInt(products.getPrice());


                    if (productPrice >= minPrice && productPrice <= maxPrice) {
                        data_products.add(products);
                    }
                }

                recyclerViewProducts.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void filterList(String text) {
        ArrayList<Products> filteredlist = new ArrayList<>();
        for(Products item : data_products)
        {
            if(item.getProducts_name().toLowerCase().contains(text.toLowerCase()))
            {
                filteredlist.add(item);
            }
        }
        if(filteredlist.isEmpty())
        {

        }
        else
        {

            recyclerViewProducts.setFilterList_Products(filteredlist);
        }
    }

    private void setGonehome() {
        rcBrans.setVisibility(View.GONE);
        rcCategories.setVisibility(View.GONE);
        imageSlider.setVisibility(View.GONE);
        tvTitle_products.setVisibility(View.GONE);
        tvTitle_categories.setVisibility(View.GONE);
        tvTitle_brands.setVisibility(View.GONE);
        MainActivity.bottomNavigationView.setVisibility(View.GONE);

    }

//    private void setVisibityhome() {
//        rcBrans.setVisibility(View.VISIBLE);
//        rcCategories.setVisibility(View.VISIBLE);
//        rcProducts.setVisibility(View.VISIBLE);
//        viewFlipper.setVisibility(View.VISIBLE);
//        tvTitle_products.setVisibility(View.VISIBLE);
//        tvTitle_categories.setVisibility(View.VISIBLE);
//        tvTitle_brands.setVisibility(View.VISIBLE);
//        MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
//        MainActivity.toolbar.setVisibility(View.VISIBLE);
//    }
private void setVisibityhome() {
    rcBrans.setVisibility(View.VISIBLE);
    rcCategories.setVisibility(View.VISIBLE);
    imageSlider.setVisibility(View.VISIBLE);
    tvTitle_products.setVisibility(View.VISIBLE);
    tvTitle_categories.setVisibility(View.VISIBLE);
    tvTitle_brands.setVisibility(View.VISIBLE);
    if (isUserLoggedin()){
        MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
    }
    else {
        MainActivity.bottomNavigationView.setVisibility(View.GONE);
    }
}
    private boolean isUserLoggedin() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARE_PREFS, Context.MODE_PRIVATE);
        String check = sharedPreferences.getString("check", "");
        return check.equals("true");
    }


}