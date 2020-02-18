package com.welearn.wemath.lessons;

/*Custom class for the Lesson questions extending the basic Question interface*/

import android.util.Pair;

import com.welearn.wemath.Question;

import java.util.ArrayList;

public class LessonQuestion extends Question {

    LessonQuestion(String problem, ArrayList<Pair<String, Boolean>> answers, boolean multipleChoice){
        super(problem, answers, multipleChoice);
    }
}
