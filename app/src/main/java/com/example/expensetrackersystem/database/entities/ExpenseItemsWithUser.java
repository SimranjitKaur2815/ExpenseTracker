package com.example.expensetrackersystem.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.expensetrackersystem.utils.DateTypeConverter;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

@Entity
public class ExpenseItemsWithUser {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "avatar")
    private String avatar;

    @ColumnInfo(name = "item_name")
    private String itemName;

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;

    @ColumnInfo(name = "age")
    private int age;

    @ColumnInfo(name = "user_created_timestamp")
    @TypeConverters({DateTypeConverter.class})
    private Date userCreatedTimeStamp;

    @ColumnInfo(name = "item_price")
    private Double itemPrice;

    @ColumnInfo(name = "is_deleted")
    private Boolean isDeleted = false;


    @ColumnInfo(name = "created_timestamp")
    @TypeConverters({DateTypeConverter.class})
    private Date createdTimeStamp = Calendar.getInstance().getTime();

    @ColumnInfo(name = "created_date")
    private String createdDate = DateFormat.getDateInstance().format(createdTimeStamp);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getUserCreatedTimeStamp() {
        return userCreatedTimeStamp;
    }

    public void setUserCreatedTimeStamp(Date userCreatedTimeStamp) {
        this.userCreatedTimeStamp = userCreatedTimeStamp;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "-------\n\nExpenseItemsWithUser{" +
                "id=" + id +
                ", userId=" + userId +
                ", avatar='" + avatar + '\'' +
                ", itemName='" + itemName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", userCreatedTimeStamp=" + userCreatedTimeStamp +
                ", itemPrice=" + itemPrice +
                ", isDeleted=" + isDeleted +
                ", createdTimeStamp=" + createdTimeStamp +
                ", createdDate='" + createdDate + '\'' +
                '}';
    }
}
