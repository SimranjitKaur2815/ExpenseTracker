package com.example.expensetrackersystem.ui.activities.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.expensetrackersystem.R;
import com.example.expensetrackersystem.models.ExpenseDetailModel;
import com.example.expensetrackersystem.models.ProfileExpensesDetailModel;
import com.example.expensetrackersystem.models.ProfileOptionsModel;
import com.example.expensetrackersystem.utils.db.DbHelper;
import com.example.expensetrackersystem.utils.db.ExpenseDbListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private ImageView backBtn;
    private List<ProfileOptionsModel> modelList = new ArrayList<>();
    private List<ProfileExpensesDetailModel> expensesDetailModelList = new ArrayList<>();
    private RecyclerView profileOptionsRV, expenseDetailRV;

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
        modelList.add(new ProfileOptionsModel(R.drawable.ic_user_profile, "Manage Account", "MA"));
        modelList.add(new ProfileOptionsModel(R.drawable.ic_user_profile, "Change Avatar", "CA"));
        modelList.add(new ProfileOptionsModel(R.drawable.ic_user_profile, "Change Username", "CU"));
        modelList.add(new ProfileOptionsModel(R.drawable.ic_user_profile, "Change Password", "CP"));
    }

    private void setExpenseDetails(ExpenseDetailModel model) {
        expensesDetailModelList.add(new ProfileExpensesDetailModel("TE", "Expenses", String.valueOf(model.getTot_expenses()), R.color.blue, R.drawable.circle_blue_bg));
        expensesDetailModelList.add(new ProfileExpensesDetailModel("TEA", "Amount", String.valueOf(model.getTot_price()), R.color.red, R.drawable.circle_red_bg));
        expenseDetailRV.setAdapter(new ProfileExpenseDetailAdapter(this, expensesDetailModelList));
    }


    private void initElements() {
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
    }

    private void initListeners() {
        backBtn.setOnClickListener(v -> finish());
    }
}