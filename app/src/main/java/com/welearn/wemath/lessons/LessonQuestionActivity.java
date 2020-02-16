package com.welearn.wemath.lessons;

import android.content.Context;
import android.content.Intent;
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

import com.welearn.wemath.R;

import java.util.ArrayList;
import java.util.Arrays;


public class LessonQuestionActivity extends AppCompatActivity {

    //the LessonQuestion object
    private LessonQuestion mLessonQuestion;

    //references to the various widgets on the activity
    private RadioGroup mRadioGroup;
    private Button mNextButton;
    private TextView mQuestionTextView;
    private Button[] mAnswers; //generic button to hold the radio or checkboxes

    //public static Intent newIntent(Context packageContext, boolean answerIsTrue){
    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, LessonQuestionActivity.class);
        //intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) { //called the activity is created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_question); //deflating the activity

        //instantiating the LessonQuestion object
         mLessonQuestion = new LessonQuestion(
                getResources().getString(R.string.question1),
                new ArrayList<Pair<String, Boolean>>(Arrays.asList(
                        new Pair<>(getResources().getString(R.string.answer1_1), false),
                        new Pair<>(getResources().getString(R.string.answer1_2), true),
                        new Pair<>(getResources().getString(R.string.answer1_3), false),
                        new Pair<>(getResources().getString(R.string.answer1_4), true))),
                true);

        mQuestionTextView = findViewById(R.id.question_text_view); //link the reference to the actual widget item
        mQuestionTextView.setText(mLessonQuestion.getProblem()); //set the text to the problem

        mRadioGroup = findViewById(R.id.answers_radio_group); //link the reference to the radiogroup widget


        //setting up the answer choices
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

        //setting up the behaviour of the Next button
        mNextButton = findViewById(R.id.next_lesson_button);
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


    //method to compute the choice of the user
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
        //little message indicating if it's correct or not
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

    }


}
