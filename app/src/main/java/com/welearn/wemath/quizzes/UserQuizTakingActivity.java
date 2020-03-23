package com.welearn.wemath.quizzes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.welearn.wemath.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class UserQuizTakingActivity extends AppCompatActivity {

    //the LessonQuestion object
    private ArrayList<QuizQuestion> mQuizQuestions;
    private UserQuiz mUserQuiz;
    FirebaseFirestore mDB;
    private int mCurrentIndex;
    private boolean[] mResults;

    //references to the various widgets on the activity
    private RadioGroup mRadioGroup;
    private ImageView mNextButton;
    private ImageView mPrevButton;
    private Button mFinishButton;
    private TextView mQuestionTextView, mQuestionNumber, mTimer;
    private Button[] mAnswers; //generic button to hold the radio or checkboxes


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_quiz_taking);
        mCurrentIndex = 0;
        mResults = new boolean[10];
        mDB = FirebaseFirestore.getInstance();

        //retrieve the section from the bundle
        Intent intent = this.getIntent();
        String quiz_id = intent.getStringExtra("quiz_id");

        mQuestionTextView = findViewById(R.id.user_quiz_taking_problem); //link the reference to the actual widget item
        mRadioGroup = findViewById(R.id.user_quiz_taking_radio_group); //link the reference to the radiogroup widget
        mNextButton = findViewById(R.id.user_quiz_taking_next_question);
        mPrevButton = findViewById(R.id.user_quiz_taking_previous_question);
        mFinishButton = findViewById(R.id.user_quiz_taking_finish_button);
        mQuestionNumber = findViewById(R.id.user_quiz_taking_question_number);
        mTimer = findViewById(R.id.user_quiz_taking_timer);



        DocumentReference docRef = mDB.collection("quiz").document(quiz_id);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("GETTING", document.getId() + " => " + document.getData());
                                Toast.makeText(getBaseContext(),"Good luck!", Toast.LENGTH_LONG).show();

                                //mQuizQuestions = ((ArrayList<QuizQuestion>) document.get("quizQuestions"));
                                mUserQuiz = document.toObject(UserQuiz.class);
                                mQuizQuestions = (ArrayList)mUserQuiz.getQuizQuestions();
                                setup(mQuizQuestions);
                                mTimer.setText("0:00");

                            } else {
                                Log.d("GETTING", "No such document");
                            }

                        } else {
                            Log.d("GETTING", "Error getting documents: ", task.getException());
                            Toast.makeText(getBaseContext(),"Fetching failed", Toast.LENGTH_LONG).show();
                        }

                    }
                });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mRadioGroup.getCheckedRadioButtonId()!=-1) {
                    updateAswer();

                    if (mCurrentIndex >= 0) {
                        mCurrentIndex = Math.abs((mCurrentIndex + 1) % 10);
                        setup(mQuizQuestions);
                    } else {
                        mCurrentIndex = Math.abs((mCurrentIndex + 1) % 10);
                        setup(mQuizQuestions);
                    }

                }
                else
                    Toast.makeText(getBaseContext(), "Please select an answer", Toast.LENGTH_SHORT).show();
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mRadioGroup.getCheckedRadioButtonId()!=-1) {

                    updateAswer();

                    if (mCurrentIndex == 0) {
                        mCurrentIndex = 9;
                        setup(mQuizQuestions);
                    } else {
                        mCurrentIndex = Math.abs((mCurrentIndex - 1) % 10);
                        setup(mQuizQuestions);
                    }
                }
                else
                    Toast.makeText(getBaseContext(), "Please select an answer", Toast.LENGTH_SHORT).show();

            }
        });

        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

    }

    void setup(ArrayList<QuizQuestion> questions){
        //setting up the answer choices
        QuizQuestion currentQuestion = questions.get(mCurrentIndex);
        mQuestionTextView.setText(currentQuestion.getProblem());
        mQuestionNumber.setText("Question " + (mCurrentIndex+1) + "/10");

        if(!currentQuestion.isMultipleChoice()) {
            mAnswers = new RadioButton[4];
            for(int i = 0; i<currentQuestion.getChoices().size();i++){
                mAnswers[i] = (RadioButton)mRadioGroup.getChildAt(i);
                mAnswers[i].setText(currentQuestion.getChoices().get(i));
                mAnswers[i].setVisibility(View.VISIBLE);
            }

        }
        else{
            mAnswers = new CheckBox[4];
            for(int i = 0; i<currentQuestion.getChoices().size();i++){
                // mAnswers[i] = new CheckBox(this);
                mAnswers[i] = (CheckBox)mRadioGroup.getChildAt(i+4);
                mAnswers[i].setText(currentQuestion.getChoices().get(i));
                mAnswers[i].setVisibility(View.VISIBLE);

            }
        }

    }


    private void updateAswer(){
        int answer;
        QuizQuestion question = mQuizQuestions.get(mCurrentIndex);
        if(!question.isMultipleChoice()) {
            int selectedID = mRadioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = mRadioGroup.findViewById(selectedID);
            answer = mRadioGroup.indexOfChild(radioButton);
            checkAnswer(answer, question);
        } else{
            checkAnswer(0, question);
        }
    }

    //method to compute the choice of the user
    private void checkAnswer(int answerNumber, QuizQuestion question){

        boolean result;

        if(!question.isMultipleChoice()) {
            if (question.getTruths().get(answerNumber)) {
                result = true;
                mRadioGroup.clearCheck();
            } else {
                result = false;
                mRadioGroup.clearCheck();
            }
        } else{

            int score = 0;
            for(int i = 0; i<question.getAnswersSize();i++){
                if(((CheckBox)mAnswers[i]).isChecked()){
                    if (question.getTruths().get(i)){
                        score++;
                        ((CheckBox)mAnswers[i]).setChecked(false);
                    }
                    else{
                        score=-1;
                        ((CheckBox)mAnswers[0]).setChecked(false);
                        ((CheckBox)mAnswers[1]).setChecked(false);
                        ((CheckBox)mAnswers[2]).setChecked(false);
                        ((CheckBox)mAnswers[3]).setChecked(false);
                        break;
                    }
                }
            }
            if(score == question.getNbCorrectAnswers()){
                result = true;
            } else {
                result = false;
            }
        }
        //little message indicating if it's correct or not
        Toast.makeText(this, String.valueOf(result), Toast.LENGTH_SHORT).show();
        mResults[mCurrentIndex] = result;

    }
}
