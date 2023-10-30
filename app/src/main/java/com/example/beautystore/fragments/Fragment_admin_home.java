package com.example.beautystore.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.beautystore.R;

public class Fragment_admin_home extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
               View view =  inflater.inflate(R.layout.fragment_admin_home, container, false);
        Button button = view.findViewById(R.id.btnNutBam);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_fragment_admin_home_to_activity_add_Brands);
            }
        });
               return  view;
    }
}