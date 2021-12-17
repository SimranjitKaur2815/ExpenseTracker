package com.example.expensetrackersystem.ui.activities.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensetrackersystem.R;
import com.example.expensetrackersystem.database.entities.User;
import com.example.expensetrackersystem.ui.activities.MainActivity;
import com.example.expensetrackersystem.ui.activities.SignUpActivity;
import com.example.expensetrackersystem.utils.db.DbHelper;
import com.example.expensetrackersystem.utils.db.UserDbListener;
import com.google.android.material.textfield.TextInputEditText;

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
        initElements();//initializing the xml elements with their respective IDs.
        initListeners();//setting listeners on the xml elements
        getRegisteredUsers();//getting registered users
    }


    private void initElements() {
        isFromProfile = getIntent().
                getStringExtra("fromProfile") != null && !TextUtils.isEmpty(getIntent().
                getStringExtra("fromProfile"));
        passLay = findViewById(R.id.passLay);
        backBtn = findViewById(R.id.backButton);
        addUserBtn = findViewById(R.id.addUserBtn);
        if (isFromProfile)
        {
            backBtn.setVisibility(View.VISIBLE);
            addUserBtn.setVisibility(View.VISIBLE);
        }
        else
        {
            backBtn.setVisibility(View.GONE);
            addUserBtn.setVisibility(View.GONE);
        }
        usersLay = findViewById(R.id.usersLay);
        password = findViewById(R.id.password);
        passBackBtn = findViewById(R.id.backBtn);
        usersRecyc = findViewById(R.id.usersRecyc);
        submit = findViewById(R.id.submit);
        //setting linear layout to the recycler view
        usersRecyc.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initListeners() {
        backBtn.setOnClickListener(v -> finish());//this listener helps user to get back from the current activity
        addUserBtn.setOnClickListener(v -> {
            //this listener helps to register a new user to the application with the help of intent
            startActivity(new Intent(this, SignUpActivity.class));
            finish();
        });
        passBackBtn.setOnClickListener(v -> {
            passLay.setVisibility(View.GONE);
            password.setText("");
            usersLay.setVisibility(View.VISIBLE);
        });
        submit.setOnClickListener(v -> {
            //getting instance of DBHelper class which will authenticate the current user with the appropriate credentials
            DbHelper.getInstance().loginUser(this, user.getId(), password.getText().toString(), new UserDbListener.onAuthListener() {
                @Override
                public void onSuccess() {
                    //if the authentication gets successful user will redirect to the main activity
                    startActivity(new Intent(LoginActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    //by clicking back, application closes
                    finishAffinity();
                }

                @Override
                public void onFailure(String msg) {
                    //if the authentication fails user will get a toast as a warning
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    //method to get all the registered users
    private void getRegisteredUsers() {
        //getting instance of DBHelper class which will help application to get all the users with the appropriate names
        DbHelper.getInstance().getAllUsers(this, new UserDbListener.onGetUsersListener() {
            @Override
            public void onSuccess(List<User> users) {
                //if above request gets successful, LoginAdapter would be set on the user recyclerView which returns the list of users
                usersRecyc.setAdapter(new LoginAdapter(LoginActivity.this, users, new LoginInterface() {
                    @Override
                    public void onUserClick(User user)
                    {
                        //onUserClick password layout gone to be visible and user will be initialized
                        LoginActivity.this.user = user;
                        passLay.setVisibility(View.VISIBLE);
                        usersLay.setVisibility(View.GONE);
                    }

                    @Override
                    public void onUserDelete(User user) {
                        //getting instance of DBHelper class which will help application to delete the user from it
                        DbHelper.getInstance().deleteUser(LoginActivity.this, user, new UserDbListener.onDeleteAccountListener() {
                            @Override
                            public void onSuccess() {
                                //getting registered users
                                getRegisteredUsers();
                            }

                            @Override
                            public void onFailure(String msg) {
                                //if there is any error getting user from DBHelper,
                                // the toast will display the error message
                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPasswordRequired(UserDbListener.onPasswordRequiredListener listener) {
                                //initializing alertdialog.builder when there is password required on the activity
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                //inflating the layout to the view
                                View diaView = LayoutInflater.from(LoginActivity.this).inflate(R.layout.password_req_dia_lay, null, false);
                                //setting the layout to builder
                                builder.setView(diaView);
                                //creating builder
                                AlertDialog dialog = builder.create();
                                //initializing the elements in the dialog box
                                Button deleteBtn = diaView.findViewById(R.id.deleteBtn);
                                TextView authenticationMessage=diaView.findViewById(R.id.authenticationMessage);
                                TextInputEditText pass = diaView.findViewById(R.id.diaPassET);
                                //setting text to the textview
                                authenticationMessage.setText("As this is not your account, so password will be required.");

                                deleteBtn.setOnClickListener(v -> {
                                    //if password field is empty while submitting then user will get an error
                                    if (TextUtils.isEmpty(pass.getText().toString())) {
                                        pass.setError("Password required");
                                        return;
                                    }
                                    //getting instance of DBHelper class to get the password authentication
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