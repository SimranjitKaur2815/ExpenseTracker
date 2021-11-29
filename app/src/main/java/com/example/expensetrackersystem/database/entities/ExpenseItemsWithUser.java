package com.example.expensetrackersystem.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.expensetrackersystem.utils.DateTypeConverter;

import java.util.Date;

@Entity
public class ExpenseItemsWithUser {

    @PrimaryKey
    public int item_id;

    public int user_id;

    public String item_name, first_name, last_name;

    public Double item_price;

    public Boolean is_deleted = false;

    @TypeConverters({DateTypeConverter.class})
    public Date created_at;

    @Override
    public String toString() {
        return "ExpenseItemsWithUser{" +
                "item_id=" + item_id +
                ", user_id=" + user_id +
                ", item_name='" + item_name + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", item_price=" + item_price +
                ", is_deleted=" + is_deleted +
                ", created_at=" + created_at +
                '}';
    }
}
