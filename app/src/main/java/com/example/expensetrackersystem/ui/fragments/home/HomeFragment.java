package com.example.expensetrackersystem.ui.fragments.home;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackersystem.R;
import com.example.expensetrackersystem.database.DatabaseClient;
import com.example.expensetrackersystem.database.entities.ExpenseItems;
import com.example.expensetrackersystem.database.entities.ExpenseItemsWithUser;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {
    View view, bottomSheetView;
    Context context;
    FloatingActionButton addExpenseBtn;
    String itemName = "";
    Double itemPrice = 0.0d;
    TextInputEditText itemNameET, itemPriceET;
    MaterialButton addExpenseMatBtn;
    BottomSheetDialog bottomSheetDialog;
    Executor executor;
    TextView expenseDay, expenseDate;
    RecyclerView expenseRecyc;

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
        expenseDay = view.findViewById(R.id.expenseDay);
        expenseDate = view.findViewById(R.id.expenseDate);
        expenseRecyc = view.findViewById(R.id.expensesRecyc);
        expenseRecyc.setLayoutManager(new LinearLayoutManager(context));
        expenseRecyc.setAdapter(new HomeExpensesAdapter());
        bottomSheetView = LayoutInflater.from(context).inflate(R.layout.add_expense_bottom_sheet, null, false);
        itemNameET = bottomSheetView.findViewById(R.id.itemName);
        itemPriceET = bottomSheetView.findViewById(R.id.itemPrice);
        addExpenseMatBtn = bottomSheetView.findViewById(R.id.addExpenseBtn);
        bottomSheetDialog = new BottomSheetDialog(context, R.style.CustomShapeAppearanceBottomSheetDialog);
        executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            for (ExpenseItemsWithUser items : DatabaseClient.getInstance(context).getAppDatabase().expensesDao().getExpenses(1, DateFormat.getDateInstance().format(Calendar.getInstance().getTime()))) {
                Log.e("Home Fragment", "Data : " + items.toString());
            }
        });


    }

    private void initListeners() {
        addExpenseBtn.setOnClickListener(v -> {
            bottomSheetDialog.setContentView(bottomSheetView);
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
    }

    private void AddExpense() {
        ExpenseItems expenseItems = new ExpenseItems();
        expenseItems.setItemPrice(itemPrice);
        expenseItems.setItemName(itemName);
        expenseItems.setUserId(1);
        executor.execute(() -> {

            DatabaseClient.getInstance(context).getAppDatabase().expensesDao().insertExpense(expenseItems);

            for (ExpenseItemsWithUser items : DatabaseClient.getInstance(context).getAppDatabase().expensesDao().getExpenses(1, DateFormat.getDateInstance().format(Calendar.getInstance().getTime()))) {
                Log.e("Home Fragment", "Data : " + items.toString());
            }
            bottomSheetDialog.dismiss();

        });
    }
}
