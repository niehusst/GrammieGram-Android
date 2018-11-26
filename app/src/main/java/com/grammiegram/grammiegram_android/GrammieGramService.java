package com.grammiegram.grammiegram_android;

import com.grammiegram.grammiegram_android.POJO.BoardListResponse;
import com.grammiegram.grammiegram_android.POJO.GramsListResponse;
import com.grammiegram.grammiegram_android.POJO.LoginResponse;
import com.grammiegram.grammiegram_android.POJO.SettingsResponse;
import com.grammiegram.grammiegram_android.interfaces.APIResponse;
import com.grammiegram.grammiegram_android.interfaces.CallBack;
import com.grammiegram.grammiegram_android.interfaces.GrammieGramAPI;

import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The handling of the GrammieGramAPI calls
 */
public class GrammieGramService {

    private Retrofit retrofit;
    private GrammieGramAPI api;
    private CallBack callBack;

    /**
     * Make the retrofit api objects that will be used to call the GrammieGram API
     *
     * @param callBack - the activity (which implements the specific callback interface)
     *                 that will handle the responses from the api.
     */
    public GrammieGramService(CallBack callBack) {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://grammiegram.com/api/")
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
     */
    public void getGrams() {
        //get the api call object
        Call<GramsListResponse> call = api.getGrams();

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

}
