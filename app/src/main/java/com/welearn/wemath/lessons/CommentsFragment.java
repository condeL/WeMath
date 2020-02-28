package com.welearn.wemath.lessons;

/*Fragment for the comments*/

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.welearn.wemath.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class CommentsFragment extends Fragment {


    public CommentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_comments, container, false);

        RecyclerView view = root.findViewById(R.id.comments_view);
        ContentAdapterComments adapter = new ContentAdapterComments(view.getContext());
        view.setAdapter(adapter);
        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(getActivity()));

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = ViewModelProviders.of(this).get(LessonTopicViewModel.class);
        //mViewModel = new LessonTopicViewModel(mYear, mSection);
        // TODO: Use the ViewModel
    }

    public static class ViewHolderComments extends RecyclerView.ViewHolder{
        public TextView usernameC, contentC, dateC;
        public RecyclerView repliesRecycler;

        public ViewHolderComments(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.comment_card,parent, false));
            usernameC = itemView.findViewById(R.id.comment_card_username);
            contentC = itemView.findViewById(R.id.comment_card_content);
            dateC = itemView.findViewById(R.id.comment_card_date);
            repliesRecycler = itemView.findViewById(R.id.replies_view);


            //itemView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_lessonTopicFragment_to_lessonSelectionFragment, null));
        }
    }

    //the contentR adapter where the views are binded together
    public static class ContentAdapterComments extends RecyclerView.Adapter<ViewHolderComments>{

        private final ArrayList<String> mUsernamesC, mContentsC, mDatesC;
        Context mContextC;
        //private final ProgressBar[] mProgressBars;

        //pass it the year and section to represent the choice of the user
        public ContentAdapterComments(Context context){
            Resources resources = context.getResources();
            //String year = viewModel.getYear();
            //String section = viewModel.getSection();

            mUsernamesC = new ArrayList<>();
            mContentsC = new ArrayList<>();
            mDatesC = new ArrayList<>();
            mContextC = context;

            try {
                // get JSONObject from JSON file
                JSONObject obj = new JSONObject(loadJSONFromAsset(context));
                // fetch JSONArray named users
                JSONArray userArray = obj.getJSONArray("comments");
                // implement for loop for getting users list data
                for (int i = 0; i < userArray.length(); i++) {
                    // create a JSONObject for fetching single user data
                    JSONObject comment = userArray.getJSONObject(i);
                    // fetch email and name and store it in arraylist
                    mUsernamesC.add(comment.getString("user"));
                    mContentsC.add(comment.getString("content"));
                    mDatesC.add(comment.getString("date"));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public String loadJSONFromAsset(Context context) {
            String json = null;
            try {
                InputStream is = context.getAssets().open("comments.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
            return json;
        }

        @Override
        public ViewHolderComments onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolderComments(LayoutInflater.from(parent.getContext()), parent);
        }

        //put the resource elements in the views using the ViewHolder
        @Override
        public void onBindViewHolder(ViewHolderComments holder, int position) {
            holder.usernameC.setText(mUsernamesC.get(position % mUsernamesC.size()));
            holder.contentC.setText(mContentsC.get(position % mContentsC.size()));
            //holder.percentage.setText(mPercentages[position % mPercentages.length]);
            holder.dateC.setText(mDatesC.get(position % mDatesC.size()));


            ContentAdapterReplies adapterR = new ContentAdapterReplies(mContextC, position);
            holder.repliesRecycler.setAdapter(adapterR);
            holder.repliesRecycler.setHasFixedSize(true);
            //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContextC,LinearLayoutManager.VERTICAL, false);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContextC);
            holder.repliesRecycler.setLayoutManager(linearLayoutManager);

           //holder.repliesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        @Override
        public int getItemCount() {
            return mUsernamesC.size();
        }

    }











    public static class ViewHolderReplies extends RecyclerView.ViewHolder{
        public TextView usernameR, contentR, dateR;

        public ViewHolderReplies(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.comment_reply_card, parent, false));
            usernameR = itemView.findViewById(R.id.reply_card_username);
            contentR = itemView.findViewById(R.id.reply_card_content);
            dateR = itemView.findViewById(R.id.reply_card_date);

            //itemView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_lessonTopicFragment_to_lessonSelectionFragment, null));
        }
    }

    //the contentR adapter where the views are binded together
    public static class ContentAdapterReplies extends RecyclerView.Adapter<ViewHolderReplies>{

        private final ArrayList<String> mUsernamesR, mContentsR, mDatesR;
        //private final ProgressBar[] mProgressBars;

        //pass it the year and section to represent the choice of the user
        public ContentAdapterReplies(Context context, int parentPosition){
            Resources resources = context.getResources();
            //String year = viewModel.getYear();
            //String section = viewModel.getSection();

            mUsernamesR = new ArrayList<>();
            mContentsR = new ArrayList<>();
            mDatesR = new ArrayList<>();

            try {
                // get JSONObject from JSON file
                JSONObject obj = new JSONObject(loadJSONFromAsset(context));
                // fetch JSONArray named users
                JSONArray userArray = obj.getJSONArray("comments");
                JSONArray replies = userArray.getJSONObject(parentPosition).getJSONArray("replies");


                // implement for loop for getting users list data
                for (int i = 0; i < replies.length(); i++) {
                    // create a JSONObject for fetching single user data
                    JSONObject comment = replies.getJSONObject(i);
                    // fetch email and name and store it in arraylist
                    mUsernamesR.add(comment.getString("user"));
                    mContentsR.add(comment.getString("content"));
                    mDatesR.add(comment.getString("date"));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public String loadJSONFromAsset(Context context) {
            String json = null;
            try {
                InputStream is = context.getAssets().open("comments.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
            return json;
        }

        @Override
        public ViewHolderReplies onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolderReplies(LayoutInflater.from(parent.getContext()), parent);
        }

        //put the resource elements in the views using the ViewHolder
        @Override
        public void onBindViewHolder(ViewHolderReplies holder, int position) {
            holder.usernameR.setText(mUsernamesR.get(position % mUsernamesR.size()));
            holder.contentR.setText(mContentsR.get(position % mContentsR.size()));
            //holder.percentage.setText(mPercentages[position % mPercentages.length]);
            holder.dateR.setText(mDatesR.get(position % mDatesR.size()));
        }

        @Override
        public int getItemCount() {
            return mUsernamesR.size();
        }

    }

}
