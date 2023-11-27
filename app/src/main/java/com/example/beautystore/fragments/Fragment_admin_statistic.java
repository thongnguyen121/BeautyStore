package com.example.beautystore.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.beautystore.R;
import com.example.beautystore.model.OrderStatus;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
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

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Fragment_admin_statistic extends Fragment {

    View view;
    ArrayList lineArray;
    LineChart lineChart_day, lineChart_month;
    BarChart barChart_year;
    RadioButton rdNgay, rdThang, rdNam;
    ImageView ivdateTimePicker;
    TextView tvTitle, tvDatetime, tvTileYear, tvDatetimeYear;
    LinearLayout layout_year, layout_monthYear;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin_statistic, container, false);

        setControl(view);
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Tháng hiện tại
        int currentYear = calendar.get(Calendar.YEAR); // Năm hiện tại

        calculateRevenue(currentMonth, currentYear);
        rdNgay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    lineChart_month.setVisibility(View.GONE);
                    lineChart_day.setVisibility(View.VISIBLE);
                    barChart_year.setVisibility(View.GONE);
                    calculateRevenue(currentMonth, currentYear);

                    layout_monthYear.setVisibility(View.VISIBLE);
                    layout_year.setVisibility(View.GONE);

                }

            }
        });
        rdThang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    lineChart_month.setVisibility(View.VISIBLE);
                    lineChart_day.setVisibility(View.GONE);
                    barChart_year.setVisibility(View.GONE);
                    Calendar calendar = Calendar.getInstance();
                    int currentYear = calendar.get(Calendar.YEAR);

                    calculateRevenueByYearAndMonth(currentYear);
                    layout_monthYear.setVisibility(View.GONE);
                    layout_year.setVisibility(View.VISIBLE);


                }
            }
        });
        rdNam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    barChart_year.setVisibility(View.VISIBLE);
                    lineChart_month.setVisibility(View.GONE);
                    lineChart_day.setVisibility(View.GONE);
