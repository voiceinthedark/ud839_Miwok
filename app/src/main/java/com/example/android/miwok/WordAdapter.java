package com.example.android.miwok;


import android.content.Context;
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

    private List<Word> mWords;
    private int mLayoutResourceID;
    private int mColorResourceID;
    public WordAdapter(Context context, int resourceId, List<Word> words, int colorResourceId){
        super(context, resourceId, words);
        mWords = words;
        mLayoutResourceID = resourceId;
        mColorResourceID = colorResourceId;
    }

    /**
     * Overrid getView in our word adapter to display our views in our custom layout
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
        Word w = getItem(position);
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

        return view;
    }
}
