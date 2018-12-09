package com.grammiegram.grammiegram_android;

import android.util.Log;

import com.grammiegram.grammiegram_android.POJO.BoardListResponse;
import com.grammiegram.grammiegram_android.POJO.CheckNewResponse;
import com.grammiegram.grammiegram_android.POJO.ErrorResponse;
import com.grammiegram.grammiegram_android.POJO.GramsListResponse;
import com.grammiegram.grammiegram_android.POJO.LoginResponse;
import com.grammiegram.grammiegram_android.POJO.SettingsResponse;
import com.grammiegram.grammiegram_android.interfaces.GrammieGramAPI;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.mock.Calls;
import retrofit2.mock.BehaviorDelegate;


public class MockGrammieGramServiceError implements GrammieGramAPI {
    private static final String TAG = "MockResponseError";
    private BehaviorDelegate<GrammieGramAPI> delegate;

    public MockGrammieGramServiceError(BehaviorDelegate<GrammieGramAPI> service) {
        this.delegate = service;
    }

    /**
     * Stubbed error response object from API call
     * @param auth - login token
     * @return - Error response object
     */
    @Override
    public Call<BoardListResponse> getBoards(String auth) {
        //set up error response
        ErrorResponse error = new ErrorResponse();

        error.setError("ServerError");

        String json = error.convertToJson();
        try {
            //server error getting data
            Response response = Response.error(500, ResponseBody.create(MediaType.parse("application/json") ,json));
            return this.delegate.returning(Calls.response(response)).getBoards(auth);
        } catch (Exception e) {
            Log.e(TAG, "JSON Processing exception:",e);
            return Calls.failure(e);
        }
    }

    /**
     * Stubbed failed API login retrofit response
     * @return - stubbed API object
     */
    @Override
    public Call<LoginResponse> login(String username, String password) {
        //create a stubbed instance of api response
        //set up error response
        ErrorResponse error = new ErrorResponse();

        error.setError("Please provide correct username and password");

        String json = error.convertToJson();
        try {
            //user error inputting bad username password
            Response response = Response.error(400, ResponseBody.create(MediaType.parse("application/json") ,json));
            return this.delegate.returning(Calls.response(response)).login(username, password);
        } catch (Exception e) {
            Log.e(TAG, "JSON Processing exception:",e);
            return Calls.failure(e);
        }

    }

    /**
     * Stubbed failed API list grams retrofit response
     * @param auth - login token
     * @param boardDisplayNames - board to get grams for
     * @return - stubbed API GramsListResponse object
     */
    @Override
    public Call<GramsListResponse> getGrams(String auth, String boardDisplayNames) {
        //create a stubbed instance of api response
        //set up error response
        ErrorResponse error = new ErrorResponse();

        error.setError("ServerError");

        String json = error.convertToJson();
        try {
            //server error getting grams
            Response response = Response.error(500, ResponseBody.create(MediaType.parse("application/json") ,json));
            return this.delegate.returning(Calls.response(response)).getGrams(auth, boardDisplayNames);
        } catch (Exception e) {
            Log.e(TAG, "JSON Processing exception:",e);
            return Calls.failure(e);
        }
    }

    /**
     * Stubbed API settingsUpdate retrofit response
     * @param auth - login token
     * @param audioNotification - whether to make sound when message arrives
     * @param profanityFilter - whether to strip bad words from grams
     * @return - stubbed API SettingsResponse object
     */
    @Override
    public Call<SettingsResponse> updateSettings(String auth, boolean audioNotification, boolean profanityFilter) {
        //set stubbed data into response object
        ErrorResponse error = new ErrorResponse();

        error.setError("ServerError");

        String json = error.convertToJson();
        try {
            //server error setting new data in database
            Response response = Response.error(500, ResponseBody.create(MediaType.parse("application/json") ,json));
            return this.delegate.returning(Calls.response(response)).updateSettings(auth, audioNotification, profanityFilter);
        } catch (Exception e) {
            Log.e(TAG, "JSON Processing exception:",e);
            return Calls.failure(e);
        }
    }

    /**
     * Stubbed API settingsUpdate retrofit response
     * @param auth - login token
     * @param boardDisplayName - the display name of the board to check new grams for
     * @return - stubbed API SettingsResponse object
     */
    @Override
    public Call<CheckNewResponse> checkNewGrams(String auth, String boardDisplayName) {
        //set stubbed data into response object
        ErrorResponse error = new ErrorResponse();

        error.setError("ServerError");

        String json = error.convertToJson();
        try {
            //server error setting new data in database
            Response response = Response.error(500, ResponseBody.create(MediaType.parse("application/json") ,json));
            return this.delegate.returning(Calls.response(response)).checkNewGrams(auth, boardDisplayName);
        } catch (Exception e) {
            Log.e(TAG, "JSON Processing exception:",e);
            return Calls.failure(e);
        }
    }

    /*
    later add sendgram, makeboard, getcontacts
     */
}
