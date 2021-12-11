package com.example.expensetrackersystem.utils.db;

import com.example.expensetrackersystem.database.entities.User;

import java.util.List;


public interface UserDbListener {
     interface onAuthListener {
        void onSuccess();
        void onFailure(String msg);
    }
    interface onGetUsersListener {
        void onSuccess(List<User> users);
        void onFailure(String msg);
    }
    interface onGetCurrentUserListener {
        void onSuccess(User user);
        void onFailure(String msg);
    }
    interface onDeleteAccountListener{
         void onSuccess();
         void onFailure(String msg);
    }
}
