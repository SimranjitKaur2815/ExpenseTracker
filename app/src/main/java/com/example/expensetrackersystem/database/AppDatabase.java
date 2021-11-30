package com.example.expensetrackersystem.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.expensetrackersystem.database.dao.ExpensesDao;
import com.example.expensetrackersystem.database.dao.UserDao;
import com.example.expensetrackersystem.database.entities.ExpenseItems;
import com.example.expensetrackersystem.database.entities.User;
import com.example.expensetrackersystem.database.entities.ExpenseItemsWithUser;

@Database(entities = {User.class, ExpenseItems.class, ExpenseItemsWithUser.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ExpensesDao expensesDao();
}
