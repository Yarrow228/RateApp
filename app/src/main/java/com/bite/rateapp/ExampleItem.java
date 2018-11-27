package com.bite.rateapp;

public class ExampleItem {

    private String mPostDate;
    private String mPostComment;
    private String mPostMark;
    private String mPostTime;


    public ExampleItem(String postDate, String postTime, String postComment, String postMark){
        mPostDate = postDate;
        mPostComment = postComment;
        mPostMark = postMark;
        mPostTime = postTime;

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
}
