package com.example.expensetrackersystem.database.entities;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.expensetrackersystem.utils.DateHelper;
import com.example.expensetrackersystem.utils.DateTypeConverter;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "expenseItems")
public class ExpenseItems {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "item_name")
    private String itemName;

    @ColumnInfo(name = "item_price")
    private Double itemPrice;

    @ColumnInfo(name = "user_id", index = true)
    private int userId;

    @ColumnInfo(name = "is_deleted")
    private Boolean isDeleted = false;

    @ColumnInfo(name = "created_timestamp")
    @TypeConverters({DateTypeConverter.class})
    private Date createdTimeStamp;

    @ColumnInfo(name = "created_date")
    private String createdDate;


    public ExpenseItems(String itemName, Double itemPrice, int userId, Date createdTimeStamp) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.userId = userId;
        this.createdTimeStamp = createdTimeStamp;
        this.createdDate=DateHelper.getDateInCommonFormat(createdTimeStamp.getTime());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Date getCreatedTimeStamp() {
        return createdTimeStamp;
    }

    public void setCreatedTimeStamp(Date createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "\nExpenseItems{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                ", userId=" + userId +
                ", isDeleted=" + isDeleted +
                ", createdTimeStamp=" + createdTimeStamp +
                ", createdDate='" + createdDate + '\'' +
                '}';
    }
}
