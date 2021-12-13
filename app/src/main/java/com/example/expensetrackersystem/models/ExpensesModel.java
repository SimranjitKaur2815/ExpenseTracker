package com.example.expensetrackersystem.models;

import com.example.expensetrackersystem.database.entities.ExpenseItems;

import java.util.Date;
import java.util.List;

public class ExpensesModel {
    private List<ExpenseItems> expenseItems;
    private String submittedDate;
    private Double totalPrice;

    /* getter and setter for total price starts here*/
    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
    /* getter and setter for total price ends here*/

    /* getter and setter for List of expense items starts here*/
    public List<ExpenseItems> getExpenseItems() {
        return expenseItems;
    }

    public void setExpenseItems(List<ExpenseItems> expenseItems) {
        this.expenseItems = expenseItems;
    }
    /* getter and setter for List of expense items ends here*/

    /* getter and setter for submit date starts here*/
    public String getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(String submittedDate) {
        this.submittedDate = submittedDate;
    }
    /* getter and setter for submit date ends here*/

    /* toString method returns textual representation of Expenses Model*/
    @Override
    public String toString() {
        return "ExpensesModel{" +
                "expenseItems=" + expenseItems +
                ", submittedDate='" + submittedDate + '\'' +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
