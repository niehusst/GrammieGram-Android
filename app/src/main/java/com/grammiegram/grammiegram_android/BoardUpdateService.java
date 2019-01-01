package com.grammiegram.grammiegram_android;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.grammiegram.grammiegram_android.POJO.CheckNewResponse;
import com.grammiegram.grammiegram_android.adapters.BoardFragmentPagerAdapter;
import com.grammiegram.grammiegram_android.interfaces.APIResponse;
import com.grammiegram.grammiegram_android.interfaces.CallBack;

import okhttp3.ResponseBody;

/**
 * A service that asynchronously handles checking for new grams and deleting
 * expired ones from the fragment state pager adapter.
 * TODO: while(1) { if(apiresponse.boardUpdateNeeded()) { do stuff }}
 */
public class BoardUpdateService extends AsyncTask implements CallBack {

    private static final int CHECK_RATE_SECONDS = 10;
    private final String BOARD_DISPLAY_NAME;

    private BoardFragmentPagerAdapter adapter;

    private SharedPreferences sharedPreferences;

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
        this.sharedPreferences = prefs;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            while (true) {
                wait(1000 * CHECK_RATE_SECONDS);
                //destroy expired grams
                this.adapter.removeExpiredGrams();

                //check new
                checkNewAPI.checkNewGrams(sharedPreferences.getString("auth_token", "DEFUALT"),
                        BOARD_DISPLAY_NAME);

                //TODO: also update time and date text views on board

            }
        } catch (Exception e) {
            //TODO: this is definitly not how to do this
        }

        return null;
    }


    private String getDate() {

    }

    private String getTime() {

    }


    /**                API checkNewGrams response callbacks                 */
    @Override
    public void onSuccess(APIResponse response) {
        CheckNewResponse checkNew = (CheckNewResponse) response;
        if(checkNew.getNeeded()) {
            String token = sharedPreferences.getString("auth_token", "DEFAULT");
            gramsAPI.getGrams(token, BOARD_DISPLAY_NAME);
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
