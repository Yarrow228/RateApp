package com.bite.rateapp;

public class RateItem {

    private String mUserName;
    private String mUserSurname;
    private String mUserRating;
    private String mUserId;


    public RateItem(String userName, String userSurname, String userRating, String userId){
        mUserName = userName;
        mUserSurname = userSurname;
        mUserRating = userRating;
        mUserId = userId;
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

    public String getmUserId() {
        return mUserId;
    }
}
