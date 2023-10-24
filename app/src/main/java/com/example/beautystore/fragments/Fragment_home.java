package com.example.beautystore.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.beautystore.R;
import com.example.beautystore.adapter.RecyclerViewBrands;
import com.example.beautystore.adapter.RecyclerViewCategories;
import com.example.beautystore.adapter.RecyclerViewProducts;
import com.example.beautystore.model.Brands;
import com.example.beautystore.model.Categories;
import com.example.beautystore.model.Products;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class Fragment_home extends Fragment  {

    RecyclerViewProducts recyclerViewProducts;
    RecyclerViewBrands recyclerViewBands;
   RecyclerViewCategories recylerViewCategories;
    RecyclerView rcProducts, rcCategories, rcBrans;
    SearchView searchView;
    ViewFlipper viewFlipper;
    ImageView btnFilter, btnBack;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ArrayList<Products> data = new ArrayList<>();
    ArrayList<Categories>data_categories = new ArrayList<>();
    ArrayList<Brands>data_brands = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        setControl(view);
        khoiTao();

//        setDrawerLayout();
        RunWiewLipper();
        //Display products list
        recyclerViewProducts = new RecyclerViewProducts(this, R.layout.layout_item_products, data);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcProducts.setLayoutManager(layoutManager);
        rcProducts.setAdapter(recyclerViewProducts);

        //Display categories list

        recylerViewCategories = new RecyclerViewCategories(this,R.layout.layout_items_categories, data_categories);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getContext(), 1);
        layoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        rcCategories.setLayoutManager(layoutManager1);
        rcCategories.setAdapter(recylerViewCategories);


        //Display brands list

        recyclerViewBands = new RecyclerViewBrands(this,R.layout.layout_items_brans, data_brands);
        GridLayoutManager layoutManager2 = new GridLayoutManager(getContext(), 1);
        layoutManager2.setOrientation(RecyclerView.HORIZONTAL);
        rcBrans.setLayoutManager(layoutManager2);
        rcBrans.setAdapter(recyclerViewBands);



        return view;

    }
    private void setControl(View view) {
        btnBack = view.findViewById(R.id.btnBackhome);
        btnFilter = view.findViewById(R.id.btnFileter);
        rcProducts =  view.findViewById(R.id.rcProducts);
        rcBrans = view.findViewById(R.id.rcBrand);
        rcCategories = view.findViewById(R.id.rcCategories);
        viewFlipper = view.findViewById(R.id.viewflipper);

    }
    private void khoiTao()
    {
        //Add data products
        data.add(new Products("asdasd","asdasd","asdasd","adsasdsad","asdasdsad","asdasd","asdasd",R.drawable.mypham));
        data.add(new Products("asdasd","asdasd","asdasd","adsasdsad","asdasdsad","asdasd","asdasd",R.drawable.mypham));
        data.add(new Products("asdasd","asdasd","asdasd","adsasdsad","asdasdsad","asdasd","asdasd",R.drawable.mypham));
        data.add(new Products("asdasd","asdasd","asdasd","adsasdsad","asdasdsad","asdasd","asdasd",R.drawable.mypham));
        data.add(new Products("asdasd","asdasd","asdasd","adsasdsad","asdasdsad","asdasd","asdasd",R.drawable.mypham));
        data.add(new Products("asdasd","asdasd","asdasd","adsasdsad","asdasdsad","asdasd","asdasd",R.drawable.mypham));
        data.add(new Products("asdasd","asdasd","asdasd","adsasdsad","asdasdsad","asdasd","asdasd",R.drawable.hinh));
        data.add(new Products("asdasd","asdasd","asdasd","adsasdsad","asdasdsad","asdasd","asdasd",R.drawable.hinh));
        data.add(new Products("asdasd","asdasd","asdasd","adsasdsad","asdasdsad","asdasd","asdasd",R.drawable.hinh));


        //Add categories

        data_categories.add(new Categories("son moi",R.drawable.mypham));
        data_categories.add(new Categories("son moi",R.drawable.mypham));
        data_categories.add(new Categories("son moi",R.drawable.mypham));
        data_categories.add(new Categories("son moi",R.drawable.mypham));
        data_categories.add(new Categories("son moi",R.drawable.mypham));
        data_categories.add(new Categories("son moi",R.drawable.mypham));
        data_categories.add(new Categories("son moi",R.drawable.mypham));


        //Add brands
        data_brands.add(new Brands("son moi",R.drawable.mypham));
        data_brands.add(new Brands("son moi",R.drawable.mypham));
        data_brands.add(new Brands("son moi",R.drawable.mypham));
        data_brands.add(new Brands("son moi",R.drawable.mypham));
        data_brands.add(new Brands("son moi",R.drawable.mypham));
        data_brands.add(new Brands("son moi",R.drawable.mypham));
        data_brands.add(new Brands("son moi",R.drawable.mypham));

    }
    public void RunWiewLipper() {


        ArrayList<String> litsviewlippers = new ArrayList<>();
        litsviewlippers.add("https://firebasestorage.googleapis.com/v0/b/bookstoreapp-b0b38.appspot.com/o/qc1.jpg?alt=media&token=1dd1c8b2-ee28-47a4-9deb-051db1eccb2c");
        litsviewlippers.add("https://firebasestorage.googleapis.com/v0/b/bookstoreapp-b0b38.appspot.com/o/qc2.webp?alt=media&token=a28991f9-7414-443f-8e3e-1977c6c19b04");
        litsviewlippers.add("https://firebasestorage.googleapis.com/v0/b/bookstoreapp-b0b38.appspot.com/o/qc4.jpg?alt=media&token=52360472-2605-4206-a924-e7909d82838b");
        litsviewlippers.add("https://firebasestorage.googleapis.com/v0/b/bookstoreapp-b0b38.appspot.com/o/qc5.jpg?alt=media&token=7c8fdf14-f6ea-44cd-920d-2ccb4362c953");
        for (int i = 0; i < litsviewlippers.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            Picasso.get().load(litsviewlippers.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);

        viewFlipper.setInAnimation(getContext(), android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(getContext(), android.R.anim.slide_out_right);

    }

}