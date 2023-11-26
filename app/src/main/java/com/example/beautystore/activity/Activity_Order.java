package com.example.beautystore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beautystore.MainActivity;
import com.example.beautystore.NotificationSender;
import com.example.beautystore.R;
import com.example.beautystore.adapter.PaymentMethodAdapter;
import com.example.beautystore.adapter.RecyclerView_Order;
import com.example.beautystore.model.Cart;
import com.example.beautystore.model.CartDetail;
import com.example.beautystore.model.Customer;
import com.example.beautystore.model.Order;
import com.example.beautystore.model.OrderStatus;
import com.example.beautystore.model.PaymentMethod;
import com.example.beautystore.model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.momo.momo_partner.AppMoMoLib;

public class Activity_Order extends AppCompatActivity {

    EditText edtUserName, edtAddress, edtPhoneNumber;
    TextView tvTotalPrice;
    Spinner spPaymentMethod;
    PaymentMethodAdapter paymentMethodAdapter;
    Button btnOrder;
    ImageView ivBack;
    ArrayList<CartDetail> cartDetails = new ArrayList<>();
    RecyclerView orderDetailRecyclerView;
    RecyclerView_Order orderDetailAdapter;
    String uid;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    List<CartDetail> detail = new ArrayList<>();
    String autoID = "", total, address, phoneNum, name, productId, price, productQty,savedate;
    boolean check;
    NotificationSender notificationSender;
    int paymentMethod = 0;

