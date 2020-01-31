package com.welearn.wemath;

import android.util.Pair;

import java.util.ArrayList;

public class LessonQuestion extends Question {

    LessonQuestion(String problem, ArrayList<Pair<String, Boolean>> answers, boolean multipleChoice){
        super(problem, answers, multipleChoice);
    }
}
