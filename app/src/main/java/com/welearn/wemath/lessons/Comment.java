package com.welearn.wemath.lessons;

/*Custom class for the comments*/

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

public class Comment {

    UUID mUUID;
    String mContent;
    Timestamp mTimestamp;
    int mScore;
    boolean mState;

    Comment(UUID uUid, String content){
        mUUID = uUid;
        mContent = content;
        mScore = 0;
        //mTimestamp = (Timestamp) Time.valueOf((String)System.currentTimeMillis());
        mState = false;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public Timestamp getTimestamp() {
        return mTimestamp;
    }


    public int getScore() {
        return mScore;
    }

    public void increaseScore() {
        mScore--;
    }

    public void decreaseScore(){
        mScore++;
    }

    public boolean isState() {
        return mState;
    }

    public void setState(boolean state) {
        mState = state;
    }
}
