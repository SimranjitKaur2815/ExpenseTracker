package com.example.expensetrackersystem.database;

import android.content.Context;

import androidx.room.Room;

//Singleton Databaseclient class
public class DatabaseClient {
    private Context mCtx;
    private static DatabaseClient mInstance;

    //our app database object
    private AppDatabase appDatabase;

    private DatabaseClient(Context mCtx) {
        this.mCtx = mCtx;
        //Building Room database
        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, "ExpenseTracker").build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }
    //To get access to the created data access objects
    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
