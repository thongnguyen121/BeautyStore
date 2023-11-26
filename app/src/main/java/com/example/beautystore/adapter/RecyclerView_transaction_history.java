package com.example.beautystore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beautystore.R;
import com.example.beautystore.model.History;
import com.example.beautystore.model.Members;
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

public class RecyclerView_transaction_history extends RecyclerView.Adapter<RecyclerView_transaction_history.HistoryHolder> {

    private Context context;
    private int resource;
    private ArrayList<History> data;
    RecyclerView_Order orderDetailAdapter;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    String uid;
    String order_id = "";
    String status = "";
    String role = "";
    String member_id = "";
    String create_at = "";

    public RecyclerView_transaction_history(Context context, int resource, ArrayList<History> data) {
        this.context = context;
        this.resource = resource;
        this.data = data;
    }
    public void setFilterList(ArrayList<History> filterlist) {
        this.data = filterlist;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView_transaction_history.HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardViewItem = (CardView) LayoutInflater.from(context).inflate(viewType, parent, false);
        return new RecyclerView_transaction_history.HistoryHolder(cardViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView_transaction_history.HistoryHolder holder, int position) {
        History history = data.get(position);

        uid = FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        order_id = history.getOrder_id();
        loadInformation_order(holder, order_id);
        loadInformation_status(holder, history.getStatus());
        setClick_Close(holder);
        getMember_name(holder, uid);
    }


    private void loadInformation_order(RecyclerView_transaction_history.HistoryHolder holder, String order_id)
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
    private void getMember_name(RecyclerView_transaction_history.HistoryHolder holder, String uid)
    {

        if (member_id.equals(""))
        {
            holder.tvShipper.setText("Chưa có");
        }
        else {
            databaseReference.child("Member").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Members members = snapshot.getValue(Members.class);
                        if (members != null) {

                            holder.tvShipper.setText(members.getUsername());

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private void setClick_Close(RecyclerView_transaction_history.HistoryHolder holder){

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

    private void loadInformation_status(RecyclerView_transaction_history.HistoryHolder holder, String condition)
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

    @Override
    public int getItemViewType(int position) {
        return resource;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class HistoryHolder extends RecyclerView.ViewHolder {
        TextView tvOrdernumber, tvCustomer_name, tvPhonenumber, tvAddress, tvClick, tvClose, tvDatetime,
                tvTotalmoney, tvStatus, tvShipper, tvClick_note, tvTile_status, tvDatetime_transaction;
        RecyclerView rcOrderDetail;
        LinearLayout linearLayout, linner_note;
        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            tvOrdernumber = itemView.findViewById(R.id.tvOrdernumberqueue_history_admin);
            tvCustomer_name = itemView.findViewById(R.id.tvCustomer_name_orderHistory_admin);
            tvPhonenumber = itemView.findViewById(R.id.tvPhone_orderHistory_admin);
            tvAddress = itemView.findViewById(R.id.tvAddresses_orderHistory_admin);
            tvClick = itemView.findViewById(R.id.tvClick_detailorder_history_admin);
            tvClose = itemView.findViewById(R.id.tvClose_orderHistory_admin);
            tvDatetime = itemView.findViewById(R.id.tvDatetime_orderHistory_admin);
            tvTotalmoney = itemView.findViewById(R.id.tvTotal_moneyHistory_admin);
            tvStatus = itemView.findViewById(R.id.tvTotal_StatusHistory_admin);
            tvShipper = itemView.findViewById(R.id.tvShipper_history);
            rcOrderDetail = itemView.findViewById(R.id.rcOrder_queue_history_admin);
            linearLayout = itemView.findViewById(R.id.linner_orderDetailhistory_admin);
            tvTile_status = itemView.findViewById(R.id.tvstatusTitle_history);
            tvDatetime_transaction = itemView.findViewById(R.id.tvDatime_history);
        }
    }
}
