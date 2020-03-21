package com.welearn.wemath.quizzes;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.welearn.wemath.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserQuizCreationQuestionFragment extends Fragment {

    private Button mSubmitButton;
    private RecyclerView mQuestions;

    public UserQuizCreationQuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user_quiz_creation_question, container, false);

        mQuestions = root.findViewById(R.id.user_quiz_creation_question_recyclerView);

        return root;
    }

}
