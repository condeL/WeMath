package com.welearn.wemath.quizzes;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.welearn.wemath.R;


public class AdminQuizSelectionFragment extends Fragment {


    public AdminQuizSelectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_admin_quiz_selection, container, false);
        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mSection.toUpperCase() + " Year " + mYear);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Admin Quiz");

        //get a reference to the recycler view and give it to the custom adapter
        RecyclerView view = root.findViewById(R.id.admin_quiz_list);
        //ContentAdapter adapter = new ContentAdapter(view.getContext(), mYear, mSection);
        //view.setAdapter(adapter);
        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(getActivity()));

        return root;
    }

    //View holder that will hold references to all the views in the RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, number, percentage;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.topic_card, parent, false));
        }
    }
}
