package com.example.expensetrackersystem.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.expensetrackersystem.database.entities.ExpenseItems;
import com.example.expensetrackersystem.database.entities.ExpenseItemsWithUser;
import com.example.expensetrackersystem.models.ExpenseDetailModel;


import java.util.Date;
import java.util.List;

@Dao
public interface ExpensesDao {
    @Query("SELECT * FROM expenseItems WHERE user_id=:userId AND created_date=:createdDate")
    List<ExpenseItems> getExpenses(int userId, String createdDate);

    @Query("SELECT COUNT(*) as tot_expenses, SUM(item_price) as tot_price FROM expenseItems WHERE user_id = :user_id")
    ExpenseDetailModel getExpensesDetails(int user_id);

    @Query("SELECT DISTINCT created_date FROM expenseItems")
    List<String> getExpensesDates();

    @Update
    void updateExpense(ExpenseItems expenseItems);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertExpense(ExpenseItems... expenseItems);

    @Delete
    void delete(ExpenseItems expenseItem);

    @Query("DELETE FROM expenseItems WHERE created_date=:createdDate")
    void deleteByDate(String createdDate);

}
