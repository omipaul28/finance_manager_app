package com.artofcode.yumbox;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EntryActivity extends AppCompatActivity {

    private ArrayList<String> todayHistory = new ArrayList<>();
    private LinearLayout entryHistory;
    private AppDatabase db;
    private EntryDao entryDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        db = AppDatabase.getInstance(this);
        entryDao = db.entryDao();


        // Views
        EditText inputEarn = findViewById(R.id.inputEarn);
        EditText inputCost = findViewById(R.id.inputCost);
        Button btnAddEarn = findViewById(R.id.btnAddEarn);
        Button btnAddCost = findViewById(R.id.btnAddCost);

        entryHistory = findViewById(R.id.entryHistory);

        // Back Button Action
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Add Earn Action
        btnAddEarn.setOnClickListener(v -> {
            String earnStr = inputEarn.getText().toString();
            if (!earnStr.isEmpty()) {
                addEntry("Earn", Double.parseDouble(earnStr));
                inputEarn.setText("");
            } else {
                Toast.makeText(this, "Please enter a valid amount!", Toast.LENGTH_SHORT).show();
            }
        });

        // Add Cost Action
        btnAddCost.setOnClickListener(v -> {
            String costStr = inputCost.getText().toString();
            if (!costStr.isEmpty()) {
                addEntry("Cost", Double.parseDouble(costStr));
                inputCost.setText("");
            } else {
                Toast.makeText(this, "Please enter a valid amount!", Toast.LENGTH_SHORT).show();
            }
        });


        loadTodayHistory();
        checkForNewMonth();
    }

    private void addEntry(String type, double amount) {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Entry entry = new Entry();
        entry.type = type;
        entry.amount = amount;
        entry.date = today;

        entryDao.insert(entry);

        // Create a new TextView to display the entry
        TextView entryView = new TextView(this);
        entryView.setText(entry.type + "        |" + entry.amount + " BDT");

        // Modify font color, size, and other properties
        entryView.setTextColor(getResources().getColor(R.color.Black)); // Change text color
        entryView.setTextSize(27f); // Set font size in sp
        entryView.setTypeface(null, Typeface.BOLD); // Set font style to bold
        entryView.setPadding(8, 8, 8, 8); // Add padding if needed


        // Add the TextView to the entryHistory LinearLayout
        entryHistory.addView(entryView);

        // Show toast for feedback
        Toast.makeText(this, type + " added!", Toast.LENGTH_SHORT).show();
    }

    private void loadTodayHistory() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        List<Entry> entries = entryDao.getEntriesForDate(today);

        entryHistory.removeAllViews(); // Clear old views

        // Loop through the entries and display them
        for (Entry entry : entries) {
            TextView entryView = new TextView(this);
            entryView.setText(entry.type + "        |" + entry.amount + " BDT");

            // Modify font color, size, and other properties
            entryView.setTextColor(getResources().getColor(R.color.Black)); // Change text color
            entryView.setTextSize(27f); // Set font size in sp
            entryView.setTypeface(null, Typeface.BOLD); // Set font style to bold
            entryView.setPadding(8, 8, 8, 8); // Add padding if needed

            // Add the TextView to the entryHistory LinearLayout
            entryHistory.addView(entryView);
        }
    }


    private void checkForNewMonth() {
        // Get current month in "yyyy-MM" format
        String currentMonth = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date());

        // Get the last month from shared preferences (to track the last reset)
        String lastMonth = getSharedPreferences("YumBoxPrefs", MODE_PRIVATE).getString("lastMonth", "");

        // If the month has changed, reset the data
        if (!lastMonth.equals(currentMonth)) {
            // Delete entries from the previous month
            entryDao.deleteOldEntriesForMonth(lastMonth);

            // Update shared preferences to store the current month as the last reset month
            getSharedPreferences("YumBoxPrefs", MODE_PRIVATE)
                    .edit()
                    .putString("lastMonth", currentMonth)
                    .apply();
        }
    }
}
