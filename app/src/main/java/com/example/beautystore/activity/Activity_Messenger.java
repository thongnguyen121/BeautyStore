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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Activity_Messenger extends AppCompatActivity {

    ImageView ivBack, ivSendButton;
    EditText edtMessage;
    FirebaseUser fuser;
    RecyclerView_Messages messagesAdapter;
    RecyclerView messageRecyclerView;
    ArrayList<Chat> chats;
    Intent intent;

    ValueEventListener seenListener;
    String products_id;
    String chatId;
    boolean notify = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        setScreenElement();

        intent = getIntent();
        products_id = intent.getStringExtra("products_id");
        chatId = intent.getStringExtra("chatId");
        fuser = FirebaseAuth.getInstance().getCurrentUser();




        //Back button:
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Send button:
        ivSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
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
        String date = String.valueOf(LocalDateTime.now());
        chatGroupHashMap.put("date", date);


        //Get Chat ID:
        //Check fuser.getUid is Customer or not
        membersReference.child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    chatReference.child(chatId).push().setValue(chatHashMap);
                    chatGroupHashMap.put("id", chatId);
                    chatGroupHashMap.put("status", "Unseen");
                    chatGroupReference.child(chatId).push().setValue(chatGroupHashMap);
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
                    chatGroupHashMap.put("id", fuser.getUid());
                    chatGroupHashMap.put("status", "Unseen");
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
        DatabaseReference chatListReference = FirebaseDatabase.getInstance().getReference().child("ChatList");

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
}