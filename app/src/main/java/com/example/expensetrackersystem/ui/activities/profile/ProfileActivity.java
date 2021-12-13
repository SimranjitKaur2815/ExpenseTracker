package com.example.expensetrackersystem.ui.activities.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensetrackersystem.R;
import com.example.expensetrackersystem.database.entities.User;
import com.example.expensetrackersystem.models.ExpenseDetailModel;
import com.example.expensetrackersystem.models.ProfileExpensesDetailModel;
import com.example.expensetrackersystem.models.ProfileOptionsModel;
import com.example.expensetrackersystem.ui.activities.login.LoginActivity;
import com.example.expensetrackersystem.utils.db.DbHelper;
import com.example.expensetrackersystem.utils.db.ExpenseDbListener;
import com.example.expensetrackersystem.utils.db.UserDbListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private ImageView backBtn;
    private List<ProfileOptionsModel> modelList = new ArrayList<>();
    private List<ProfileExpensesDetailModel> expensesDetailModelList = new ArrayList<>();
    private RecyclerView profileOptionsRV, expenseDetailRV;
    private User currentUser;
    private TextView userName;

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
        userName = findViewById(R.id.userName);
        backBtn = findViewById(R.id.backBtn);
        profileOptionsRV = findViewById(R.id.profileOptionsRV);
        expenseDetailRV = findViewById(R.id.expenseDetailRV);
        expenseDetailRV.setLayoutManager(new GridLayoutManager(this, 2));
        profileOptionsRV.setLayoutManager(new LinearLayoutManager(this));
        profileOptionsRV.setAdapter(new ProfileOptionsAdapter(this, modelList));
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
        DbHelper.getInstance().getCurrentUser(this, new UserDbListener.onGetCurrentUserListener() {
            @Override
            public void onSuccess(User user) {
                currentUser = user;
                userName.setText(user.getFirstName() + " " + user.getLastName());
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(ProfileActivity.this, "User not logged in.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finishAffinity();
            }
        });
    }

    private void initListeners() {
        backBtn.setOnClickListener(v -> finish());
    }
}