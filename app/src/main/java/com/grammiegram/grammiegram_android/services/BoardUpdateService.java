package com.grammiegram.grammiegram_android.services;

import android.content.SharedPreferences;

import com.grammiegram.grammiegram_android.POJO.CheckNewResponse;
import com.grammiegram.grammiegram_android.adapters.BoardFragmentPagerAdapter;
import com.grammiegram.grammiegram_android.interfaces.APIResponse;
import com.grammiegram.grammiegram_android.interfaces.CallBack;

import okhttp3.ResponseBody;

/**
 * A service that asynchronously handles checking for new grams and deleting
 * expired ones from the fragment state pager adapter.
 */
public class BoardUpdateService implements CallBack, Runnable {

    public static final int CHECK_RATE_SECONDS = 10;
    private final String BOARD_DISPLAY_NAME;

    private BoardFragmentPagerAdapter adapter;

    private SharedPreferences prefs;

    //The callback implemented by BoardActivity for getGrams api call
    private CallBack gramsListCallBack;

    private GrammieGramService gramsAPI;
    private GrammieGramService checkNewAPI;

    public BoardUpdateService(BoardFragmentPagerAdapter adapter, CallBack cb, String boardName, SharedPreferences prefs) {
        this.adapter = adapter;
        this.gramsListCallBack = cb;
        gramsAPI = new GrammieGramService(cb);
        checkNewAPI = new GrammieGramService(this);
        this.BOARD_DISPLAY_NAME = boardName;
        this.prefs = prefs;
    }

    /**
     * Work to run asynchronously in background thread. Removes expired grams and checks
     * for new grams to add to adapter.
     */
    @Override
    public void run() {
        //destroy expired grams
        this.adapter.removeExpiredGrams();

        //api call to check for new grams
        checkNewAPI.checkNewGrams(prefs.getString("auth_token", "DEFAULT"), BOARD_DISPLAY_NAME);
    }


    /**
     * Successful response from api to check for new grams. If an update to the board is needed,
     * then the get grams api method is called to update the adapter that holds gram fragments.
     * @param response - CheckNewResponse from Retrofit api
     */
    @Override
    public void onSuccess(APIResponse response) {
        CheckNewResponse checkNew = (CheckNewResponse) response;
        if(checkNew.getNeeded()) {
            gramsAPI.getGrams(prefs.getString("auth_token", "DEFAULT"), BOARD_DISPLAY_NAME);
        }
    }

    @Override
    public void onNetworkError(String error) {
        gramsListCallBack.onNetworkError(error);
    }

    @Override
    public void onServerError(int code, ResponseBody body) {
        gramsListCallBack.onServerError(code, body);
    }

}
