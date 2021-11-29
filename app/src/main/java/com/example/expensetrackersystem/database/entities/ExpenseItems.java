package com.example.expensetrackersystem.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.expensetrackersystem.utils.DateTypeConverter;

import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "expenseItems",
        foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "user_id", childColumns = "user_id")}
)
public class ExpenseItems {
    @PrimaryKey(autoGenerate = true)
    public int item_id;

    public String item_name;

    public Double item_price;

    public int user_id;

    public Boolean is_deleted = false;

    @TypeConverters({DateTypeConverter.class})
    public Date created_at= Calendar.getInstance().getTime();

}
