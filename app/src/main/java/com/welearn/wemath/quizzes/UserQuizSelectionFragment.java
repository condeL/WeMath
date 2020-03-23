package com.welearn.wemath.quizzes;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.welearn.wemath.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserQuizSelectionFragment extends Fragment {

    private RecyclerView mQuizRecycler;

    public UserQuizSelectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_user_quiz_selection, container, false);

        mQuizRecycler = root.findViewById(R.id.user_quiz_selection_recycle);

        Toast.makeText(getContext(),"Fetching quizzes...", Toast.LENGTH_LONG).show();

        ContentAdapter adapter = new ContentAdapter(root.getContext());
        mQuizRecycler.setAdapter(adapter);
        mQuizRecycler.setHasFixedSize(true);
        mQuizRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        return root;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title, details, author, difficulty;
        public RatingBar rating;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.quiz_users_card,parent, false));
            title = itemView.findViewById(R.id.user_quiz_card_title);
            details = itemView.findViewById(R.id.user_quiz_card_details);
            author = itemView.findViewById(R.id.user_quiz_card_author);
            difficulty = itemView.findViewById(R.id.user_quiz_card_difficulty);
            rating = itemView.findViewById(R.id.user_quiz_card_rating);

            //itemView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_lessonTopicFragment_to_lessonSelectionFragment, null));
        }
    }

    //the contentR adapter where the views are binded together
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder>{

        private final ArrayList<Pair<UserQuiz,String>> mUserQuizzes;
        FirebaseFirestore mDB = FirebaseFirestore.getInstance();
        Context mContext;

        public ContentAdapter(final Context context ){
            mUserQuizzes = new ArrayList<>();
            mContext = context;
            mDB.collection("quiz")
                    .orderBy("timestamp",Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("GETTING", document.getId() + " => " + document.getData());
                                    mUserQuizzes.add(new Pair(document.toObject(UserQuiz.class), document.getId()));

                                    Toast.makeText(mContext,"Fetching successful!", Toast.LENGTH_LONG).show();
                                    notifyDataSetChanged();

                                }
                            } else {
                                Log.d("GETTING", "Error getting documents: ", task.getException());
                                Toast.makeText(mContext,"Fetching failed", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        //put the resource elements in the views using the ViewHolder
        @Override
        public void onBindViewHolder(ViewHolder holder,final int position) {
            holder.title.setText(mUserQuizzes.get(position).first.getTitle());
            holder.details.setText(mUserQuizzes.get(position).first.getSection().toUpperCase() + " / " + mUserQuizzes.get(position).first.getYear());
            holder.author.setText("Created by: " + mUserQuizzes.get(position).first.getUsername());
            holder.difficulty.setText("Difficulty: " + mUserQuizzes.get(position).first.getDifficulty());
            holder.rating.setRating((int)(mUserQuizzes.get(position).first.getRating()));





           holder.itemView.setOnClickListener(new View.OnClickListener(){
               public void onClick(View v){
                   String quiz_id = mUserQuizzes.get(position).second;
                   //NavDirections action = UserQuizSelectionFragmentDirections.actionUserQuizSelectionToUserQuizTakingActivity(false, quiz_id);
                   Toast.makeText(mContext,"Setting up quiz...", Toast.LENGTH_LONG).show();
                  // Navigation.findNavController(v).navigate(action);

                   Intent intent = new Intent(mContext, UserQuizTakingActivity.class);
                   intent.putExtra("quiz_id", quiz_id);
                   mContext.startActivity(intent);

               }
           });

        }

        @Override
        public int getItemCount() {
            return mUserQuizzes.size();
        }

    }
}
