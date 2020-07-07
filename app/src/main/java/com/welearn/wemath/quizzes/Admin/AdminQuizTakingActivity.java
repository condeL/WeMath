package com.welearn.wemath.quizzes.Admin;

/*activity for taking local quizzes stored in JSON format*/

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.welearn.wemath.quizzes.QuizQuestion;
import com.welearn.wemath.quizzes.QuizResultActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AdminQuizTakingActivity extends AppCompatActivity {

    private ArrayList<QuizQuestion> mQuizQuestions;
    //private AdminQuiz mAdminQuiz;
    private int mCurrentIndex;
    private boolean[] mResults; //to store the scores
    private boolean[][] mMemory; //to remember the answers the user has selected

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

        Intent intent = this.getIntent();
        int quiz_id = intent.getIntExtra("quiz_id",0);

        mQuestionTextView = findViewById(R.id.admin_quiz_taking_problem);
        mRadioGroup = findViewById(R.id.admin_quiz_taking_radio_group);
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
            for(int i=0;i<10;i++){
                explanation[i] = mQuizQuestions.get(i).getExplanation();
            }
            intent1.putExtra("explanations", explanation);
            startActivity(intent1);
            finish();
        });

    }

    //get the quizquestions from the JSON file
    void getJSONQuiz(int id){
        try {
            JSONObject quiz = new JSONObject(loadJSONFromAsset(getBaseContext(),id));
            JSONArray questions = quiz.getJSONArray("questions");

            for (int i = 0; i < questions.length(); i++) {
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

    //sets up the answer choices based on the current index
    void setup(ArrayList<QuizQuestion> questions){
        //setting up the answer choices
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
        //little message indicating if it's correct or not for debugging purposes
        //Toast.makeText(this, String.valueOf(result), Toast.LENGTH_SHORT).show();
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

    //prevents the user from exiting the quiz
    @Override
    public void onBackPressed() {
    }
}
