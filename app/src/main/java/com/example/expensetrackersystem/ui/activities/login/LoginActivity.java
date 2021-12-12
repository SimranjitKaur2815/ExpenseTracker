package com.example.expensetrackersystem.ui.activities.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
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

    LinearLayout passLay, usersLay;
    EditText password;
    ImageView passBackBtn, backBtn;
    RecyclerView usersRecyc;
    Button submit, addUserBtn;
    User user;
    boolean isFromProfile = false;


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
        isFromProfile = getIntent().getStringExtra("fromProfile") != null && !TextUtils.isEmpty(getIntent().getStringExtra("fromProfile"));
        passLay = findViewById(R.id.passLay);
        backBtn = findViewById(R.id.backButton);
        addUserBtn = findViewById(R.id.addUserBtn);
        if (isFromProfile) {
            backBtn.setVisibility(View.VISIBLE);
            addUserBtn.setVisibility(View.VISIBLE);
        } else {
            backBtn.setVisibility(View.GONE);
            addUserBtn.setVisibility(View.GONE);
        }
        usersLay = findViewById(R.id.usersLay);
        password = findViewById(R.id.password);
        passBackBtn = findViewById(R.id.backBtn);
        usersRecyc = findViewById(R.id.usersRecyc);
        submit = findViewById(R.id.submit);
        usersRecyc.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initListeners() {
        backBtn.setOnClickListener(v -> finish());
        addUserBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));
            finish();
        });
        passBackBtn.setOnClickListener(v -> {
            passLay.setVisibility(View.GONE);
            password.setText("");
            usersLay.setVisibility(View.VISIBLE);
        });
        submit.setOnClickListener(v -> {
            DbHelper.getInstance().loginUser(this, user.getId(), password.getText().toString(), new UserDbListener.onAuthListener() {
                @Override
                public void onSuccess() {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finishAffinity();
                }

                @Override
                public void onFailure(String msg) {
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void getRegisteredUsers() {
        DbHelper.getInstance().getAllUsers(this, new UserDbListener.onGetUsersListener() {
            @Override
            public void onSuccess(List<User> users) {

                usersRecyc.setAdapter(new LoginAdapter(LoginActivity.this, users, new LoginInterface() {
                    @Override
                    public void onUserClick(User user) {
                        LoginActivity.this.user = user;
                        passLay.setVisibility(View.VISIBLE);
                        usersLay.setVisibility(View.GONE);
                    }

                    @Override
                    public void onUserDelete(User user) {

                        DbHelper.getInstance().deleteUser(LoginActivity.this, user, new UserDbListener.onDeleteAccountListener() {
                            @Override
                            public void onSuccess() {
                                getRegisteredUsers();
                            }

                            @Override
                            public void onFailure(String msg) {
                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPasswordRequired(UserDbListener.onPasswordRequiredListener listener) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

                                View diaView = LayoutInflater.from(LoginActivity.this).inflate(R.layout.password_req_dia_lay, null, false);
                                builder.setView(diaView);
                                AlertDialog dialog = builder.create();
                                Button deleteBtn = diaView.findViewById(R.id.deleteBtn);
                                EditText pass = diaView.findViewById(R.id.diaPassET);
                                dialog.setTitle("Authentication");
                                dialog.setMessage("As this is not your account, so password will be required.");

                                deleteBtn.setOnClickListener(v -> {
                                    if (TextUtils.isEmpty(pass.getText().toString())) {
                                        pass.setError("Password required");
                                        return;
                                    }
                                    DbHelper.getInstance().authenticatePassword(LoginActivity.this, user, pass.getText().toString(), new UserDbListener.onAuthListener() {
                                        @Override
                                        public void onSuccess() {
                                            listener.onSuccess();
                                            dialog.dismiss();
                                        }

                                        @Override
                                        public void onFailure(String msg) {
                                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                });
                                dialog.show();
                            }
                        });
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