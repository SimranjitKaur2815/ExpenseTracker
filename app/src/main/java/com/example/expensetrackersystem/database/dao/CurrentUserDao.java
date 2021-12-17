package com.example.expensetrackersystem.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.expensetrackersystem.database.entities.CurrentUser;

//Current User's Data Access Object(DAO)
@Dao
public interface CurrentUserDao {

    //Get currently logged in user
    @Query("SELECT user_id FROM currentuser;")
    int getCurrentUser();

    //Add current user
    @Insert
    void addCurrentUser(CurrentUser... currentUser);

    //Delete current user
    @Query("DELETE FROM currentuser")
    void deleteCurrentUser();
}
