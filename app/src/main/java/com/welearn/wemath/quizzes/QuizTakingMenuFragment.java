package com.welearn.wemath.quizzes;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.welearn.wemath.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizTakingMenuFragment extends Fragment {

    private ImageButton mAdminButton;
    private ImageButton mUserButton;
    //private ImageButton mResultsButton;

    public QuizTakingMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_quiz_taking_menu, container, false);

        mAdminButton = root.findViewById(R.id.quiz_taking_admin_quiz_image);
        mUserButton = root.findViewById(R.id.quiz_taking_user_quiz_image);
        //mResultsButton = root.findViewById(R.id.quiz_taking_past_reults_image);

        mAdminButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_quizTakingMenuFragment_to_adminQuizSelection, null));
        mUserButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_quizTakingMenuFragment_to_userQuizSelection, null));

        return root;
    }

}
