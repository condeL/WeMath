package com.welearn.wemath;

import android.util.Pair;

import java.util.ArrayList;

public class QuizQuestion extends Question {

    public String mExplanation;

    public QuizQuestion(){};

    public QuizQuestion(String problem, ArrayList<Pair<String, Boolean>> answers, boolean multipleChoice, String explanation){
        super(problem, answers, multipleChoice);

        mExplanation = explanation;
    }

    public String getExplanation() {
        return mExplanation;
    }

    public void setExplanation(String explanation) {
        mExplanation = explanation;
    }
}
