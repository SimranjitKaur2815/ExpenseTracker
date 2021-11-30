package com.example.expensetrackersystem.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.expensetrackersystem.database.entities.ExpenseItems;
import com.example.expensetrackersystem.database.entities.ExpenseItemsWithUser;


import java.util.Date;
import java.util.List;

@Dao
public interface ExpensesDao {
    @Query("SELECT * FROM expenseItems e JOIN user u on e.user_id=u.id WHERE e.user_id=:userId AND e.created_date=:createdDate")
    List<ExpenseItemsWithUser> getExpenses(int userId,String createdDate);

    @Query("SELECT * FROM expenseItems WHERE id = :expenseItemId")
    ExpenseItems getExpenseById(int expenseItemId);


    @Query("SELECT DISTINCT created_date FROM expenseItems")
    List<String> getExpensesDates();

    @Insert
    void insertExpense(ExpenseItems... expenseItems);

    @Delete
    void delete(ExpenseItems expenseItem);
}
