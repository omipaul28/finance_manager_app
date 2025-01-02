package com.artofcode.yumbox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MonthlyDataAdapter extends RecyclerView.Adapter<MonthlyDataAdapter.MonthlyDataViewHolder> {

    private List<MonthlyData> monthlyDataList = new ArrayList<>(); // Ensures list is never null

    public MonthlyDataAdapter(List<MonthlyData> monthlyDataList) {
        if (monthlyDataList != null) {
            this.monthlyDataList = monthlyDataList;
        }
    }

    // Create and return the ViewHolder
    @Override
    public MonthlyDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the list
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_monthly_data, parent, false);
        return new MonthlyDataViewHolder(view);
    }

    // Bind the data to the ViewHolder
    @Override
    public void onBindViewHolder(MonthlyDataViewHolder holder, int position) {
        MonthlyData data = monthlyDataList.get(position);

        // Set text values for each item in the RecyclerView
        holder.monthText.setText(data.month);
        holder.totalCostText.setText(String.format("Total Cost: %.2f BDT", data.totalCost));
        holder.totalEarnText.setText(String.format("Total Earn: %.2f BDT", data.totalEarn));
        holder.totalProfitText.setText(String.format("Total Profit: %.2f BDT", data.totalProfit));
    }

    // Get the number of items in the data set
    @Override
    public int getItemCount() {
        return monthlyDataList.size();
    }

    // ViewHolder class to hold individual item views
    public static class MonthlyDataViewHolder extends RecyclerView.ViewHolder {

        TextView monthText, totalCostText, totalEarnText, totalProfitText;

        public MonthlyDataViewHolder(View itemView) {
            super(itemView);

            // Initialize the TextViews by finding them from the layout
            monthText = itemView.findViewById(R.id.monthText);
            totalCostText = itemView.findViewById(R.id.totalCostText);
            totalEarnText = itemView.findViewById(R.id.totalEarnText);
            totalProfitText = itemView.findViewById(R.id.totalProfitText);
        }
    }

    // Method to update the data in the adapter
    public void updateData(List<MonthlyData> newMonthlyDataList) {
        if (newMonthlyDataList != null) {
            monthlyDataList.clear();
            monthlyDataList.addAll(newMonthlyDataList);
            notifyDataSetChanged(); // Notify the adapter that the data has changed
        }
    }
}
