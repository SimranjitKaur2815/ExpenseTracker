package com.example.expensetrackersystem.models;

import com.example.expensetrackersystem.database.entities.ExpenseItems;

import java.util.Date;
import java.util.List;

public class ExpensesModel {
    private List<ExpenseItems> expenseItems;
    private Date submittedDate;

    public List<ExpenseItems> getExpenseItems() {
        return expenseItems;
    }

    public void setExpenseItems(List<ExpenseItems> expenseItems) {
        this.expenseItems = expenseItems;
    }

    public Date getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Date submittedDate) {
        this.submittedDate = submittedDate;
    }

}
