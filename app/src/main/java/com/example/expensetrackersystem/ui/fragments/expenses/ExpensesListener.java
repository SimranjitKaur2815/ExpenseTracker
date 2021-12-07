package com.example.expensetrackersystem.ui.fragments.expenses;

import com.example.expensetrackersystem.database.entities.ExpenseItems;

import java.util.List;

public interface ExpensesListener {
    void onEdit(String createdDate);

    void onDelete(String createdDate);

    void onClick(List<ExpenseItems> expenseItemsList);

    interface AllExpenseListener {
        void onEdit(String createdDate);
    }

    interface RecyclerViewClickListener {
        void onClick();
    }
}
