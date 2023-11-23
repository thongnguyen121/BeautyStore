package com.example.beautystore;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.beautystore.model.Customer;
import com.example.beautystore.model.Members;
import com.example.beautystore.model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NotificationSender {
    private Context context;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    public NotificationSender(Context context) {
        this.context = context;
    }

    public void sendNotification(String status, String order_id) {
        final String[] fcmToken = new String[1];
        final String[] status1 = new String[1];
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Status").child(status).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    status1[0] = snapshot.getValue(String.class);
                    databaseReference.child("Order").child(order_id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Order order = snapshot.getValue(Order.class);
                                String userID = order.getCustomer_id();
                                databaseReference.child("Customer").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            Customer customer = snapshot.getValue(Customer.class);
                                            fcmToken[0] = customer.getFcmToken();
                                            try {
                                                JSONObject jsonObject = new JSONObject();
                                                JSONObject notificationObj = new JSONObject();
                                                notificationObj.put("title", customer.getUsername());
                                                notificationObj.put("body", order_id + " " + status1[0]);
                                                JSONObject dataObj = new JSONObject();
                                                dataObj.put("userID", snapshot.getKey());

                                                jsonObject.put("notification", notificationObj);
                                                jsonObject.put("data", dataObj);
                                                jsonObject.put("to", customer.getFcmToken());
                                                callAPI(jsonObject);
                                            } catch (Exception e) {

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void sendNotificationShipper(String status, String orderId) {
        final String[] status1 = new String[1];
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Status").child(status).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    status1[0] = snapshot.getValue(String.class);
                    databaseReference.child("Order").child(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                DatabaseReference referenceMember = FirebaseDatabase.getInstance().getReference("Member");
                                referenceMember.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {

                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                Members member = dataSnapshot.getValue(Members.class);
                                                if (member.getRole().equals("1")) {
                                                    referenceMember.child(member.getId()).child("fcmToken").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            String fcmToken = snapshot.getValue(String.class);
                                                            try {
                                                                JSONObject jsonObject = new JSONObject();
                                                                JSONObject notificationObj = new JSONObject();
                                                                notificationObj.put("title", "Thông báo");
                                                                notificationObj.put("body", "Có đơn hàng mới đang chờ vận chuyển ");
                                                                JSONObject dataObj = new JSONObject();
                                                                dataObj.put("userID", member.getId());

                                                                jsonObject.put("notification", notificationObj);
                                                                jsonObject.put("data", dataObj);
                                                                jsonObject.put("to", fcmToken);
                                                                callAPI(jsonObject);
                                                            } catch (Exception e) {

                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });

                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void sendNotificationAdmin(String status, String orderId) {
        final String[] status1 = new String[1];
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Status").child(status).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    status1[0] = snapshot.getValue(String.class);
                    databaseReference.child("Order").child(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                DatabaseReference referenceMember = FirebaseDatabase.getInstance().getReference("Member");
                                referenceMember.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {

                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                Members member = dataSnapshot.getValue(Members.class);
                                                if (member.getRole().equals("0")) {
                                                    referenceMember.child(member.getId()).child("fcmToken").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            String fcmToken = snapshot.getValue(String.class);
                                                            try {
                                                                JSONObject jsonObject = new JSONObject();
                                                                JSONObject notificationObj = new JSONObject();
                                                                notificationObj.put("title", "Đơn hàng "+ orderId);
                                                                notificationObj.put("body", "Trạng thái: "+ status1[0]);
                                                                JSONObject dataObj = new JSONObject();
                                                                dataObj.put("userID", member.getId());

                                                                jsonObject.put("notification", notificationObj);
                                                                jsonObject.put("data", dataObj);
                                                                jsonObject.put("to", fcmToken);
                                                                callAPI(jsonObject);
                                                            } catch (Exception e) {

                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });

                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void callAPI(JSONObject jsonObject) {
        MediaType type = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(), type);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", "key=AAAAQbWEeFk:APA91bEYQk7epdZpu9NBFEbXG4eosTgLvlIaOt2GDg_gxpatFvbOG7uz_MrRfwOPPJW8Chs09vF2XwSZtlUJTuKkczV8Oa9mcbnk2droxVPPSzsUb2033Y6y3eldGI7_gPGGOi3Eoupt")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });
    }
}
