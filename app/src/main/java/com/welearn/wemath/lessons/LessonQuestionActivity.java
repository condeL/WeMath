package com.welearn.wemath.lessons;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import static java.lang.Boolean.parseBoolean;


public class
LessonQuestionActivity extends AppCompatActivity {


    final int NEXT = 1;
    final int PREV = 2;

    private RadioGroup mRadioGroup;
    private Button mNextButton;
    private Button mPrevButton;
    private Button mSkipButton;
    private TextView mQuestionTextView;
    private Button[] mAnswers; //generic button to hold the radio or checkboxes

    private LessonQuestion mLessonQuestion;

    private String mYear, mSection, mSubject;
    private int mTopic, mLesson, mClearedLesson;
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_question);

        mYear = getIntent().getStringExtra("year");
        mSection = getIntent().getStringExtra("section");
        mTopic = getIntent().getIntExtra("topic",1);
        mLesson = getIntent().getIntExtra("lesson",1);

        String choice = "_" + mSection + mYear+ "_" + mTopic + "_" + mLesson;

        int questionID = getResources().getIdentifier("question" + choice , "string", getPackageName());
        int answerID = getResources().getIdentifier("answer" + choice, "array", getPackageName());
        int truthID = getResources().getIdentifier("truth" + choice, "array", getPackageName());
        int mcqID = getResources().getIdentifier("mcq"+ choice, "string", getPackageName());

        mSubject = mSection + mYear + mTopic;

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mClearedLesson = mPrefs.getInt(mSubject, 1);

        String[] answers = getResources().getStringArray(answerID);
        String[] truths = getResources().getStringArray(truthID);

        ArrayList<Pair<String, Boolean>> answerTruths = new ArrayList<>();

        for (int i = 0; i<answers.length;i++){
            answerTruths.add(new Pair<>(answers[i], Boolean.parseBoolean(truths[i])));
        }

        //instantiating the LessonQuestion object
         mLessonQuestion = new LessonQuestion(getResources().getString(questionID), answerTruths, parseBoolean(getResources().getString(mcqID)));

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

        mNextButton = findViewById(R.id.lesson_question_next_button);
        mNextButton.setOnClickListener(v -> {
                int answer;
                if (!mLessonQuestion.isMultipleChoice()) {
                    if(mRadioGroup.getCheckedRadioButtonId()!=-1) {
                        int selectedID = mRadioGroup.getCheckedRadioButtonId();
                        RadioButton radioButton = mRadioGroup.findViewById(selectedID);
                        answer = mRadioGroup.indexOfChild(radioButton);
                        checkAnswer(answer);
                    } else
                        Toast.makeText(getBaseContext(),"Please choose an answer", Toast.LENGTH_SHORT).show();

                } else if(checkChecked()) {
                    checkAnswer(0);
                }  else
                    Toast.makeText(getBaseContext(),"Please choose an answer", Toast.LENGTH_SHORT).show();

            });


        mPrevButton = findViewById(R.id.prev_lesson_button);
        mPrevButton.setOnClickListener(v -> navigateBack(PREV));

        if(mClearedLesson > mLesson) {
            mSkipButton = findViewById(R.id.lesson_question_skip_button);
            mSkipButton.setVisibility(View.VISIBLE);
            mSkipButton.setOnClickListener(v -> navigateBack(NEXT));
        }
    }

    void navigateBack(int answer){
        Intent returnIntent = new Intent();
        switch (answer){
            case NEXT:
                updatePreferences();
                setResult(LessonActivity.NEXT_REQUEST_CODE,returnIntent);
                finish();
                break;
            case PREV:
                setResult(LessonActivity.PREV_REQUEST_CODE,returnIntent);
                finish();
                break;
            default:
                setResult(LessonActivity.RESULT_CANCELED,returnIntent);
                finish();
                break;
        }
    }

    //method to compute the answer of the user
    private void checkAnswer(int answerNumber){

        int messageResId = 0;
        int correct = 0;

        if(!mLessonQuestion.isMultipleChoice()) {
            if (mLessonQuestion.getAnswerValue(answerNumber)) {
                messageResId = R.string.correct_toast;
                correct = NEXT;
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
                correct = NEXT;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        navigateBack(correct);

    }

     private void updatePreferences(){
        SharedPreferences.Editor editor = mPrefs.edit();

        if(mClearedLesson == mLesson) {
            editor.putInt(mSubject, mClearedLesson+1);
            editor.commit();
        }
     }

    private boolean checkChecked() {
        for (int i = 0; i < 3; i++) {
            if (((CheckBox) mAnswers[i]).isChecked()) {
                return true;
            }
        }
        return false;
    }
}
