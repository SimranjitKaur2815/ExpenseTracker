package com.example.expensetrackersystem.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.expensetrackersystem.database.dao.CurrentUserDao;
import com.example.expensetrackersystem.database.dao.ExpensesDao;
import com.example.expensetrackersystem.database.dao.UserDao;
import com.example.expensetrackersystem.database.entities.CurrentUser;
import com.example.expensetrackersystem.database.entities.ExpenseItems;
import com.example.expensetrackersystem.database.entities.User;
import com.example.expensetrackersystem.database.entities.ExpenseItemsWithUser;

@Database(entities = {User.class, ExpenseItems.class, ExpenseItemsWithUser.class, CurrentUser.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();//Get user data access object
    public abstract ExpensesDao expensesDao();//Get expenses data access object
    public abstract CurrentUserDao currentUserDao();//Get current user data access object
}
