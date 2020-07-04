package com.welearn.wemath.quizzes.User;

/*Adds a rating, a state for admin approval and a timestanmp*/


import com.welearn.wemath.quizzes.Quiz;
import com.welearn.wemath.quizzes.QuizQuestion;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class UserQuiz extends Quiz {

    private double mRating;
    private boolean mState;
    private Date mTimestamp;

public UserQuiz(){

}

    public UserQuiz(String uid, String username, String title, String section, String year, List<String> topics, String difficulty, List<QuizQuestion> questions){
        super(uid, username, title, section, year, topics, difficulty, questions);

        mState = false;
        mRating = 0.0;
        mTimestamp = new Timestamp(new Date().getTime());

    }

    public double getRating() {
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
