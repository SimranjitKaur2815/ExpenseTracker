package com.example.expensetrackersystem.ui.fragments.expenses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.arasthel.spannedgridlayoutmanager.SpannedGridLayoutManager;
import com.example.expensetrackersystem.R;
import com.example.expensetrackersystem.database.entities.ExpenseItems;
import com.example.expensetrackersystem.models.ExpensesModel;
import com.example.expensetrackersystem.utils.db.DbHelper;
import com.example.expensetrackersystem.utils.db.ExpenseDbListener;

import java.util.ArrayList;
import java.util.List;

public class ExpensesFragment extends Fragment {
    private static final String TAG = "ExpensesFragment";
    View view;
    Context context;
    RecyclerView expenses_rv, expenseDialogRV;
    ExpensesListener.AllExpenseListener listener;
    AlertDialog.Builder expenseDialog;
    View expenseDialogView;


    public ExpensesFragment(ExpensesListener.AllExpenseListener listener) {
        this.listener = listener;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.expenses_fragment_layout, container, false);
        init();

        return view;
    }

    private void init() {
        initElements();
        getAllExpenses();
    }


    private void initElements() {
        expenses_rv = view.findViewById(R.id.expenses_rv);
        expenses_rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        expenseDialog = new AlertDialog.Builder(context);

    }


    private void getAllExpenses() {
        DbHelper.getInstance().getAllExpenses(context, new ExpenseDbListener.GetAllExpenseListener() {
            @Override
            public void onSuccess(List<ExpensesModel> expensesModelList) {
                ((Activity) context).runOnUiThread(() -> expenses_rv.setAdapter(new ExpensesAdapter(context, expensesModelList, new ExpensesListener() {
                    @Override
                    public void onEdit(String createdDate) {
                        listener.onEdit(createdDate);
                    }

                    @Override
                    public void onDelete(String createdDate) {
                        DbHelper.getInstance().deleteExpenseByDate(context, createdDate, new ExpenseDbListener.DeleteExpenseListener() {
                            @Override
                            public void onSuccess() {
                                getAllExpenses();
                            }

                            @Override
                            public void onFailure(String msg) {
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onClick(List<ExpenseItems> expenseItemsList) {
                        expenseDialogView = LayoutInflater.from(context).inflate(R.layout.expense_dia_layout, null, false);
                        expenseDialogRV = expenseDialogView.findViewById(R.id.expensesDialogRV);
                        expenseDialogRV.setLayoutManager(new LinearLayoutManager(context));
                        expenseDialog.setView(expenseDialogView);
                        expenseDialog.setPositiveButton("OK", (d, i) -> {
                            expenseDialogRV.setAdapter(null);
                        });
                        expenseDialog.create().show();
                        expenseDialogRV.setAdapter(new ExpensesDialogAdapter(context, expenseItemsList));
                    }
                })));
            }

            @Override
            public void onFailure(String msg) {
                Log.e(TAG, "onFailure: " + msg);
            }
        });
    }
}
