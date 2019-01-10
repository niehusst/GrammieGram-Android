package com.grammiegram.grammiegram_android;

import com.grammiegram.grammiegram_android.POJO.Board;
import com.grammiegram.grammiegram_android.POJO.BoardListResponse;
import com.grammiegram.grammiegram_android.POJO.CheckNewResponse;
import com.grammiegram.grammiegram_android.POJO.Gram;
import com.grammiegram.grammiegram_android.POJO.GramsListResponse;
import com.grammiegram.grammiegram_android.POJO.LoginResponse;
import com.grammiegram.grammiegram_android.POJO.UpdateSettingsResponse;
import com.grammiegram.grammiegram_android.interfaces.GrammieGramAPI;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.mock.BehaviorDelegate;

/**
 * Mocks API responses to retrofit object
 */
public class MockGrammieGramService implements GrammieGramAPI {

    private final BehaviorDelegate<GrammieGramAPI> delegate;

    /**
     * Construct the class from a mockRetrofit object
     * @param service
     */
    MockGrammieGramService(BehaviorDelegate<GrammieGramAPI> service) {
        this.delegate = service;
    }

    /**
     * Stubbed API BoardList retrofit response
     * @param auth - auth token
     * @return - stubbed API BoardListResponse object
     */
    @Override
    public Call<BoardListResponse> getBoards(String auth) {
        //create a stubbed instance of api response
        Board board = new Board();
        board.setBoardFirstName("Grammie");
        board.setBoardLastName("Gram");
        board.setBoardDisplayName("grammie");

        ArrayList<Board> boardList = new ArrayList<>();
        boardList.add(board);

        //set stubbed data into response object
        BoardListResponse response = new BoardListResponse();
        response.setUsername("grammiegram");
        response.setFirstName("Gram");
        response.setBoardList(boardList);

        return this.delegate.returningResponse(response).getBoards(auth);
    }

    /**
     * Stubbed API login retrofit response
     * @param username
     * @param password
     * @return - stubbed API LoginResponse object
     */
    @Override
    public Call<LoginResponse> login(String username, String password) {
        //ignore username and password for stubbed response
        //set stubbed data into response object
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAuthenticated(true);
        loginResponse.setToken("t0ken123");

        return this.delegate.returningResponse(loginResponse).login(username, password);
    }

    /**
     * Stubbed API list grams retrofit response
     * @param auth - login token
     * @param boardDisplayName - diplay name of the board to get grams for
     * @return - stubbed API GramsListResponse object
     */
    @Override
    public Call<GramsListResponse> getGrams(String auth, String boardDisplayName) {
        //create a stubbed instance of api response
        Gram gram = new Gram();
        gram.setSenderFirstName("sender");
        gram.setMessage("Hello World");
        gram.setDay(2);
        gram.setHour(3);
        gram.setMinute(30);
        gram.setMonth(1);
        gram.setYear(2000);
        gram.setTill("2018-11-30 05:49:00+00:00");

        ArrayList<Gram> gramList = new ArrayList<>();
        gramList.add(gram);

        //set stubbed data into response object
        GramsListResponse grams = new GramsListResponse();
        grams.setBoardDisplayName("gg1");
        grams.setGrams(gramList);
        grams.setBoardFirstName("Grammie");

        return this.delegate.returningResponse(grams).getGrams(auth, boardDisplayName);
    }

    /**
     * Stubbed API settingsUpdate retrofit response
     * @param auth - login token
     * @param audioNotification - whether to make sounds for new grams
     * @param profanityFilter - whether to strip profanity from grams
     * @return - stubbed API UpdateSettingsResponse object
     */
    @Override
    public Call<UpdateSettingsResponse> updateSettings(String auth, boolean audioNotification, boolean profanityFilter) {
        //set stubbed data into response object
        UpdateSettingsResponse settingsResponse =  new UpdateSettingsResponse();
        settingsResponse.setUpdated(true);

        return this.delegate.returningResponse(settingsResponse).updateSettings(auth, audioNotification, profanityFilter);
    }

    /**
     * Stubbed API checkNewGrams retrofit response
     * @param auth - login token
     * @param boardDisplayName - display name of the board to check for new grams from
     * @return - A Call for a stubbed response object
     */
    @Override
    public Call<CheckNewResponse> checkNewGrams(String auth, String boardDisplayName) {
        //stub response
        Gram gram = new Gram();
        gram.setDay(1);
        gram.setHour(12);
        gram.setMessage("Test Message");
        gram.setMinute(0);
        gram.setMonth(12);
        gram.setSenderFirstName("Sender");
        gram.setTill("2018-11-30 05:49:00+00:00");
        gram.setYear(2018);

        ArrayList<Gram> gramsList = new ArrayList<>();
        gramsList.add(gram);

        CheckNewResponse response = new CheckNewResponse();
        response.setNeeded(true);
        response.setNewGrams(gramsList);

        return this.delegate.returningResponse(response).checkNewGrams(auth, boardDisplayName);
    }

    /*
    later add sendgram, makeboard, getcontacts
     */
}
