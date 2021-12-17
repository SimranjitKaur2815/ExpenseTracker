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

//Expenses Data Access Object(DAO)
@Dao
public interface ExpensesDao {
    //Get all the entered expenses filtered by user_idd and date
    @Query("SELECT * FROM expenseItems WHERE user_id=:userId AND created_date=:createdDate")
    List<ExpenseItems> getExpenses(int userId, String createdDate);

    //Get all added expense details, total expenses and total expenses price
    @Query("SELECT COUNT(*) as total_expenses, SUM(item_price) as total_price FROM expenseItems WHERE user_id = :user_id")
    ExpenseDetailModel getExpensesDetails(int user_id);

    //Get expenses dates
    @Query("SELECT DISTINCT created_date FROM expenseItems")
    List<String> getExpensesDates();

    //Update the expense
    @Update
    void updateExpense(ExpenseItems expenseItems);

    //Add the expense
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertExpense(ExpenseItems... expenseItems);

    //Delete the expense
    @Delete
    void delete(ExpenseItems expenseItem);

    //Delete one particular expense by date.
    @Query("DELETE FROM expenseItems WHERE created_date=:createdDate")
    void deleteByDate(String createdDate);

}
