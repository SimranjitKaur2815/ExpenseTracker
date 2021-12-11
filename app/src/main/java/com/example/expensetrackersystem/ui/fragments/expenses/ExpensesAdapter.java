package com.example.expensetrackersystem.ui.fragments.expenses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackersystem.R;
import com.example.expensetrackersystem.database.entities.ExpenseItems;

import java.util.ArrayList;
import java.util.List;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ExpensesViewHolder> {
    public Context context;
    private List<String> expensesModels=new ArrayList<>();

    public ExpensesAdapter(List<ExpenseItems> ss) {
//        this.expensesModels = expensesModels;
        expensesModels.add("sadasdadasdasd");
        expensesModels.add("sadasdadasdasd nbehbfjbf djfhjdbfjbdjf jfjdenfj");
        expensesModels.add("sadasdadasdasd fdbfjebdjdc jdckdnybjdbcdc,djbfjebfjejejebjebjbjebjefbj ejbfjebfjebfjebjeb");
        expensesModels.add("sadasdadasdasd nvdnjjc bdjsbcjksdb dscjksdbcks dsbdsjkb");
        expensesModels.add("sadasdadasdasddbjds nd");
        expensesModels.add("sadasdadasdasd jbdejwbkjwbkwbk");
        expensesModels.add("sadasdadasdasd jdjsbkkkkkkkkkkkkkkkkkkkk bdjsbjjhh dbjwbhjbdvhcbjwh dbjcdbsbjdsbj dsbjsbfksb vbkjdbfjkbwekbfkbkewbf\n hdvfhjdvfjdvfv\n hefhjwvf jvfjewvfewjfevjwvfhjwevfjew");

    }


    @NonNull
    @Override
    public ExpensesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expenses_adapter, parent, false);
        return new ExpensesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpensesViewHolder holder, int position) {
        holder.expense_content.setText(expensesModels.get(position));

    }

    @Override
    public int getItemCount() {
        return expensesModels.size();
    }

    public static class ExpensesViewHolder extends RecyclerView.ViewHolder {
        ImageView edit, delete;
        TextView calculated_amount, expense_content;

        public ExpensesViewHolder(@NonNull View itemView) {

            super(itemView);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
            calculated_amount = itemView.findViewById(R.id.calculated_amount);
            expense_content = itemView.findViewById(R.id.expense_content);
        }
    }
}
