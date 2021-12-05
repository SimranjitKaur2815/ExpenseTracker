package com.example.expensetrackersystem.utils.db;

import com.example.expensetrackersystem.database.entities.User;

import java.util.List;


public interface UserDbListener {
     interface AuthListener{
        void onSuccess();
        void onFailure(String msg);
    }
    interface GetUsersListener{
        void onSuccess(List<User> users);
        void onFailure(String msg);
    }
    interface GetCurrentUserListener{
        void onSuccess(User user);
        void onFailure(String msg);
    }
}
