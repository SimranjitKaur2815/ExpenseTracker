package com.example.expensetrackersystem.ui.fragments.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackersystem.R;
import com.example.expensetrackersystem.database.entities.ExpenseItems;
import com.example.expensetrackersystem.database.entities.User;
import com.example.expensetrackersystem.ui.activities.login.LoginActivity;
import com.example.expensetrackersystem.utils.DateHelper;
import com.example.expensetrackersystem.utils.db.DbHelper;
import com.example.expensetrackersystem.utils.db.ExpenseDbListener;
import com.example.expensetrackersystem.utils.db.UserDbListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    View view, bottomSheetView;
    Context context;
    FloatingActionButton addExpenseBtn;
    String itemName = "", expenseDateString;
    Double itemPrice = 0.0d;
    TextInputEditText itemNameET, itemPriceET;
    MaterialButton addExpenseMatBtn, updateExpenseMatBtn;
    BottomSheetDialog bottomSheetDialog;
    Executor executor;
    TextView expenseDayTV, expenseDateTV;
    RecyclerView expenseRecyc;
    User user;
    ExpenseItems updatedExpenseItem;
    Date expenseDate;


    public HomeFragment() {
        expenseDate = Calendar.getInstance().getTime();
    }

    public HomeFragment(Date expenseDate) {
        this.expenseDate = expenseDate;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment_layout, container, false);
        init();
        return view;
    }

    private void init() {
        initElements();
        initListeners();
    }


    private void initElements() {
        addExpenseBtn = view.findViewById(R.id.addExpense);
        expenseDayTV = view.findViewById(R.id.expenseDay);
        expenseDateTV = view.findViewById(R.id.expenseDate);
        expenseRecyc = view.findViewById(R.id.expensesRecyc);
        expenseRecyc.setLayoutManager(new LinearLayoutManager(context));
        bottomSheetView = LayoutInflater.from(context).inflate(R.layout.add_expense_bottom_sheet, null, false);
        itemNameET = bottomSheetView.findViewById(R.id.itemName);
        itemPriceET = bottomSheetView.findViewById(R.id.itemPrice);
        addExpenseMatBtn = bottomSheetView.findViewById(R.id.addExpenseBtn);
        updateExpenseMatBtn = bottomSheetView.findViewById(R.id.updateExpenseMatBtn);
        bottomSheetDialog = new BottomSheetDialog(context, R.style.CustomShapeAppearanceBottomSheetDialog);
        bottomSheetDialog.setContentView(bottomSheetView);
        executor = Executors.newSingleThreadExecutor();
        expenseDateString = DateHelper.getDateInCommonFormat(expenseDate.getTime());
        expenseDateTV.setText(expenseDateString);
        bottomSheetDialog.setContentView(bottomSheetView);
        DbHelper.getInstance().getCurrentUser(context, new UserDbListener.onGetCurrentUserListener() {
            @Override
            public void onSuccess(User currentUser) {
                user = currentUser;
                getExpenses();
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(context, "User not logged in.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(context, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                ((Activity) context).finishAffinity();
            }
        });
    }

    private void initListeners() {
        addExpenseBtn.setOnClickListener(v -> {
            bottomSheetDialog.show();
        });
        addExpenseMatBtn.setOnClickListener(v -> {
            itemName = itemNameET.getText().toString().trim();
            itemPrice = Double.valueOf(itemPriceET.getText().toString().trim());
            if (TextUtils.isEmpty(itemName)) {
                itemNameET.setError("Enter item name");
                return;
            }
            if (itemPrice == null || itemPrice <= 0) {
                itemPriceET.setError("Enter valid price");
                return;
            }
            AddExpense();
        });
        updateExpenseMatBtn.setOnClickListener(v -> {
            itemName = itemNameET.getText().toString().trim();
            itemPrice = Double.valueOf(itemPriceET.getText().toString().trim());
            if (TextUtils.isEmpty(itemName)) {
                itemNameET.setError("Enter item name");
                return;
            }
            if (itemPrice == null || itemPrice <= 0) {
                itemPriceET.setError("Enter valid price");
                return;
            }
            UpdateExpense();
        });
        expenseDateTV.setOnClickListener(v -> changeDate());
    }


    private void getExpenses() {
        addExpenseMatBtn.setVisibility(View.VISIBLE);
        updateExpenseMatBtn.setVisibility(View.GONE);
        DbHelper.getInstance().getExpenses(context, user.getId(), expenseDateString, new ExpenseDbListener.onGetExpensesListener() {
            @Override
            public void onSuccess(List<ExpenseItems> items) {
                expenseRecyc.setAdapter(new HomeExpensesAdapter(context, items, new HomeExpensesListener() {
                    @Override
                    public void onEdit(ExpenseItems expenseItems) {
                        updatedExpenseItem = expenseItems;
                        addExpenseMatBtn.setVisibility(View.GONE);
                        updateExpenseMatBtn.setVisibility(View.VISIBLE);
                        itemNameET.setText(expenseItems.getItemName());
                        itemPriceET.setText(String.valueOf(expenseItems.getItemPrice()));
                        bottomSheetDialog.show();
                    }

                    @Override
                    public void onDelete(ExpenseItems expenseItems) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Warning");
                        builder.setMessage("Do you really wan to delete this expense ?");
                        builder.setPositiveButton("Yes", (d, i) -> {
                            DbHelper.getInstance().deleteExpense(context, expenseItems, new ExpenseDbListener.onDeleteExpenseListener() {
                                @Override
                                public void onSuccess() {
                                    getExpenses();
                                }

                                @Override
                                public void onFailure(String msg) {
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                }
                            });
                        });
                        builder.setNegativeButton("No", null);
                        builder.show();

                    }
                }));
            }

            @Override
            public void onFailure(String msg) {
                Log.e(TAG, "onFailure: " + msg);
                expenseRecyc.setAdapter(null);
            }
        });
    }

    private void changeDate() {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar dateCal = Calendar.getInstance();
                dateCal.set(Calendar.YEAR, year);
                dateCal.set(Calendar.MONTH, month);
                dateCal.set(Calendar.DAY_OF_MONTH, day);
                expenseDate = dateCal.getTime();
                if (DateHelper.getTodayDate("dd").equals(String.valueOf(day))) {
                    expenseDayTV.setText("Today's expenses");
                } else {
                    expenseDayTV.setText(DateHelper.getDay(day, "EEEE") + "'s expenses");
                }
                expenseDateTV.setText(DateHelper.getProcessedDate(year, month, day));
                expenseDateString = expenseDateTV.getText().toString();
                getExpenses();
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void AddExpense() {
        ExpenseItems expenseItems = new ExpenseItems(itemName, itemPrice, user.getId(), expenseDate);
        DbHelper.getInstance().addExpense(context, expenseItems, new ExpenseDbListener.onAddExpenseListener() {
            @Override
            public void onSuccess() {
                itemNameET.setText("");
                itemPriceET.setText("");
                getExpenses();
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(context, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                ((Activity) context).finishAffinity();
            }
        });

        bottomSheetDialog.dismiss();
    }

    private void UpdateExpense() {
        updatedExpenseItem.setItemPrice(itemPrice);
        updatedExpenseItem.setItemName(itemName);
        DbHelper.getInstance().updateExpense(context, updatedExpenseItem, new ExpenseDbListener.onAddExpenseListener() {
            @Override
            public void onSuccess() {
                getExpenses();
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });

        bottomSheetDialog.dismiss();
    }
}
