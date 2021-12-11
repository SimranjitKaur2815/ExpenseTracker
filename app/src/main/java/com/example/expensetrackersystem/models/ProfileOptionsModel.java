package com.example.expensetrackersystem.models;

public class ProfileOptionsModel {
    private int optionDrawableId;
    private String optionName,optionCode;

    public ProfileOptionsModel(int optionDrawableId, String optionName, String optionCode) {
        this.optionDrawableId = optionDrawableId;
        this.optionName = optionName;
        this.optionCode = optionCode;
    }

    public int getOptionDrawableId() {
        return optionDrawableId;
    }

    public void setOptionDrawableId(int optionDrawableId) {
        this.optionDrawableId = optionDrawableId;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionCode() {
        return optionCode;
    }

    public void setOptionCode(String optionCode) {
        this.optionCode = optionCode;
    }
}
