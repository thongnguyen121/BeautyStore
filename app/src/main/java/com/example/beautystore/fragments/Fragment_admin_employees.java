package com.example.beautystore.fragments;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.beautystore.R;
import com.example.beautystore.activity.Activity_add_members;
import com.example.beautystore.activity.Admin_MainActivity;
import com.example.beautystore.adapter.RecyclerView_Member;
import com.example.beautystore.model.Members;
import com.example.beautystore.model.OrderStatus;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class Fragment_admin_employees extends Fragment {
    RecyclerView_Member memberAdapter;
    ArrayList<Members> data = new ArrayList<>();
    RecyclerView recyclerView;
    DatabaseReference reference;
    RadioButton rdoAll, rdoShipper, rdoTVV, rdoLock;
    String checkrole = "";
    RadioGroup radioGroup;
    private List<String> selectedRoles = new ArrayList<>();
    public static  boolean statusEmployee = true;
    private boolean isSearchViewExpanded = false;
    SpinKitView spinKitView;
    SearchView searchView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_employees, container, false);
        setHasOptionsMenu(true);
        setControl(view);
        reference = FirebaseDatabase.getInstance().getReference("Member");

        getData();
        setSearchView();
        data.clear();
        rdoAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    data.clear();
                    getData();
                }

            }
        });
        rdoShipper.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    data.clear();
                    checkrole = "1";
                    getShipperList(checkrole);
                }
            }
        });
        rdoTVV.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    data.clear();
                    checkrole = "2";
                    getShipperList(checkrole);
                }

            }
        });
        rdoLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    data.clear();
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                Members member = dataSnapshot.getValue(Members.class);
                                if (member.getStatus().equals("1"))
                                    data.add(member);
                            }
                            memberAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        return view;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.appBar_home){
            statusEmployee = true;
            Intent intent = new Intent(getContext(), Activity_add_members.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void getShipperList(String role) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Members member = dataSnapshot.getValue(Members.class);
                    if (member.getRole().equals(role)){
                        data.add(member);
                    }

                }
                recyclerView.setVisibility(View.VISIBLE);
                spinKitView.setVisibility(View.GONE);
                memberAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        memberAdapter.setFilterList(data);

    }
    private void getData() {
        memberAdapter = new RecyclerView_Member(requireContext(), data);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(memberAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager( getContext()));
//        recyclerView.setAdapter(memberAdapter);

        reference.orderByChild("role").startAt("1").endAt("2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Members member = snapshot.getValue(Members.class);
                    data.add(member);
                }
                memberAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int pos = viewHolder.getAdapterPosition();
            Members members = memberAdapter.getData(pos);
            switch (direction){
                case ItemTouchHelper.LEFT:
                    reference.child(members.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            recyclerView.setVisibility(View.GONE);
                            spinKitView.setVisibility(View.VISIBLE);
                            Members members1 = snapshot.getValue(Members.class);
                            final String[] status = {""};
                            Toast.makeText(getContext(), "Đã khóa " +members1.getId(), Toast.LENGTH_SHORT).show();
//                            changeStatusMember(members1.getId(), members1.getStatus());
                            reference.child(members1.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        Map<String, Object> update = new HashMap<>();
                                        if (members1.getStatus().equals("0")){
                                            update.put("status", "1");
                                            status[0] = "1";
//                        reference.child(id).updateChildren(update);
                                        }
                                        else{
                                            update.put("status", "0");
                                            status[0] = "0";
                                        }
                                        reference.child(members1.getId()).updateChildren(update).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                members.setStatus(status[0]);
                                                Log.d("TAG", "role : " + status[0]);
                                                if (rdoAll.isChecked()){
                                                    recyclerView.setVisibility(View.VISIBLE);
                                                    spinKitView.setVisibility(View.GONE);
                                                    memberAdapter.notifyDataSetChanged();
                                                }
                                                else if (rdoShipper.isChecked()){
                                                    data.clear();
                                                    getShipperList("1");
                                                } else if (rdoTVV.isChecked()) {
                                                    data.clear();
                                                    getShipperList("2");
                                                }
                                                else{
                                                    data.clear();
                                                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                                Members member = dataSnapshot.getValue(Members.class);
                                                                if (member.getStatus().equals("1"))
                                                                    data.add(member);
                                                            }
                                                            recyclerView.setVisibility(View.VISIBLE);
                                                            spinKitView.setVisibility(View.GONE);
                                                            memberAdapter.notifyDataSetChanged();
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }

                                            }
                                        });
                                        Toast.makeText(getContext(), "Thành công ", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getContext(), "không thành công", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    break;
                case ItemTouchHelper.RIGHT:
                    statusEmployee = false;
                    Intent intent = new Intent(getContext(), Activity_add_members.class);
                    intent.putExtra("id", members.getId());
                    startActivity(intent);
                    memberAdapter.notifyDataSetChanged();
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.bg_pink))
                    .addSwipeLeftActionIcon(R.drawable.baseline_lock_person_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.red))
                    .addSwipeRightActionIcon(R.drawable.baseline_edit_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void changeStatusMember(String id, String status) {
        reference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Map<String, Object> update = new HashMap<>();
                    if (status.equals("0")){
                        update.put("status", "1");
//                        reference.child(id).updateChildren(update);
                    }
                    else{
                        update.put("status", "0");

                    }
                    reference.child(id).updateChildren(update).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                    Toast.makeText(getContext(), "Thành công" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Không thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setSearchView()
    {
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Admin_MainActivity.bottomNavigationView.setVisibility(View.GONE);
                isSearchViewExpanded = true;
            }
        });


      searchView.setOnCloseListener(new SearchView.OnCloseListener() {
          @Override
          public boolean onClose() {
              if (isSearchViewExpanded)
              {
                  Admin_MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
              }
              return false;
          }
      });
      searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
          @Override
          public boolean onQueryTextSubmit(String query) {
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
public static String removeDiacritics(String input) {
    String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    return pattern.matcher(nfdNormalizedString).replaceAll("").toLowerCase();
}
    private void filterList(String text) {
        ArrayList<Members> filteredlist = new ArrayList<>();
        for (Members item : data) {
            if (item.getUsername().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }

        memberAdapter.setFilterList(filteredlist);
    }
    private void setControl(View view) {
        recyclerView = view.findViewById(R.id.rcMember);
        radioGroup = view.findViewById(R.id.rdoGroup);
        rdoAll = view.findViewById(R.id.rdoAll);
        rdoShipper = view.findViewById(R.id.rdoShipper);
        rdoTVV = view.findViewById(R.id.rdoTVV);
        rdoLock = view.findViewById(R.id.rdoLock);
        searchView = view.findViewById(R.id.idSearch_employees);
        spinKitView = view.findViewById(R.id.spin_kitFilter);
    }
}