package com.welearn.wemath.quizzes;

/*Adds an explanation compared to normal questions*/

import android.util.Pair;

import com.welearn.wemath.Question;

import java.util.ArrayList;
import java.util.List;

public class QuizQuestion extends Question {

    public String mExplanation;

    public QuizQuestion(){};

    public QuizQuestion(String problem, List<Pair<String, Boolean>> answers, boolean multipleChoice, String explanation){
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
