package com.grammiegram.grammiegram_android;

import android.content.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

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

        mockRetrofit = new MockRetrofit.Builder(retrofit)
                .networkBehavior(behavior)
                .build();
    }

    @Test
    public void boardListSuccessTest() {
        //create mock retrofit API that returns stubbed successful responses
        BehaviorDelegate<GrammieGramService> delegate = mockRetrofit.create(GrammieGramService.class);
        GrammieGramService mockAPI = new MockGrammieGramService(delegate);

        //get and execute api call
        Call<BoardListResponse> boardList = mockAPI.getBoards();
        Response<BoardListResponse> response = boardList.execute();

        //assert response has expected data
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals("grammiegram", response.body().getUsername());
        Assert.assertEquals("Gram", response.body().getFirstName());
        Assert.assertEquals("Grammie", response.body().getBoardList().get(0).getFirstName());
        Assert.assertEquals("Gram", response.body().getBoardList().get(0).getLastName());
        Assert.assertEquals("grammie", response.body().getBoardNames().get(0));
    }

    @Test
    public void boardListFailureTest() {
        //create mock retrofit API that returns stubbed unsuccessful responses
        BehaviorDelegate<GrammieGramService> delegate = mockRetrofit.create(GrammieGramService.class);
        GrammieGramService mockAPI = new MockGrammieGramServiceError(delegate);

        //get and execute api call
        Call<BoardListResponse> boardList = mockAPI.getBoards();
        Response<BoardListResponse> response = boardList.execute();

        //assert response has expected data
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals("ServerError", response.body().getError());
    }

    @Test
    public void loginSuccessTest() {
        //create mock retrofit API that returns stubbed successful responses
        BehaviorDelegate<GrammieGramService> delegate = mockRetrofit.create(GrammieGramService.class);
        GrammieGramService mockAPI = new MockGrammieGramService(delegate);

        //get and execute api call
        Call<LoginResponse> login = mockAPI.login();
        Response<LoginResponse> response = login.execute();

        //assert response has expected data
        Assert.assertTrue(response.isSuccessful());
        Assert.assertTrue(response.body().getAuthenticated());
        Assert.assertEquals("t0ken123", response.body().getToken());
    }

    @Test
    public void loginFailureTest() {
        //create mock retrofit API that returns stubbed unsuccessful responses
        BehaviorDelegate<GrammieGramService> delegate = mockRetrofit.create(GrammieGramService.class);
        GrammieGramService mockAPI = new MockGrammieGramServiceError(delegate);

        //get and execute api call
        Call<LoginResponse> login = mockAPI.login();
        Response<LoginResponse> response = login.execute();

        //assert response has expected data
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals("Please provide correct username and password", response.body().getError());
    }

    @Test
    public void getGramsSuccessTest() {
        //create mock retrofit API that returns stubbed successful responses
        BehaviorDelegate<GrammieGramService> delegate = mockRetrofit.create(GrammieGramService.class);
        GrammieGramService mockAPI = new MockGrammieGramService(delegate);

        //get and execute api call
        Call<GramsListResponse> grams = mockAPI.getGrams();
        Response<GramsListResponse> response = grams.execute();

        //assert response has expected data
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals("gg1", response.body().getBoardDisplayName());
        Assert.assertEquals("Grammie", response.body().getBoardFirstName());
        Assert.assertEquals("sender", response.body().getGrams().get(0).getSenderFirstName());
        Assert.assertEquals("Hello World", response.body().getGrams().get(0).getMessage());
    }

    @Test
    public void getGramsFailureTest() {
        //create mock retrofit API that returns stubbed unsuccessful responses
        BehaviorDelegate<GrammieGramService> delegate = mockRetrofit.create(GrammieGramService.class);
        GrammieGramService mockAPI = new MockGrammieGramServiceError(delegate);

        //get and execute api call
        Call<GramsListResponse> grams = mockAPI.getGrams();
        Response<GramsListResponse> response = grams.execute();

        //assert response has expected data
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals("ServerError", response.body().getError());
    }

    @Test
    public void updatingSettingsSuccessTest() {
        //create mock retrofit API that returns stubbed successful responses
        BehaviorDelegate<GrammieGramService> delegate = mockRetrofit.create(GrammieGramService.class);
        GrammieGramService mockAPI = new MockGrammieGramService(delegate);

        //get and execute api call
        Call<SettingsResponse> update = mockAPI.updateSettings();
        Response<SettingsResponse> response = update.execute();

        //assert response has expected data
        Assert.assertTrue(response.isSuccessful());
        Assert.assertTrue(response.body().getUpdated());
    }

    @Test
    public void updateSettingsFailureTest() {
        //create mock retrofit API that returns stubbed unsuccessful responses
        BehaviorDelegate<GrammieGramService> delegate = mockRetrofit.create(GrammieGramService.class);
        GrammieGramService mockAPI = new MockGrammieGramServiceError(delegate);

        //get and execute api call
        Call<SettingsResponse> update = mockAPI.updateSettings();
        Response<SettingsResponse> response = update.execute();

        //assert response has expected data
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals("ServerError", response.body().getError());
    }
}