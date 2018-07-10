/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class NumbersActivity extends AppCompatActivity {

    private static final String TAG = "mainactivity";
    //Media player to play audio files
    private MediaPlayer mMediaPlayer;
    /**
     * Register a change listener on the AudioFocus of the {@link AudioManager}
     */
    AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    if (mMediaPlayer != null) {
                        //if focus is lost for short period of time, pause playing
                        if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                            mMediaPlayer.pause();
                        }
                        //if focus is lost permanently then stop playing the sound
                        else if(focusChange == AudioManager.AUDIOFOCUS_LOSS){
                            mMediaPlayer.stop();
                            releaseMediaPlayer();
                        }
                        //if focus is lost for short duration with possibiliy of lowering
                        //the volume then we should pause the sound
                        else if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                            mMediaPlayer.pause();
                        }
                        //if we gain audio focus after losing it for short duration, then resume
                        //playing the audio
                        else if(focusChange == AudioManager.AUDIOFOCUS_GAIN){
                            mMediaPlayer.start();
                        }


                    }
                }
            };


    //Audio manager to control the audio states
    private AudioManager mAudioManager;
    private ArrayList<Word> words;
    /**
     * When the user presses or touches an item in our listview play the sound
     * associated with that particular word
     */
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /**
             * request audio focus
             * request stream type {@link AudioManager.STREAM_MUSIC}
             * duration {@link AudioManager.AUDIOFOCUS_GAIN_TRANSIENT}
             */
            //setup audio manager
            mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            //request Focus
            int result = mAudioManager.requestAudioFocus(mAudioFocusChangeListener,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);


            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                //get the word at position in our arraylist
                Word word = words.get(position);

                //Mediaplayer is associated with the word once the user click the view
                mMediaPlayer = MediaPlayer.create(NumbersActivity.this, word.getSoundResourceId());

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
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        //Add and init the words array
        words = new ArrayList<>();
        words.add(new Word("one", "lutti", R.drawable.number_one, R.raw.number_one));
        words.add(new Word("two", "otiiko", R.drawable.number_two, R.raw.number_two));
        words.add(new Word("three", "tolookosu", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("four", "oyyisa", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("five", "massokka", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("six", "temmokka", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("eight", "kawinta", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word("nine", "wo'e", R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("ten", "na'aacha", R.drawable.number_ten, R.raw.number_ten));


        //setup an ArrayAdapter
        WordAdapter itemsAdapter =
                new WordAdapter(this, R.layout.list_item, words, R.color.category_numbers);

        //setup the ListView
        ListView listView = (ListView) findViewById(R.id.list);

        //attach the addapter to the listview
        listView.setAdapter(itemsAdapter);

        releaseMediaPlayer();
        listView.setOnItemClickListener(mOnItemClickListener);



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

            //release the audio focus
            mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        releaseMediaPlayer();
    }


}
