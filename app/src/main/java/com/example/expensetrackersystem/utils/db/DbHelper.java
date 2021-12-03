package com.example.expensetrackersystem.utils;

import android.content.Context;

import com.example.expensetrackersystem.database.DatabaseClient;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DbHelper {
    private Executor executor;
    private static DbHelper instance = null;

    private DbHelper() {
        executor = Executors.newSingleThreadExecutor();
    }

    public interface DbHelperInterface {
        void onSuccess();

        void onFailure(String msg);
    }

    public static synchronized DbHelper getInstance() {
        if (instance == null) {
            return new DbHelper();
        }
        return instance;
    }


    public void UserSignUp(Context context, String firstName, String lastName, int age, DbHelperInterface listener) {
        executor.execute(() -> {
            DatabaseClient.getInstance()
        });
    }
}
