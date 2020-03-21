package com.welearn.wemath.quizzes;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.welearn.wemath.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserQuizCreationFragment extends Fragment {

    private Button mNextButton;

    public UserQuizCreationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user_quiz_creation, container, false);

        mNextButton = root.findViewById(R.id.user_quiz_creation_next_button);
        mNextButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_userQuizCreationFragment_to_userQuizCreationQuestionFragment, null));

        return root;
    }

}
