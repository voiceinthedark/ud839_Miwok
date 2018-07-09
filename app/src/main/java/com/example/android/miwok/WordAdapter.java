package com.example.android.miwok;


import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Custom {@link ArrayAdapter} holding a Word variable, responsible of relaying data
 * to our View Adapter
 */
public class WordAdapter extends ArrayAdapter<Word> {

    private static final String TAG = "wordadapter";

    private List<Word> mWords;
    private int mLayoutResourceID;
    private int mColorResourceID;

    private MediaPlayer mMediaPlayer;

    public WordAdapter(Context context, int resourceId, List<Word> words, int colorResourceId){
        super(context, resourceId, words);
        mWords = words;
        mLayoutResourceID = resourceId;
        mColorResourceID = colorResourceId;
    }

    /**
     * Override getView in our word adapter to display our views in our custom layout
     * @param position position of the index of the view currently in the list
     * @param convertView the view used by the {@link android.widget.ListView} to display our custom
     *                    layout view.
     * @param parent The {@link ViewGroup} parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //check if the existing view is being reused, otherwise inflate the view
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(getContext())
                    .inflate(mLayoutResourceID, parent, false);
        }

        //Instantiate our referenced views; 2 TextViews and an ImageView
        TextView mMiwokWordTextView = (TextView) view.findViewById(R.id.word_miwok);
        TextView mDefaultWordTextView = (TextView) view.findViewById(R.id.word_english);
        ImageView mWordImageView = (ImageView) view.findViewById(R.id.word_image_view);

        //Get a reference to the LinearLayout that holds the two textviews
        LinearLayout wordLinearLayout = (LinearLayout) view.findViewById(R.id.word_list_view);

        //set the background color according to the colorresource id
        wordLinearLayout.setBackgroundResource(mColorResourceID);

        //Get the Word object at the current position; getItem is a member method of ArrayAdapter
        final Word w = getItem(position);
        //Set Text of our text views to display the Word state variables
        mMiwokWordTextView.setText(w.getWordMiwoki());
        mDefaultWordTextView.setText(w.getWordDefault());
        //set the image resource to point to our drawable resource id associated with the word.
        if(w.hasImage()) {
            //if the Word has an image set the image
            mWordImageView.setImageResource(w.getImageResourceId());

            //and make sure it is visible in case an older view was recycled
            mWordImageView.setVisibility(View.VISIBLE);
        }
        else{
            //Set the image visibility to GONE; which indicates that it is INVISIBLE and holds
            //no space
            mWordImageView.setVisibility(View.GONE);
        }




        /**
         * Attach a listener on the entire list_item_view
         */
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Associates {@link MediaPlayer} object with the {@link Word} sound
                 */
                int soundResource = w.getSoundResourceId();
                String soundName = getContext().getResources().getResourceName(soundResource);
                //Mediaplayer is associated with the word once the user click the view
                mMediaPlayer = MediaPlayer.create(getContext(), soundResource);
                //Log.i(TAG, "filename: " + soundName);
                //if the user presses the view play sound
                mMediaPlayer.start();

                //Clean up resources
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        releaseMediaPlayer();
                    }
                });
            }
        });

        return view;
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;
        }
    }
}
