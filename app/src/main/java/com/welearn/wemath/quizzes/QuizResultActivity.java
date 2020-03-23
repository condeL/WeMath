package com.welearn.wemath.quizzes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.welearn.wemath.MainActivity;
import com.welearn.wemath.R;

public class QuizResultActivity extends AppCompatActivity {

    private RecyclerView mResultsRcycler;
    private Button mFinishButton;
    private TextView mScore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);
        Intent intent = this.getIntent();
        boolean[] results = intent.getBooleanArrayExtra("results");
        String[] explanation = intent.getStringArrayExtra("explanations");

        mResultsRcycler = findViewById(R.id.quiz_result_activity_recycler);
        mFinishButton = findViewById(R.id.quiz_result_activity_finish_button);
        mScore = findViewById(R.id.quiz_result_activity_score);

        int score = 0;
        for(boolean r:results){
            if(r){
                score++;
            }
        }
        mScore.setText("Score: " + score + "/10");

        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ContentAdapter adapter = new ContentAdapter(results, explanation);
        mResultsRcycler.setAdapter(adapter);
        mResultsRcycler.setHasFixedSize(true);
        mResultsRcycler.setLayoutManager(new LinearLayoutManager(this));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView result, explanation;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.quiz_result_card,parent, false));
            result = itemView.findViewById(R.id.quiz_result_card_result);
            explanation = itemView.findViewById(R.id.quiz_result_card_explanation);

        }
    }

    //the contentR adapter where the views are binded together
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder>{

        private String[] mExplanations;
        private boolean[] mResults;


        public ContentAdapter(boolean[] result, String[] explanation){

            mResults = result;
            mExplanations = explanation;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        //put the resource elements in the views using the ViewHolder
        @Override
        public void onBindViewHolder(ViewHolder holder,final int position) {
            holder.result.setText(position + ": " + mResults[position]);
            holder.explanation.setText(mExplanations[position]);


        }

        @Override
        public int getItemCount() {
            return mExplanations.length;
        }

    }
}
