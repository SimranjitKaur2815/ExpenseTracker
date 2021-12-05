package com.example.expensetrackersystem.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.expensetrackersystem.utils.DateTypeConverter;

import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "user")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "avatar")
    private String avatar;

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;

    @ColumnInfo(name = "password")
    private String password;

    @TypeConverters({DateTypeConverter.class})
    @ColumnInfo(name = "dob")
    private Date dob;

    @ColumnInfo(name = "user_created_timestamp")
    @TypeConverters({DateTypeConverter.class})
    private Date userCreatedTimeStamp = Calendar.getInstance().getTime();

    public User(String avatar, String firstName, String lastName, String password, Date dob) {
        this.avatar = avatar;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.dob = dob;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Date getUserCreatedTimeStamp() {
        return userCreatedTimeStamp;
    }

    public void setUserCreatedTimeStamp(Date userCreatedTimeStamp) {
        this.userCreatedTimeStamp = userCreatedTimeStamp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", avatar='" + avatar + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", dob=" + dob +
                ", userCreatedTimeStamp=" + userCreatedTimeStamp +
                '}';
    }
}
