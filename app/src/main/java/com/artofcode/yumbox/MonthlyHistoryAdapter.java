package com.artofcode.yumbox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MonthlyHistoryAdapter extends RecyclerView.Adapter<MonthlyHistoryAdapter.ViewHolder> {

    private final List<DailyHistory> historyList;

    public MonthlyHistoryAdapter(List<DailyHistory> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_daily_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DailyHistory history = historyList.get(position);

        holder.dateView.setText(history.date);
        holder.totalEarnView.setText("Earn: " + history.totalEarn + " BDT");
        holder.totalCostView.setText("Cost: " + history.totalCost + " BDT");
        holder.profitView.setText("Profit: " + history.profit + " BDT");
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateView, totalEarnView, totalCostView, profitView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateView = itemView.findViewById(R.id.date);
            totalEarnView = itemView.findViewById(R.id.totalEarn);
            totalCostView = itemView.findViewById(R.id.totalCost);
            profitView = itemView.findViewById(R.id.profit);
        }
    }
}
