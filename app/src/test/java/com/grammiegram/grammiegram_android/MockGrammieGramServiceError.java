package com.grammiegram.grammiegram_android;

import android.util.Log;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.mock.Calls;
import retrofit2.mock.BehaviorDelegate;


public class MockGrammieGramServiceError implements GrammieGramService {
    private static final String TAG = "MockResponseError";
    private BehaviorDelegate<GrammieGramService> delegate;

    public MockGrammieGramServiceError(BehaviorDelegate<GrammieGramService> service) {
        this.delegate = service;
    }

    /**
     * Stubbed error response object from API call
     * @return - Error response object
     */
    @Override
    public Call<BoardListResponse> getBoards() {
        //set up error response
        ErrorResponse error = new ErrorResponse();

        error.setCode(500);
        error.setMessage("ServerError");


        String json = ""; //TODO: set json string
        try {
            //server error getting data
            Response response = Response.error(500, ResponseBody.create(MediaType.parse("application/json") ,json));
            return this.delegate.returning(Calls.response(response)).getBoards();
        } catch (Exception e) {
            Log.e(TAG, "JSON Processing exception:",e);
            return (Call<BoardListResponse>) Calls.failure(e);
        }
    }

    /**
     * Stubbed failed API login retrofit response
     * @return - stubbed API object
     */
    @Override
    public Call<LoginResponse> login(String username, String password) { //TODO; correct way to use user/pass?
        //create a stubbed instance of api response
        //set up error response
        ErrorResponse error = new ErrorResponse();

        error.setCode(400);
        error.setMessage("Please provide correct username and password");


        String json = error.convertToJson(); //TODO: set json string
        try {
            //user error inputting bad username password
            Response response = Response.error(400, ResponseBody.create(MediaType.parse("application/json") ,json));
            return this.delegate.returning(Calls.response(response)).getBoards();
        } catch (Exception e) {
            Log.e(TAG, "JSON Processing exception:",e);
            return (Call<LoginResponse>) Calls.failure(e);
        }

    }

    /**
     * Stubbed failed API list grams retrofit response
     * @return - stubbed API GramsListResponse object
     */
    @Override
    public Call<GramsListResponse> getGrams() { //TODO: does this take the login token??
        //create a stubbed instance of api response
        //set up error response
        ErrorResponse error = new ErrorResponse();

        error.setCode(500);
        error.setMessage("ServerError");


        String json = ""; //TODO: set json string
        try {
            //server error getting grams
            Response response = Response.error(500, ResponseBody.create(MediaType.parse("application/json") ,json));
            return this.delegate.returning(Calls.response(response)).getBoards();
        } catch (Exception e) {
            Log.e(TAG, "JSON Processing exception:",e);
            return (Call<GramsListResponse>) Calls.failure(e);
        }
    }

    /**
     * Stubbed API settingsUpdate retrofit response
     * @return - stubbed API SettingsResponse object
     */
    @Override
    public Call<SettingsResponse> updateSettings(boolean audioNotification, boolean profanityFilter, int fontSize) {
        //set stubbed data into response object
        //set up error response
        ErrorResponse error = new ErrorResponse();

        error.setCode(500);
        error.setMessage("ServerError");


        String json = ""; //TODO: set json string
        try {
            //server error setting new data in database
            Response response = Response.error(500, ResponseBody.create(MediaType.parse("application/json") ,json));
            return this.delegate.returning(Calls.response(response)).getBoards();
        } catch (Exception e) {
            Log.e(TAG, "JSON Processing exception:",e);
            return (Call<SettingsResponse>) Calls.failure(e);
        }
    }

    /*
    later add sendgram, makeboard, getcontacts
     */
}