    private String amount = "1";
    private String fee = "0";
    int environment = 0;//developer default
    private String merchantName = "Merchant123556666";
    private String merchantCode = "MOMOIQA420180417";
    private String merchantNameLabel = "Nhà cung cấp";
    private String description = "Thanh toán đơn hàng";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT); // AppMoMoLib.ENVIRONMENT.PRODUCTION
        uid = FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        check = getIntent().getBooleanExtra("buynow", false);
        productId = getIntent().getStringExtra("productId");
        price = getIntent().getStringExtra("price");
        productQty = getIntent().getStringExtra("productQty");
        databaseReference = database.getReference();
        notificationSender = new NotificationSender(getApplicationContext());
        setScreenElement();
        getIDOrder();
        if (check) {
            cartDetails.clear();
            cartDetails.add(new CartDetail(productId, price, productQty));
            detail.add(new CartDetail(productId, price, productQty));
            orderDetailAdapter.notifyDataSetChanged();
        } else {
            getCartItem(databaseReference, uid);
            saveCartItem(databaseReference, uid);
        }
        getUserInfo(databaseReference, uid);

        getTotalPrice();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        savedate = simpleDateFormat.format(calendar.getTime());
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (paymentMethod != 0){
                    requestPayment();
                }
                else {
                    createOrder();
                }

            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void createOrder(){

        Log.d("TAG", "auto id: " + autoID);
        address = edtAddress.getText().toString();
        phoneNum = edtPhoneNumber.getText().toString();
        name = edtUserName.getText().toString();
        Order order = new Order(autoID, uid, "0",savedate , total, address, phoneNum, name, detail);
        OrderStatus orderStatus = new OrderStatus(order.getOrder_id(), "0", "", "", "");
        databaseReference.child("Order").child(autoID).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                for (CartDetail item : detail) {
                    DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Products").child(item.getProduct_id());
                    productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Products products = snapshot.getValue(Products.class);
                                int newQty = Integer.parseInt(products.getQuantity()) - Integer.parseInt(item.getQty());
                                productRef.child("quantity").setValue(String.valueOf(newQty)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        databaseReference.child("OrderStatus").child(order.getOrder_id()).setValue(orderStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                String order_id = order.getOrder_id();
                                                Toast.makeText(Activity_Order.this, "thanh cong", Toast.LENGTH_SHORT).show();
                                                notificationSender.sendNotificationAdmin("0", order_id);
                                                if (!check) {
                                                    databaseReference.child("Cart").child(uid).removeValue();
                                                }

                                            }
                                        });

                                        Intent intent = new Intent(Activity_Order.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Activity_Order.this, "khong thanh cong", Toast.LENGTH_SHORT).show();
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
        if (check) {
            int totalmoney = Integer.parseInt(price) * Integer.parseInt(productQty);
            total = String.valueOf(totalmoney);
            tvTotalPrice.setText(decimalFormat.format(totalmoney) + " Đ");
        } else {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("Cart").child(FirebaseAuth.getInstance().getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    total = snapshot.child("total").getValue(String.class);
//                holder.tvPrice.setText(decimalFormat.format(Integer.valueOf(products.getPrice().trim()))+ " Đ");
                    if (snapshot.exists()) {
                        tvTotalPrice.setText(decimalFormat.format(Integer.valueOf(total.trim())) + " Đ");
                    } else {
                        tvTotalPrice.setText("0 Đ");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
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
        ivBack = findViewById(R.id.ivBackOrder);
        tvTotalPrice = findViewById(R.id.tvOrderTotalMoney);
        setPaymentMethodSpinner();
        btnOrder = findViewById(R.id.btnOrderOrderButton);
        orderDetailAdapter = new RecyclerView_Order(cartDetails, this, R.layout.layout_item_order);

        orderDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderDetailRecyclerView.setAdapter(orderDetailAdapter);
    }

    public void setPaymentMethodSpinner(){
        spPaymentMethod = findViewById(R.id.spOrderPaymentMethod);
        List<PaymentMethod> list = new ArrayList<>();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference paymentMethodRef = firebaseDatabase.getReference().child("PaymentMethod");
        paymentMethodRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    PaymentMethod paymentMethod = dataSnapshot.getValue(PaymentMethod.class);
                    list.add(paymentMethod);
                }
                paymentMethodAdapter = new PaymentMethodAdapter(Activity_Order.this, R.layout.item_spinner_payment_method_selected, list);
                spPaymentMethod.setAdapter(paymentMethodAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        spPaymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                paymentMethod = Integer.parseInt(paymentMethodAdapter.getItem(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                    Log.d("TAG", "ko: " + autoID);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Get token through MoMo app
    private void requestPayment() {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);
//        if (tvTotalPrice.getText().toString() != null && tvTotalPrice.getText().toString().trim().length() != 0)
//            amount = tvTotalPrice.getText().toString().trim();

        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put("merchantname", merchantName); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue.put("amount", 1); //Kiểu integer
        eventValue.put("orderId", autoID); //uniqueue id cho Bill order, giá trị duy nhất cho mỗi đơn hàng
        eventValue.put("orderLabel", "Thanh toan Momo"); //gán nhãn

        //client Optional - bill info
        eventValue.put("merchantnamelabel", "Dịch vụ");//gán nhãn
        eventValue.put("fee", 1); //Kiểu integer
        eventValue.put("description", description); //mô tả đơn hàng - short description

        //client extra data
        eventValue.put("requestId",  merchantCode+"merchant_billId_"+System.currentTimeMillis());
        eventValue.put("partnerCode", merchantCode);
        //Example extra data
        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("site_code", "008");
            objExtraData.put("site_name", "CGV Cresent Mall");
            objExtraData.put("screen_code", 0);
            objExtraData.put("screen_name", "Special");
            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3");
            objExtraData.put("movie_format", "2D");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put("extraData", objExtraData.toString());

        eventValue.put("extra", "");
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);


    }
    //Get token callback from MoMo app an submit to server side
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if(data != null) {
                if(data.getIntExtra("status", -1) == 0) {
                    //TOKEN IS AVAILABLE
                    String token = data.getStringExtra("data"); //Token response
                    String phoneNumber = data.getStringExtra("phonenumber");
                    String env = data.getStringExtra("env");
                    if(env == null){
                        env = "app";
                    }

                    if(token != null && !token.equals("")) {
                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
                        // IF Momo topup success, continue to process your order
                        createOrder();
                    } else {
//                        tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                    }
                } else if(data.getIntExtra("status", -1) == 1) {
                    //TOKEN FAIL
                    String message = data.getStringExtra("message") != null?data.getStringExtra("message"):"Thất bại";
//                    tvMessage.setText("message: " + message);
                } else if(data.getIntExtra("status", -1) == 2) {
                    //TOKEN FAIL
//                    tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                } else {
                    //TOKEN FAIL
//                    tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                }
            } else {
//                tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
            }
        } else {
//            tvMessage.setText("message: " + this.getString(R.string.not_receive_info_err));
        }
    }
}