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

import com.example.beautystore.R;
import com.example.beautystore.model.CartDetail;
import com.example.beautystore.model.History;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

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
    String autoId_history = "";
    String shipper_id = "";

    public RecyclerView_order_shipper(Context context, int resource, ArrayList<OrderStatus> data) {
        this.context = context;
        this.resource = resource;
        this.data = data;
    }
    public void setFilterList(ArrayList<OrderStatus> filterlist) {
        this.data = filterlist;
        notifyDataSetChanged();
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
        uid = FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        order_id = orderStatus.getOrder_id();
        status = orderStatus.getStatus();
        member_id = orderStatus.getMember_id();

        getCartItem(databaseReference, order_id, holder);
        loadInformation_order(holder, order_id);
        setClick_Close(holder);
        setClick_Status(holder, order_id, uid);

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
    private void setClick_Status(RecyclerView_order_shipper.ShipperHolder holder, String order_id, String uid)
    {
        getIDHistory();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String savedate = simpleDateFormat.format(calendar.getTime());

        databaseReference.child("Member").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Members members = snapshot.getValue(Members.class);
                    if (members != null) {
                        shipper_id = members.getId();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                        History history = new History(autoId_history,"7",order_id, member_id, savedate);
                        orderStatuslist.put("order_id", orderStatus.getOrder_id());
                        orderStatuslist.put("status", orderStatus.getStatus());
                        orderStatuslist.put("member_id", orderStatus.getMember_id());
                        orderStatuslist.put("note", orderStatus.getNote());
                        orderStatuslist.put("create_at", orderStatus.getCreate_at());
                        databaseReference.child("OrderStatus").child(order_id).setValue(orderStatuslist).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                databaseReference.child("History").child(autoId_history).setValue(history).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context, "Hủy đơn hàng thành công", Toast.LENGTH_SHORT).show();
                                    }
                                });
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

        holder.btnConfirm_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HashMap<String, Object> orderStatuslist = new HashMap<>();
                OrderStatus orderStatus = new OrderStatus(order_id, "4", member_id,"", savedate);
                History history = new History(autoId_history,"6",order_id, member_id, savedate);
                orderStatuslist.put("order_id", orderStatus.getOrder_id());
                orderStatuslist.put("status", orderStatus.getStatus());
                orderStatuslist.put("member_id", orderStatus.getMember_id());
                orderStatuslist.put("note", orderStatus.getNote());
                orderStatuslist.put("create_at", orderStatus.getCreate_at());
                databaseReference.child("OrderStatus").child(order_id).setValue(orderStatuslist).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        databaseReference.child("History").child(autoId_history).setValue(history).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "Hoàn thành giao hàng", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

    public void getIDHistory() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("History");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> dsUser = new ArrayList<>();
                if (snapshot.exists()){

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        dsUser.add(dataSnapshot.getKey());
                    }
                    String[] temp = dsUser.get(dsUser.size() - 1).split("HT");
                    String id = "";
                    int idNumber = Integer.parseInt(temp[1])+1;
                    if (idNumber < 10) {
                        id = "HT0" + idNumber;
                    } else {
                        id = "HT" + idNumber;
                    }
                    autoId_history = id;

                }else {

                    autoId_history = "HT01";

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                                dialog.dismiss();
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
