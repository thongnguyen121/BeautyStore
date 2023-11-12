package com.example.beautystore.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.beautystore.R;
import com.example.beautystore.model.CartDetail;
import com.example.beautystore.model.Customer;
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
import java.util.ArrayList;

public class RecyclerViewProgress_order extends RecyclerView.Adapter<RecyclerViewProgress_order.ProgressHolder> {

    private Context context;
    private int resource;
    private ArrayList<OrderStatus> data;
    RecyclerView_Order orderDetailAdapter;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    String uid;

    String order_id = "";
    String status = "";

    public RecyclerViewProgress_order(Context context, int resource, ArrayList<OrderStatus> data) {
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerViewProgress_order.ProgressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardViewItem = (CardView) LayoutInflater.from(context).inflate(viewType, parent, false);
        return new RecyclerViewProgress_order.ProgressHolder(cardViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewProgress_order.ProgressHolder holder, int position) {
        OrderStatus orderStatus = data.get(position);
        uid = FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        order_id = orderStatus.getOrder_id();
        status = orderStatus.getStatus();
        loadInformation_order(holder, order_id);
        getCartItem(databaseReference, order_id, holder);
        setClickConfirm_progress(holder, order_id);

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
    private void getCartItem(DatabaseReference databaseReference, String order_id, RecyclerViewProgress_order.ProgressHolder holder) {
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


    private void loadInformation_order(RecyclerViewProgress_order.ProgressHolder holder, String order_id)
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
    private void setClickConfirm_progress(RecyclerViewProgress_order.ProgressHolder holder, String order_id) {
        if(status.equals("0"))
        {
            holder.linner_button.setVisibility(View.VISIBLE);
        }
        else if (status.equals("1"))
        {
            holder.linner_button.setVisibility(View.GONE);
            holder.btnConfirm_packing.setVisibility(View.VISIBLE);
        }

        holder.btnConfirm_progressm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child("OrderStatus").child(order_id).child("status").setValue("1").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Đơn hàng đã được xử lý", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Cập nhật trạng thái không thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        holder.btnConfirm_packing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("OrderStatus").child(order_id).child("status").setValue("2").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Đơn hàng đã được đóng gói", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Cập nhật trạng thái không thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

    public static class ProgressHolder extends RecyclerView.ViewHolder {

        TextView tvOrdernumber, tvCustomer_name, tvPhonenumber, tvAddress, tvClick, tvClose, tvDatetime, tvTotalmoney;
        RecyclerView rcOrderDetail;
        Button btnCancle, btnConfirm_progressm, btnConfirm_packing;
        LinearLayout linearLayout, linner_button;
        public ProgressHolder(@NonNull View itemView) {
            super(itemView);
            tvOrdernumber = itemView.findViewById(R.id.tvOrdernumber_admin);
            tvCustomer_name = itemView.findViewById(R.id.tvCustomer_name_order_admin);
            tvPhonenumber = itemView.findViewById(R.id.tvPhone_order_admin);
            tvAddress = itemView.findViewById(R.id.tvAddresses_order_admin);
            tvDatetime = itemView.findViewById(R.id.tvDatetime_order_admin);
            tvClick = itemView.findViewById(R.id.tvClick_detail_order_admin);
            tvClose = itemView.findViewById(R.id.tvClose_order_admin);
            tvTotalmoney = itemView.findViewById(R.id.tvTotal_money_admin);
            rcOrderDetail= itemView.findViewById(R.id.rcOrder_list_admin);
            btnCancle = itemView.findViewById(R.id.btnCacel_order_admin);
            btnConfirm_progressm = itemView.findViewById(R.id.btnConfirm_order_admin);
            btnConfirm_packing = itemView.findViewById(R.id.btnConfirm_order_admin_packing);
            linearLayout = itemView.findViewById(R.id.linner_orderDetail_admin);
            linner_button = itemView.findViewById(R.id.linner_button_order_admin);
        }
    }
}