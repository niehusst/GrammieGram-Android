package com.grammiegram.grammiegram_android.interfaces;

import com.grammiegram.grammiegram_android.POJO.BoardListResponse;
import com.grammiegram.grammiegram_android.POJO.CheckNewResponse;
import com.grammiegram.grammiegram_android.POJO.GramsListResponse;
import com.grammiegram.grammiegram_android.POJO.LoginResponse;
import com.grammiegram.grammiegram_android.POJO.SettingsResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GrammieGramAPI {

    @GET("board_list")
    Call<BoardListResponse> getBoards(@Header("Authorization") String auth);

    @POST("update_settings")
    Call<SettingsResponse> updateSettings(@Header("Authorization") String auth, @Field("audio_notifications") boolean audioNotifications, @Field("profanity_filter") boolean profanityFilter);

    @POST("login")
    Call<LoginResponse> login(String username, String password);

    @POST("list_grams")
    Call<GramsListResponse> getGrams(@Header("Authorization") String auth, String boardDisplayName); //TODO: requires @Query

    @POST("check_new_add")
    Call<CheckNewResponse> checkNewGrams(@Header("Authorization") String auth, String boardDisplayName);

    /*
    make board, send gram, get contacts
     */
}
