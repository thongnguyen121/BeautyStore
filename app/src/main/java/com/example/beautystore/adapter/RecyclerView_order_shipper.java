package com.example.beautystore.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beautystore.NotificationSender;
import com.example.beautystore.R;
import com.example.beautystore.model.CartDetail;
import com.example.beautystore.model.Members;
import com.example.beautystore.model.Order;
import com.example.beautystore.model.OrderStatus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecyclerView_order_shipper extends RecyclerView.Adapter<RecyclerView_order_shipper.ShipperHolder> {

    private Context context;
    private int resource;
    private ArrayList<OrderStatus> data;
    RecyclerView_Order orderDetailAdapter;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    String uid;
    String order_id = "";
    String status = "";
    String role = "";
    String member_id = "";
    Button btnSave;
    EditText edtNote;
    NotificationSender notificationSender;

    public RecyclerView_order_shipper(Context context, int resource, ArrayList<OrderStatus> data) {
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView_order_shipper.ShipperHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardViewItem = (CardView) LayoutInflater.from(context).inflate(viewType, parent, false);
        return new RecyclerView_order_shipper.ShipperHolder(cardViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView_order_shipper.ShipperHolder holder, int position) {
        OrderStatus orderStatus = data.get(position);
        notificationSender = new NotificationSender(context.getApplicationContext());
        uid = FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        order_id = orderStatus.getOrder_id();
        status = orderStatus.getStatus();
        member_id = orderStatus.getMember_id();

        getCartItem(databaseReference, order_id, holder);
        loadInformation_order(holder, order_id);
        setClick_Close(holder);
        setClick_Status(holder);

    }

    private void getCartItem(DatabaseReference databaseReference, String order_id, RecyclerView_order_shipper.ShipperHolder holder) {
        // Khởi tạo danh sách cartDetails nếu chưa được khởi tạo

        ArrayList<CartDetail>  cartDetails = new ArrayList<>();

        orderDetailAdapter = new RecyclerView_Order(cartDetails, context, R.layout.layout_item_order);
        holder.rcOrderDetail.setLayoutManager(new LinearLayoutManager(context));
        holder.rcOrderDetail.setAdapter(orderDetailAdapter);

        databaseReference.child("Order").child(order_id).child("items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                cartDetails.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
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
    private void setClick_Status(RecyclerView_order_shipper.ShipperHolder holder)
    {

        holder.btnCancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
                myDialog.setTitle("Question");
                myDialog.setMessage("Bạn có chắc muốn hủy đơn hàng này?");
                myDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        final HashMap<String, Object> orderStatuslist = new HashMap<>();
                        OrderStatus orderStatus = new OrderStatus(order_id, "2", "","", "");
                        orderStatuslist.put("order_id", orderStatus.getOrder_id());
                        orderStatuslist.put("status", orderStatus.getStatus());
                        orderStatuslist.put("member_id", orderStatus.getMember_id());
                        orderStatuslist.put("note", orderStatus.getNote());
                        orderStatuslist.put("create_at", orderStatus.getCreate_at());
                        databaseReference.child("OrderStatus").child(order_id).setValue(orderStatuslist).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Hủy đơn hàng thành công", Toast.LENGTH_SHORT).show();
                                    notificationSender.sendNotificationShipper("2", order_id);
                                } else {
                                    Toast.makeText(context, "Hủy đơn hàng thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                myDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                myDialog.create().show();
            }
        });
        holder.btnReturn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
                myDialog.setTitle("Question");
                myDialog.setMessage("Hoàn trả đơn hàng từ khách");
                myDialog.setPositiveButton("Tiếp tục", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openDialog_update_status(Gravity.CENTER);
                    }
                });
                myDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                myDialog.create().show();
            }
        });


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String savedate = simpleDateFormat.format(calendar.getTime());
        holder.btnConfirm_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HashMap<String, Object> orderStatuslist = new HashMap<>();
                OrderStatus orderStatus = new OrderStatus(order_id, "4", member_id,"", savedate);
                orderStatuslist.put("order_id", orderStatus.getOrder_id());
                orderStatuslist.put("status", orderStatus.getStatus());
                orderStatuslist.put("member_id", orderStatus.getMember_id());
                orderStatuslist.put("note", orderStatus.getNote());
                orderStatuslist.put("create_at", orderStatus.getCreate_at());
                databaseReference.child("OrderStatus").child(order_id).setValue(orderStatuslist).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Đơn hàng đã được giao", Toast.LENGTH_SHORT).show();
                            notificationSender.sendNotificationAdmin("4", order_id);
                        } else {
                            Toast.makeText(context, "không thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void openDialog_update_status(int gravity) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_note);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        edtNote = dialog.findViewById(R.id.edt_Note);
        btnSave = dialog.findViewById(R.id.btn_save_dialogNote);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String savedate = simpleDateFormat.format(calendar.getTime());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order_id != null && savedate != null && context != null && edtNote != null) {
                    final HashMap<String, Object> orderStatuslist = new HashMap<>();
                    OrderStatus orderStatus = new OrderStatus(order_id, "5",member_id,edtNote.getText().toString(), savedate);

                    orderStatuslist.put("order_id", orderStatus.getOrder_id());
                    orderStatuslist.put("status", orderStatus.getStatus());
                    orderStatuslist.put("member_id", orderStatus.getMember_id());
                    orderStatuslist.put("note", orderStatus.getNote());
                    orderStatuslist.put("create_at", orderStatus.getCreate_at());
                    databaseReference.child("OrderStatus").child(order_id).setValue(orderStatuslist).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Hoàn trả đơn hàng thành công", Toast.LENGTH_SHORT).show();
                                notificationSender.sendNotification("5",order_id);
                                notificationSender.sendNotificationAdmin("5",order_id);
                            } else {
                                Toast.makeText(context, "Hoàn trả đơn hàng không thành công", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // Handle the case where one of the variables is null
                    Toast.makeText(context, "Đã xảy ra lỗi. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();

    }
    private void loadInformation_order(RecyclerView_order_shipper.ShipperHolder holder, String order_id)
    {
        databaseReference.child("Order").child(order_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Order order = snapshot.getValue(Order.class);
                    if (order != null) {
                        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
                        holder.tvCustomer_name.setText(order.getName());
                        holder.tvDatetime.setText(order.getCreate_at());
                        holder.tvTotalmoney.setText(decimalFormat.format(Integer.valueOf(order.getTotal_amount().trim()))+ " Đ");
                        holder.tvPhonenumber.setText(order.getPhoneNumber());
                        holder.tvAddress.setText(order.getAddress());
                        holder.tvOrdernumber.setText(order.getOrder_id());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setClick_Close(RecyclerView_order_shipper.ShipperHolder holder){

        holder.tvClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.linearLayout.setVisibility(View.VISIBLE);
                holder.tvClose.setVisibility(View.VISIBLE);
                holder.tvClick.setVisibility(View.GONE);
            }
        });
        holder.tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.linearLayout.setVisibility(View.GONE);
                holder.tvClose.setVisibility(View.GONE);
                holder.tvClick.setVisibility(View.VISIBLE);
            }
        });
    }
    @Override
    public int getItemViewType(int position) {
        return resource;
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ShipperHolder extends RecyclerView.ViewHolder {
        TextView tvOrdernumber, tvCustomer_name, tvPhonenumber, tvAddress, tvClick, tvClose, tvDatetime, tvTotalmoney, tvStatus, tvShipper;
        RecyclerView rcOrderDetail;
        Button btnReturn_order, btnConfirm_order, btnCancel_order;
        LinearLayout linearLayout;
        public ShipperHolder(@NonNull View itemView) {
            super(itemView);
            tvOrdernumber = itemView.findViewById(R.id.tvNumber_order_shipper);
            tvCustomer_name = itemView.findViewById(R.id.tvCustomer_name_orderShipper);
            tvPhonenumber = itemView.findViewById(R.id.tvPhone_order_shipper);
            tvAddress = itemView.findViewById(R.id.tvAddress_order_shipper);
            tvClick = itemView.findViewById(R.id.tvClick_detail_order_shipper);
            tvClose = itemView.findViewById(R.id.tvClose_order_shipper);
            tvDatetime = itemView.findViewById(R.id.tvDatetime_order_shipper);
            tvTotalmoney = itemView.findViewById(R.id.tvTotal_money_shipper);
//            tvShipper = itemView.findViewById(R.id.tvShipper);
            rcOrderDetail = itemView.findViewById(R.id.rcOrder_list_shipper);
            btnConfirm_order = itemView.findViewById(R.id.btnConfirm_order_shipper);
            btnCancel_order = itemView.findViewById(R.id.btnCancel_order_shipper);
            btnReturn_order = itemView.findViewById(R.id.btnReturns_order_shipper);
            linearLayout = itemView.findViewById(R.id.linner_orderDetail_shipper);
        }
    }
}
