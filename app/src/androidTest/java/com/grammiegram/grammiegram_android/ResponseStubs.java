package com.grammiegram.grammiegram_android;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.grammiegram.grammiegram_android.POJO.Board;
import com.grammiegram.grammiegram_android.POJO.BoardListResponse;
import com.grammiegram.grammiegram_android.POJO.ErrorResponse;

import java.util.ArrayList;

/**
 * A helper class to get stubbed responses
 */
public class ResponseStubs {

    public static JsonObject BoardListResponseStub() {
        //setup response
        /*
        BoardListResponse response = new BoardListResponse();
        response.setUsername("grammie");
        response.setFirstName("gram");
        ArrayList<Board> lst = new ArrayList<>();
        for(int i = 0; i < 30; i++) {
            Board board = new Board();
            board.setBoardFirstName("First");
            board.setBoardLastName("Last " + (i+1));
            board.setBoardDisplayName("eb" + (i+1));
        }
        response.setBoardList(lst);
        */
        JsonObject json = new JsonObject();
        json.addProperty("username", "grammie");
        json.addProperty("first_name", "gram");

        JsonArray jArray = new JsonArray();
        for(int i = 0; i < 30; i++) {
            JsonObject board = new JsonObject();
            board.addProperty("board_first_name", "First");
            board.addProperty("board_last_name", "Last " + (i+1));
            board.addProperty("board_display_name", "eb" + (i+1));
            jArray.add(board);
        }
        json.add("board_list", jArray);

        //return json object
        return json;
    }

    public static JsonObject ErrorResponseStub() {
        //setup stub
        JsonObject json = new JsonObject();
        json.addProperty("error", "Server Error");

        //return json
        return json;
    }
}
