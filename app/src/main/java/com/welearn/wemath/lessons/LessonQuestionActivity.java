package com.welearn.wemath.lessons;

import android.content.Context;
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
import java.util.Arrays;

import static java.lang.Boolean.parseBoolean;


public class
LessonQuestionActivity extends AppCompatActivity {

    //the LessonQuestion object
    private LessonQuestion mLessonQuestion;

    final int NEXT = 1;
    final int PREV = 2;

    //references to the various widgets on the activity
    private RadioGroup mRadioGroup;
    private Button mNextButton;
    private Button mPrevButton;
    private TextView mQuestionTextView;
    private Button[] mAnswers; //generic button to hold the radio or checkboxes

    private String mYear, mSection;
    private int mTopic, mLesson;


    //public static Intent newIntent(Context packageContext, boolean answerIsTrue){
    /*public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, LessonQuestionActivity.class);
        //intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) { //called the activity is created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_question); //deflating the activity

        mYear = getIntent().getStringExtra("year");
        mSection = getIntent().getStringExtra("section");
        mTopic = getIntent().getIntExtra("topic",1);
        mLesson = getIntent().getIntExtra("lesson",1);

        String choice = "_" + mSection + mYear+ "_" + mTopic + "_" + mLesson;

        int questionID = getResources().getIdentifier("question" + choice , "string", getPackageName());
        int answerID = getResources().getIdentifier("answer" + choice, "array", getPackageName());
        int truthID = getResources().getIdentifier("truth" + choice, "array", getPackageName());
        int mcqID = getResources().getIdentifier("mcq"+ choice, "string", getPackageName());


        String[] answers = getResources().getStringArray(answerID);
        String[] truths = getResources().getStringArray(truthID);

        //instantiating the LessonQuestion object
         mLessonQuestion = new LessonQuestion(
                getResources().getString(questionID),
                new ArrayList<Pair<String, Boolean>>(Arrays.asList(
                        new Pair<>(answers[0], Boolean.parseBoolean(truths[0])),
                        new Pair<>(answers[1], Boolean.parseBoolean(truths[1])),
                        new Pair<>(answers[2], Boolean.parseBoolean(truths[2])),
                        new Pair<>(answers[3], Boolean.parseBoolean(truths[3])))),
                parseBoolean(getResources().getString(mcqID)));

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
        mNextButton = findViewById(R.id.lesson_question_next_button);
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

        //final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //final SharedPreferences preferences1;
        mPrevButton = findViewById(R.id.prev_lesson_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateBack(PREV);
                /*int numberpage = preferences.getInt("numberpage", 1);
                if (numberpage != 1) {
                    numberpage--;

                    SharedPreferences.Editor edit = preferences.edit();
                    edit.putInt("numberpage", numberpage);
                    edit.commit();
                    //preferences1 = PreferenceManager.getDefaultSharedPreferences(this);
                    //int numberpage = preferences1.getInt("numberpage", 1);
                    //numberpage--;
                    Intent intent = LessonActivity.newIntent(LessonQuestionActivity.this);
                    startActivity(intent);
                } else {
                    onBackPressed();
                }*/
            }
        });

    }


    void navigateBack(int answer){
        Intent returnIntent = new Intent();
        switch (answer){
            case NEXT:
                //returnIntent.putExtra("result",result);
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
        /*if(answer){
            Intent returnIntent = new Intent();
            //returnIntent.putExtra("result",result);
            setResult(LessonActivity.RESULT_OK,returnIntent);
            finish();
        }
        else{
            Intent returnIntent = new Intent();
            //returnIntent.putExtra("result",result);
            setResult(LessonActivity.RESULT_CANCELED,returnIntent);
            finish();
        }*/
    }

    //method to compute the choice of the user
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
        //little message indicating if it's correct or not
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        navigateBack(correct);

    }



}
