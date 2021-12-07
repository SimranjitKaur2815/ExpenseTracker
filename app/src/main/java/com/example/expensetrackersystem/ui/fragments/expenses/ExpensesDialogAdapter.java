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

public class ExpensesDialogAdapter extends RecyclerView.Adapter<ExpensesDialogAdapter.ViewHolder> {
    Context context;
    List<ExpenseItems> expenseItemsList;

    public ExpensesDialogAdapter(Context context, List<ExpenseItems> expenseItemsList) {
        this.context = context;
        this.expenseItemsList = expenseItemsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.expenses_dia_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ExpenseItems items = expenseItemsList.get(position);
        holder.itemNo.setText(String.valueOf(position + 1));
        holder.itemName.setText(items.getItemName());
        holder.itemPrice.setText("$" + items.getItemPrice());

    }

    @Override
    public int getItemCount() {
        return expenseItemsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemNo, itemName, itemPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNo = itemView.findViewById(R.id.itemNo);
            itemName = itemView.findViewById(R.id.expenseItemName);
            itemPrice = itemView.findViewById(R.id.expenseItemPrice);
        }
    }
}
