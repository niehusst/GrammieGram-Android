package com.grammiegram.grammiegram_android;

import com.grammiegram.grammiegram_android.POJO.BoardListResponse;
import com.grammiegram.grammiegram_android.POJO.CheckNewResponse;
import com.grammiegram.grammiegram_android.POJO.GramsListResponse;
import com.grammiegram.grammiegram_android.POJO.LoginResponse;
import com.grammiegram.grammiegram_android.POJO.SettingsResponse;
import com.grammiegram.grammiegram_android.interfaces.CallBack;
import com.grammiegram.grammiegram_android.interfaces.GrammieGramAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The handling of the GrammieGramAPI calls
 */
public class GrammieGramService {

    private GrammieGramAPI api;
    private CallBack callBack;
    public static String BASE_URL = "https://grammiegram.com/api/";

    /**
     * Make the retrofit api objects that will be used to call the GrammieGram API
     *
     * @param callBack - the activity (which implements the specific callback interface)
     *                 that will handle the responses from the api.
     */
    public GrammieGramService(CallBack callBack) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.api = retrofit.create(GrammieGramAPI.class);
        this.callBack = callBack;
    }

    /**
     * Handling of the API response when getBoards is called
     */
    public void getBoards() {
        //get the api call object
        Call<BoardListResponse> call = api.getBoards();

        //execute asynchronously to avoid hogging UI thread
        call.enqueue(new Callback<BoardListResponse>() {

            @Override
            public void onResponse(Call<BoardListResponse> call, Response<BoardListResponse> response) {
                //handle api response
                if (response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                } else {
                    callBack.onServerError(response.code(), response.errorBody());
                    // must close response to avoid memory leaks
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(Call<BoardListResponse> call, Throwable t) {
                //network error, unable to connect with the server for any reason
                callBack.onNetworkError(t.toString());
            }
        });
    }

    /**
     * Handling of the API response when login is called
     *
     * @param username - username to login to the database with for an existing account
     * @param password - password to login to the database with for an existing account
     */
    public void login(String username, String password) {
        //get the api call object
        Call<LoginResponse> call = api.login(username, password);

        //execute asynchronously to avoid hogging UI thread
        call.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                //handle api response
                if (response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                } else {
                    callBack.onServerError(response.code(), response.errorBody());
                    // must close response to avoid memory leaks
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                //network error, unable to connect with the server for any reason
                callBack.onNetworkError(t.toString());
            }
        });
    }

    /**
     * Handling of the API response when getGrams is called
     *
     * @param boardDisplayName - the board to get grams for
     */
    public void getGrams(String boardDisplayName) {
        //get the api call object
        Call<GramsListResponse> call = api.getGrams(boardDisplayName);

        //execute asynchronously to avoid hogging UI thread
        call.enqueue(new Callback<GramsListResponse>() {

            @Override
            public void onResponse(Call<GramsListResponse> call, Response<GramsListResponse> response) {
                //handle api response
                if (response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                } else {
                    callBack.onServerError(response.code(), response.errorBody());
                    // must close response to avoid memory leaks
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(Call<GramsListResponse> call, Throwable t) {
                //network error, unable to connect with the server for any reason
                callBack.onNetworkError(t.toString());
            }
        });
    }

    /**
     * Handling of the API response when updateSettings is called
     *
     * @param fontSize - size of the text to display on board in pt units
     * @param audioNotification - whether or not to have active audio notifications
     * @param profanityFilter - whether or not to filter profanity
     */
    public void updateSettings(int fontSize, boolean audioNotification, boolean profanityFilter) {
        //get the api call object
        Call<SettingsResponse> call = api.updateSettings(fontSize, audioNotification, profanityFilter);

        //execute asynchronously to avoid hogging UI thread
        call.enqueue(new Callback<SettingsResponse>() {

            @Override
            public void onResponse(Call<SettingsResponse> call, Response<SettingsResponse> response) {
                //handle api response
                if (response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                } else {
                    callBack.onServerError(response.code(), response.errorBody());
                    // must close response to avoid memory leaks
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(Call<SettingsResponse> call, Throwable t) {
                //network error, unable to connect with the server for any reason
                callBack.onNetworkError(t.toString());
            }
        });
    }

    public void checkNewGrams(String boardDisplayName) {
        //get the api call object
        Call<CheckNewResponse> call = api.checkNewGrams(boardDisplayName);

        //execute asynchronously to avoid hogging UI thread
        call.enqueue(new Callback<CheckNewResponse>() {

            @Override
            public void onResponse(Call<CheckNewResponse> call, Response<CheckNewResponse> response) {
                //handle api response
                if (response.isSuccessful()) {
                    callBack.onSuccess(response.body()); //TODO: special board activity callback interface that has a success check new method
                } else {
                    callBack.onServerError(response.code(), response.errorBody());
                    // must close response to avoid memory leaks
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(Call<CheckNewResponse> call, Throwable t) {
                //network error, unable to connect with the server for any reason
                callBack.onNetworkError(t.toString());
            }
        });
    }

}
