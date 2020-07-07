package com.welearn.wemath.quizzes.User;

/*fragment for creating quiz questions
* Makes use of a viewpager of QuizCreationCards*/


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.welearn.wemath.R;
import com.welearn.wemath.quizzes.QuizQuestion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UserQuizCreationQuestionFragment extends Fragment {

    private Button mSubmitButton;
    private ViewPager2 viewPager;
    private ScreenSlidePagerAdapter pagerAdapter;

    private RadioGroup mDifficultyGroup;

    private String mTitle, mSection, mYear;
    private String[] mTopics;

    public UserQuizCreationQuestionFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_user_quiz_creation_question, container, false);

        mTitle = UserQuizCreationQuestionFragmentArgs.fromBundle(getArguments()).getTitle();
        mSection = UserQuizCreationQuestionFragmentArgs.fromBundle(getArguments()).getSection();
        mYear = UserQuizCreationQuestionFragmentArgs.fromBundle(getArguments()).getYear();
        mTopics = UserQuizCreationQuestionFragmentArgs.fromBundle(getArguments()).getTopics();

        viewPager = root.findViewById(R.id.user_quiz_creation_question_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(this, getContext());
        viewPager.setAdapter(pagerAdapter);


        TabLayout tabLayout = root.findViewById(R.id.user_suiz_creation_question_tabs);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText("Question " + (position + 1))).attach();

        mDifficultyGroup = root.findViewById(R.id.user_quiz_creation_question_difficulty_radioGroup);

        mSubmitButton = root.findViewById(R.id.user_quiz_creation_question_submit_button);

        mSubmitButton.setOnClickListener(v -> {
            Toast.makeText(getContext(),"Sending quiz...!", Toast.LENGTH_LONG).show();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            UserQuiz quiz = new UserQuiz(user.getUid(), user.getDisplayName(),mTitle,mSection,mYear,Arrays.asList(mTopics),getDifficulty(), pagerAdapter.submit());

            db.collection("quiz").document(quiz.getTitle())
                    .set(quiz)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("success", "DocumentSnapshot successfully written!");
                        Toast.makeText(getContext(),"Quiz sent!", Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e -> {
                        Log.w("failure", "Error writing document", e);
                        Toast.makeText(getContext(),"Error sending comment", Toast.LENGTH_LONG).show();

                    });
        });
        return root;
    }


    public String getDifficulty(){
        int selectedID = mDifficultyGroup.getCheckedRadioButtonId();
        RadioButton selectedSectionRadio = (mDifficultyGroup.findViewById(selectedID));
        return selectedSectionRadio.getText().toString();
    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {

       private Context mContext;
       ArrayList<QuizQuestion> mQuizQuestions;
       private List<UserQuizCreationQuestionCard> card_list = new ArrayList<>();

        public ScreenSlidePagerAdapter(Fragment fr, Context context) {
            super(fr);
            mContext = context;
            mQuizQuestions = new ArrayList<>();
        }

        @Override
        public Fragment createFragment(int position) {
            UserQuizCreationQuestionCard questionCard =  UserQuizCreationQuestionCard.newInstance(position);
            card_list.add(questionCard);
            return questionCard;
        }

        @Override
        public int getItemCount() {
            return 10;
        }

       public UserQuizCreationQuestionCard getCard(int position){
           return card_list.get(position);
       }

       public List<QuizQuestion> submit(){
           for(int i = 0; i<10;i++){
               String problem = getCard(i).mProblem.getText().toString();

               boolean multipleChoice = getCard(i).mMultiple.isChecked();

               List<Pair<String, Boolean>> answers = new ArrayList<>();

               if(multipleChoice) {
                   for (int y = 0; y < 4; y++) {
                       answers.add(new Pair<>(getCard(i).mAnswer[y].getText().toString(), getCard(i).mTruthCheckboxes[y].isChecked()));
                   }
               } else{
                   for (int y = 0; y < 4; y++) {
                       int selectedID = getCard(i).mTruthRadioGroup.getCheckedRadioButtonId();
                       RadioButton selectedSectionRadio = (getCard(i).mTruthRadioGroup.findViewById(selectedID));
                       answers.add(new Pair<>(getCard(i).mAnswer[y].getText().toString(), getCard(i).mTruthRadios[y].isChecked()));
                   }
               }

               String explanation;
               if(!getCard(i).mExplanation.getText().toString().isEmpty())
                   explanation = getCard(i).mExplanation.getText().toString();
               else
                   explanation = " ";
               mQuizQuestions.add(new QuizQuestion(problem, answers, multipleChoice, explanation));
           }
           return mQuizQuestions;
       }
    }
}
