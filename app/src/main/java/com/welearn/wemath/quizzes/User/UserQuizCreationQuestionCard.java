package com.welearn.wemath.quizzes.User;

/*fragment for the quiz creation viewpager*/

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.welearn.wemath.R;


public class UserQuizCreationQuestionCard extends Fragment {
    public int mPosition;
    public TextView mNumber;
    public EditText mProblem, mExplanation;
    public EditText[] mAnswer;
    public CheckBox mMultiple;
    public CheckBox[] mTruthCheckboxes;
    public RadioButton[] mTruthRadios;
    public RadioGroup mTruthRadioGroup;
    public LinearLayout mTruthCheckBoxGroup;

    public UserQuizCreationQuestionCard() {
    }


   public static UserQuizCreationQuestionCard newInstance(int position) {
        UserQuizCreationQuestionCard fragment = new UserQuizCreationQuestionCard();
        Bundle args = new Bundle(position);
        args.putInt("Position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.user_quiz_creation_question_card, container, false);

        mPosition = getArguments().getInt("Position");

        mNumber = root.findViewById(R.id.user_quiz_creation_card_number);
        mProblem = root.findViewById(R.id.user_quiz_creation_card_problem);
        mMultiple = root.findViewById(R.id.user_quiz_creation_card_multipleChoiceBox);

        mNumber.setText("Question " +(mPosition+1));

        mAnswer = new EditText[4];
        mAnswer[0] = root.findViewById(R.id.user_quiz_creation_card_answer1);
        mAnswer[1] = root.findViewById(R.id.user_quiz_creation_card_answer2);
        mAnswer[2] = root.findViewById(R.id.user_quiz_creation_card_answer3);
        mAnswer[3] = root.findViewById(R.id.user_quiz_creation_card_answer4);

        mTruthCheckboxes = new CheckBox[4];
        mTruthCheckboxes[0] = root.findViewById(R.id.user_quiz_creation_card_checkbox1);
        mTruthCheckboxes[1] = root.findViewById(R.id.user_quiz_creation_card_checkbox2);
        mTruthCheckboxes[2] = root.findViewById(R.id.user_quiz_creation_card_checkbox3);
        mTruthCheckboxes[3] = root.findViewById(R.id.user_quiz_creation_card_checkbox4);

        mTruthRadios = new RadioButton[4];
        mTruthRadios[0] = root.findViewById(R.id.user_quiz_creation_card_radioButton1);
        mTruthRadios[1] = root.findViewById(R.id.user_quiz_creation_card_radioButton2);
        mTruthRadios[2] = root.findViewById(R.id.user_quiz_creation_card_radioButton3);
        mTruthRadios[3] = root.findViewById(R.id.user_quiz_creation_card_radioButton4);


        mTruthRadioGroup = root.findViewById(R.id.user_quiz_creation_card_radioGroup);
        mTruthCheckBoxGroup = root.findViewById(R.id.user_quiz_creation_card_checkboxGroup);
        mExplanation = root.findViewById(R.id.user_quiz_creation_card_explanation);

        mMultiple.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isChecked()) {
                mTruthRadioGroup.setVisibility(View.GONE);
                mTruthCheckBoxGroup.setVisibility(View.VISIBLE);
            }
            else
            {
                mTruthCheckBoxGroup.setVisibility(View.GONE);
                mTruthRadioGroup.setVisibility(View.VISIBLE);
            }
        }
        );
        return root;
    }
}