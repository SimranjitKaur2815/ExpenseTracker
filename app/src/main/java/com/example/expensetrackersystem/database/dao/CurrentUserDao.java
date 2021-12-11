package com.example.expensetrackersystem.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.expensetrackersystem.database.entities.CurrentUser;

@Dao
public interface CurrentUserDao {

    @Query("SELECT user_id FROM currentuser;")
    int getCurrentUser();

    @Insert
    void addCurrentUser(CurrentUser... currentUser);

    @Query("DELETE FROM currentuser")
    void deleteCurrentUser();
}
