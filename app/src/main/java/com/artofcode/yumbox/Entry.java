package com.artofcode.yumbox;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "entries")
public class Entry {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String type; // Earn or Cost
    public double amount;
    public String date; // YYYY-MM-DD format
}