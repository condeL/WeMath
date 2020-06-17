package com.welearn.wemath.quizzes;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.welearn.wemath.R;
import com.welearn.wemath.UserQuizCreationQuestionCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserQuizCreationQuestionFragment extends Fragment {

    private Button mSubmitButton;
    private RecyclerView mQuestions;
    private ViewPager2 viewPager;
    private ScreenSlidePagerAdapter pagerAdapter;

    private RadioGroup mDifficultyGroup;

    private String mTitle, mSection, mYear;
    private String[] mTopics;

    public UserQuizCreationQuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user_quiz_creation_question, container, false);

        mTitle = UserQuizCreationQuestionFragmentArgs.fromBundle(getArguments()).getTitle();
        mSection = UserQuizCreationQuestionFragmentArgs.fromBundle(getArguments()).getSection();
        mYear = UserQuizCreationQuestionFragmentArgs.fromBundle(getArguments()).getYear();
        mTopics = UserQuizCreationQuestionFragmentArgs.fromBundle(getArguments()).getTopics();

       // mQuestions = root.findViewById(R.id.user_quiz_creation_question_recyclerView);

        viewPager = root.findViewById(R.id.user_quiz_creation_question_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(this, getContext());
        viewPager.setAdapter(pagerAdapter);


        TabLayout tabLayout = root.findViewById(R.id.user_suiz_creation_question_tabs);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText("Question " + (position + 1))).attach();


       /* final ContentAdapter adapter = new ContentAdapter(getContext());
        mQuestions.setAdapter(adapter);
        mQuestions.setHasFixedSize(true);
        mQuestions.setLayoutManager(new LinearLayoutManager(getActivity()));*/

        mDifficultyGroup = root.findViewById(R.id.user_quiz_creation_question_difficulty_radioGroup);

        mSubmitButton = root.findViewById(R.id.user_quiz_creation_question_submit_button);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                Toast.makeText(getContext(),"Sending quiz...!", Toast.LENGTH_LONG).show();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                UserQuiz quiz = new UserQuiz(user.getUid(), user.getDisplayName(),mTitle,mSection,mYear,Arrays.asList(mTopics),getDifficulty(), pagerAdapter.submit(mQuestions));

                db.collection("quiz").document(quiz.getTitle())
                        .set(quiz)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("success", "DocumentSnapshot successfully written!");
                                Toast.makeText(getContext(),"Quiz sent!", Toast.LENGTH_LONG).show();
                                //editText.setText("");
                                //mAdapter = new CommentsFragment.ContentAdapterComments(getContext());
                                //mView.setAdapter(mAdapter);
                                //mAdapter.notifyDataSetChanged();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("failure", "Error writing document", e);
                                Toast.makeText(getContext(),"Error sending comment", Toast.LENGTH_LONG).show();

                            }
                        });
                //Toast.makeText(getContext(),quiz.toString(), Toast.LENGTH_LONG).show();


            }
        });
        return root;
    }


    public String getDifficulty(){
        int selectedID = mDifficultyGroup.getCheckedRadioButtonId();
        RadioButton selectedSectionRadio = (mDifficultyGroup.findViewById(selectedID));
        return selectedSectionRadio.getText().toString();
    }
/*
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView number;
        public EditText problem, explanation;
        public EditText[] answer;
        public CheckBox multiple;
        public CheckBox[] truthCheckboxes;
        public RadioButton[] truthRadios;
        public RadioGroup truthRadioGroup;
        public LinearLayout truthCheckBoxes;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.user_quiz_creation_question_card, parent, false));
            number = itemView.findViewById(R.id.user_quiz_creation_card_number);
            problem = itemView.findViewById(R.id.user_quiz_creation_card_problem);
            multiple = itemView.findViewById(R.id.user_quiz_creation_card_multipleChoiceBox);




        }
    }

    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder>{

        private Context mContext;
        ArrayList<QuizQuestion> mQuizQuestions;
        private List<ViewHolder> card_list = new ArrayList<>();

        public ContentAdapter(Context context){

            mContext = context;
            mQuizQuestions = new ArrayList<>();

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            card_list.add(holder);
            holder.number.setText("Problem " +(position+1));

            holder.multiple.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    if (buttonView.isChecked()) {
                        holder.truthRadioGroup.setVisibility(View.GONE);
                        holder.truthCheckBoxes.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        holder.truthCheckBoxes.setVisibility(View.GONE);
                        holder.truthRadioGroup.setVisibility(View.VISIBLE);
                    }
                }
            }
            );

        }

        @Override
        public int getItemCount() {
            return 10;
        }

        public ViewHolder getHolder(int position){
            return card_list.get(position);
        }

        public List<QuizQuestion> submit(RecyclerView r){
            for(int i = 0; i<10;i++){
                String problem = getHolder(i).problem.getText().toString();

                boolean multipleChoice = getHolder(i).multiple.isChecked();

                List<Pair<String, Boolean>> answers = new ArrayList<>();

                if(multipleChoice) {
                    for (int y = 0; y < 4; y++) {
                        answers.add(new Pair<>(getHolder(i).answer[y].getText().toString(), getHolder(i).truthCheckboxes[y].isChecked()));
                    }
                } else{
                    for (int y = 0; y < 4; y++) {
                        int selectedID = getHolder(i).truthRadioGroup.getCheckedRadioButtonId();
                        RadioButton selectedSectionRadio = (getHolder(i).truthRadioGroup.findViewById(selectedID));
                        answers.add(new Pair<>(getHolder(i).answer[y].getText().toString(), getHolder(i).truthRadios[y].isChecked()));
                    }
                }

                String explanation;
                if(!getHolder(i).explanation.getText().toString().isEmpty())
                    explanation = getHolder(i).explanation.getText().toString();
                else
                    explanation = " ";
                mQuizQuestions.add(new QuizQuestion(problem, answers, multipleChoice, explanation));
            }
            return mQuizQuestions;
        }

    }
*/
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

       public List<QuizQuestion> submit(RecyclerView r){
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
