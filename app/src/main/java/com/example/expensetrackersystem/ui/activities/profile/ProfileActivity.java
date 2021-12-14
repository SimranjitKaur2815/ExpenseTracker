package com.example.expensetrackersystem.ui.activities.profile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensetrackersystem.R;
import com.example.expensetrackersystem.database.entities.User;
import com.example.expensetrackersystem.models.ExpenseDetailModel;
import com.example.expensetrackersystem.models.ProfileExpensesDetailModel;
import com.example.expensetrackersystem.models.ProfileOptionsModel;
import com.example.expensetrackersystem.ui.activities.MainActivity;
import com.example.expensetrackersystem.ui.activities.login.LoginActivity;
import com.example.expensetrackersystem.utils.db.DbHelper;
import com.example.expensetrackersystem.utils.db.ExpenseDbListener;
import com.example.expensetrackersystem.utils.db.UserDbListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements onProfileChangesListener {
    private static final String TAG = "ProfileActivity";
    private ImageView backBtn;
    private List<ProfileOptionsModel> modelList = new ArrayList<>();
    private List<ProfileExpensesDetailModel> expensesDetailModelList = new ArrayList<>();
    private RecyclerView profileOptionsRV, expenseDetailRV;
    private User currentUser;
    private TextView userName, myProfileIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
    }

    private void init() {
        setProfileOptions();
        initElements();
        initListeners();
    }


    private void setProfileOptions() {

        modelList.add(new ProfileOptionsModel(R.drawable.ic_manage_account, "Manage Account", "MA"));
        modelList.add(new ProfileOptionsModel(R.drawable.ic_change_username, "Change Username", "CU"));
        modelList.add(new ProfileOptionsModel(R.drawable.ic_change_password, "Change Password", "CP"));

    }

    private void setExpenseDetails(ExpenseDetailModel model) {
        expensesDetailModelList.add(new ProfileExpensesDetailModel("TE", "Expenses", String.valueOf(model.getTotal_expenses()), R.color.blue, R.drawable.circle_blue_bg));
        expensesDetailModelList.add(new ProfileExpensesDetailModel("TEA", "Amount", String.valueOf(model.getTotal_price()), R.color.red, R.drawable.circle_red_bg));
        expenseDetailRV.setAdapter(new ProfileExpenseDetailAdapter(this, expensesDetailModelList));
    }


    private void initElements() {
        myProfileIcon = findViewById(R.id.myProfileIcon);
        userName = findViewById(R.id.userName);
        backBtn = findViewById(R.id.backBtn);
        profileOptionsRV = findViewById(R.id.profileOptionsRV);
        expenseDetailRV = findViewById(R.id.expenseDetailRV);
        expenseDetailRV.setLayoutManager(new GridLayoutManager(this, 2));
        profileOptionsRV.setLayoutManager(new LinearLayoutManager(this));
        profileOptionsRV.setAdapter(new ProfileOptionsAdapter(this, modelList, this));
        DbHelper.getInstance().getExpenseDetails(this, new ExpenseDbListener.onGetExpenseDetailsListener() {
            @Override
            public void onSuccess(ExpenseDetailModel expenseDetailModel) {
                setExpenseDetails(expenseDetailModel);
            }

            @Override
            public void onFailure(String msg) {
                Log.e(TAG, "onFailure: " + msg);
            }
        });
        getCurrentUser();

    }

    private void initListeners() {
        backBtn.setOnClickListener(v -> finish());
    }

    public void getCurrentUser(){
        DbHelper.getInstance().getCurrentUser(this, new UserDbListener.onGetCurrentUserListener() {
            @Override
            public void onSuccess(User user) {
                currentUser = user;
                userName.setText(user.getFirstName() + " " + user.getLastName());
                myProfileIcon.setText(user.getFirstName().substring(0, 1).toUpperCase());
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(ProfileActivity.this, "User not logged in.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finishAffinity();
            }
        });
    }

    @Override
    public void onUsernameChange() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Username");
        View dialogView = LayoutInflater.from(this).inflate(R.layout.change_username_dia_lay, null, false);
        builder.setView(dialogView);
        TextInputEditText newFirstname = dialogView.findViewById(R.id.newFirstnameET);
        TextInputEditText newLastname = dialogView.findViewById(R.id.newLastnameET);
        TextInputEditText passwordET = dialogView.findViewById(R.id.passwordET);
        MaterialButton changeUsernameButton = dialogView.findViewById(R.id.changeUsernameButton);
        AlertDialog dialog = builder.create();
        changeUsernameButton.setOnClickListener(v -> {
            if (TextUtils.isEmpty(newFirstname.getText().toString())) {
                newFirstname.setError("Field required");
                return;
            }
            if (TextUtils.isEmpty(newLastname.getText().toString())) {
                newLastname.setError("Field required");
                return;
            }
            if (TextUtils.isEmpty(passwordET.getText().toString())) {
                passwordET.setError("Field required");
                return;
            }
            DbHelper.getInstance().updateUsername(this,
                    newFirstname.getText().toString().trim(),
                    newLastname.getText().toString().trim(),
                    passwordET.getText().toString().trim(),
                    new UserDbListener.onAuthListener() {
                        @Override
                        public void onSuccess() {
                            dialog.dismiss();
                            startActivity(new Intent(ProfileActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finishAffinity();

                        }

                        @Override
                        public void onFailure(String msg) {
                            Toast.makeText(ProfileActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });

        });

        dialog.show();

    }

    @Override
    public void onPasswordChange() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.change_password_dia_layout, null, false);
        builder.setView(dialogView);
        TextInputEditText oldPassword = dialogView.findViewById(R.id.oldPassword);
        TextInputEditText newPassword = dialogView.findViewById(R.id.newPassword);
        MaterialButton changePasswordButton=dialogView.findViewById(R.id.changePasswordButton);
        AlertDialog dialog = builder.create();
        changePasswordButton.setOnClickListener(v -> {
            if (TextUtils.isEmpty(oldPassword.getText().toString())) {
                oldPassword.setError("Field required");
                return;
            }
            if (TextUtils.isEmpty(newPassword.getText().toString())) {
                newPassword.setError("Field required");
                return;
            }

            DbHelper.getInstance().updatePassword(this,
                    oldPassword.getText().toString().trim(),
                    newPassword.getText().toString().trim(),
                    new UserDbListener.onAuthListener() {
                        @Override
                        public void onSuccess() {
                            dialog.dismiss();
                            startActivity(new Intent(ProfileActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finishAffinity();

                        }

                        @Override
                        public void onFailure(String msg) {
                            Toast.makeText(ProfileActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });

        });
        dialog.show();
    }
}