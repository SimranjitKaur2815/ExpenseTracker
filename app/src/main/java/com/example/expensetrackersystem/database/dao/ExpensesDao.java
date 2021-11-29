package com.example.expensetrackersystem.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.expensetrackersystem.database.entities.ExpenseItems;
import com.example.expensetrackersystem.database.entities.ExpenseItemsWithUser;


import java.util.List;

@Dao
public interface ExpensesDao {
    @Query("SELECT * FROM expenseItems e JOIN user u on e.user_id=u.user_id WHERE e.user_id=:user_id")
    List<ExpenseItemsWithUser> getExpenses(int user_id);

    @Query("SELECT * FROM expenseItems WHERE item_id = :expenseItemId")
    ExpenseItems getExpenseById(int expenseItemId);

    @Insert
    void insertExpense(ExpenseItems... expenseItems);

    @Delete
    void delete(ExpenseItems expenseItem);
}
