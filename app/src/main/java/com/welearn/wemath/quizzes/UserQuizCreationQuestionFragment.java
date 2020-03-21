package com.welearn.wemath.quizzes;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.welearn.wemath.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserQuizCreationQuestionFragment extends Fragment {

    private Button mSubmitButton;
    private RecyclerView mQuestions;

    public UserQuizCreationQuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user_quiz_creation_question, container, false);

        mQuestions = root.findViewById(R.id.user_quiz_creation_question_recyclerView);

        ContentAdapter adapter = new ContentAdapter(getContext());
        mQuestions.setAdapter(adapter);
        mQuestions.setHasFixedSize(true);
        mQuestions.setLayoutManager(new LinearLayoutManager(getActivity()));

        return root;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView number;
        public EditText problem, answer1, answer2, answer3, answer4;
        public CheckBox multiple;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.user_quiz_creation_question_card, parent, false));
            number = itemView.findViewById(R.id.user_quiz_creation_card_number);
            problem = itemView.findViewById(R.id.user_quiz_creation_card_problem);
            multiple = itemView.findViewById(R.id.user_quiz_creation_card_multipleChoiceBox);
            answer1 = itemView.findViewById(R.id.user_quiz_creation_card_answer1);
            answer2 = itemView.findViewById(R.id.user_quiz_creation_card_answer2);
            answer3 = itemView.findViewById(R.id.user_quiz_creation_card_answer3);
            answer4 = itemView.findViewById(R.id.user_quiz_creation_card_answer4);


        }
    }

    //the contentR adapter where the views are binded together
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder>{

        private Context mContext;
        //pass it the year and section to represent the choice of the user
        public ContentAdapter(Context context){

            mContext = context;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        //put the resource elements in the views using the ViewHolder
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.number.setText("Problem " +(position+1));

        }

        @Override
        public int getItemCount() {
            return 10;
        }

    }

}
