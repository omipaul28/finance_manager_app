package com.artofcode.yumbox;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Entry.class, MonthlyData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EntryDao entryDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "yumbox_db")
                    .allowMainThreadQueries() // For simplicity; avoid in production apps
                    .build();
        }
        return INSTANCE;
    }
}