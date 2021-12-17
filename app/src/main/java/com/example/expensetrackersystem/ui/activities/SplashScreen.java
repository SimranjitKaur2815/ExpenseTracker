package com.example.expensetrackersystem.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.expensetrackersystem.R;
import com.example.expensetrackersystem.ui.activities.login.LoginActivity;
import com.example.expensetrackersystem.utils.db.DbHelper;
import com.example.expensetrackersystem.utils.db.UserDbListener;


public class SplashScreen extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_splash_screen);


        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                DbHelper.getInstance().isLoggedIn(SplashScreen.this, new UserDbListener.onAuthListener() {
                    @Override
                    public void onSuccess() {
                        startActivity(new Intent(SplashScreen.this,MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(String msg) {
                        startActivity(new Intent(SplashScreen.this,LoginActivity.class));
                        finish();
                    }
                });
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
