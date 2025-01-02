package com.artofcode.yumbox;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SetProfitGoalActivity extends AppCompatActivity {

    private EditText goalEditText;
    private static final String PREFS_NAME = "YumboxPrefs";
    private static final String KEY_DAILY_GOAL = "dailyGoal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profit_goal);

        goalEditText = findViewById(R.id.goalEditText);

        findViewById(R.id.btnSaveGoal).setOnClickListener(v -> {
            String goalString = goalEditText.getText().toString();
            if (!goalString.isEmpty()) {
                double goal = Double.parseDouble(goalString);
                // Save the goal using SharedPreferences
                saveDailyGoal(goal);
                Toast.makeText(SetProfitGoalActivity.this, "Goal Set Successfully!", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity after saving the goal
            } else {
                Toast.makeText(SetProfitGoalActivity.this, "Please enter a valid goal!", Toast.LENGTH_SHORT).show();
            }
        });
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    private void saveDailyGoal(double goal) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(KEY_DAILY_GOAL, (float) goal);
        editor.apply();  // Save the goal
    }
}
