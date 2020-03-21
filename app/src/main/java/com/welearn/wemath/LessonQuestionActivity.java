package com.welearn.wemath;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;


public class
LessonQuestionActivity extends AppCompatActivity {

    private RadioGroup mRadioGroup;
    private Button mNextButton;
    private TextView mQuestionTextView;
    private LessonQuestion mLessonQuestion;
    private Button[] mAnswers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_question);

         mLessonQuestion = new LessonQuestion(
                getResources().getString(R.string.question1),
                new ArrayList<Pair<String, Boolean>>(Arrays.asList(
                        new Pair<>(getResources().getString(R.string.answer1_1), false),
                        new Pair<>(getResources().getString(R.string.answer1_2), true),
                        new Pair<>(getResources().getString(R.string.answer1_3), false),
                        new Pair<>(getResources().getString(R.string.answer1_4), true))),
                false);

        mQuestionTextView = findViewById(R.id.question_text_view);
        mQuestionTextView.setText(mLessonQuestion.getProblem());

        /*mAnswerChoice1 = new RadioButton(this);
        mAnswerChoice2 = new RadioButton(this);
        mAnswerChoice3 = new RadioButton(this);*/

        mRadioGroup = findViewById(R.id.radio_group);

        if(!mLessonQuestion.isMultipleChoice()) {
            mAnswers = new RadioButton[mLessonQuestion.getAnswersSize()];
            for(int i = 0; i<mLessonQuestion.getAnswersSize();i++){
                mAnswers[i] = (RadioButton)mRadioGroup.getChildAt(i);
                mAnswers[i].setText(mLessonQuestion.getAnswerText(i));
                mAnswers[i].setVisibility(View.VISIBLE);
            }

        }
        else{
            mAnswers = new CheckBox[mLessonQuestion.getAnswersSize()];
            for(int i = 0; i<mLessonQuestion.getAnswersSize();i++){
               // mAnswers[i] = new CheckBox(this);
                mAnswers[i] = (CheckBox)mRadioGroup.getChildAt(i+4);
                mAnswers[i].setText(mLessonQuestion.getAnswerText(i));
                mAnswers[i].setVisibility(View.VISIBLE);

            }
        }



        /*for(int i = 0; i<mLessonQuestion.getAnswers().size();i++){
            if(!mLessonQuestion.isMultipleChoice()) {
                mRadioGroup.addView(new RadioButton(this), i);
                ((RadioButton) mRadioGroup.getChildAt(i)).setText(mLessonQuestion.getAnswers().get(i).first);
            }
            else{
                mRadioGroup.addView(new CheckBox(this), i);
                ((CheckBox) mRadioGroup.getChildAt(i)).setText(mLessonQuestion.getAnswers().get(i).first);
            }
        }*/


        /*private RadioButton mAnswerChoice1;
        private RadioButton mAnswerChoice2;
        private RadioButton mAnswerChoice3;

        mRadioGroup.addView(mAnswerChoice1);
        mRadioGroup.addView(mAnswerChoice2);
        mRadioGroup.addView(mAnswerChoice3);*/

        /*mAnswerChoice1 = findViewById(R.id.answer_choice1_button);
        mAnswerChoice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                checkAnswer(0);
            }
        });

        mAnswerChoice2 = findViewById(R.id.answer_choice2_button);
        mAnswerChoice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(1);
            }
        });

        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mAnswerChoice3 = findViewById(R.id.answer_choice3_button);
        mAnswerChoice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(2);
            }
        });*/

        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int answer;
                if(!mLessonQuestion.isMultipleChoice()) {
                    int selectedID = mRadioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = mRadioGroup.findViewById(selectedID);
                    answer = mRadioGroup.indexOfChild(radioButton);
                    checkAnswer(answer);
                } else{
                    checkAnswer(0);
                }
            }
        });


    }



    private void checkAnswer(int answerNumber){

        int messageResId = 0;

        if(!mLessonQuestion.isMultipleChoice()) {
            if (mLessonQuestion.getAnswerValue(answerNumber)) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        } else{
            int score = 0;
            for(int i = 0; i<mLessonQuestion.getAnswersSize();i++){
                if(((CheckBox)mAnswers[i]).isChecked()){
                    if (mLessonQuestion.getAnswerValue(i)){
                        score++;
                    }
                    else{
                        score=-1;
                        break;
                    }
                }
            }
            if(score == mLessonQuestion.getNbCorrectAnswers()){
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

    }


}
