package com.example.beautystore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.beautystore.R;
import com.example.beautystore.adapter.RecyclerView_Messages;
import com.example.beautystore.model.Chat;
import com.example.beautystore.model.ChatGroup;
import com.example.beautystore.model.ChatNoti;
import com.example.beautystore.model.Members;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Activity_Messenger extends AppCompatActivity {

    ImageView ivBack, ivSendButton;
    EditText edtMessage;
    FirebaseUser fuser;
    RecyclerView_Messages messagesAdapter;
    RecyclerView messageRecyclerView;
    ArrayList<Chat> chats;
    Intent intent;
    String customerToken;

    ValueEventListener seenListener;
    String products_id;
    String chatId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        setScreenElement();

        intent = getIntent();
        products_id = intent.getStringExtra("products_id");
        chatId = intent.getStringExtra("chatId");
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference().child("Customer");
        customerRef.child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    FirebaseDatabase.getInstance().getReference().child("InChat").child(fuser.getUid()).setValue("1");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Back button:
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference membersReference = FirebaseDatabase.getInstance().getReference().child("Member");
                DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference().child("Customer");
                membersReference.child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            DatabaseReference chatGroupRef = FirebaseDatabase.getInstance().getReference().child("ChatGroup").child(chatId);
                            Map<String, Object> status = new HashMap<>();
                            status.put("status","3");
                            chatGroupRef.updateChildren(status);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                customerRef.child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            FirebaseDatabase.getInstance().getReference().child("InChat").child(fuser.getUid()).setValue("0");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                onBackPressed();
            }
        });

        //Send button:
        ivSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = edtMessage.getText().toString();
                if (!msg.equals("")){
                    sendMessage(fuser.getUid(), msg);
                } else {
                    Toast.makeText(Activity_Messenger.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                edtMessage.setText("");
            }
        });

        readMesagges(chatId);
    }

    private void sendMessage(String sender, String message){

        DatabaseReference membersReference = FirebaseDatabase.getInstance().getReference().child("Member");
        DatabaseReference customerReference = FirebaseDatabase.getInstance().getReference().child("Customer");
        DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference().child("Chats");
        DatabaseReference chatGroupReference = FirebaseDatabase.getInstance().getReference().child("ChatGroup");

        HashMap<String, Object> chatHashMap = new HashMap<>();
        chatHashMap.put("sender", sender);
        chatHashMap.put("message", message);

        HashMap<String, Object> chatGroupHashMap = new HashMap<>();
        chatGroupHashMap.put("latestMessage", message);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        chatGroupHashMap.put("date", date);
        chatGroupHashMap.put("status", "1");


        //Get Chat ID:
        //Check fuser.getUid is Customer or not
        membersReference.child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Members members = snapshot.getValue(Members.class);
                    chatReference.child(chatId).push().setValue(chatHashMap);
                    chatGroupReference.child(chatId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                ChatGroup chatGroup = snapshot.getValue(ChatGroup.class);
                                if (chatGroup.getStatus().equals("2")){
                                    chatGroupHashMap.put("status", "2");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    chatGroupHashMap.put("id", chatId);
                    chatGroupReference.child(chatId).setValue(chatGroupHashMap);


                    DatabaseReference tokenRef = FirebaseDatabase.getInstance().getReference().child("Tokens");
                    tokenRef.child(chatId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            customerToken = (String) snapshot.getValue();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    try {
                        sendNotification(members.getUsername(), fuser.getUid(), customerToken, message);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    ChatNoti chatNoti = new ChatNoti(fuser.getUid(), message, "0", dtf.format(now), chatId);
                    DatabaseReference inChatRef = FirebaseDatabase.getInstance().getReference().child("InChat").child(chatId);
                    inChatRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String inChat = (String) snapshot.getValue();
                            if (inChat.equals("0")){
                                FirebaseDatabase.getInstance().getReference().child("ChatNoti").child(fuser.getUid()).setValue(chatNoti);
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

        customerReference.child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    chatReference.child(fuser.getUid()).push().setValue(chatHashMap);
                    chatGroupReference.child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                ChatGroup chatGroup = snapshot.getValue(ChatGroup.class);
                                if (chatGroup.getStatus().equals("2")){
                                    chatGroupHashMap.put("status", "2");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    chatGroupHashMap.put("id", fuser.getUid());
                    chatGroupReference.child(fuser.getUid()).setValue(chatGroupHashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMesagges(final String chatId){
        chats = new ArrayList<>();
        DatabaseReference membersReference = FirebaseDatabase.getInstance().getReference().child("Member");
        DatabaseReference customerReference = FirebaseDatabase.getInstance().getReference().child("Customer");
        DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference().child("Chats");

        membersReference.child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    chatReference.child(chatId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            chats.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                Chat chat = snapshot.getValue(Chat.class);
                                chats.add(chat);

                                messagesAdapter = new RecyclerView_Messages(Activity_Messenger.this, chats);
                                messageRecyclerView.setAdapter(messagesAdapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        customerReference.child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    chatReference.child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            chats.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                Chat chat = snapshot.getValue(Chat.class);
                                chats.add(chat);

                                messagesAdapter = new RecyclerView_Messages(Activity_Messenger.this, chats);
                                messageRecyclerView.setAdapter(messagesAdapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setScreenElement(){
        messageRecyclerView = findViewById(R.id.rvMessengerMessagesList);
        messageRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        messageRecyclerView.setLayoutManager(linearLayoutManager);

        ivBack = findViewById(R.id.ivMessengerBackButton);
        ivSendButton = findViewById(R.id.ivMessengerSendButton);
        edtMessage = findViewById(R.id.edtMessengerMessage);
    }

    void sendNotification(String currentUserName, String uid, String receiver, String message) throws JSONException {

        JSONObject jsonObject  = new JSONObject();

        JSONObject notificationObj = new JSONObject();
        notificationObj.put("title",currentUserName);
        notificationObj.put("body",message);

        JSONObject dataObj = new JSONObject();
        dataObj.put("userId",uid);

        jsonObject.put("notification",notificationObj);
        jsonObject.put("data",dataObj);
        jsonObject.put("to",receiver);

        callApi(jsonObject);

    }

    void callApi(JSONObject jsonObject){
        MediaType JSON = MediaType.get("application/json");

        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", "Bearer AAAAQbWEeFk:APA91bEYQk7epdZpu9NBFEbXG4eosTgLvlIaOt2GDg_gxpatFvbOG7uz_MrRfwOPPJW8Chs09vF2XwSZtlUJTuKkczV8Oa9mcbnk2droxVPPSzsUb2033Y6y3eldGI7_gPGGOi3Eoupt")
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