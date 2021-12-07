package com.example.expensetrackersystem.models;

import com.example.expensetrackersystem.database.entities.ExpenseItems;

import java.util.Date;
import java.util.List;

public class ExpensesModel {
    private List<ExpenseItems> expenseItems;
    private String submittedDate;
    private Double totalPrice;

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<ExpenseItems> getExpenseItems() {
        return expenseItems;
    }

    public void setExpenseItems(List<ExpenseItems> expenseItems) {
        this.expenseItems = expenseItems;
    }

    public String getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(String submittedDate) {
        this.submittedDate = submittedDate;
    }

    @Override
    public String toString() {
        return "ExpensesModel{" +
                "expenseItems=" + expenseItems +
                ", submittedDate='" + submittedDate + '\'' +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
