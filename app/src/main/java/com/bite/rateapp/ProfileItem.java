package com.bite.rateapp;

public class ProfileItem {

    private String mPostDate;
    private String mPostComment;
    private String mPostMark;
    private String mPostTime;

    private String mPostTypeOfEvent;
    private String mPostLevelOfEvent;



    public ProfileItem(String postDate, String postTime, String postComment, String postMark, String postTypeOfEvent, String postLevelOfEvent){
        mPostDate = postDate;
        mPostComment = postComment;
        mPostMark = postMark;
        mPostTime = postTime;

        mPostLevelOfEvent = postLevelOfEvent;
        mPostTypeOfEvent = postTypeOfEvent;

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

}
