package com.artofcode.yumbox;

public class DailyHistory {
    public String date;
    public double totalEarn;
    public double totalCost;
    public double profit;

    public DailyHistory(String date, double totalEarn, double totalCost, double profit) {
        this.date = date;
        this.totalEarn = totalEarn;
        this.totalCost = totalCost;
        this.profit = profit;
    }
}
