package com.welearn.wemath.quizzes.User;

/*activity for taking online quizzes stored in Firebase*/


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.welearn.wemath.R;
import com.welearn.wemath.quizzes.QuizQuestion;
import com.welearn.wemath.quizzes.QuizResultActivity;

import java.util.ArrayList;

public class UserQuizTakingActivity extends AppCompatActivity {

    FirebaseFirestore mDB;

    private ArrayList<QuizQuestion> mQuizQuestions;
    private UserQuiz mUserQuiz;
    private int mCurrentIndex;
    private boolean[] mResults;  //to store the scores
    private boolean[][] mMemory; //to remember the answers the user has selected

    private RadioGroup mRadioGroup;
    private ImageView mNextButton;
    private ImageView mPrevButton;
    private Button mFinishButton;
    private TextView mQuestionTextView, mQuestionNumber;
    //private TextView mTimer;
    private CheckBox[] mAnswersChoice; //button to hold the radio or checkboxes
    private RadioButton[] mAnswersRadio; //button to hold the radio or checkboxes



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_quiz_taking);

        mCurrentIndex = 0;
        mResults = new boolean[10];
        mMemory = new boolean[10][4];
        mDB = FirebaseFirestore.getInstance();

        Intent intent = this.getIntent();
        String quiz_id = intent.getStringExtra("quiz_id");

        mQuestionTextView = findViewById(R.id.user_quiz_taking_problem);
        mRadioGroup = findViewById(R.id.user_quiz_taking_radio_group);
        mNextButton = findViewById(R.id.user_quiz_taking_next_question);
        mPrevButton = findViewById(R.id.user_quiz_taking_previous_question);
        mFinishButton = findViewById(R.id.user_quiz_taking_finish_button);
        mQuestionNumber = findViewById(R.id.user_quiz_taking_question_number);
        //mTimer = findViewById(R.id.user_quiz_taking_timer);

        mAnswersRadio = new RadioButton[4];
        mAnswersChoice = new CheckBox[4];
        for(int i = 0; i<4;i++) {
            mAnswersRadio[i] = (RadioButton) mRadioGroup.getChildAt(i);
            mAnswersChoice[i] = (CheckBox)mRadioGroup.getChildAt(i+4);
        }

        DocumentReference docRef = mDB.collection("quiz").document(quiz_id);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("GETTING", document.getId() + " => " + document.getData());
                                Toast.makeText(getBaseContext(),"Good luck!", Toast.LENGTH_LONG).show();
                                mUserQuiz = document.toObject(UserQuiz.class);
                                mQuizQuestions = (ArrayList)mUserQuiz.getQuizQuestions();
                                setup(mQuizQuestions);
                                //mTimer.setText("0:00");
                            } else {
                                Log.d("GETTING", "No such document");
                            }

                        } else {
                            Log.d("GETTING", "Error getting documents: ", task.getException());
                            Toast.makeText(getBaseContext(),"Fetching failed", Toast.LENGTH_LONG).show();
                        }

                    }
                });

        mNextButton.setOnClickListener(v -> {
            //check if an answer is selected
            if (mRadioGroup.getCheckedRadioButtonId()!=-1 || checkChecked()) {
                updateAswer();
            }
            mCurrentIndex = Math.abs((mCurrentIndex + 1) % 10);
            setup(mQuizQuestions);
        });

        mPrevButton.setOnClickListener(v -> {
            if (mRadioGroup.getCheckedRadioButtonId()!=-1 || checkChecked()) {
                updateAswer();
            }
                if (mCurrentIndex == 0) {
                    mCurrentIndex = 9;
                } else {
                    mCurrentIndex = Math.abs((mCurrentIndex - 1) % 10);
                }
            setup(mQuizQuestions);
        });

        mFinishButton.setOnClickListener(v -> {

            if (mRadioGroup.getCheckedRadioButtonId()!=-1 || checkChecked()) {
                updateAswer();
            }

            Intent intent1 = new Intent(getApplicationContext(), QuizResultActivity.class);
            intent1.putExtra("results", mResults);
            String[] explanation = new String[10];
            for(int i=0;i<mQuizQuestions.size();i++){
                explanation[i] = mQuizQuestions.get(i).getExplanation();
            }
            intent1.putExtra("explanations", explanation);
            startActivity(intent1);
            finish();
        });

    }

    //sets up the answer choices based on the current index
    void setup(ArrayList<QuizQuestion> questions){
        QuizQuestion currentQuestion = questions.get(mCurrentIndex);
        mQuestionTextView.setText(currentQuestion.getProblem());
        mQuestionNumber.setText("Question " + (mCurrentIndex+1) + "/10");

        for(int i = 0; i<4; i++){
            mAnswersChoice[i].setVisibility(View.GONE);
            mAnswersRadio[i].setVisibility(View.GONE);
        }

        if(!currentQuestion.isMultipleChoice()) {
            for(int i = 0; i<currentQuestion.getChoices().size();i++){
                mAnswersChoice[i].setVisibility(View.GONE);
                mAnswersRadio[i].setVisibility(View.VISIBLE);
                mAnswersRadio[i].setText(currentQuestion.getChoices().get(i));
                mAnswersRadio[i].setChecked(mMemory[mCurrentIndex][i]);
            }

        }
        else{
            for(int i = 0; i<currentQuestion.getChoices().size();i++){
                mAnswersRadio[i].setVisibility(View.GONE);
                mAnswersChoice[i].setVisibility(View.VISIBLE);
                mAnswersChoice[i].setText(currentQuestion.getChoices().get(i));
                mAnswersChoice[i].setChecked(mMemory[mCurrentIndex][i]);
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

    //method to compute the answer of the user
    private void checkAnswer(int answerNumber, QuizQuestion question){
        boolean result;

        if(!question.isMultipleChoice()) {
            if (question.getTruths().get(answerNumber)) {
                result = true;
            } else {
                result = false;
            }

            for(int i=0;i<4;i++){
                mMemory[mCurrentIndex][i] = mAnswersRadio[i].isChecked();
            }
            mRadioGroup.clearCheck();

        } else{
            int score = 0;
            for(int i = 0; i<4;i++){
                if(((mAnswersChoice[i]).isChecked())){
                    if (question.getTruths().get(i)){
                        score++;
                    }
                    else{
                        score=-1;
                        break;
                    }
                }
            }
            if(score == question.getNbCorrectAnswers()){
                result = true;
            } else {
                result = false;
            }
            for(int i=0;i<4;i++){
                mMemory[mCurrentIndex][i] = mAnswersChoice[i].isChecked();
            }
            clearChecked();
        }
        //little message indicating if it's correct or not for debugging purposes
        //Toast.makeText(this, String.valueOf(result), Toast.LENGTH_SHORT).show();
        mResults[mCurrentIndex] = result;
    }

    //to check if a multiple choice answer is checked
    private boolean checkChecked(){
        for(int i = 0; i<4;i++) {
            if(mAnswersChoice[i].isChecked()){
                return true;
            }
        }
        return false;
    }

    private void clearChecked(){
        for(int i = 0; i<4;i++) {
            mAnswersChoice[i].setChecked(false);
            }
    }

    //prevents the user from exiting the quiz
    @Override
    public void onBackPressed() {
    }
}
