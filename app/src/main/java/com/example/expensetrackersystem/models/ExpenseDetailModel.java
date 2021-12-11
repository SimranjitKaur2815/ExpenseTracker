package com.example.expensetrackersystem.models;

public class ExpenseDetailModel {
    int tot_expenses;
    double tot_price;

    public int getTot_expenses() {
        return tot_expenses;
    }

    public void setTot_expenses(int tot_expenses) {
        this.tot_expenses = tot_expenses;
    }

    public double getTot_price() {
        return tot_price;
    }

    public void setTot_price(double tot_price) {
        this.tot_price = tot_price;
    }

    @Override
    public String toString() {
        return "ExpenseDetailModel{" +
                "tot_expenses=" + tot_expenses +
                ", tot_price=" + tot_price +
                '}';
    }
}
