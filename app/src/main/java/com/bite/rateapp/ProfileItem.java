package com.bite.rateapp;

public class ProfileItem {

    private String mPostDate;
    private String mPostComment;
    private String mPostMark;
    private String mPostTime;

    private String mPostTypeOfEvent;
    private String mPostLevelOfEvent;

    private String mPlaceOfEvent;
    private String mPostConfirm;


    public ProfileItem(String postDate, String postTime, String postComment, String postMark, String postTypeOfEvent, String postLevelOfEvent, String postConfirm, String placeOfEvent){
        mPostDate = postDate;
        mPostComment = postComment;
        mPostMark = postMark;
        mPostTime = postTime;

        mPlaceOfEvent = placeOfEvent;

        mPostLevelOfEvent = postLevelOfEvent;
        mPostTypeOfEvent = postTypeOfEvent;

        mPostConfirm = postConfirm;
    }

    public String getmPostDate() {
        return mPostDate;
    }

    public String getmPostComment() {
        return mPostComment;
    }

    public String getmPostMark() {
        return mPostMark;
    }

    public String getmPostTime() {
        return mPostTime;
    }

    public String getmPostTypeOfEvent() {
        return mPostTypeOfEvent;
    }

    public String getmPostLevelOfEvent() {
        return mPostLevelOfEvent;
    }

    public String getmPostConfirm() {
        return mPostConfirm;
    }

    public String getmPlaceOfEvent() {
        return mPlaceOfEvent;
    }
}
