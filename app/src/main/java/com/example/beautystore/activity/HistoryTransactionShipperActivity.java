package com.example.beautystore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SearchView;

import com.example.beautystore.R;
import com.example.beautystore.adapter.RecyclerView_Brands_WH;
import com.example.beautystore.adapter.RecyclerView_transaction_history;
import com.example.beautystore.model.Brands;
import com.example.beautystore.model.History;
import com.example.beautystore.model.OrderStatus;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryTransactionShipperActivity extends AppCompatActivity {
    
    RecyclerView_transaction_history recyclerViewTransactionHistory;
    RecyclerView rcHistoryShipper;
    SearchView searchView;
    RadioButton rdoAll, rdoDelivered, rdoCancled;
    ImageView ivBack;
    ArrayList<History> data_History = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_transaction_shipper);
        setControl();
        getData_history();
        setSearchView();
        rdoAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                  getData_history();
                }

            }
        });
        rdoDelivered.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    getData_filter("6");
                }
            }
        });
        rdoCancled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    getData_filter("7");
                }

            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setControl() {
        rcHistoryShipper = findViewById(R.id.rcOrder_historyShipper);
        searchView = findViewById(R.id.idsearchview_history_shipper);
        rdoAll = findViewById(R.id.rdoAll_history_shipper);
        rdoDelivered = findViewById(R.id.rdoDelivered_shipper);
        rdoCancled = findViewById(R.id.rdoCancel_shipper);
        ivBack = findViewById(R.id.ivBack_historyShipper);

    }

    private void getData_history() {

        recyclerViewTransactionHistory = new RecyclerView_transaction_history(this, R.layout.layout_item_history_admin, data_History);
        GridLayoutManager layoutManager2 = new GridLayoutManager(this, 1);
        layoutManager2.setOrientation(RecyclerView.VERTICAL);
        rcHistoryShipper.setLayoutManager(layoutManager2);
        rcHistoryShipper.setAdapter(recyclerViewTransactionHistory);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("History");

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (data_History != null) {
                    data_History.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    History history = dataSnapshot.getValue(History.class);
                    data_History.add(history);
                }
                recyclerViewTransactionHistory.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getData_filter(String status) {

        recyclerViewTransactionHistory = new RecyclerView_transaction_history(this, R.layout.layout_item_history_admin, data_History);
        GridLayoutManager layoutManager2 = new GridLayoutManager(this, 1);
        layoutManager2.setOrientation(RecyclerView.VERTICAL);
        rcHistoryShipper.setLayoutManager(layoutManager2);
        rcHistoryShipper.setAdapter(recyclerViewTransactionHistory);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("History");

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (data_History != null) {
                    data_History.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    History history = dataSnapshot.getValue(History.class);
                    if(history.getStatus().equals(status))
                    {
                        data_History.add(history);
                    }

                }
                recyclerViewTransactionHistory.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setSearchView()
    {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                filterList(newText);

                return false;

            }
        });
    }


    private void filterList(String text) {
        ArrayList<History> filteredlist = new ArrayList<>();
        for (History item : data_History) {
            if (item.getOrder_id().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if(filteredlist.isEmpty())
        {

        }
        else
        {
            recyclerViewTransactionHistory.setFilterList(filteredlist);
        }


    }
    @Override
    public void onResume() {
        super.onResume();

        recyclerViewTransactionHistory.setFilterList(data_History);

    }
}