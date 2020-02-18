package com.welearn.wemath.quizzes;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.welearn.wemath.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminQuizSelectionFragment extends Fragment {


    public AdminQuizSelectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_quiz_selection, container, false);
    }

}
