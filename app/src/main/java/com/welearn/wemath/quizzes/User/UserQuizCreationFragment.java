package com.welearn.wemath.quizzes.User;

/*fragment for selecting the title, section and school year when creating a quiz*/


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.welearn.wemath.R;


public class UserQuizCreationFragment extends Fragment {

    private EditText mTitle;
    private RadioGroup mSection, mYear;
    //private CheckBox[] mTopics;
    private Button mNextButton;

    public UserQuizCreationFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_user_quiz_creation, container, false);


        mTitle = root.findViewById(R.id.user_quiz_creation_editname);
        mSection = root.findViewById(R.id.user_quiz_creation_sectionGroup);
        mYear = root.findViewById(R.id.user_quiz_creation_yearGroup);
        /*mTopics = new CheckBox[5];

        mTopics[0] = root.findViewById(R.id.user_quiz_creation_topic1_button);
        mTopics[1] = root.findViewById(R.id.user_quiz_creation_topic2_button);
        mTopics[2] = root.findViewById(R.id.user_quiz_creation_topic3_button);
        mTopics[3] = root.findViewById(R.id.user_quiz_creation_topic4_button);
        mTopics[4] = root.findViewById(R.id.user_quiz_creation_topic5_button);*/


        mNextButton = root.findViewById(R.id.user_quiz_creation_next_button);
        mNextButton.setOnClickListener((View.OnClickListener) v -> {
            if (mTitle.getText().toString().trim().length() != 0) {
                String title = mTitle.getText().toString();

                int selectedSectionID = mSection.getCheckedRadioButtonId();
                RadioButton selectedSectionRadio = (RadioButton) mSection.findViewById(selectedSectionID);
                String section = selectedSectionRadio.getText().toString().toLowerCase();

                int selectedYearID = mYear.getCheckedRadioButtonId();
                RadioButton selectedYearRadio = (RadioButton) mYear.findViewById(selectedYearID);
                String year = String.valueOf(selectedYearRadio.getText().toString().charAt(5));

                String[] topics = new String[5];
            /*for(int i=0; i<5;i++){
                if(mTopics[i].isChecked()){
                    topics[i] = mTopics[i].getText().toString();
                }
            }*/

                //set up the navigation action with the parameters
                NavDirections action = UserQuizCreationFragmentDirections.actionUserQuizCreationFragmentToUserQuizCreationQuestionFragment(title, section, year, topics);
                Navigation.findNavController(v).navigate(action);

            }else {
                Toast.makeText(getContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
                }
        });

        return root;
    }

}
