package com.bite.rateapp;

public class ConfItem {

    private String mConfName;
    private String mConfSurname;
    private String mConfDate;
    private String mConfTime;
    private String mConfComment;
    private String mConfType;
    private String mConfLevel;

    public ConfItem(String confName, String confSurname, String confDate, String confTime, String confComment, String confType, String confLevel){


        mConfName = confName;
        mConfSurname = confSurname;
        mConfDate = confDate;
        mConfTime = confTime;
        mConfComment = confComment;
        mConfType = confType;
        mConfLevel = confLevel;

    }


    public String getmConfName() {
        return mConfName;
    }

    public String getmConfSurname() {
        return mConfSurname;
    }

    public String getmConfDate() {
        return mConfDate;
    }

    public String getmConfTime() {
        return mConfTime;
    }

    public String getmConfComment() {
        return mConfComment;
    }

    public String getmConfType() {
        return mConfType;
    }

    public String getmConfLevel() {
        return mConfLevel;
    }
}
