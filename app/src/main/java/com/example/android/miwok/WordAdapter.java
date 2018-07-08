package com.example.android.miwok;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class WordAdapter extends ArrayAdapter<Word> {

    private List<Word> mWords;
    private int mResourceID;
    public WordAdapter(Context context, int resourceId, List<Word> words){
        super(context, resourceId, words);
        mWords = words;
        mResourceID = resourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //check if the existing view is being reused, otherwise inflate the view
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(getContext())
                    .inflate(mResourceID, parent, false);
        }

        TextView mMiwokWordTextView = (TextView) view.findViewById(R.id.word_miwok);
        TextView mDefaultWordTextView = (TextView) view.findViewById(R.id.word_english);

        Word w = getItem(position);
        mMiwokWordTextView.setText(w.getWordMiwoki());
        mDefaultWordTextView.setText(w.getWordDefault());

        return view;
    }
}
