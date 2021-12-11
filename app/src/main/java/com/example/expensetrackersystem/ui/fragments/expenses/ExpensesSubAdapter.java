package com.example.expensetrackersystem.ui.fragments.expenses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackersystem.R;
import com.example.expensetrackersystem.database.entities.ExpenseItems;

import java.util.List;

public class ExpensesSubAdapter extends RecyclerView.Adapter<ExpensesSubAdapter.ViewHolder> {

    Context context;
    List<ExpenseItems> expenseItems;
    ExpensesListener.RecyclerViewClickListener listener;

    public ExpensesSubAdapter(Context context, List<ExpenseItems> expenseItems, ExpensesListener.RecyclerViewClickListener listener) {
        this.context = context;
        this.expenseItems = expenseItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_expenses_sub_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.expenseItemName.setText(expenseItems.get(position).getItemName());
        holder.expenseItemPrice.setText("$" + expenseItems.get(position).getItemPrice());
        holder.itemView.setOnClickListener(v -> listener.onClick());
    }

    @Override
    public int getItemCount() {
        return Math.min(expenseItems.size(), 10);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView expenseItemName, expenseItemPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseItemName = itemView.findViewById(R.id.expenseItemName);
            expenseItemPrice = itemView.findViewById(R.id.expenseItemPrice);
        }
    }
}
