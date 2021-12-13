package com.example.expensetrackersystem.models;

public class ExpenseDetailModel {
    int total_expenses;
    double total_price;

    /* getter and setter for total_expenses starts here*/
    public int getTotal_expenses() {
        return total_expenses;
    }

    public void setTotal_expenses(int total_expenses) {
        this.total_expenses = total_expenses;
    }
    /* getter and setter for total_expenses ends here*/


    /* getter and setter for total_price starts here*/
    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    /* getter and setter for total_price ends here*/

    /* toString method returns textual representation of Expenses Model*/
    @Override
    public String toString() {
        return "ExpenseDetailModel{" +
                "tot_expenses=" + total_expenses +
                ", tot_price=" + total_price +
                '}';
    }
}
