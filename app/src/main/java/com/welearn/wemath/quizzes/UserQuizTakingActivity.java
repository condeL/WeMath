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

    //references to the various widgets on the activity
    private RadioGroup mRadioGroup;
    private Button mNextButton;
    private Button mPrevButton;
    private TextView mQuestionTextView;
    private Button[] mAnswers; //generic button to hold the radio or checkboxes


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_quiz_taking);

        mDB = FirebaseFirestore.getInstance();

        //retrieve the section from the bundle
        Intent intent = this.getIntent();
        String quiz_id = intent.getStringExtra("quiz_id");
        mQuestionTextView = findViewById(R.id.user_quiz_taking_problem); //link the reference to the actual widget item


        DocumentReference docRef = mDB.collection("quiz").document(quiz_id);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("GETTING", document.getId() + " => " + document.getData());
                                Toast.makeText(getBaseContext(),"YATTTA!", Toast.LENGTH_LONG).show();

                                //mQuizQuestions = ((ArrayList<QuizQuestion>) document.get("quizQuestions"));
                                mUserQuiz = document.toObject(UserQuiz.class);
                                setup(mUserQuiz);

                            } else {
                                Log.d("GETTING", "No such document");
                            }

                        } else {
                            Log.d("GETTING", "Error getting documents: ", task.getException());
                            Toast.makeText(getBaseContext(),"Fetching failed", Toast.LENGTH_LONG).show();
                        }

                    }
                });


    }

    void setup(UserQuiz userQuiz){
        mQuestionTextView.setText(mUserQuiz.getQuizQuestions().get(0).getProblem());

    }
}
