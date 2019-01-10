package com.grammiegram.grammiegram_android.interfaces;

import com.grammiegram.grammiegram_android.POJO.BoardListResponse;
import com.grammiegram.grammiegram_android.POJO.CheckNewResponse;
import com.grammiegram.grammiegram_android.POJO.GetSettingsResponse;
import com.grammiegram.grammiegram_android.POJO.GramsListResponse;
import com.grammiegram.grammiegram_android.POJO.LoginResponse;
import com.grammiegram.grammiegram_android.POJO.UpdateSettingsResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GrammieGramAPI {

    @GET("list_boards/")
    Call<BoardListResponse> getBoards(@Header("Authorization") String auth);

    @FormUrlEncoded
    @POST("update_settings/")

    Call<UpdateSettingsResponse> updateSettings(@Header("Authorization") String auth,
                                                @Field("audio_notifications") String audioNotifications,
                                                @Field("profanity_filter") boolean profanityFilter);

    @FormUrlEncoded
    @POST("login/")
    Call<LoginResponse> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("list_grams/") //TODO: make it possible to receive image grams!!!
    Call<GramsListResponse> getGrams(@Header("Authorization") String authToken, @Field("display_name") String displayName);

    @FormUrlEncoded
    @POST("check_new_add/")
    Call<CheckNewResponse> checkNewGrams(@Header("Authorization") String auth, @Field("display_name") String displayName);

    @GET("get_settings/")
    Call<GetSettingsResponse> getSettings(@Header("Authorization") String auth);

    /*
    make board, send gram, get contacts

    this multipart decorator may be necessary for getting picture media get/send Grams???
    @Multipart
@PUT("user/photo")
Call<User> updateUser(@Part("photo") RequestBody photo, @Part("description") RequestBody description);
     */
}
