package com.welearn.wemath.quizzes;

import com.welearn.wemath.Quiz;
import com.welearn.wemath.QuizQuestion;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class UserQuiz extends Quiz {

    private float mRating;
    private boolean mState;
    private Date mTimestamp;


    public UserQuiz(String uid, String username, String title, String section, String year, String[] topics, String difficulty, ArrayList<QuizQuestion> questions){
        super(uid, username, title, section, year, topics, difficulty, questions);

        mState = false;
        mRating = 0;
        mTimestamp = new Timestamp(new Date().getTime());

    }

    public float getRating() {
        return mRating;
    }

    public void setRating(float rating) {
        mRating = rating;
    }

    public boolean isState() {
        return mState;
    }

    public void setState(boolean state) {
        mState = state;
    }

    public Date getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(Date timestamp) {
        mTimestamp = timestamp;
    }
}
