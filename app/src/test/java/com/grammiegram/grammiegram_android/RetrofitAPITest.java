package com.grammiegram.grammiegram_android;

import android.content.Context;

import com.grammiegram.grammiegram_android.POJO.BoardListResponse;
import com.grammiegram.grammiegram_android.POJO.CheckNewResponse;
import com.grammiegram.grammiegram_android.POJO.ErrorResponse;
import com.grammiegram.grammiegram_android.POJO.GramsListResponse;
import com.grammiegram.grammiegram_android.POJO.LoginResponse;
import com.grammiegram.grammiegram_android.POJO.SettingsResponse;
import com.grammiegram.grammiegram_android.interfaces.GrammieGramAPI;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

@RunWith(JUnit4.class)
public class RetrofitAPITest {
    private Retrofit retrofit;
    private MockRetrofit mockRetrofit;
    private static final String BASE_URL = "https://grammiegram.com/api/";

    /**
     * Set up the MockRetrofit object
     */
    @Before
    public void setUp() {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NetworkBehavior behavior = NetworkBehavior.create();
        behavior.setFailurePercent(0); //dont allow it to randomly fail in stubbed test environment

        mockRetrofit = new MockRetrofit.Builder(retrofit)
                .networkBehavior(behavior)
                .build();
    }

    @After
    public void tearDown() {
        this.retrofit = null;
        this.mockRetrofit = null;
    }

    @Test
    public void boardListSuccessTest() throws IOException {
        //create mock retrofit API that returns stubbed successful responses
        BehaviorDelegate<GrammieGramAPI> delegate = mockRetrofit.create(GrammieGramAPI.class);
        GrammieGramAPI mockAPI = new MockGrammieGramService(delegate);

        //get and execute api call
        Call<BoardListResponse> boardList = mockAPI.getBoards("Token 123");
        Response<BoardListResponse> response = boardList.execute();

        //assert response has expected data
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals("grammiegram", response.body().getUsername());
        Assert.assertEquals("Gram", response.body().getFirstName());
        Assert.assertEquals("Grammie", response.body().getBoardList().get(0).getBoardFirstName());
        Assert.assertEquals("Gram", response.body().getBoardList().get(0).getBoardLastName());
        Assert.assertEquals("grammie", response.body().getBoardList().get(0).getBoardDisplayName());
    }

    @Test
    public void boardListFailureTest() throws IOException {
        //create mock retrofit API that returns stubbed unsuccessful responses
        BehaviorDelegate<GrammieGramAPI> delegate = mockRetrofit.create(GrammieGramAPI.class);
        GrammieGramAPI mockAPI = new MockGrammieGramServiceError(delegate);

        //get and execute api call
        Call<BoardListResponse> boardList = mockAPI.getBoards("Token 123");
        Response<BoardListResponse> response = boardList.execute();

        //assert response has expected data
        Assert.assertFalse(response.isSuccessful());

        Converter<ResponseBody, ErrorResponse> errorConverter =
                retrofit.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
        ErrorResponse error = errorConverter.convert(response.errorBody());

        Assert.assertEquals(500, response.code());
        Assert.assertEquals("ServerError", error.getError());
    }

    @Test
    public void loginSuccessTest() throws IOException {
        //create mock retrofit API that returns stubbed successful responses
        BehaviorDelegate<GrammieGramAPI> delegate = mockRetrofit.create(GrammieGramAPI.class);
        GrammieGramAPI mockAPI = new MockGrammieGramService(delegate);

        //get and execute api call
        Call<LoginResponse> login = mockAPI.login("username", "password");
        Response<LoginResponse> response = login.execute();

        //assert response has expected data
        Assert.assertTrue(response.isSuccessful());
        Assert.assertTrue(response.body().getAuthenticated());
        Assert.assertEquals("t0ken123", response.body().getToken());
    }

    @Test
    public void loginFailureTest() throws IOException {
        //create mock retrofit API that returns stubbed unsuccessful responses
        BehaviorDelegate<GrammieGramAPI> delegate = mockRetrofit.create(GrammieGramAPI.class);
        GrammieGramAPI mockAPI = new MockGrammieGramServiceError(delegate);

        //get and execute api call
        Call<LoginResponse> login = mockAPI.login("username", "password");
        Response<LoginResponse> response = login.execute();

        //assert response has expected data
        Assert.assertFalse(response.isSuccessful());
        //convert expected returned response to ErrorResponse
        Converter<ResponseBody, ErrorResponse> errorConverter =
                retrofit.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
        ErrorResponse error = errorConverter.convert(response.errorBody());

        Assert.assertEquals(400, response.code());
        Assert.assertEquals("Please provide correct username and password", error.getError());
    }

    @Test
    public void getGramsSuccessTest() throws IOException {
        //create mock retrofit API that returns stubbed successful responses
        BehaviorDelegate<GrammieGramAPI> delegate = mockRetrofit.create(GrammieGramAPI.class);
        GrammieGramAPI mockAPI = new MockGrammieGramService(delegate);

        //get and execute api call
        Call<GramsListResponse> grams = mockAPI.getGrams("Token 123", "display");
        Response<GramsListResponse> response = grams.execute();

        //assert response has expected data
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals("gg1", response.body().getBoardDisplayName());
        Assert.assertEquals("Grammie", response.body().getBoardFirstName());
        Assert.assertEquals("sender", response.body().getGrams().get(0).getSenderFirstName());
        Assert.assertEquals("Hello World", response.body().getGrams().get(0).getMessage());
        Assert.assertEquals("2018-11-30 05:49:00+00:00", response.body().getGrams().get(0).getTill());
        Assert.assertEquals(new Integer(2), response.body().getGrams().get(0).getDay());
        Assert.assertEquals(new Integer(3), response.body().getGrams().get(0).getHour());
        Assert.assertEquals(new Integer(30), response.body().getGrams().get(0).getMinute());
        Assert.assertEquals(new Integer(1), response.body().getGrams().get(0).getMonth());
        Assert.assertEquals(new Integer(2000), response.body().getGrams().get(0).getYear());
    }

