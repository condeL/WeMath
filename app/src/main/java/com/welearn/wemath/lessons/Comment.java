package com.welearn.wemath.lessons;

/*Custom class for the comments*/

import java.sql.Timestamp;
import java.util.Date;

public class Comment {

    String mUID;
    String mName;
    String mMessage;
    Date mTimestamp;
    int mDownvotes;
    int mUpvotes;
    boolean mState;

    Comment(){

    }

    Comment(String uUid, String name, String message){
        mUID = uUid;
        mMessage = message;
        mName = name;
        mDownvotes = 0;
        mUpvotes = 0;
        mTimestamp = new Timestamp(new Date().getTime());
        mState = false;
    }

    public String getUID() {
        return mUID;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Date getTimestamp() {
        return mTimestamp;
    }

    public void setUID(String UID) {
        mUID = UID;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setTimestamp(Date timestamp) {
        mTimestamp = timestamp;
    }

    public void setDownvotes(int downvotes) {
        mDownvotes = downvotes;
    }

    public void setUpvotes(int upvotes) {
        mUpvotes = upvotes;
    }

    public int getDownvotes() {
        return mDownvotes;
    }

    public String getName() {
        return mName;
    }

    public int getUpvotes() {
        return mUpvotes;
    }


    public boolean isState() {
        return mState;
    }

    public void setState(boolean state) {
        mState = state;
    }
}
