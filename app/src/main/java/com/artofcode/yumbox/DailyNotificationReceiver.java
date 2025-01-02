package com.artofcode.yumbox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.Random;

public class DailyNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String[] messages = {
                "Keep hustling, your goals are closer than you think!",
                "Remember to track your progress today!",
                "Small steps lead to big changes!",
                "Success is the sum of small efforts, repeated daily.",
                "Set your goals for the day and crush them!",
                "Your potential is limitless. Keep pushing forward!",
                "Every day is a new beginning. Make it count!",
                "Stay consistent. Your hard work will pay off!",
                "Take a moment to reflect on your achievements.",
                "You’ve got this! Keep striving for excellence."
        };

        // Pick a random message
        String randomMessage = messages[new Random().nextInt(messages.length)];

        // Show the notification
        NotificationHelper.showNotification(context, "Yumbox Reminder", randomMessage);
    }
}
