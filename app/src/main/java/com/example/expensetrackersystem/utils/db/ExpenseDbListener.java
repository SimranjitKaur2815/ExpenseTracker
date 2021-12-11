package com.example.expensetrackersystem.utils.db;

import com.example.expensetrackersystem.database.entities.ExpenseItems;
import com.example.expensetrackersystem.database.entities.ExpenseItemsWithUser;
import com.example.expensetrackersystem.models.ExpenseDetailModel;
import com.example.expensetrackersystem.models.ExpensesModel;

import java.util.List;

public interface ExpenseDbListener {
    interface onGetExpensesListener {
        void onSuccess(List<ExpenseItems> items);

        void onFailure(String msg);
    }

    interface onAddExpenseListener {
        void onSuccess();

        void onFailure(String msg);
    }

    interface onGetAllExpenseListener {
        void onSuccess(List<ExpensesModel> expensesModelList);

        void onFailure(String msg);
    }

    interface onDeleteExpenseListener {
        void onSuccess();

        void onFailure(String msg);
    }

    interface onGetExpenseDetailsListener {
        void onSuccess(ExpenseDetailModel expenseDetailModel);

        void onFailure(String msg);
    }
}
