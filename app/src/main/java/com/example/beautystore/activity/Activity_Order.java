package com.example.beautystore.activity;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beautystore.MainActivity;
import com.example.beautystore.R;
import com.example.beautystore.adapter.RecyclerView_Order;
import com.example.beautystore.model.Cart;
import com.example.beautystore.model.CartDetail;
import com.example.beautystore.model.Customer;
import com.example.beautystore.model.Order;
import com.example.beautystore.model.OrderStatus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Activity_Order extends AppCompatActivity {

    EditText edtUserName, edtAddress, edtPhoneNumber;
    TextView tvTotalPrice;
    Spinner spPaymentMethod;
    Button btnOrder;
    ArrayList<CartDetail> cartDetails = new ArrayList<>();
    RecyclerView orderDetailRecyclerView;
    RecyclerView_Order orderDetailAdapter;
    String uid;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    List<CartDetail> detail = new ArrayList<>();
    String autoID ="", total, address, phoneNum, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        uid = FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        setScreenElement();
        getIDOrder();

        getUserInfo( databaseReference, uid);
        getCartItem(databaseReference, uid);
        saveCartItem(databaseReference, uid);
        getTotalPrice();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String savedate = simpleDateFormat.format(calendar.getTime());
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "auto id: " + autoID);
                address = edtAddress.getText().toString();
                phoneNum = edtPhoneNumber.getText().toString();
                name = edtUserName.getText().toString();
                Order order = new Order(autoID,uid,"0",savedate,total,address,phoneNum,name,detail);
                OrderStatus orderStatus = new OrderStatus(order.getOrder_id(),"0","");
                databaseReference.child("Order").child(autoID).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        databaseReference.child("OrderStatus").child(order.getOrder_id()).setValue(orderStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(Activity_Order.this, "thanh cong", Toast.LENGTH_SHORT).show();
                                databaseReference.child("Cart").child(uid).removeValue();
                            }
                        });

                        Intent intent = new Intent(Activity_Order.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Activity_Order.this, "khong thanh cong", Toast.LENGTH_SHORT).show();
                    }
                });
            }


        });

    }

    private void saveCartItem(DatabaseReference databaseReference, String uid) {
        databaseReference.child("Cart").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Cart cart = snapshot.getValue(Cart.class);
                     List<CartDetail>items = cart.getItems();
                    for (CartDetail cartDetail : items){
                        detail.add(cartDetail);
                        Log.d("TAG", "onDataChange: " + detail);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTotalPrice() {
            DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("Cart").child(FirebaseAuth.getInstance().getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    total = snapshot.child("total").getValue(String.class);
//                holder.tvPrice.setText(decimalFormat.format(Integer.valueOf(products.getPrice().trim()))+ " Đ");
                    if (snapshot.exists()){
                        tvTotalPrice.setText(decimalFormat.format(Integer.valueOf(total.trim())) + " Đ");
                    }else{
                        tvTotalPrice.setText( "0 Đ");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }

    private void getCartItem(DatabaseReference databaseReference, String uid) {
        orderDetailAdapter = new RecyclerView_Order(cartDetails,this,R.layout.layout_item_order);

        orderDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderDetailRecyclerView.setAdapter(orderDetailAdapter);
        databaseReference.child("Cart").child(uid).child("items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartDetails.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CartDetail item = dataSnapshot.getValue(CartDetail.class);
                    cartDetails.add(item);
                }
                orderDetailAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUserInfo(DatabaseReference reference, String uid) {
        reference.child("Customer").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Customer customer = snapshot.getValue(Customer.class);
                    edtUserName.setText(customer.getUsername());
                    edtAddress.setText(customer.getAddress());
                    edtPhoneNumber.setText(customer.getPhoneNumber());


                }
                else{
                    Log.d("TAG", "khong co: ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setScreenElement(){

        //Recyclerview:
        orderDetailRecyclerView = findViewById(R.id.rvOrderDetail);
//        orderDetails = new ArrayList<>();
//        createOrderDetailList();
//        orderDetailAdapter = new RecyclerView_Order(orderDetails, this);
//        orderDetailRecyclerView.setAdapter(orderDetailAdapter);
//        orderDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        edtUserName = findViewById(R.id.edtOrderUserName);
        edtAddress = findViewById(R.id.edtOrderAddress);
        edtPhoneNumber = findViewById(R.id.edtOrderPhoneNumber);

        tvTotalPrice = findViewById(R.id.tvOrderTotalMoney);
        spPaymentMethod = findViewById(R.id.spOrderPaymentMethod);
        btnOrder = findViewById(R.id.btnOrderOrderButton);
    }
    public void getIDOrder() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Order");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> dsUser = new ArrayList<>();
                if (snapshot.exists()){

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        dsUser.add(dataSnapshot.getKey());
                    }
                    String[] temp = dsUser.get(dsUser.size() - 1).split("DH");
                    String id = "";
                    int idNumber = Integer.parseInt(temp[1])+1;
                    if (idNumber < 10) {
                        id = "DH0" + idNumber;
                    } else {
                        id = "DH" + idNumber;
                    }
                    autoID = id;
                    Log.d("TAG", "cco: " + autoID);
                }else {

                    autoID = "DH01";
                    Log.d(TAG, "ko: " + autoID);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}