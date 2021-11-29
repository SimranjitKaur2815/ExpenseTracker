package com.example.expensetrackersystem.ui.fragments.Expenses;

import android.content.Context;
import android.os.Bundle;
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

import java.util.ArrayList;

public class ExpensesFragment extends Fragment {
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

        return view;
    }
}
