package com.example.expensetrackersystem.ui.activities.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.expensetrackersystem.R;
import com.example.expensetrackersystem.database.entities.User;
import com.example.expensetrackersystem.ui.activities.MainActivity;
import com.example.expensetrackersystem.ui.activities.SignUpActivity;
import com.example.expensetrackersystem.utils.db.DbHelper;
import com.example.expensetrackersystem.utils.db.UserDbListener;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    LinearLayout passLay;
    EditText password;
    ImageView backBtn;
    RecyclerView usersRecyc;
    Button submit;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        initElements();
        initListeners();
        getRegisteredUsers();
    }


    private void initElements() {
        passLay = findViewById(R.id.passLay);
        password = findViewById(R.id.password);
        backBtn = findViewById(R.id.backBtn);
        usersRecyc = findViewById(R.id.usersRecyc);
        submit = findViewById(R.id.submit);
        usersRecyc.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initListeners() {
        backBtn.setOnClickListener(v -> {
            passLay.setVisibility(View.INVISIBLE);
            password.setText("");
            usersRecyc.setVisibility(View.VISIBLE);
        });
        submit.setOnClickListener(v -> {
            DbHelper.getInstance().loginUser(this, user.getId(), password.getText().toString(), new UserDbListener.AuthListener() {
                @Override
                public void onSuccess() {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }

                @Override
                public void onFailure(String msg) {
                   Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void getRegisteredUsers() {
        DbHelper.getInstance().getAllUsers(this, new UserDbListener.GetUsersListener() {
            @Override
            public void onSuccess(List<User> users) {

                    usersRecyc.setAdapter(new LoginAdapter(LoginActivity.this, users, new LoginInterface() {
                        @Override
                        public void onUserClick(User user) {
                            LoginActivity.this.user = user;
                            passLay.setVisibility(View.VISIBLE);
                            usersRecyc.setVisibility(View.INVISIBLE);
                        }
                    }));

            }

            @Override
            public void onFailure(String msg) {
                 Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                finish();
            }
        });

    }
}