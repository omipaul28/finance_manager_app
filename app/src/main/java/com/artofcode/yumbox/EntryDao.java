package com.artofcode.yumbox;

import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EntryDao {

    @Insert
    void insert(Entry entry);

    @Query("SELECT * FROM entries WHERE date = :date")
    List<Entry> getEntriesForDate(String date);

    @Query("DELETE FROM entries WHERE date NOT LIKE :month || '%'")
    void deleteOldEntriesForMonth(String month); // Delete entries older than the current month

    // Calculate daily totals
    @Query("SELECT SUM(amount) FROM entries WHERE type = 'Earn' AND date = :date")
    double getTotalEarn(String date);

    @Query("SELECT SUM(amount) FROM entries WHERE type = 'Cost' AND date = :date")
    double getTotalCost(String date);

    // Calculate monthly totals
    @Query("SELECT SUM(amount) FROM entries WHERE type = 'Cost' AND date LIKE :month || '%'")
    double getMonthlyTotalCost(String month);

    @Query("SELECT SUM(amount) FROM entries WHERE type = 'Earn' AND date LIKE :month || '%'")
    double getMonthlyTotalEarn(String month);

    // Calculate the profit (Earn - Cost) for a specific month
    @Query("SELECT (SELECT SUM(amount) FROM entries WHERE type = 'Earn' AND date LIKE :month || '%') - " +
            "(SELECT SUM(amount) FROM entries WHERE type = 'Cost' AND date LIKE :month || '%') AS profit")
    double getMonthlyProfit(String month);

    // Get monthly history
    @Query("SELECT date, SUM(CASE WHEN type = 'Earn' THEN amount ELSE 0 END) AS totalEarn, " +
            "SUM(CASE WHEN type = 'Cost' THEN amount ELSE 0 END) AS totalCost, " +
            "SUM(CASE WHEN type = 'Earn' THEN amount ELSE 0 END) - SUM(CASE WHEN type = 'Cost' THEN amount ELSE 0 END) AS profit " +
            "FROM entries WHERE date LIKE :month || '%' GROUP BY date ORDER BY date ASC")
    List<DailyHistory> getMonthlyHistory(String month);

    // Insert monthly data
    @Insert
    void insertMonthlyData(MonthlyData monthlyData);

    // Get all monthly data for the year
    // Get all monthly data for the year
//    @Query("SELECT * FROM monthly_data WHERE month LIKE :year ORDER BY month ASC")
//    List<MonthlyData> getYearlyData(String year);
    @Query("SELECT * FROM monthly_data WHERE month LIKE :year || '%' ORDER BY month ASC")
    List<MonthlyData> getYearlyData(String year);


    // Delete all monthly data
    @Query("DELETE FROM monthly_data")
    void deleteAllMonthlyData();

    @Query("SELECT * FROM monthly_data WHERE month = :month LIMIT 1")
    MonthlyData getMonthlyDataForMonth(String month);

    // Update existing monthly data
    @Update
    void updateMonthlyData(MonthlyData monthlyData);


}


