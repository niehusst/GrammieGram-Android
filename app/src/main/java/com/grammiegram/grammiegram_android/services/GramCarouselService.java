package com.grammiegram.grammiegram_android.services;

import com.grammiegram.grammiegram_android.interfaces.OnGramFragmentClickListener;

/**
 * A Runnable service that cycles through grams in the board adapter every 30 seconds (starting
 * from when the fragment was created).
 */
public class GramCarouselService implements Runnable {

    private long gramInitTime;
    private OnGramFragmentClickListener parentActivity;

    public GramCarouselService(long gramInitTime, OnGramFragmentClickListener listener) {
        this.gramInitTime = gramInitTime;
        this.parentActivity = listener;
    }

    /**
     * If its been 30 secs since fragment creation, cycle to the next gram in the fragment
     */
    @Override
    public void run() {
        long currTimeMillis = System.currentTimeMillis();
        if(currTimeMillis >= gramInitTime+30000) {
            parentActivity.onRightClick();
        }
    }
}
