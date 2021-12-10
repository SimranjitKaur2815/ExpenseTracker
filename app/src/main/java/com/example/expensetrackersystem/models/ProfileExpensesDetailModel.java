package com.example.expensetrackersystem.models;

public class ProfileExpensesDetailModel {
    String detailCode, detailName, detailAmount;
    int colorId, bgId;


    public ProfileExpensesDetailModel(String detailCode, String detailName, String detailAmount, int colorId, int bgId) {
        this.detailCode = detailCode;
        this.detailName = detailName;
        this.detailAmount = detailAmount;
        this.colorId = colorId;
        this.bgId = bgId;
    }

    public String getDetailCode() {
        return detailCode;
    }

    public void setDetailCode(String detailCode) {
        this.detailCode = detailCode;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public String getDetailAmount() {
        return detailAmount;
    }

    public void setDetailAmount(String detailAmount) {
        this.detailAmount = detailAmount;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public int getBgId() {
        return bgId;
    }

    public void setBgId(int bgId) {
        this.bgId = bgId;
    }
}
