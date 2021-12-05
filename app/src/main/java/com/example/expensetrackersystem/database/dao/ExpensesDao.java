package com.example.expensetrackersystem.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.expensetrackersystem.database.entities.ExpenseItems;
import com.example.expensetrackersystem.database.entities.ExpenseItemsWithUser;


import java.util.Date;
import java.util.List;

@Dao
public interface ExpensesDao {
    @Query("SELECT * FROM expenseItems WHERE user_id=:userId AND created_date=:createdDate")
    List<ExpenseItems> getExpenses(int userId, String createdDate);

    @Query("SELECT * FROM expenseItems WHERE id = :expenseItemId")
    ExpenseItems getExpenseById(int expenseItemId);

    @Query("SELECT DISTINCT created_date FROM expenseItems")
    List<String> getExpensesDates();

    @Update
    void updateExpense(ExpenseItems expenseItems);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertExpense(ExpenseItems... expenseItems);

    @Delete
    void delete(ExpenseItems expenseItem);
}
