package com.welearn.wemath.quizzes.User;

/*fragment for creating quiz questions
* Makes use of a viewpager of QuizCreationCards*/


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

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
        viewPager.setOffscreenPageLimit(10);
        viewPager.setAdapter(pagerAdapter);


        TabLayout tabLayout = root.findViewById(R.id.user_suiz_creation_question_tabs);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText("Question " + (position + 1))).attach();

        mDifficultyGroup = root.findViewById(R.id.user_quiz_creation_question_difficulty_radioGroup);

        mSubmitButton = root.findViewById(R.id.user_quiz_creation_question_submit_button);

        mSubmitButton.setOnClickListener(v -> {
            Toast.makeText(getContext(),"Sending quiz...", Toast.LENGTH_LONG).show();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            if(pagerAdapter.submit()) {
                //block user until quiz is sent
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Loading...");
                progressDialog.setTitle("Sending quiz");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();

                List<QuizQuestion> quizQuestion = pagerAdapter.mQuizQuestions;
                UserQuiz quiz = new UserQuiz(user.getUid(), user.getDisplayName(), mTitle, mSection, mYear, Arrays.asList(mTopics), getDifficulty(), quizQuestion);

                db.collection("quiz").document(quiz.getTitle())
                        .set(quiz)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("success", "DocumentSnapshot successfully written!");
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Quiz sent!", Toast.LENGTH_LONG).show();
                            NavDirections action = UserQuizCreationQuestionFragmentDirections.actionUserQuizCreationQuestionFragmentToNavigationQuizMain();
                            Navigation.findNavController(v).navigate(action);
                        })
                        .addOnFailureListener(e -> {
                            Log.w("failure", "Error writing document", e);
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), centeredToastMsg("Error sending quiz.\nPlease check your connection and try again"), Toast.LENGTH_LONG).show();
                        });
            }
        });
        return root;
    }


    public String getDifficulty(){
        int selectedID = mDifficultyGroup.getCheckedRadioButtonId();
        RadioButton selectedSectionRadio = (mDifficultyGroup.findViewById(selectedID));
        return selectedSectionRadio.getText().toString();
    }

    private Spannable centeredToastMsg(String text){
        Spannable centeredText = new SpannableString(text);
        centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                0, text.length() - 1,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return centeredText;
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

       public boolean submit(){
            try {
                for (int i = 0; i < 10; i++) {
                    String problem ="";
                    if (getCard(i).mProblem.getText().toString().trim().length() !=0) { //no empty problem statements
                        problem = getCard(i).mProblem.getText().toString();
                    } else {
                        Toast.makeText(getContext(),centeredToastMsg("Question statements must be provided for each questions"), Toast.LENGTH_LONG).show();
                        mQuizQuestions.clear();
                        return false;
                    }

                    boolean multipleChoice = getCard(i).mMultiple.isChecked();

                    List<Pair<String, Boolean>> answers = new ArrayList<>();

                    if (multipleChoice) {
                        for (int y = 0; y < 4; y++) {
                            if (getCard(i).mAnswer[y].getText().toString().trim().length() != 0) {
                                answers.add(new Pair<>(getCard(i).mAnswer[y].getText().toString(), getCard(i).mTruthCheckboxes[y].isChecked()));
                            } else {
                                if (y < 2) { //at least 2 answers are needed
                                    Toast.makeText(getContext(),centeredToastMsg("At least 2 answers must be provided for each questions"), Toast.LENGTH_LONG).show();
                                    mQuizQuestions.clear();
                                    return false;
                                }
                            }
                        }
                    } else {
                        for (int y = 0; y < 4; y++) {
                            if (getCard(i).mAnswer[y].getText().toString().trim().length() != 0) {
                                answers.add(new Pair<>(getCard(i).mAnswer[y].getText().toString(), getCard(i).mTruthRadios[y].isChecked()));
                            }else {
                                if (y < 2) { //at least 2 answers are needed
                                    Toast.makeText(getContext(),centeredToastMsg("At least 2 answers must be provided for each questions"), Toast.LENGTH_LONG).show();
                                    mQuizQuestions.clear();
                                    return false;
                                }
                            }
                        }
                    }

                    for(int z = answers.size()-1; z>=0; z--){ //at least 1 answer must be true
                        if(answers.get(z).second == true) {
                            break;
                        } else{
                            if(z ==0){
                                Toast.makeText(getContext(),centeredToastMsg("At least 1 correct answer must be provided for each questions"), Toast.LENGTH_LONG).show();
                                mQuizQuestions.clear();
                                return false;
                            }
                        }
                    }
                    String explanation;
                    if (!getCard(i).mExplanation.getText().toString().isEmpty())
                        explanation = getCard(i).mExplanation.getText().toString();
                    else
                        explanation = " ";
                    mQuizQuestions.add(i, new QuizQuestion(problem, answers, multipleChoice, explanation));
                }

            }catch (IndexOutOfBoundsException e){
                Toast.makeText(getContext(),"Please create 10 questions", Toast.LENGTH_LONG).show();
                mQuizQuestions.clear();
                return false;
            }catch (Exception e){
                Toast.makeText(getContext(),centeredToastMsg("Error sending quiz.\nPlease review and try again"), Toast.LENGTH_LONG).show();
                mQuizQuestions.clear();
                return false;
            }
           return true;
       }
    }
}