//                    setBarChart_year();
                    calculateRevenueByYear();
                    layout_monthYear.setVisibility(View.GONE);
                    layout_year.setVisibility(View.GONE);
                }

            }
        });


        ivdateTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMonthYearPickerDialog();
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
        barChart_year = view.findViewById(R.id.barChart_year);
        ivdateTimePicker = view.findViewById(R.id.ivDatetiemPicker);
        tvDatetime = view.findViewById(R.id.tvDatetime_statistic);
        tvTitle = view.findViewById(R.id.tvTitle_datetime);
        tvTileYear = view.findViewById(R.id.tvTitle_datetime_year);
        tvDatetimeYear = view.findViewById(R.id.tvDatetimeYear_statistic);
        layout_year = view.findViewById(R.id.linnerYear);
        layout_monthYear = view.findViewById(R.id.linner_monthYear);
    }


    // Thống kê doanh thu của ngày trong tháng hiện tại và năm hiện tại

    private void calculateRevenue(int currentMonth, int currentYear) {
        DatabaseReference orderStatusRef = FirebaseDatabase.getInstance().getReference().child("OrderStatus");
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Order");

        Map<String, Integer> dailyRevenue = new LinkedHashMap<>();

        for (int i = 1; i <= 31; i++) {
            dailyRevenue.put(String.valueOf(i), 0);
        }
        tvTitle.setText("Tháng/Năm: ");
        tvDatetime.setText(String.valueOf(currentMonth) + "/" + String.valueOf(currentYear));
        orderStatusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Integer> updatedRevenue = new LinkedHashMap<>(dailyRevenue);

                for (DataSnapshot statusSnapshot : dataSnapshot.getChildren()) {
                    String createAt = statusSnapshot.child("create_at").getValue(String.class);
                    int month = extractMonth(createAt);
                    int year = extractYear(createAt);

                    if (month != -1 && year == currentYear) {
                        if (month == currentMonth) {
                            int day = extractDay(createAt);
                            String dayKey = (day != -1) ? String.valueOf(day) : null;

                            if (dayKey != null && updatedRevenue.containsKey(dayKey)) {
                                String status = statusSnapshot.child("status").getValue(String.class);

                                if (status != null && (status.equals("4") || status.equals("6"))) {
                                    String orderID = statusSnapshot.child("order_id").getValue(String.class);
                                    orderRef.child(orderID).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot orderSnapshot) {
                                            String totalAmount = orderSnapshot.child("total_amount").getValue(String.class);
                                            if (totalAmount != null && !totalAmount.isEmpty()) {
                                                int currentRevenue = updatedRevenue.get(dayKey);
                                                int revenue = currentRevenue + Integer.parseInt(totalAmount);
                                                updatedRevenue.put(dayKey, revenue);
                                                displayRevenueByDay(updatedRevenue);
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
                }
                displayRevenueByDay(updatedRevenue);
                // Kiểm tra và hiển thị chỉ khi có dữ liệu mới
                boolean hasNewData = !updatedRevenue.equals(dailyRevenue);
                if (hasNewData) {
                    displayRevenueByDay(updatedRevenue);
                    // Cập nhật từng cặp key-value trong dailyRevenue với dữ liệu mới từ updatedRevenue
                    for (Map.Entry<String, Integer> entry : updatedRevenue.entrySet()) {
                        dailyRevenue.put(entry.getKey(), entry.getValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    private int extractDay(String createAt) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

        try {
            Date date = inputFormat.parse(createAt);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.DAY_OF_MONTH); // Trả về ngày (1-31)
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void displayRevenueByDay(Map<String, Integer> dailyRevenue) {
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

//        for (String dayKey : dailyRevenue.keySet()) {
//            int revenue = dailyRevenue.getOrDefault(dayKey, 0);
//            entries.add(new Entry(Integer.parseInt(dayKey) - 1, revenue));
//            labels.add(dayKey);
//        }
        for (int i = 1; i <= 31; i++) {
            int revenue = dailyRevenue.getOrDefault(String.valueOf(i), 0);
            entries.add(new Entry(i - 1, revenue));
            labels.add("Ngày " + i);
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
    private int extractYear(String createAt) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

        try {
            Date date = inputFormat.parse(createAt);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
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

    // Thống kê thang theo năm hiện tại
    private Map<String, Integer> calculateRevenueByYearAndMonth(int currentYear) {
        DatabaseReference orderStatusRef = FirebaseDatabase.getInstance().getReference().child("OrderStatus");
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Order");

        Map<String, Integer> monthlyRevenue = new HashMap<>();

        for (int i = 1; i <= 12; i++) {
            monthlyRevenue.put(String.valueOf(i), 0);
        }
        tvTileYear.setText("Năm: ");
        tvDatetimeYear.setText(String.valueOf(currentYear));

        orderStatusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (monthlyRevenue != null)
                {
                    monthlyRevenue.clear();
                }
                for (DataSnapshot statusSnapshot : dataSnapshot.getChildren()) {
                    String createAt = statusSnapshot.child("create_at").getValue(String.class);
                    int monthKey = extractByMonth(createAt);
                    int year = extractByYear(createAt);

                    if (monthKey != -1 && year == currentYear) {

                        Log.d("", "onDataChange: "+ year);
                        String status = statusSnapshot.child("status").getValue(String.class);
                        if (status != null && (status.equals("4") || status.equals("6"))) {
                            String orderID = statusSnapshot.child("order_id").getValue(String.class);
                            orderRef.child(orderID).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot orderSnapshot) {
                                    String totalAmount = orderSnapshot.child("total_amount").getValue(String.class);
                                    int currentRevenue = monthlyRevenue.getOrDefault(String.valueOf(monthKey), 0);
                                    int revenue = currentRevenue + (totalAmount != null ? Integer.parseInt(totalAmount) : 0);
                                    monthlyRevenue.put(String.valueOf(monthKey), revenue);

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

                // hiện tháng có doanh thu không doanh thu
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

    // Thông kê tổng doanh thu các năm

    private void calculateRevenueByYear() {
        DatabaseReference orderStatusRef = FirebaseDatabase.getInstance().getReference().child("OrderStatus");
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Order");

        Map<String, Integer> yearlyRevenue = new HashMap<>();

        orderStatusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (yearlyRevenue != null)
                {
                    yearlyRevenue.clear();
                }
                for (DataSnapshot statusSnapshot : dataSnapshot.getChildren()) {
                    String createAt = statusSnapshot.child("create_at").getValue(String.class);
                    String year = convertCreateAtToYear(createAt);
                    if (year != null) {
                        String status = statusSnapshot.child("status").getValue(String.class);
                        if (status != null && (status.equals("4") || status.equals("6"))) {
                            String orderID = statusSnapshot.child("order_id").getValue(String.class);
                            orderRef.child(orderID).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot orderSnapshot) {
                                    String totalAmount = orderSnapshot.child("total_amount").getValue(String.class);
                                    int currentYearlyRevenue = yearlyRevenue.getOrDefault(year, 0);
                                    int revenue = currentYearlyRevenue + (totalAmount != null ? Integer.parseInt(totalAmount) : 0);
                                    yearlyRevenue.put(year, revenue);
                                    displayRevenueByYear(yearlyRevenue);
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


    private void displayRevenueByYear(Map<String, Integer> yearlyRevenue) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(yearlyRevenue.entrySet());
        Collections.sort(sortedList, (o1, o2) -> {
            try {
                return Integer.parseInt(o1.getKey()) - Integer.parseInt(o2.getKey());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return 0;
            }
        });

        int index = 0;
        for (Map.Entry<String, Integer> entry : sortedList) {
            String year = entry.getKey();
            int revenue = entry.getValue();
            entries.add(new BarEntry(index, revenue));
            labels.add(year);
            index++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu theo năm");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);

        BarData barData = new BarData(dataSet);

        barChart_year.setData(barData);
        barChart_year.getDescription().setEnabled(false);
        barChart_year.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart_year.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart_year.animateY(1000);
        barChart_year.invalidate();
    }
    private String convertCreateAtToYear(String createAt) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        try {
            Date date = inputFormat.parse(createAt);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void showMonthYearPickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int dayOfMonth) {
                        String selectedDate = (selectedMonth + 1) + "/" + selectedYear;
                        calculateRevenueByYearAndMonth(selectedYear);
                        calculateRevenue(selectedMonth + 1, selectedYear);

                    }
                },
                currentYear,
                currentMonth,
                1
        );

        // Ẩn cột ngày
        try {
            Field datePickerDialogField = datePickerDialog.getClass().getDeclaredField("mDatePicker");
            datePickerDialogField.setAccessible(true);
            DatePicker datePicker = (DatePicker) datePickerDialogField.get(datePickerDialog);

            Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
            for (Field datePickerField : datePickerFields) {
                if ("mDaySpinner".equals(datePickerField.getName())) {
                    datePickerField.setAccessible(true);
                    View daySpinner = (View) datePickerField.get(datePicker);
                    daySpinner.setVisibility(View.GONE);
                }
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        datePickerDialog.setTitle("Chọn tháng và năm");
        datePickerDialog.show();
    }
}

