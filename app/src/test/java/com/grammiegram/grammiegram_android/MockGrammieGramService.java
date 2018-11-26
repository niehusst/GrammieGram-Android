package com.grammiegram.grammiegram_android;

import com.grammiegram.grammiegram_android.POJO.Board;
import com.grammiegram.grammiegram_android.POJO.BoardListResponse;
import com.grammiegram.grammiegram_android.POJO.Gram;
import com.grammiegram.grammiegram_android.POJO.GramsListResponse;
import com.grammiegram.grammiegram_android.POJO.LoginResponse;
import com.grammiegram.grammiegram_android.POJO.SettingsResponse;
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
    public MockGrammieGramService(BehaviorDelegate<GrammieGramAPI> service) {
        this.delegate = service;
    }

    /**
     * Stubbed API BoardList retrofit response
     * @return - stubbed API BoardListResponse object
     */
    @Override
    public Call<BoardListResponse> getBoards() {
        //create a stubbed instance of api response
        Board board = new Board();
        board.setFirstName("Grammie");
        board.setLastName("Gram");

        ArrayList<Board> boardList = new ArrayList<>();
        boardList.add(board);

        ArrayList<String> displayNames = new ArrayList<>();
        displayNames.add("grammie");

        //set stubbed data into response object
        BoardListResponse response = new BoardListResponse();
        response.setUsername("grammiegram");
        response.setFirstName("Gram");
        response.setBoardList(boardList);
        response.setBoardNames(displayNames);

        return this.delegate.returningResponse(response).getBoards();
    }

    /**
     * Stubbed API login retrofit response
     * @return - stubbed API LoginResponse object
     */
    @Override
    public Call<LoginResponse> login(String username, String password) { //TODO; correct way to use user/pass?
        //create a stubbed instance of api response

        //send username and password
        //receive authenticated

        //set stubbed data into response object
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAuthenticated(true);
        loginResponse.setToken("t0ken123");

        return this.delegate.returningResponse(loginResponse).login(username, password);
    }

    /**
     * Stubbed API list grams retrofit response
     * @return - stubbed API GramsListResponse object
     */
    @Override
    public Call<GramsListResponse> getGrams() { //TODO: does this take the login token?? what to do about headers??
        //create a stubbed instance of api response
        Gram gram = new Gram();
        gram.setSenderFirstName("sender");
        gram.setMessage("Hello World");

        ArrayList<Gram> gramList = new ArrayList<>();
        gramList.add(gram);

        //set stubbed data into response object
        GramsListResponse grams = new GramsListResponse();
        grams.setBoardDisplayName("gg1");
        grams.setGrams(gramList);
        grams.setBoardFirstName("Grammie");

        return this.delegate.returningResponse(grams).getGrams();
    }

    /**
     * Stubbed API settingsUpdate retrofit response
     * @return - stubbed API SettingsResponse object
     */
    @Override
    public Call<SettingsResponse> updateSettings(boolean audioNotification, boolean profanityFilter, int fontSize) {
        //set stubbed data into response object
        SettingsResponse settingsResponse =  new SettingsResponse();
        settingsResponse.setUpdated(true);

        return this.delegate.returningResponse(settingsResponse).updateSettings(audioNotification, profanityFilter, fontSize);
    }

    /*
    later add sendgram, makeboard, getcontacts
     */
}
