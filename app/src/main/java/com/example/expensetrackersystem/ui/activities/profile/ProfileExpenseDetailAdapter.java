package com.example.expensetrackersystem.ui.activities.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackersystem.R;
import com.example.expensetrackersystem.models.ProfileExpensesDetailModel;

import java.util.List;

public class ProfileExpenseDetailAdapter extends RecyclerView.Adapter<ProfileExpenseDetailAdapter.ViewHolder> {
    Context context;
    List<ProfileExpensesDetailModel> modelList;

    public ProfileExpenseDetailAdapter(Context context, List<ProfileExpensesDetailModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_expense_details_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProfileExpensesDetailModel model = modelList.get(position);
        if (model.getDetailCode().equals("TEA")) {

            holder.detailAmount.setText("$"+model.getDetailAmount());
        } else {

            holder.detailAmount.setText(model.getDetailAmount());
        }

        holder.detailAmount.setTextColor(context.getResources().getColor(model.getColorId()));
        holder.detailAmount.setBackgroundResource(model.getBgId());
        holder.detailName.setTextColor(context.getResources().getColor(model.getColorId()));
        holder.detailName.setText(model.getDetailName());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView detailAmount, detailName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            detailAmount = itemView.findViewById(R.id.detailAmount);
            detailName = itemView.findViewById(R.id.detailName);
        }
    }
}