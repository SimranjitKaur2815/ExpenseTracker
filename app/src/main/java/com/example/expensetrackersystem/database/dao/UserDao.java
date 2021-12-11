package com.example.expensetrackersystem.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.expensetrackersystem.database.entities.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Query("SELECT * FROM user WHERE id = :userId AND password=:password LIMIT 1")
    User getUserByIdAndPassword(int userId, String password);

    @Query("SELECT * FROM user WHERE id = :userId LIMIT 1")
    User getUserById(int userId);

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    User getUserByName(String first, String last);

    @Insert
    void insertUser(User... users);

    @Delete
    void deleteUser(User user);
}
