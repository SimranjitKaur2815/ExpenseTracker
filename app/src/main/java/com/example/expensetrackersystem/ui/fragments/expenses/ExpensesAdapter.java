package com.example.expensetrackersystem.ui.fragments.expenses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackersystem.R;
import com.example.expensetrackersystem.database.entities.ExpenseItems;
import com.example.expensetrackersystem.models.ExpensesModel;

import java.util.ArrayList;
import java.util.List;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ExpensesViewHolder> {
    public Context context;

    private List<ExpensesModel> expensesModels;
    private ExpensesListener listener;

    public ExpensesAdapter(Context context, List<ExpensesModel> expensesModels, ExpensesListener listener) {
        this.context = context;
        this.expensesModels = expensesModels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExpensesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expenses_adapter, parent, false);
        return new ExpensesViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpensesViewHolder holder, int position) {

        ExpensesModel model = expensesModels.get(position);
        holder.expenseDate.setText(model.getSubmittedDate());
        holder.calculated_amount.setText("$" + model.getTotalPrice());
        holder.expensesRV.setAdapter(new ExpensesSubAdapter(context, model.getExpenseItems(), () -> listener.onClick(model.getExpenseItems())));
        if (model.getExpenseItems().size() > 10) {
            holder.moreText.setVisibility(View.VISIBLE);
        } else {

            holder.moreText.setVisibility(View.GONE);
        }

        holder.delete.setOnClickListener(v -> listener.onDelete(model.getSubmittedDate()));
        holder.edit.setOnClickListener(v -> listener.onEdit(model.getSubmittedDate()));
        holder.itemView.setOnClickListener(v -> listener.onClick(model.getExpenseItems()));
        holder.expensesRV.setOnClickListener(v -> listener.onClick(model.getExpenseItems()));
    }

    @Override
    public int getItemCount() {
        return expensesModels.size();
    }

    public static class ExpensesViewHolder extends RecyclerView.ViewHolder {
        ImageView edit, delete;
        TextView calculated_amount, expenseDate, moreText;
        RecyclerView expensesRV;


        public ExpensesViewHolder(@NonNull View itemView, Context context) {

            super(itemView);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
            calculated_amount = itemView.findViewById(R.id.calculated_amount);
            expensesRV = itemView.findViewById(R.id.expensesRv);
            expensesRV.setLayoutManager(new LinearLayoutManager(context));
            expenseDate = itemView.findViewById(R.id.expenseDate);
            moreText = itemView.findViewById(R.id.moreText);
        }
    }
}
