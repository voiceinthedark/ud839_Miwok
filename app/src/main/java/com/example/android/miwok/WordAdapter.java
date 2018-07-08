package com.example.android.miwok;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Custom {@link ArrayAdapter} holding a Word variable, responsible of relaying data
 * to our View Adapter
 */
public class WordAdapter extends ArrayAdapter<Word> {

    private List<Word> mWords;
    private int mResourceID;
    public WordAdapter(Context context, int resourceId, List<Word> words){
        super(context, resourceId, words);
        mWords = words;
        mResourceID = resourceId;
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
                    .inflate(mResourceID, parent, false);
        }

        //Instantiate our referenced views; 2 TextViews and an ImageView
        TextView mMiwokWordTextView = (TextView) view.findViewById(R.id.word_miwok);
        TextView mDefaultWordTextView = (TextView) view.findViewById(R.id.word_english);
        ImageView mWordImageView = (ImageView) view.findViewById(R.id.word_image_view);

        //Get the Word object at the current position; getItem is a member method of ArrayAdapter
        Word w = getItem(position);
        //Set Text of our text views to display the Word state variables
        mMiwokWordTextView.setText(w.getWordMiwoki());
        mDefaultWordTextView.setText(w.getWordDefault());
        //set the image resource to point to our drawable resource id associated with the word.
        mWordImageView.setImageResource(w.getImageResourceId());



        return view;
    }
}
