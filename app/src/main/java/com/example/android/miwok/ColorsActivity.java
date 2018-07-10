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

public class ColorsActivity extends AppCompatActivity {

    private static final String TAG = "mainactivity";
    private MediaPlayer mMediaPlayer;
    private ArrayList<Word> words;

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
                            //return the pointer to the begining of the audio
                            mMediaPlayer.seekTo(0);
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
                            //return the pointer to the begining of the audio
                            mMediaPlayer.seekTo(0);
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        //Add and init the words array
        words = new ArrayList<>();
        words.add(new Word("red", "wetetti", R.drawable.color_red, R.raw.color_red));
        words.add(new Word("green", "chokokki", R.drawable.color_green, R.raw.color_green));
        words.add(new Word("brown", "takaakki", R.drawable.color_brown, R.raw.color_brown));
        words.add(new Word("gray", "topoppi", R.drawable.color_gray, R.raw.color_gray));
        words.add(new Word("black", "kululli", R.drawable.color_black, R.raw.color_black));
        words.add(new Word("white", "kelelli", R.drawable.color_white, R.raw.color_white));
        words.add(new Word("dusty yellow", "topiise", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        words.add(new Word("mustard yellow", "chiwiite", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));


        //setup an ArrayAdapter
        WordAdapter itemsAdapter =
                new WordAdapter(this, R.layout.list_item, words, R.color.category_colors);

        //setup the ListView
        ListView listView = (ListView) findViewById(R.id.list);

        //attach the addapter to the listview
        listView.setAdapter(itemsAdapter);

        /**
         * Attach {@link ListView} items to call media player to play a file when clicked
         */
        releaseMediaPlayer();
        listView.setOnItemClickListener(mOnItemClickListener);

    }

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
            //request Focus and capture the result
            int result = mAudioManager.requestAudioFocus(mAudioFocusChangeListener,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                Word word = words.get(position);

                //Mediaplayer is associated with the word once the user click the view
                mMediaPlayer = MediaPlayer.create(ColorsActivity.this, word.getSoundResourceId());

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
