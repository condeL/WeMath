package com.welearn.wemath.lessons;

/*Fragment for the comments*/

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.welearn.wemath.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class CommentsFragment extends Fragment {

    private FirebaseUser mUser;
    FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private TextView mProfilePicture;
    private EditText mMessage;
    private ImageButton mSendButton;
    private ContentAdapterComments mAdapter;
    private RecyclerView mView;


    public CommentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_comments, container, false);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        String name = mUser.getDisplayName();

        mProfilePicture = root.findViewById(R.id.comments_fragment_profile_picture);
        mMessage = root.findViewById(R.id.comments_fragment_editText);
        mSendButton = root.findViewById(R.id.comments_fragment_send_button);

        mProfilePicture.setText(String.valueOf(name.toUpperCase().charAt(0)));

        int[] profileColors = getResources().getIntArray(R.array.profile_colors);
        Paint paint = new Paint();
        paint.setColor(profileColors[name.charAt(0)%6]);
        mProfilePicture.getBackground().setColorFilter(paint.getColor(), PorterDuff.Mode.ADD);

        mSendButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick (View v){

                /*Toast.makeText(getContext(), mMessage.getText().toString(), Toast.LENGTH_LONG).show();
                mMessage.setText("");*/
                postComment(getContext(), mMessage);

            }
        });

        mView = root.findViewById(R.id.comments_view);
        mAdapter = new ContentAdapterComments(mView.getContext());
        mView.setAdapter(mAdapter);
        mView.setHasFixedSize(true);
        mView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = ViewModelProviders.of(this).get(LessonTopicViewModel.class);
        //mViewModel = new LessonTopicViewModel(mYear, mSection);
        // TODO: Use the ViewModel
    }



    void postComment(final Context context, EditText editText){
        Comment comment = new Comment(mUser.getUid(),mUser.getDisplayName(),editText.getText().toString());

        mDb.collection("comments/jhs/year/topic0/lesson1").document()
                .set(comment)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("success", "DocumentSnapshot successfully written!");
                        Toast.makeText(context,"Comment sent", Toast.LENGTH_LONG).show();
                        mAdapter = new ContentAdapterComments(getContext());
                        mView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("failure", "Error writing document", e);
                        Toast.makeText(context,"Error pushing document", Toast.LENGTH_LONG).show();

                    }
                });
        Toast.makeText(context, editText.getText().toString(), Toast.LENGTH_LONG).show();
        editText.setText("");

    }









    public static class ViewHolderComments extends RecyclerView.ViewHolder{
        public TextView usernameC, contentC, dateC, pictureC;
        public TextView repliesButtonC;
        public Button replyButtonC;
        public EditText messageC;
        public TextView downvotesC, upvotesC;
        public ImageButton sendButtonC;
        public LinearLayout replyBarC;
        public RecyclerView repliesRecycler;

        public ViewHolderComments(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.comment_card,parent, false));
            usernameC = itemView.findViewById(R.id.comment_card_username);
            contentC = itemView.findViewById(R.id.comment_card_content);
            dateC = itemView.findViewById(R.id.comment_card_date);
            pictureC = itemView.findViewById(R.id.comment_card_profile_pic);

            downvotesC = itemView.findViewById(R.id.comment_card_downvotes_numbers);
            upvotesC = itemView.findViewById(R.id.comment_card_upvotes_numbers);

            repliesButtonC = itemView.findViewById(R.id.comment_card_replies);

            replyButtonC = itemView.findViewById(R.id.comment_card_reply_button);
            replyBarC = itemView.findViewById(R.id.comment_card_reply_bar);
            messageC = itemView.findViewById(R.id.comment_card_editText);
            sendButtonC = itemView.findViewById(R.id.comment_card_send_button);
            repliesRecycler = itemView.findViewById(R.id.replies_view);


            //itemView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_lessonTopicFragment_to_lessonSelectionFragment, null));
        }
    }

    //the contentR adapter where the views are binded together
    public static class ContentAdapterComments extends RecyclerView.Adapter<ViewHolderComments>{

        //private final ArrayList<String> mUsernamesC, mContentsC, mDatesC, mDownvotesC, mUpvotesC;
        private final ArrayList<Comment> mCommentsC;
        Context mContextC;
        FirebaseFirestore mDB = FirebaseFirestore.getInstance();

        //private final ProgressBar[] mProgressBars;

        //pass it the year and section to represent the choice of the user
        public ContentAdapterComments(final Context context){
            Resources resources = context.getResources();
            //String year = viewModel.getYear();
            //String section = viewModel.getSection();

            /*mUsernamesC = new ArrayList<>();
            mContentsC = new ArrayList<>();
            mDatesC = new ArrayList<>();
            mDownvotesC = new ArrayList<>();
            mUpvotesC = new ArrayList<>();*/
            mCommentsC = new ArrayList<>();

            mContextC = context;




            Query query = mDB.collection("comments/jhs/year/topic0/lesson1")
                    .orderBy("timestamp", Query.Direction.ASCENDING);

            /*FirestoreRecyclerOptions<Comment> options = new FirestoreRecyclerOptions.Builder<Comment>()
                    .setQuery(query, Comment.class)
                    .build();*/


            mDB.collection("comments/jhs/year/topic0/lesson1")
                    .orderBy("timestamp",Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("GETTING", document.getId() + " => " + document.getData());
                                    mCommentsC.add(document.toObject(Comment.class));
                                    Toast.makeText(context,"Getting successful", Toast.LENGTH_LONG).show();
                                    notifyDataSetChanged();



                                }
                            } else {
                                Log.d("GETTING", "Error getting documents: ", task.getException());
                                Toast.makeText(context,"Getting failed", Toast.LENGTH_LONG).show();

                            }
                        }
                    });


        }



        @Override
        public ViewHolderComments onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolderComments(LayoutInflater.from(parent.getContext()), parent);
        }

        //put the resource elements in the views using the ViewHolder
        @Override
        public void onBindViewHolder(final ViewHolderComments holder, int position) {
            /*holder.usernameC.setText(mUsernamesC.get(position % mUsernamesC.size()));
            holder.contentC.setText(mContentsC.get(position % mContentsC.size()));
            holder.dateC.setText(mDatesC.get(position % mDatesC.size()));
            holder.downvotesC.setText(mDownvotesC.get(position%mDownvotesC.size()));
            holder.upvotesC.setText(mUpvotesC.get(position%mUpvotesC.size()));
            */

            holder.usernameC.setText(mCommentsC.get(position % mCommentsC.size()).getName());
            holder.contentC.setText(mCommentsC.get(position % mCommentsC.size()).getMessage());
            holder.dateC.setText(new SimpleDateFormat("dd MMM yyyy | hh:mm").format(mCommentsC.get(position % mCommentsC.size()).getTimestamp()));
            holder.downvotesC.setText(String.valueOf(mCommentsC.get(position%mCommentsC.size()).getDownvotes()));
            holder.upvotesC.setText(String.valueOf(mCommentsC.get(position%mCommentsC.size()).getUpvotes()));

            String name = mCommentsC.get(position%mCommentsC.size()).getName();
            holder.pictureC.setText(String.valueOf(name.toUpperCase().charAt(0)));
            int[] profileColors = mContextC.getResources().getIntArray(R.array.profile_colors);
            Paint paint = new Paint();
            paint.setColor(profileColors[name.charAt(0)%6]);
            holder.pictureC.getBackground().setColorFilter(paint.getColor(), PorterDuff.Mode.ADD);

            holder.repliesButtonC.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick (View v){

                    if(holder.repliesRecycler.getVisibility() == View.GONE){
                        holder.repliesRecycler.setVisibility(View.VISIBLE);
                    } else {
                        holder.repliesRecycler.setVisibility(View.GONE);
                    }
                }
            });

            holder.replyButtonC.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick (View v){

                    if(holder.replyBarC.getVisibility() == View.GONE){
                        holder.replyBarC.setVisibility(View.VISIBLE);
                    } else {
                        holder.replyBarC.setVisibility(View.GONE);
                    }
                }
            });

            holder.sendButtonC.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick (View v){

                    /*Toast.makeText(mContextC, holder.messageC.getText().toString(), Toast.LENGTH_LONG).show();
                    holder.messageC.setText("");*/
                    //postComment(mContextC, holder.messageC);
                }
            });

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
            return mCommentsC.size();
        }

        @Override
        public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
            super.registerAdapterDataObserver(observer);

            }





    }











    public static class ViewHolderReplies extends RecyclerView.ViewHolder{
        public TextView usernameR, contentR, dateR;
        public Button replyButtonR;
        public EditText messageR;
        public ImageButton sendButtonR;
        public LinearLayout replyBarR;

        public ViewHolderReplies(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.comment_reply_card, parent, false));
            usernameR = itemView.findViewById(R.id.reply_card_username);
            contentR = itemView.findViewById(R.id.reply_card_content);
            dateR = itemView.findViewById(R.id.reply_card_date);

            replyButtonR = itemView.findViewById(R.id.reply_card_reply_button);
            replyBarR = itemView.findViewById(R.id.reply_card_reply_bar);
            messageR = itemView.findViewById(R.id.reply_card_editText);
            sendButtonR = itemView.findViewById(R.id.reply_card_send_button);

            //itemView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_lessonTopicFragment_to_lessonSelectionFragment, null));
        }
    }

    //the contentR adapter where the views are binded together
    public static class ContentAdapterReplies extends RecyclerView.Adapter<ViewHolderReplies>{

        private final ArrayList<String> mUsernamesR, mContentsR, mDatesR;
        Context mContextC;

        //private final ProgressBar[] mProgressBars;

        //pass it the year and section to represent the choice of the user
        public ContentAdapterReplies(Context context, int parentPosition){
            Resources resources = context.getResources();
            //String year = viewModel.getYear();
            //String section = viewModel.getSection();
            mContextC = context;

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
        public void onBindViewHolder(final ViewHolderReplies holder, int position) {
            holder.usernameR.setText(mUsernamesR.get(position % mUsernamesR.size()));
            holder.contentR.setText(mContentsR.get(position % mContentsR.size()));
            //holder.percentage.setText(mPercentages[position % mPercentages.length]);
            holder.dateR.setText(mDatesR.get(position % mDatesR.size()));

            holder.replyButtonR.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick (View v){

                    if(holder.replyBarR.getVisibility() == View.GONE){
                        holder.replyBarR.setVisibility(View.VISIBLE);
                    } else {
                        holder.replyBarR.setVisibility(View.GONE);
                    }
                }
            });

            holder.sendButtonR.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick (View v){

                    /*Toast.makeText(mContextC, holder.messageR.getText().toString(), Toast.LENGTH_LONG).show();
                    holder.messageR.setText("");*/
                    //postComment(mContextC, holder.messageR);


                }
            });

        }

        @Override
        public int getItemCount() {
            return mUsernamesR.size();
        }

    }

}
