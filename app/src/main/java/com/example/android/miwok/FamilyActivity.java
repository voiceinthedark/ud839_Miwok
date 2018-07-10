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

public class FamilyActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
    private ArrayList<Word> words;
    private static final String TAG = "mainactivity";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        //Add and init the words array
        words = new ArrayList<>();
        words.add(new Word("father", "epe", R.drawable.family_father, R.raw.family_father));
        words.add(new Word("mother", "eta", R.drawable.family_mother, R.raw.family_mother));
        words.add(new Word("son", "angsi", R.drawable.family_son, R.raw.family_son));
        words.add(new Word("daughter", "tune", R.drawable.family_daughter, R.raw.family_daughter));
        words.add(new Word("older brother", "taachi", R.drawable.family_older_brother, R.raw.family_older_brother));
        words.add(new Word("younger brother", "chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        words.add(new Word("older sister", "tete", R.drawable.family_older_sister, R.raw.family_older_sister));
        words.add(new Word("younger sister", "kolliti", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        words.add(new Word("grandmother", "ama", R.drawable.family_grandmother, R.raw.family_grandmother));
        words.add(new Word("grandfather", "paapa", R.drawable.family_grandfather, R.raw.family_grandfather));


        //setup an ArrayAdapter
        WordAdapter itemsAdapter =
                new WordAdapter(this, R.layout.list_item, words, R.color.category_family);

        //setup the ListView
        ListView listView = (ListView) findViewById(R.id.list);

        //attach the addapter to the listview
        listView.setAdapter(itemsAdapter);

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
            //request Focus
            mAudioManager.requestAudioFocus(mAudioFocusChangeListener,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

            Word word = words.get(position);

            //Mediaplayer is associated with the word once the user click the view
            mMediaPlayer = MediaPlayer.create(FamilyActivity.this, word.getSoundResourceId());

            //if the user presses the view play sound
            mMediaPlayer.start();

            //Clean up resources
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    releaseMediaPlayer();
                    //release the audio focus
                    mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
                }
            });
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
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        releaseMediaPlayer();
    }
}
