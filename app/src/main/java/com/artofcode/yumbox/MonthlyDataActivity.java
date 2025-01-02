package com.artofcode.yumbox;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MonthlyDataActivity extends AppCompatActivity {

    private AppDatabase db;
    private EntryDao entryDao;

    private TextView totalEarnView;
    private TextView totalCostView;
    private TextView totalProfitView;
    private RecyclerView historyRecyclerView;
    private LineChart profitChart;  // Added for chart

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_data);

        db = AppDatabase.getInstance(this);
        entryDao = db.entryDao();

        // Views
        totalEarnView = findViewById(R.id.totalEarn);
        totalCostView = findViewById(R.id.totalCost);
        totalProfitView = findViewById(R.id.totalProfit);
        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        profitChart = findViewById(R.id.profitChart);  // Find the chart view

        // Set up RecyclerView
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Load data
        loadMonthlyData();
    }

    private void loadMonthlyData() {
        String currentMonth = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date());

        // Fetch totals
        double totalCost = entryDao.getMonthlyTotalCost(currentMonth);
        double totalEarn = entryDao.getMonthlyTotalEarn(currentMonth);
        double totalProfit = totalEarn - totalCost;

        // Display totals
        totalCostView.setText("Total Cost: " + totalCost + " BDT");
        totalEarnView.setText("Total Earn: " + totalEarn + " BDT");
        totalProfitView.setText("Total Profit: " + totalProfit + " BDT");

        // Fetch daily history
        List<DailyHistory> history = entryDao.getMonthlyHistory(currentMonth);

        // Set RecyclerView adapter
        MonthlyHistoryAdapter adapter = new MonthlyHistoryAdapter(history);
        historyRecyclerView.setAdapter(adapter);

        // Set up the "Day vs Profit" graph
        setupProfitChart(history);
    }

    private void setupProfitChart(List<DailyHistory> history) {
        // Create lists for the graph
        ArrayList<Entry> profitEntries = new ArrayList<>();

        // Add daily data to the graph
        for (int i = 0; i < history.size(); i++) {
            DailyHistory dailyHistory = history.get(i);
            float profit = (float) (dailyHistory.totalEarn - dailyHistory.totalCost);  // Profit = Earn - Cost
            profitEntries.add(new Entry(i, profit));  // i is the day, profit is the y-value
        }

        // Create a LineDataSet and customize the chart
        LineDataSet lineDataSet = new LineDataSet(profitEntries, "Profit");
        lineDataSet.setColor(getResources().getColor(R.color.Black));  // Customize line color
        lineDataSet.setValueTextColor(getResources().getColor(R.color.colorPrimaryDark));  // Customize value text color
        lineDataSet.setLineWidth(2f);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setCircleColor(getResources().getColor(R.color.Black));  // Circle color

        // Create a LineData object
        LineData lineData = new LineData(lineDataSet);

        // Set the data to the chart
        profitChart.setData(lineData);

        // Customize chart appearance
        profitChart.setExtraOffsets(10, 10, 10, 10); // Left, Top, Right, Bottom offsets

        profitChart.invalidate();  // Refresh the chart
        profitChart.getDescription().setEnabled(false);  // Disable description
        profitChart.setTouchEnabled(true);  // Enable touch interaction
        profitChart.setDragEnabled(false);  // Enable dragging
        profitChart.setScaleEnabled(false);  // Enable zooming
    }

}
