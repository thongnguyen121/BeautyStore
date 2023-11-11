package com.example.beautystore.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beautystore.R;
import com.example.beautystore.model.CartDetail;
import com.example.beautystore.model.Order;
import com.example.beautystore.model.OrderStatus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RecyclerViewOrder_queue extends RecyclerView.Adapter<RecyclerViewOrder_queue.QueueHolder> {

    private Context context;
    private int resource;
    private ArrayList<OrderStatus> data;
    RecyclerView_Order orderDetailAdapter;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    String uid;

    String order_id = "";
    String status = "";

    public RecyclerViewOrder_queue(Context context, int resource, ArrayList<OrderStatus> data) {
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerViewOrder_queue.QueueHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardViewItem = (CardView) LayoutInflater.from(context).inflate(viewType, parent, false);
        return new RecyclerViewOrder_queue.QueueHolder(cardViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewOrder_queue.QueueHolder holder, int position) {
        OrderStatus orderStatus = data.get(position);
        uid = FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        order_id = orderStatus.getOrder_id();
        status = orderStatus.getStatus();
        getCartItem(databaseReference, order_id, holder);
        loadInformation_status(holder, status);
        loadInformation_order(holder, order_id);
        setClick_Close(holder);
        if(orderStatus.getMember_id().equals(""))
        {
            holder.tvShipper.setText("Chưa có");
        }

    }
    private void getCartItem(DatabaseReference databaseReference, String order_id, RecyclerViewOrder_queue.QueueHolder holder) {
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
    private void setClick_Close(RecyclerViewOrder_queue.QueueHolder holder){

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

    private void loadInformation_status(RecyclerViewOrder_queue.QueueHolder holder, String condition)
    {
        databaseReference.child("Status").child(condition).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String value = snapshot.getValue(String.class);
                    if (value != null) {
                        holder.tvStatus.setText(value);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void loadInformation_order(RecyclerViewOrder_queue.QueueHolder holder, String order_id)
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
    @Override
    public int getItemViewType(int position) {
        return resource;
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class QueueHolder extends RecyclerView.ViewHolder {
        TextView tvOrdernumber, tvCustomer_name, tvPhonenumber, tvAddress, tvClick, tvClose, tvDatetime, tvTotalmoney, tvStatus, tvShipper;
        RecyclerView rcOrderDetail;
        Button btnConfirm_shipper, btnConfirm_queue;
        LinearLayout linearLayout;
        public QueueHolder(@NonNull View itemView) {
            super(itemView);
            tvOrdernumber = itemView.findViewById(R.id.tvOrdernumberqueue_admin);
            tvCustomer_name = itemView.findViewById(R.id.tvCustomer_name_orderqueue_admin);
            tvPhonenumber = itemView.findViewById(R.id.tvPhone_orderqueue_admin);
            tvAddress = itemView.findViewById(R.id.tvAddresses_orderqueue_admin);
            tvClick = itemView.findViewById(R.id.tvClick_detailorder_queue_admin);
            tvClose = itemView.findViewById(R.id.tvClose_orderqueue_admin);
            tvDatetime = itemView.findViewById(R.id.tvDatetime_orderqueue_admin);
            tvTotalmoney = itemView.findViewById(R.id.tvTotal_moneyqueue_admin);
            tvStatus = itemView.findViewById(R.id.tvTotal_Statusqueue_admin);
            tvShipper = itemView.findViewById(R.id.tvShipper);
            rcOrderDetail = itemView.findViewById(R.id.rcOrder_queue_list_admin);
            btnConfirm_shipper = itemView.findViewById(R.id.btnConfirm_shipper);
            btnConfirm_queue = itemView.findViewById(R.id.btnConfirm_queque_admin);
            linearLayout = itemView.findViewById(R.id.linner_orderDetailqueue_admin);


        }
    }
}
