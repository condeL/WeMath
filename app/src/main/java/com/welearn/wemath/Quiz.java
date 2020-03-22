package com.welearn.wemath;

import java.util.ArrayList;

public abstract class Quiz {
    private String mUid;
    private String mUsername;
    private String mTitle;
    private String mSection;
    private String mYear;
    private String[] mTopics;
    private String mDifficulty;
    private ArrayList<QuizQuestion> mQuizQuestions;

    public Quiz(){};

    public Quiz(String uid, String username, String title, String section, String year, String[] topics, String difficulty, ArrayList<QuizQuestion> questions){
        mUid = uid;
        mUsername = username;
        mTitle = title;
        mSection = section;
        mYear = year;
        mTopics = topics;
        mDifficulty = difficulty;
        mQuizQuestions = questions;

    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getSection() {
        return mSection;
    }

    public void setSection(String section) {
        mSection = section;
    }

    public String getYear() {
        return mYear;
    }

    public void setYear(String year) {
        mYear = year;
    }

    public String[] getTopics() {
        return mTopics;
    }

    public void setTopics(String[] topics) {
        mTopics = topics;
    }

    public String getDifficulty() {
        return mDifficulty;
    }

    public void setDifficulty(String difficulty) {
        mDifficulty = difficulty;
    }

    public ArrayList<QuizQuestion> getQuizQuestions() {
        return mQuizQuestions;
    }

    public void setQuizQuestions(ArrayList<QuizQuestion> quizQuestions) {
        mQuizQuestions = quizQuestions;
    }
}
