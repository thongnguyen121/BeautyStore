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
import com.example.beautystore.model.PaymentMethod;

import java.util.List;

public class PaymentMethodAdapter extends ArrayAdapter<PaymentMethod> {

    public PaymentMethodAdapter(@NonNull Context context, int resource, @NonNull List<PaymentMethod> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spinner_payment_method_selected, parent, false);
        TextView tvPaymentMethodSelected = convertView.findViewById(R.id.tvSpinnerPaymentMethodSelected);

        PaymentMethod paymentMethod = this.getItem(position);
        if (paymentMethod != null){
            tvPaymentMethodSelected.setText(paymentMethod.getName());
        }
        return convertView;    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spinner_payment_method, parent, false);
        TextView tvPaymentMethod = convertView.findViewById(R.id.tvSpinnerPaymentMethod);

        PaymentMethod paymentMethod = this.getItem(position);
        if (paymentMethod != null){
            tvPaymentMethod.setText(paymentMethod.getName());
        }
        return convertView;
    }
}
