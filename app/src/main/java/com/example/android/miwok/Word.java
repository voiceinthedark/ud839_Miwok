package com.example.android.miwok;

/**
 * class {@link Word} that contains a word in Miwok and its default translation
 */
public class Word {
    //holder for the word in Miwoki
    private String mWordMiwoki;

    /**
     * get Miwok translation of word
     * @return the Miwok word
     */
    public String getWordMiwoki() {
        return mWordMiwoki;
    }

    /**
     * get the default translation of the Miwok word
     * @return the default translation of a Miwok word
     */
    public String getWordDefault() {
        return mWordDefault;
    }

    //holder for the word in default language
    private String mWordDefault;

    private int mImageResourceId;

    public int getImageResourceId() {
        return mImageResourceId;
    }

    /**
     * public constructor; sets a Miwok word along with its translation
     * @param wordDefault the default translation
     * @param wordMiwoki the Miwok word
     */
    public Word(String wordDefault, String wordMiwoki){
        this(wordDefault, wordMiwoki, 0);
    }

    public Word(String wordDefault, String wordMiwoki, int imageResourceId){
        this.mWordDefault = wordDefault;
        this.mWordMiwoki = wordMiwoki;
        this.mImageResourceId = imageResourceId;
    }


}
