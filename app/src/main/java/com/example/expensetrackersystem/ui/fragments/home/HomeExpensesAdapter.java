package com.example.expensetrackersystem.ui.fragments.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackersystem.R;
import com.example.expensetrackersystem.database.entities.ExpenseItems;
import com.example.expensetrackersystem.database.entities.ExpenseItemsWithUser;

import java.util.List;

public class HomeExpensesAdapter extends RecyclerView.Adapter<HomeExpensesAdapter.ViewHolder> {

    Context context;
    List<ExpenseItems> expenseItemsList;
    HomeExpensesListener listener;

    public HomeExpensesAdapter(Context context, List<ExpenseItems> expenseItemsList, HomeExpensesListener listener) {
        this.context = context;
        this.expenseItemsList = expenseItemsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.expenses_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExpenseItems expenseItem = expenseItemsList.get(position);
        holder.itemName.setText(expenseItem.getItemName());
        holder.itemPrice.setText("$"+String.valueOf(expenseItem.getItemPrice()));
        holder.editExp.setOnClickListener(v ->listener.onEdit(expenseItem));
        holder.delExp.setOnClickListener(v ->listener.onDelete(expenseItem));

    }

    @Override
    public int getItemCount() {
        return expenseItemsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice;
        ImageView editExp, delExp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            editExp = itemView.findViewById(R.id.editExp);
            delExp = itemView.findViewById(R.id.delExp);
        }
    }
}
