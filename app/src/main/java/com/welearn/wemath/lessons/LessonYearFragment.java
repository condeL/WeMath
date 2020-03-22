package com.welearn.wemath.lessons;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.welearn.wemath.R;

public class LessonYearFragment extends Fragment {

    private LessonYearViewModel mViewModel;
    private ImageButton mYear1, mYear2, mYear3;
    private TextView mYear1Text, mYear2Text, mYear3Text;

    public static LessonYearFragment newInstance() {
        return new LessonYearFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.lesson_year_fragment, container, false);

        //retrieve the section from the bundle
        final String section = LessonYearFragmentArgs.fromBundle(getArguments()).getSection();

        //set the actionbar title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(section.toUpperCase());

        mYear1 = root.findViewById(R.id.year1_button);
        mYear2 = root.findViewById(R.id.year2_button);
        mYear3 = root.findViewById(R.id.year3_button);

        mYear1Text = root.findViewById(R.id.year1_text);
        mYear2Text = root.findViewById(R.id.year2_text);
        mYear3Text = root.findViewById(R.id.year3_text);

        /*Setting the behaviours of the buttons*/

        mYear1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //parameters to be passed to the topics fragment
                String year = "1";

                //set up the navigation action with the parameters
                NavDirections action = LessonYearFragmentDirections.actionLessonYearFragmentToLessonTopicFragment(year, section);
                Navigation.findNavController(v).navigate(action);

            }
        });

        mYear1Text.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //parameters to be passed to the topics fragment
                String year = "1";

                //set up the navigation action with the parameters
                NavDirections action = LessonYearFragmentDirections.actionLessonYearFragmentToLessonTopicFragment(year, section);
                Navigation.findNavController(v).navigate(action);

            }
        });

        mYear2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String year = "2";
                //String section = LessonYearFragmentArgs.fromBundle(getArguments()).getSection();
                NavDirections action = LessonYearFragmentDirections.actionLessonYearFragmentToLessonTopicFragment(year, section);
                Navigation.findNavController(v).navigate(action);

            }
        });

        mYear2Text.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String year = "2";
                //String section = LessonYearFragmentArgs.fromBundle(getArguments()).getSection();
                NavDirections action = LessonYearFragmentDirections.actionLessonYearFragmentToLessonTopicFragment(year, section);
                Navigation.findNavController(v).navigate(action);

            }
        });

        mYear3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String year = "3";
                //String section = LessonYearFragmentArgs.fromBundle(getArguments()).getSection();
                NavDirections action = LessonYearFragmentDirections.actionLessonYearFragmentToLessonTopicFragment(year, section);
                Navigation.findNavController(v).navigate(action);

            }
        });

        mYear3Text.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String year = "3";
                //String section = LessonYearFragmentArgs.fromBundle(getArguments()).getSection();
                NavDirections action = LessonYearFragmentDirections.actionLessonYearFragmentToLessonTopicFragment(year, section);
                Navigation.findNavController(v).navigate(action);

            }
        });
        return root;
    }

    //not used yet
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LessonYearViewModel.class);
        // TODO: Use the ViewModel
    }

}
