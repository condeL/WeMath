package com.welearn.wemath.quizzes;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.welearn.wemath.R;


public class QuizTakingMenuFragment extends Fragment {

    private CardView mAdminButton;
    private CardView mUserButton;
    //private CardView mResultsButton;

    public QuizTakingMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_quiz_taking_menu, container, false);

        mAdminButton = root.findViewById(R.id.quiz_taking_admin_quiz_card);
        mUserButton = root.findViewById(R.id.quiz_taking_user_quiz_card);
        //mResultsButton = root.findViewById(R.id.quiz_taking_past_results_card);

        mAdminButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_quizTakingMenuFragment_to_adminQuizSelection, null));
        mUserButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_quizTakingMenuFragment_to_userQuizSelection, null));

        return root;
    }

}
