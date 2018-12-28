package com.grammiegram.grammiegram_android.interfaces;

import com.grammiegram.grammiegram_android.POJO.BoardListResponse;
import com.grammiegram.grammiegram_android.POJO.CheckNewResponse;
import com.grammiegram.grammiegram_android.POJO.GramsListResponse;
import com.grammiegram.grammiegram_android.POJO.LoginResponse;
import com.grammiegram.grammiegram_android.POJO.SettingsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GrammieGramAPI {

    @GET("list_boards/")
    Call<BoardListResponse> getBoards(@Header("Authorization") String auth);

    @POST("update_settings/")
    Call<SettingsResponse> updateSettings(@Header("Authorization") String auth, @Body boolean audioNotifications, @Body boolean profanityFilter);

    @FormUrlEncoded
    @POST("login/")
    Call<LoginResponse> login(@Field("username") String username, @Field("password") String password);

    @POST("list_grams/")
    Call<GramsListResponse> getGrams(@Header("Authorization") String auth, @Body String boardDisplayName);

    @POST("check_new_add/")
    Call<CheckNewResponse> checkNewGrams(@Header("Authorization") String auth, @Body String boardDisplayName);

    /*
    make board, send gram, get contacts

    this multipart decorator may be necessary for getting picture media get/send Grams???
    @Multipart
@PUT("user/photo")
Call<User> updateUser(@Part("photo") RequestBody photo, @Part("description") RequestBody description);
     */
}
