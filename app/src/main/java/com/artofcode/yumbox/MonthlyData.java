package com.artofcode.yumbox;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "monthly_data")
public class MonthlyData {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "month")
    public String month;

    @ColumnInfo(name = "totalEarn")
    public double totalEarn;

    @ColumnInfo(name = "totalCost")
    public double totalCost;

    @ColumnInfo(name = "totalProfit")
    public double totalProfit;

    // Default constructor (needed by Room)
    public MonthlyData() {
    }

    // Constructor with parameters (optional, for convenience)
    public MonthlyData(String month, double totalEarn, double totalCost, double totalProfit) {
        this.month = month;
        this.totalEarn = totalEarn;
        this.totalCost = totalCost;
        this.totalProfit = totalProfit;
    }

    // Getters and setters (or you can use public fields)
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getTotalEarn() {
        return totalEarn;
    }

    public void setTotalEarn(double totalEarn) {
        this.totalEarn = totalEarn;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }
}

