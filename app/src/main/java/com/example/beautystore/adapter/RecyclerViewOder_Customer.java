package com.example.beautystore.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.beautystore.R;
import com.example.beautystore.model.CartDetail;
import com.example.beautystore.model.Customer;
import com.example.beautystore.model.Order;
import com.example.beautystore.model.OrderStatus;
import com.example.beautystore.model.Products;
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

public class RecyclerViewOder_Customer extends RecyclerView.Adapter<RecyclerViewOder_Customer.OrderHolder> {
    private Context context;
    private int resource;
    private ArrayList<OrderStatus> data;
    RecyclerView_Order orderDetailAdapter;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    String uid;
    String condition = "";
    String order_id = "";
    private ValueEventListener valueEventListener;



    public RecyclerViewOder_Customer(Context context, int resource, ArrayList<OrderStatus> data) {
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    public void setFilterList(ArrayList<OrderStatus> filterlist) {
        this.data = filterlist;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewOder_Customer.OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardViewItem = (CardView) LayoutInflater.from(context).inflate(viewType, parent, false);
        return new RecyclerViewOder_Customer.OrderHolder(cardViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewOder_Customer.OrderHolder holder, int position) {
        OrderStatus orderStatus = data.get(position);
        uid = FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        condition = orderStatus.getStatus();
        order_id = orderStatus.getOrder_id();
        getCartItem(databaseReference, order_id, holder);
        loadInformation_user(holder, condition);
        loadInformation_order(holder, order_id);
        setClick_cancle(holder, order_id);

        if (condition.equals("3") || condition.equals("4") || condition.equals("6") )
        {
            holder.btnCancle.setVisibility(View.GONE);
            if (condition.equals("4") || condition.equals("6"))
            {
                holder.linner_transaction.setVisibility(View.VISIBLE);
            }

            holder.tvDatetime_transaction.setText(orderStatus.getCreate_at());

        }
        else {
            holder.btnCancle.setVisibility(View.VISIBLE);
            holder.linner_transaction.setVisibility(View.GONE);
        }

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

    private void getCartItem(DatabaseReference databaseReference, String order_id, RecyclerViewOder_Customer.OrderHolder holder) {
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
    private void loadInformation_user(RecyclerViewOder_Customer.OrderHolder holder, String condition)
    {
        databaseReference.child("Status").child(condition).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String value = snapshot.getValue(String.class);
                    if (value != null) {
                        holder.tvCondition.setText(value);
                    }
                    Log.d("value", "Value_condition: "+value);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setClick_cancle(RecyclerViewOder_Customer.OrderHolder holder, String order_id){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String savedate = simpleDateFormat.format(calendar.getTime());
        holder.btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
                myDialog.setTitle("Question");
                myDialog.setMessage("Bản có chắc muốn hủy đơn hàng này");
                myDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final HashMap<String, Object> orderStatuslist = new HashMap<>();
                        OrderStatus orderStatus = new OrderStatus(order_id, "5", "","",savedate);
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
                                } else {
                                    Toast.makeText(context, "Hủy đơn hàng không thành công", Toast.LENGTH_SHORT).show();
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
    }
    private void loadInformation_order(RecyclerViewOder_Customer.OrderHolder holder, String order_id)
    {
        databaseReference.child("Order").child(order_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Order order = snapshot.getValue(Order.class);
                    if (order != null) {

                        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
                      holder.tvOrder_number.setText(order.getOrder_id());
                      holder.tvDatetime.setText(order.getCreate_at());
                      holder.tvTotal_money.setText(decimalFormat.format(Integer.valueOf(order.getTotal_amount().trim()))+ " Đ");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    public static class OrderHolder extends RecyclerView.ViewHolder {

        TextView tvOrder_number, tvDatetime, tvCondition, tvClick, tvTotal_money, tvClose, tvDatetime_transaction;
        RecyclerView rcOrderDetail;
        LinearLayout linearLayout, linner_transaction;
        Button btnCancle;
        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            tvOrder_number = itemView.findViewById(R.id.tvOrder_customer);
            tvDatetime = itemView.findViewById(R.id.tvDatetime_order_customer);
            tvCondition = itemView.findViewById(R.id.tvCondition_order_customer);
            tvClick = itemView.findViewById(R.id.tvClick_detail_order_customer);
            tvTotal_money = itemView.findViewById(R.id.tvtotal_order_customer);
            rcOrderDetail = itemView.findViewById(R.id.rcOrder_list_customer);
            linearLayout = itemView.findViewById(R.id.liner_order_customer);
            tvClose = itemView.findViewById(R.id.tvClose_order_customer);
            tvDatetime_transaction = itemView.findViewById(R.id.tvDatime_transaction_customer);
            linner_transaction = itemView.findViewById(R.id.linner_transaction_customer);
            btnCancle = itemView.findViewById(R.id.btn_cancel_order_customer);
        }
    }
}
