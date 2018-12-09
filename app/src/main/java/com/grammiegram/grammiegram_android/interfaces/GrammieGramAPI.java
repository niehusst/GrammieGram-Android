package com.grammiegram.grammiegram_android.interfaces;

import com.grammiegram.grammiegram_android.POJO.BoardListResponse;
import com.grammiegram.grammiegram_android.POJO.GramsListResponse;
import com.grammiegram.grammiegram_android.POJO.LoginResponse;
import com.grammiegram.grammiegram_android.POJO.SettingsResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GrammieGramAPI {

    @GET("board_list")
    Call<BoardListResponse> getBoards();

    @POST("update_settings")
    Call<SettingsResponse> updateSettings(int fontSize, boolean audioNotifications, boolean profanityFilter);

    @FormUrlEncoded
    @POST("login/")
    Call<LoginResponse> login(@Field("username") String username, @Field("password") String password);

    @POST("list_grams")
    Call<GramsListResponse> getGrams();
}
