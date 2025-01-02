package com.artofcode.yumbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class YearlyDataActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MonthlyDataAdapter adapter;
    private AppDatabase db;
    private EntryDao entryDao;
    private List<MonthlyData> monthlyDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yearly_data);

        db = AppDatabase.getInstance(this);
        entryDao = db.entryDao();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        monthlyDataList = new ArrayList<>();
        adapter = new MonthlyDataAdapter(monthlyDataList);
        recyclerView.setAdapter(adapter);



        // Get data for the year (e.g., for "2024")
        String currentYear = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        loadYearlyData(currentYear);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadYearlyData(String year) {

        // Retrieve monthly data for the year
        monthlyDataList.clear();
        monthlyDataList.addAll(entryDao.getYearlyData(year));  // Get yearly data
        adapter.notifyDataSetChanged(); // Refresh adapter
    }


}
