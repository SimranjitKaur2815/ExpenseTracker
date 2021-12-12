package com.example.expensetrackersystem.ui.activities.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackersystem.R;
import com.example.expensetrackersystem.database.entities.User;

import java.util.List;

public class LoginAdapter extends RecyclerView.Adapter<LoginAdapter.ViewHolder> {

    Context context;
    List<User> users;
    LoginInterface listener;

    public LoginAdapter(Context context, List<User> users, LoginInterface listener) {
        this.context = context;
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.login_users_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.user.setText(user.getFirstName() + " " + user.getLastName());
        holder.user.setOnClickListener(v -> {
            listener.onUserClick(user);
        });
        holder.removeAcc.setOnClickListener(v -> listener.onUserDelete(user));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView user;
        ImageView removeAcc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.user);
            removeAcc = itemView.findViewById(R.id.removeAcc);
        }
    }
}