    @Test
    public void getGramsFailureTest() throws IOException {
        //create mock retrofit API that returns stubbed unsuccessful responses
        BehaviorDelegate<GrammieGramAPI> delegate = mockRetrofit.create(GrammieGramAPI.class);
        GrammieGramAPI mockAPI = new MockGrammieGramServiceError(delegate);

        //get and execute api call
        Call<GramsListResponse> grams = mockAPI.getGrams("Token 123", "display");
        Response<GramsListResponse> response = grams.execute();

        //assert response has expected data
        Assert.assertFalse(response.isSuccessful());
        //convert expected returned response to ErrorResponse
        Converter<ResponseBody, ErrorResponse> errorConverter =
                retrofit.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
        ErrorResponse error = errorConverter.convert(response.errorBody());

        Assert.assertEquals(500, response.code());
        Assert.assertEquals("ServerError", error.getError());
    }

    @Test
    public void updatingSettingsSuccessTest() throws IOException {
        //create mock retrofit API that returns stubbed successful responses
        BehaviorDelegate<GrammieGramAPI> delegate = mockRetrofit.create(GrammieGramAPI.class);
        GrammieGramAPI mockAPI = new MockGrammieGramService(delegate);

        //get and execute api call
        Call<SettingsResponse> update = mockAPI.updateSettings("Token 123", true, true);
        Response<SettingsResponse> response = update.execute();

        //assert response has expected data
        Assert.assertTrue(response.isSuccessful());
        Assert.assertTrue(response.body().getUpdated());
    }

    @Test
    public void updateSettingsFailureTest() throws IOException {
        //create mock retrofit API that returns stubbed unsuccessful responses
        BehaviorDelegate<GrammieGramAPI> delegate = mockRetrofit.create(GrammieGramAPI.class);
        GrammieGramAPI mockAPI = new MockGrammieGramServiceError(delegate);

        //get and execute api call (for clarity, input has no effect on resulting stubbed outputs)
        Call<SettingsResponse> update = mockAPI.updateSettings("Token 123", false, false);
        Response<SettingsResponse> response = update.execute();

        //assert response has expected data
        Assert.assertFalse(response.isSuccessful());
        //convert expected returned response to ErrorResponse
        Converter<ResponseBody, ErrorResponse> errorConverter =
                retrofit.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
        ErrorResponse error = errorConverter.convert(response.errorBody());

        Assert.assertEquals(500, response.code());
        Assert.assertEquals("ServerError", error.getError());
    }

    @Test
    public void checkNewGramsSuccessTest() throws IOException {
        //create mock retrofit API that returns stubbed successful responses
        BehaviorDelegate<GrammieGramAPI> delegate = mockRetrofit.create(GrammieGramAPI.class);
        GrammieGramAPI mockAPI = new MockGrammieGramService(delegate);

        //get and execute api call
        Call<CheckNewResponse> checkNew = mockAPI.checkNewGrams("Token 123", "display");
        Response<CheckNewResponse> response = checkNew.execute();

        //assert response has expected data
        Assert.assertTrue(response.isSuccessful());
        Assert.assertTrue(response.body().getNeeded());
        Assert.assertEquals("Sender", response.body().getNewGrams().get(0).getSenderFirstName());
        Assert.assertEquals("Test Message", response.body().getNewGrams().get(0).getMessage());
        Assert.assertEquals("2018-11-30 05:49:00+00:00", response.body().getNewGrams().get(0).getTill());
        Assert.assertEquals(new Integer(1), response.body().getNewGrams().get(0).getDay());
        Assert.assertEquals(new Integer(12), response.body().getNewGrams().get(0).getHour());
        Assert.assertEquals(new Integer(0), response.body().getNewGrams().get(0).getMinute());
        Assert.assertEquals(new Integer(12), response.body().getNewGrams().get(0).getMonth());
        Assert.assertEquals(new Integer(2018), response.body().getNewGrams().get(0).getYear());
    }

    @Test
    public void checkNewGramsFailureTest() throws IOException {
        //create mock retrofit API that returns stubbed unsuccessful responses
        BehaviorDelegate<GrammieGramAPI> delegate = mockRetrofit.create(GrammieGramAPI.class);
        GrammieGramAPI mockAPI = new MockGrammieGramServiceError(delegate);

        //get and execute api call (for clarity, input has no effect on resulting stubbed outputs)
        Call<CheckNewResponse> checkNew = mockAPI.checkNewGrams("Token 123", "gramPappy");
        Response<CheckNewResponse> response = checkNew.execute();

        //assert response has expected data
        Assert.assertFalse(response.isSuccessful());
        //convert expected returned response to ErrorResponse
        Converter<ResponseBody, ErrorResponse> errorConverter =
                retrofit.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
        ErrorResponse error = errorConverter.convert(response.errorBody());

        Assert.assertEquals(500, response.code());
        Assert.assertEquals("ServerError", error.getError());
    }
}
