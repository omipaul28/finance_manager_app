package com.artofcode.yumbox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;
    private EntryDao entryDao;

    // SharedPreferences constants
    private static final String PREFS_NAME = "YumboxPrefs";
    private static final String KEY_DAILY_GOAL = "dailyGoal";

    // Fetch the saved daily goal value
    private double getDailyGoal() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getFloat(KEY_DAILY_GOAL, 0);  // Default value of 0 if not set
    }

    // Save the daily goal value
    private void setDailyGoal(double goal) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(KEY_DAILY_GOAL, (float) goal);
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);
        entryDao = db.entryDao();

        // Find Views by ID
        TextView progressText = findViewById(R.id.progressText);
        TextView todayDate = findViewById(R.id.todayDate);
        TextView earnView = findViewById(R.id.total_earn);
        TextView costView = findViewById(R.id.total_cost);
        TextView profitView = findViewById(R.id.total_profit);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        NotificationScheduler.scheduleDailyNotification(this);

        // Make ProgressBar determinate
        progressBar.setIndeterminate(false);

        // Set the current date
        String currentDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
        todayDate.setText("Date: " + currentDate);

        // Set up the buttons for navigation
        Button btnEntryToday = findViewById(R.id.btnEntryToday);
        Button btnMonthlyData = findViewById(R.id.btnMonthlyData);
        Button btnYearlyData = findViewById(R.id.btnYearlyData);
        Button btnSetProfitGoal = findViewById(R.id.btnSetProfitGoal);

        btnEntryToday.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, EntryActivity.class)));
        btnMonthlyData.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MonthlyDataActivity.class)));
        btnYearlyData.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, YearlyDataActivity.class)));
        btnSetProfitGoal.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SetProfitGoalActivity.class)));

        // About Developer Section
        findViewById(R.id.aboutDeveloper).setOnClickListener(v -> {
            // Inflate the custom layout
            LayoutInflater inflater = LayoutInflater.from(this);
            View dialogView = inflater.inflate(R.layout.dialog_about_developer, null);

            // Create the dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();

            // Set up the close button
            dialogView.findViewById(R.id.closeButton).setOnClickListener(btn -> dialog.dismiss());

            // Show the dialog
            dialog.show();
        });


        // Update daily totals
        updateDailyTotals(earnView, costView, profitView, progressBar, progressText);
    }

    // Update daily earnings, cost, profit, and goal progress
    private void updateDailyTotals(TextView earnView, TextView costView, TextView profitView, ProgressBar progressBar, TextView progressText) {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Get today's totals for earnings and costs
        double totalEarn = entryDao.getTotalEarn(today);
        double totalCost = entryDao.getTotalCost(today);
        double profit = totalEarn - totalCost;

        // Update the TextViews with today's data
        earnView.setText("Earn:\n" + totalEarn + " BDT");
        costView.setText("Cost:\n" + totalCost + " BDT");
        profitView.setText("Profit: " + profit + " BDT");

        // Fetch the user's daily goal
        double targetGoal = getDailyGoal();

        // If a target is set, update progress
        if (targetGoal > 0) {
            progressText.setText("Goal: " + targetGoal + " BDT");

            // Calculate progress percentage
            double progress = (profit / targetGoal) * 100;

            // Ensure the progress is within the 0-100 range
            int progressPercentage = (int) Math.min(progress, 100);  // Max progress is capped at 100%

            // Update the progress bar with the calculated value
            progressBar.setProgress(progressPercentage);

            // Notify user if the goal is achieved
            if (progressPercentage >= 100) {
                progressText.setText("Goal Achieved!");
            }
        } else {
            // If no goal is set, display a message or leave it empty
            progressText.setText("Target Progress");
            progressBar.setProgress(0); // Reset progress bar if no goal is set
        }
    }


    // Save monthly totals
    private void saveMonthlyData() {
        String currentMonth = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date());

        // Calculate monthly totals
        double totalEarn = entryDao.getMonthlyTotalEarn(currentMonth);
        double totalCost = entryDao.getMonthlyTotalCost(currentMonth);
        double totalProfit = totalEarn - totalCost;

        // Check if data for the current month exists
        MonthlyData existingData = entryDao.getMonthlyDataForMonth(currentMonth);

        if (existingData != null) {
            // Update existing monthly data
            existingData.totalCost = totalCost;
            existingData.totalEarn = totalEarn;
            existingData.totalProfit = totalProfit;
            entryDao.updateMonthlyData(existingData);
        } else {
            // Insert new monthly data if it doesn't exist
            MonthlyData newMonthlyData = new MonthlyData(currentMonth, totalEarn, totalCost, totalProfit);
            entryDao.insertMonthlyData(newMonthlyData);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update daily totals, including the progress bar and text
        updateDailyTotals(findViewById(R.id.total_earn), findViewById(R.id.total_cost), findViewById(R.id.total_profit), findViewById(R.id.progressBar), findViewById(R.id.progressText));
        saveMonthlyData();
    }


}
