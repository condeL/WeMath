package com.welearn.wemath.quizzes;

/*fragment for selecting the quiz menu*/

import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.welearn.wemath.R;


public class QuizMainFragment extends Fragment {

    private CardView mAnswerButton;
    private CardView mCreateButton;


    public QuizMainFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_quiz_main, container, false);

        mAnswerButton = root.findViewById(R.id.quiz_answering_card);
        mCreateButton = root.findViewById(R.id.quiz_creation_card);

        mAnswerButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_quiz_main_to_quizTakingMenuFragment, null));
        mCreateButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_quiz_main_to_userQuizCreationFragment, null));

        return root;

    }
}
