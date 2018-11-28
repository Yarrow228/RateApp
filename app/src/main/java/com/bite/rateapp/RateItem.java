package com.bite.rateapp;

public class RateItem {

    private String mUserName;
    private String mUserSurname;
    private String mUserRating;


    public RateItem(String userName, String userSurname, String userRating){
        mUserName = userName;
        mUserSurname = userSurname;
        mUserRating = userRating;

    }

    public String getmUserName() {
        return mUserName;
    }

    public String getmUserSurname() {
        return mUserSurname;
    }

    public String getmUserRating() {
        return mUserRating;
    }
}
