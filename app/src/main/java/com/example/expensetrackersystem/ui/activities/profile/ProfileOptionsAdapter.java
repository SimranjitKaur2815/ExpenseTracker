package com.example.expensetrackersystem.ui.activities.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackersystem.R;
import com.example.expensetrackersystem.models.ProfileOptionsModel;
import com.example.expensetrackersystem.ui.activities.login.LoginActivity;

import java.util.List;

public class ProfileOptionsAdapter extends RecyclerView.Adapter<ProfileOptionsAdapter.ViewHolder> {
    Context context;
    List<ProfileOptionsModel> list;
    onProfileChangesListener listener;

    public ProfileOptionsAdapter(Context context, List<ProfileOptionsModel> list, onProfileChangesListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_options_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProfileOptionsModel model = list.get(position);
        holder.name.setText(model.getOptionName());
        holder.icon.setImageResource(model.getOptionDrawableId());
        holder.itemView.setOnClickListener(v -> manageOptionClick(model.getOptionCode()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void manageOptionClick(String optionCode) {
        switch (optionCode) {
            case "MA":
                context.startActivity(new Intent(context, LoginActivity.class).putExtra("fromProfile", "Yes"));
                break;
            case "CU":
                listener.onUsernameChange();
                break;
            case "CP":
                listener.onPasswordChange();
                Toast.makeText(context, optionCode, Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(context, "Select valid option", Toast.LENGTH_SHORT).show();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}
