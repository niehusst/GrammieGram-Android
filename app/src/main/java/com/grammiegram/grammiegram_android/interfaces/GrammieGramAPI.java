package com.grammiegram.grammiegram_android.interfaces;

import com.grammiegram.grammiegram_android.POJO.BoardListResponse;
import com.grammiegram.grammiegram_android.POJO.CheckNewResponse;
import com.grammiegram.grammiegram_android.POJO.GramsListResponse;
import com.grammiegram.grammiegram_android.POJO.LoginResponse;
import com.grammiegram.grammiegram_android.POJO.SettingsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GrammieGramAPI {

    @GET("board_list")
    Call<BoardListResponse> getBoards();

    @POST("update_settings")
    Call<SettingsResponse> updateSettings(int fontSize, boolean audioNotifications, boolean profanityFilter);

    @POST("login")
    Call<LoginResponse> login(String username, String password);

    @POST("list_grams")
    Call<GramsListResponse> getGrams(String boardDisplayName);

    @POST("check_new_add")
    Call<CheckNewResponse> checkNewGrams(String boardDisplayName);

    /*
    make board, send gram, get contacts
     */
}
