package com.example.expensetrackersystem.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.expensetrackersystem.database.entities.User;

import java.util.List;

//User's Data Access Object(DAO)
@Dao
public interface UserDao {
    //Get all the registered users
    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    //Get the user detail filtered by id and password
    @Query("SELECT * FROM user WHERE id = :userId AND password=:password LIMIT 1")
    User getUserByIdAndPassword(int userId, String password);


    //Get one user detail filtered by id
    @Query("SELECT * FROM user WHERE id = :userId")
    User getUserById(int userId);


    //Get the user detail filtered by firstname and lastname
    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    User getUserByName(String first, String last);

    //Update the user
    @Update
    void updateUser(User user);


    //Insert the user
    @Insert
    void insertUser(User... users);//(... means)multiple insertion of users, for ex insertUser(user1,user2,user3,....)

    //Delete the user
    @Delete
    void deleteUser(User user);
}
