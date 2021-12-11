package com.example.expensetrackersystem.ui.fragments.home;

import com.example.expensetrackersystem.database.entities.ExpenseItems;
import com.example.expensetrackersystem.database.entities.ExpenseItemsWithUser;

public interface HomeExpensesListener {
    void onEdit(ExpenseItems expenseItems);
    void onDelete(ExpenseItems expenseItems);

}
