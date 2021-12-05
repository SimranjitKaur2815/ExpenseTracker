package com.example.expensetrackersystem.ui.fragments.expenses;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    RecyclerView expenses_rv;
    ArrayList<ExpenseItems> expensesModels = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.expenses_fragment_layout, container, false);
        expenses_rv = view.findViewById(R.id.expenses_rv);
        ExpensesAdapter expensesAdapter = new ExpensesAdapter(expensesModels);
        expenses_rv.setAdapter(expensesAdapter);
        expenses_rv.setLayoutManager(new LinearLayoutManager(context));
        DbHelper.getInstance().getAllExpenses(context, new ExpenseDbListener.GetAllExpenseListener() {
            @Override
            public void onSuccess(List<ExpensesModel> expensesModelList) {
                Log.e(TAG, "onSuccess: "+expensesModelList.toString() );
            }

            @Override
            public void onFailure(String msg) {
                Log.e(TAG, "onFailure: "+msg );
            }
        });
        return view;
    }
}
