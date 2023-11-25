package com.example.beautystore.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.example.beautystore.R;
import com.example.beautystore.model.OrderStatus;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Fragment_admin_statistic extends Fragment {

    View view;
    ArrayList lineArray;
    LineChart lineChart_day, lineChart_month, lineChart_year;
    RadioButton rdNgay, rdThang, rdNam;
    int feefilter1,feefilter2,feefilter3,feefilter4,feefilter5,feefilter6,feefilter7,feefilter8,feefilter9,
            feefilter10,feefilter11,feefilter12,feefilter13,feefilter14,feefilter15,feefilter16,feefilter17,
            feefilter18,feefilter19,feefilter20,feefilter21,feefilter22,feefilter23,feefilter24, feefilter25,
            feefilter26,feefilter27,feefilter28,feefilter29,feefilter30 = 0 ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin_statistic, container, false);

        setControl(view);
        calculateRevenue();
        rdNgay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    lineChart_month.setVisibility(View.GONE);
                    lineChart_day.setVisibility(View.VISIBLE);
//                   displaySampleChart_day();
                   calculateRevenue();

                }

            }
        });
        rdThang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    lineChart_month.setVisibility(View.VISIBLE);
                    lineChart_day.setVisibility(View.GONE);
//                 displaySampleChart_month();
                    Calendar calendar = Calendar.getInstance();
                    int currentYear = calendar.get(Calendar.YEAR);

                    calculateRevenueByYearAndMonth(currentYear);
                }
            }
        });
        rdNam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){

                }

            }
        });
        return view;
    }

    private void setControl(View view) {
        lineChart_day = view.findViewById(R.id.lineChart_date);
        lineChart_month = view.findViewById(R.id.lineChart_month);
        rdNgay = view.findViewById(R.id.rd_doanhthu_ngay);
        rdThang = view.findViewById(R.id.rd_doanhthu_thang);
        rdNam = view.findViewById(R.id.rd_doanhthu_nam);
    }

    private void getData(){
//        lineArray = new ArrayList();
//        lineArray.add(new BarEntry(1, bundle.getInt("fee1")));
//        lineArray.add(new BarEntry(2, bundle.getInt("fee2")));
//        lineArray.add(new BarEntry(3, bundle.getInt("fee3")));
//        lineArray.add(new BarEntry(4, bundle.getInt("fee4")));
//        lineArray.add(new BarEntry(8, bundle.getInt("fee5")));
//        lineArray.add(new BarEntry(5, bundle.getInt("fee6")));
//        lineArray.add(new BarEntry(6, bundle.getInt("fee7")));
//        lineArray.add(new BarEntry(7, bundle.getInt("fee8")));
//        lineArray.add(new BarEntry(9, bundle.getInt("fee9")));
//        lineArray.add(new BarEntry(10, bundle.getInt("fee10")));
//        lineArray.add(new BarEntry(11, bundle.getInt("fee11")));
//        lineArray.add(new BarEntry(12, bundle.getInt("fee12")));
    }
    private void displaySampleChart_day() {
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        // Giả sử doanh thu của 30 ngày trong tháng
        int[] sampleData = {100, 150, 200, 180, 250, 300, 280, 320, 400, 380, 350, 420, 390, 370, 410, 430, 450, 470, 490, 520, 540, 550, 530, 580, 560, 590, 620, 610, 630, 640};

        for (int i = 0; i < sampleData.length; i++) {
            // Tạo dữ liệu cho biểu đồ
            entries.add(new Entry(i, sampleData[i])); // i là vị trí của ngày, sampleData[i] là doanh thu
            labels.add("Ngày " + (i + 1)); // Nhãn của trục x
        }

        // Tạo dataSet từ dữ liệu đã tạo
        LineDataSet dataSet = new LineDataSet(entries, "Doanh thu theo ngày");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);

        // Tạo LineData từ dataSet
        LineData lineData = new LineData(dataSet);

        // Set các thuộc tính cho biểu đồ
        lineChart_day.setData(lineData);
        lineChart_day.getDescription().setEnabled(false);
        lineChart_day.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels)); // Đặt nhãn ngày
        lineChart_day.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); // Đặt nhãn ngày ở dưới
        lineChart_day.animateY(1000);
        lineChart_day.invalidate();
    }
    private void displaySampleChart_month() {
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        int[] sampleData = {1500, 1800, 2200, 1900, 2500, 3000, 2800, 3200, 4000, 3800, 3500, 4200}; // Doanh thu của từng tháng

        for (int i = 0; i < sampleData.length; i++) {
            // Tạo dữ liệu cho biểu đồ
            entries.add(new Entry(i, sampleData[i])); // i là vị trí của tháng, sampleData[i] là doanh thu của tháng đó
            labels.add("Tháng " + (i + 1)); // Nhãn của trục x
        }


        LineDataSet dataSet = new LineDataSet(entries, "Doanh thu theo tháng");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);


        LineData lineData = new LineData(dataSet);

        lineChart_month.setData(lineData);
        lineChart_month.getDescription().setEnabled(false);
        lineChart_month.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels)); // Đặt nhãn tháng
        lineChart_month.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); // Đặt nhãn tháng ở dưới
        lineChart_month.animateY(1000);
        lineChart_month.invalidate();
    }

    private void calculateRevenue() {
        DatabaseReference orderStatusRef = FirebaseDatabase.getInstance().getReference().child("OrderStatus");
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Order");

        Map<String, Integer> dailyRevenue = new HashMap<>();

        for (int i = 1; i <= 30; i++) {
            dailyRevenue.put(String.valueOf(i), 0); // Khởi tạo doanh thu là 0 cho mỗi ngày từ 1 đến 30
        }

        orderStatusRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean checkMonth = false;
                for (DataSnapshot statusSnapshot : dataSnapshot.getChildren()) {
                    String createAt = statusSnapshot.child("create_at").getValue(String.class);
                    String dayKey = convertCreateAtToDay(createAt);

                    if (dayKey != null && dailyRevenue.containsKey(dayKey)) {
                        String status = statusSnapshot.child("status").getValue(String.class);
                        if (status != null && (status.equals("4") || status.equals("6"))) {
                            String orderID = statusSnapshot.child("order_id").getValue(String.class);
                            orderRef.child(orderID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot orderSnapshot) {
                                    String totalAmount = orderSnapshot.child("total_amount").getValue(String.class);
                                    int currentRevenue = dailyRevenue.get(dayKey);
                                    int revenue = currentRevenue + (totalAmount != null ? Integer.parseInt(totalAmount) : 0);
                                    dailyRevenue.put(dayKey, revenue);

                                    // Kiểm tra nếu dayKey thuộc tháng hiện tại, hiển thị doanh thu
                                    if (extractMonth(createAt) == Calendar.getInstance().get(Calendar.MONTH) + 1) {
                                        displayRevenueByDay(dailyRevenue);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Xử lý lỗi nếu cần
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });
    }


    private void displayRevenueByDay(Map<String, Integer> dailyRevenue) {
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (String dayKey : dailyRevenue.keySet()) {
            int revenue = dailyRevenue.getOrDefault(dayKey, 0);
            entries.add(new Entry(Integer.parseInt(dayKey) - 1, revenue));
            labels.add(dayKey);
        }

        LineDataSet dataSet = new LineDataSet(entries, "Doanh thu theo ngày");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);

        lineChart_day.setData(lineData);
        lineChart_day.getDescription().setEnabled(false);
        lineChart_day.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        lineChart_day.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart_day.animateY(1000);
        lineChart_day.invalidate();
    }

    private int extractMonth(String createAt) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

        try {
            Date date = inputFormat.parse(createAt);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.MONTH) + 1;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
    private String convertCreateAtToDay(String createAt) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd", Locale.getDefault());

        try {
            Date date = inputFormat.parse(createAt);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    private Map<String, Integer> calculateRevenueByYearAndMonth(int currentYear) {
        DatabaseReference orderStatusRef = FirebaseDatabase.getInstance().getReference().child("OrderStatus");
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Order");

        Map<String, Integer> monthlyRevenue = new HashMap<>();

        // Khởi tạo doanh thu là 0 cho mỗi tháng từ 1 đến 12
        for (int i = 1; i <= 12; i++) {
            monthlyRevenue.put(String.valueOf(i), 0);
        }

        orderStatusRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot statusSnapshot : dataSnapshot.getChildren()) {
                    String createAt = statusSnapshot.child("create_at").getValue(String.class);
                    int monthKey = extractByMonth(createAt);
                    int year = extractByYear(createAt);

                    if (monthKey != -1 && year == currentYear) {
                        String status = statusSnapshot.child("status").getValue(String.class);
                        if (status != null && (status.equals("4") || status.equals("6"))) {
                            String orderID = statusSnapshot.child("order_id").getValue(String.class);
                            orderRef.child(orderID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot orderSnapshot) {
                                    String totalAmount = orderSnapshot.child("total_amount").getValue(String.class);
                                    int currentRevenue = monthlyRevenue.getOrDefault(String.valueOf(monthKey), 0);
                                    int revenue = currentRevenue + (totalAmount != null ? Integer.parseInt(totalAmount) : 0);
                                    monthlyRevenue.put(String.valueOf(monthKey), revenue);

                                    // Hiển thị doanh thu nếu có thay đổi trong dữ liệu
                                    displayRevenueByMonth(monthlyRevenue);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Xử lý lỗi nếu cần
                                }
                            });
                        }
                    }
                }

                // Hiển thị doanh thu với tất cả các tháng, bao gồm cả những tháng không có doanh thu
                displayRevenueByMonth(monthlyRevenue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });

        return monthlyRevenue;
    }

    private int extractByYear(String createAt) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

        try {
            Date date = inputFormat.parse(createAt);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.YEAR); // Trả về năm
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
    private int extractByMonth(String createAt) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

        try {
            Date date = inputFormat.parse(createAt);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.MONTH) + 1; // Trả về tháng (1-12)
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
    private void displayRevenueByMonth(Map<String, Integer> monthlyRevenue) {
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            int revenue = monthlyRevenue.getOrDefault(String.valueOf(i), 0);
            entries.add(new Entry(i - 1, revenue));
            labels.add("Tháng " + i);
        }

        LineDataSet dataSet = new LineDataSet(entries, "Doanh thu theo tháng");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);

        lineChart_month.setData(lineData);
        lineChart_month.getDescription().setEnabled(false);
        lineChart_month.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        lineChart_month.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart_month.animateY(1000);
        lineChart_month.invalidate();
    }
}

