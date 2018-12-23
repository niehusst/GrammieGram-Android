package com.grammiegram.grammiegram_android.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.grammiegram.grammiegram_android.R;
import com.grammiegram.grammiegram_android.GrammieGramService;
import com.grammiegram.grammiegram_android.POJO.Gram;
import com.grammiegram.grammiegram_android.POJO.GramsListResponse;
import com.grammiegram.grammiegram_android.interfaces.APIResponse;
import com.grammiegram.grammiegram_android.interfaces.CallBack;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;

public class ListGrams_Activity extends AppCompatActivity implements CallBack {
    private GrammieGramService api = new GrammieGramService(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("grammiegram", MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_grams);
        String token = prefs.getString("auth_token", "");
        String displayName = "testBoard";
        api.getGrams(token, displayName);
    }

    public void refresh(View v) {
        SharedPreferences prefs = getSharedPreferences("grammiegram", MODE_PRIVATE);
        String token = prefs.getString("auth_token", "");
        String displayName = "testBoard";
        api.getGrams(token, displayName);
    }

    @Override
    public void onSuccess(APIResponse response) {
        GramsListResponse gramsList = (GramsListResponse) response;
        String firstName = gramsList.getBoardFirstName();
        String displayName = gramsList.getBoardDisplayName();
        List<Gram> grams = gramsList.getGrams();
        TextView greeting = (TextView) findViewById(R.id.greeting);
        int numGrams = grams.size();
        String gramString;
        switch (numGrams) {
        case 0:
            gramString = "you have no grams.";
            break;
        case 1:
            gramString = "you have one gram.";
            break;
        default:
            String num = Integer.toString(numGrams);
            gramString = "you have " + num + " grams.";
            break;
        }
        String greet = "Hello " + firstName + ", " + gramString;
        greeting.setText(greet);
    }

    @Override
    public void onNetworkError(String err) {
        TextView error = (TextView) findViewById(R.id.greeting);
        error.setText(err);
    }

    @Override
    public void onServerError(int err, ResponseBody response) {
        TextView errorText = (TextView) findViewById(R.id.greeting);
        String error;
        try {
            error = response.string();
        } catch (IOException e) {
            error = e.toString(); }
        errorText.setText(error);
    }

}
