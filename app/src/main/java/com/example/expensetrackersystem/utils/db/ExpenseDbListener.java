package com.example.expensetrackersystem.utils.db;

import com.example.expensetrackersystem.database.entities.ExpenseItems;
import com.example.expensetrackersystem.database.entities.ExpenseItemsWithUser;
import com.example.expensetrackersystem.models.ExpensesModel;

import java.util.List;

public interface ExpenseDbListener {
    interface GetExpensesListener {
        void onSuccess(List<ExpenseItems> items);

        void onFailure(String msg);
    }

    interface AddExpenseListener {
        void onSuccess();

        void onFailure(String msg);
    }

    interface GetAllExpenseListener {
        void onSuccess(List<ExpensesModel> expensesModelList);

        void onFailure(String msg);
    }
}
