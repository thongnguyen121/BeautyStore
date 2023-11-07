package com.example.beautystore.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.beautystore.R;
import com.example.beautystore.activity.Activity_Add_Products;
import com.example.beautystore.activity.Activity_add_Brands;
import com.example.beautystore.activity.Activity_add_Categories;
import com.example.beautystore.activity.Admin_MainActivity;
import com.example.beautystore.adapter.RecyclerViewProducts_WH;
import com.example.beautystore.adapter.RecyclerView_Brands_WH;
import com.example.beautystore.adapter.RecyclerView_cate_WH;
import com.example.beautystore.model.Brands;
import com.example.beautystore.model.Categories;
import com.example.beautystore.model.Products;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Pattern;


public class Fragment_warehouse_list extends Fragment {


    public  static RecyclerView_Brands_WH recyclerViewBrandsWh;
    public static RecyclerViewProducts_WH recyclerViewProductsWh;
    public static RecyclerView_cate_WH recyclerView_cate_wh;
    public static RecyclerView rcProducst, rcCategories, rcBrands;
    public static ArrayList<Products> data_products = new ArrayList<>();
    public static ArrayList<Categories>  data_categories= new ArrayList<>();
    public static ArrayList<Brands> data_brands = new ArrayList<>();
    SearchView searchView;
    private boolean isSearchViewExpanded = false;
    View view1, view2,view3, view4,view5;
    ImageView imgFiterprice, imgAdd_products, imgCate_add, imgBrans_add;
    TextView tvTitle_brands, tvTitle_cate, tvTitle_producst;
    private NestedScrollView scrollView;
    public static boolean statusBrands = true;
    public static boolean statusProducts = true;
    public static  boolean statusCate = true;
    public static TextView tvThongbao_cate, tvThongbao_products;
    String cateName, imgCate;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_warehouse_list, container, false);
        setHasOptionsMenu(true);
        setControl(view);
        getProducts();
        getData_brands();
        getData_categories();
        setSearchView();
        getFilter_price(view);
        getIntent_Categories_add();
        imgAdd_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Activity_Add_Products.class);
                startActivity(intent);
                statusProducts = true;

            }
        });
        imgCate_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_add_Categories.class);
                startActivity(intent);
                statusCate = true;
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_appbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    private void getIntent_Categories_add(){
        imgBrans_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Activity_add_Brands.class);
                startActivity(intent);
                statusBrands = true;
            }
        });
    }

    private void setControl(View view) {
        rcBrands = view.findViewById(R.id.rcBrands_warehouse);
        rcCategories = view.findViewById(R.id.rcCategories_warehouse);
        rcProducst = view.findViewById(R.id.rcProducts_warehouse);
        searchView = view.findViewById(R.id.idsearchview_wh);
        view1 = view.findViewById(R.id.id_view1);
        view2 = view.findViewById(R.id.id_view2);
        view3 = view.findViewById(R.id.id_view3);
        view4 = view.findViewById(R.id.id_view4);
        view5 = view.findViewById(R.id.id_view5);
        imgFiterprice = view.findViewById(R.id.btnFileter_warehouse);
        imgBrans_add = view.findViewById(R.id.img_add_brands_wh);
        imgAdd_products = view.findViewById(R.id.img_add_products_wh);
        imgCate_add = view.findViewById(R.id.img_add_cate_wh);
        tvTitle_producst = view.findViewById(R.id.tiltle_products_wh);
        tvTitle_brands = view.findViewById(R.id.tiltle_brands_wh);
        tvTitle_cate = view.findViewById(R.id.tiltle_categories_wh);
//        scrollView = view.findViewById(R.id.id_scrollView_warehouse_list);
        tvThongbao_cate = view.findViewById(R.id.tvThongbao_cate);
        tvThongbao_products = view.findViewById(R.id.tvThongbao_products);


    }
    private void getProducts() {
        //Display products list
        recyclerViewProductsWh = new RecyclerViewProducts_WH(this, R.layout.layout_products_item_warehouse, data_products);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcProducst.setLayoutManager(layoutManager);
        rcProducst.setAdapter(recyclerViewProductsWh);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Products");

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (data_products != null) {
                    data_products.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Products products = dataSnapshot.getValue(Products.class);
                    data_products.add(products);
                }
                recyclerViewProductsWh.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getData_categories() {
        //Display categories list
        recyclerView_cate_wh = new RecyclerView_cate_WH(this, R.layout.layout_items_categories_warehouse, data_categories);
        GridLayoutManager layoutManager2 = new GridLayoutManager(getContext(), 1);
        layoutManager2.setOrientation(RecyclerView.HORIZONTAL);
        rcCategories.setLayoutManager(layoutManager2);
        rcCategories.setAdapter(recyclerView_cate_wh);
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
                recyclerView_cate_wh.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void getData_brands() {
        //Display brands list
        recyclerViewBrandsWh = new RecyclerView_Brands_WH(this, R.layout.layout_item_brands_warehouse, data_brands);
        GridLayoutManager layoutManager2 = new GridLayoutManager(getContext(), 1);
        layoutManager2.setOrientation(RecyclerView.HORIZONTAL);
        rcBrands.setLayoutManager(layoutManager2);
        rcBrands.setAdapter(recyclerViewBrandsWh);
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
                recyclerViewBrandsWh.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setSearchView() {

        recyclerViewProductsWh = new RecyclerViewProducts_WH(this, R.layout.layout_products_item_warehouse, data_products);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcProducst.setLayoutManager(layoutManager);
        rcProducst.setAdapter(recyclerViewProductsWh);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Products");

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (data_products != null) {
                    data_products.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Products products = dataSnapshot.getValue(Products.class);
                    data_products.add(products);
                }
                recyclerViewProductsWh.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setGonehome();
                getProducts();
                isSearchViewExpanded = true;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                if (isSearchViewExpanded) {
                    setVisibityhome();
                    getData_brands();
                    getData_categories();
                    getProducts();
                    isSearchViewExpanded = false;
                }
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                removeDiacritics(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String searchTerm = removeDiacritics(newText);
                filterList(searchTerm);

                return false;

            }
        });
    }
    private void filterList(String text) {
        ArrayList<Products> filteredlist = new ArrayList<>();
        for (Products item : data_products) {
            if (item.getProducts_name().toLowerCase().contains(text.toLowerCase()) || item.getProducts_id().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }

        recyclerViewProductsWh.setFilterList_Products(filteredlist);
    }
    private void getFilter_price(View view)
    {
        imgFiterprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setGonehome();
                getProducts();
                showPopupMenu(v);


            }
        });


    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_price, popupMenu.getMenu());
        popupMenu.show();

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

                recyclerViewProductsWh.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static String removeDiacritics(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("").toLowerCase();
    }
    private void setGonehome()
    {
        rcBrands.setVisibility(View.GONE);
        rcCategories.setVisibility(View.GONE);
        view1.setVisibility(View.GONE);
        view2.setVisibility(View.GONE);
        view3.setVisibility(View.GONE);
        view4.setVisibility(View.GONE);
        imgCate_add.setVisibility(View.GONE);
        imgBrans_add.setVisibility(View.GONE);
        imgAdd_products.setVisibility(View.GONE);
        tvTitle_cate.setVisibility(View.GONE);
        tvTitle_brands.setVisibility(View.GONE);
        Admin_MainActivity.bottomNavigationView.setVisibility(View.GONE);

    }
    private void setVisibityhome(){
        rcBrands.setVisibility(View.VISIBLE);
        rcCategories.setVisibility(View.VISIBLE);
        view1.setVisibility(View.VISIBLE);
        view2.setVisibility(View.VISIBLE);
        view3.setVisibility(View.VISIBLE);
        view4.setVisibility(View.VISIBLE);
        imgCate_add.setVisibility(View.VISIBLE);
        imgBrans_add.setVisibility(View.VISIBLE);
        imgAdd_products.setVisibility(View.VISIBLE);
        tvTitle_cate.setVisibility(View.VISIBLE);
        tvTitle_brands.setVisibility(View.VISIBLE);
        Admin_MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
    }
}