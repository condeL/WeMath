package com.welearn.wemath.quizzes;

import android.content.Context;
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

import androidx.appcompat.app.AppCompatActivity;

import com.welearn.wemath.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AdminQuizTakingActivity extends AppCompatActivity {

    private ArrayList<QuizQuestion> mQuizQuestions;
    private AdminQuiz mAdminQuiz;
    private int mCurrentIndex;
    private boolean[] mResults;
    private boolean[][] mMemory;

    //references to the various widgets on the activity
    private RadioGroup mRadioGroup;
    private ImageView mNextButton;
    private ImageView mPrevButton;
    private Button mFinishButton;
    private TextView mQuestionTextView, mQuestionNumber, mTimer;
    private CheckBox[] mAnswersChoice; //button to hold the radio or checkboxes
    private RadioButton[] mAnswersRadio; //=button to hold the radio or checkboxes



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_quiz_taking);
        mCurrentIndex = 0;
        mResults = new boolean[10];
        mMemory = new boolean[10][4];

        //retrieve the section from the bundle
        Intent intent = this.getIntent();
        int quiz_id = intent.getIntExtra("quiz_id",0);

        mQuestionTextView = findViewById(R.id.admin_quiz_taking_problem); //link the reference to the actual widget item
        mRadioGroup = findViewById(R.id.admin_quiz_taking_radio_group); //link the reference to the radiogroup widget
        mNextButton = findViewById(R.id.admin_quiz_taking_next_question);
        mPrevButton = findViewById(R.id.admin_quiz_taking_previous_question);
        mFinishButton = findViewById(R.id.admin_quiz_taking_finish_button);
        mQuestionNumber = findViewById(R.id.admin_quiz_taking_question_number);
        mTimer = findViewById(R.id.admin_quiz_taking_timer);

        mAnswersRadio = new RadioButton[4];
        mAnswersChoice = new CheckBox[4];
        for(int i = 0; i<4;i++) {
            mAnswersRadio[i] = (RadioButton) mRadioGroup.getChildAt(i);
            mAnswersChoice[i] = (CheckBox)mRadioGroup.getChildAt(i+4);
        }

        mQuizQuestions = new ArrayList<>();
        getJSONQuiz(quiz_id);
       
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mRadioGroup.getCheckedRadioButtonId()!=-1 || checkChecked()) {
                    updateAswer();
                }
                if (mCurrentIndex >= 0) {
                    mCurrentIndex = Math.abs((mCurrentIndex + 1) % 10);
                    setup(mQuizQuestions);
                } else {
                    mCurrentIndex = Math.abs((mCurrentIndex + 1) % 10);
                    setup(mQuizQuestions);
                }

                /*}
                else
                    Toast.makeText(getBaseContext(), "Please select an answer", Toast.LENGTH_SHORT).show();*/
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mRadioGroup.getCheckedRadioButtonId()!=-1 || checkChecked()) {

                    updateAswer();
                }
                if (mCurrentIndex == 0) {
                    mCurrentIndex = 9;
                    setup(mQuizQuestions);
                } else {
                    mCurrentIndex = Math.abs((mCurrentIndex - 1) % 10);
                    setup(mQuizQuestions);
                }
                /*}
                else
                    Toast.makeText(getBaseContext(), "Please select an answer", Toast.LENGTH_SHORT).show();*/

            }
        });

        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mRadioGroup.getCheckedRadioButtonId()!=-1 || checkChecked()) {

                    updateAswer();
                }

                Intent intent = new Intent(getApplicationContext(), QuizResultActivity.class);
                intent.putExtra("results", mResults);
                String[] explanation = new String[10];
                for(int i=0;i<10;i++){
                    explanation[i] = mQuizQuestions.get(i).getExplanation();
                }
                intent.putExtra("explanations", explanation);
                startActivity(intent);
                finish();

            }
        });

    }


    void getJSONQuiz(int id){
        try {
            // get JSONObject from JSON file
            JSONObject quiz = new JSONObject(loadJSONFromAsset(getBaseContext(),id));
            // fetch JSONArray named users
            JSONArray questions = quiz.getJSONArray("questions");
            // implement for loop for getting users list data
            for (int i = 0; i < questions.length(); i++) {
                // create a JSONObject for fetching single user data
                JSONObject question = questions.getJSONObject(i);

                String problem, explanation;
                problem = question.getString("problem");
                explanation = question.getString("explanation");

                boolean multiple = question.getBoolean("multiple");

                List<Pair<String, Boolean>> answers = new ArrayList<>();

                JSONArray choices = question.getJSONArray("choices");
                for(int y = 0; y<choices.length();y++) {
                    JSONObject choice = choices.getJSONObject(y);
                    String label = choice.getString("label");
                    boolean truth = choice.getBoolean("truth");
                    answers.add(new Pair<>(label,truth));
                }

                QuizQuestion quizQuestion = new QuizQuestion(problem,answers,multiple,explanation);
                mQuizQuestions.add(quizQuestion);

            }
            setup(mQuizQuestions);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String loadJSONFromAsset(Context context, int id) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("adminquiz"+id+".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }



    void setup(ArrayList<QuizQuestion> questions){
        //setting up the answer choices
        QuizQuestion currentQuestion = questions.get(mCurrentIndex);
        mQuestionTextView.setText(currentQuestion.getProblem());
        mQuestionNumber.setText("Question " + (mCurrentIndex+1) + "/10");

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

    //method to compute the choice of the user
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
        //little message indicating if it's correct or not
        Toast.makeText(this, String.valueOf(result), Toast.LENGTH_SHORT).show();
        mResults[mCurrentIndex] = result;

    }

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

    @Override
    public void onBackPressed() {
    }
}
