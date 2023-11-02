package com.example.beautystore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.beautystore.R;
import com.example.beautystore.model.Order;
import com.example.beautystore.model.OrderDetail;

import java.util.ArrayList;

public class ListView_Order extends ArrayAdapter<OrderDetail> {
    private Context context;
    private ArrayList<OrderDetail> data;
    private int resource;
    public ListView_Order(@NonNull Context context, int resource, ArrayList<OrderDetail> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        TextView tvProductName = convertView.findViewById(R.id.tvProductname);
        TextView tvProductQty = convertView.findViewById(R.id.tvOrderDetailProductQty);
        TextView tvProductPrice = convertView.findViewById(R.id.tvOrderDetailProductPrice);
        return convertView;
    }
}
