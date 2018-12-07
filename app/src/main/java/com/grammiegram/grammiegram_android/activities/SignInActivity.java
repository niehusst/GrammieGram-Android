package com.grammiegram.grammiegram_android.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.grammiegram.grammiegram_android.GrammieGramService;
import com.grammiegram.grammiegram_android.POJO.LoginResponse;
import com.grammiegram.grammiegram_android.R;
import com.grammiegram.grammiegram_android.interfaces.APIResponse;
import com.grammiegram.grammiegram_android.interfaces.CallBack;
import com.grammiegram.grammiegram_android.interfaces.LoginCallBack;

import okhttp3.ResponseBody;

public class SignInActivity extends AppCompatActivity implements CallBack {
    private GrammieGramService api;
    private SharedPreferences prefs = getSharedPreferences("grammiegram", MODE_PRIVATE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // If we already have an authentication token, jump to the board list
        if (prefs.contains("Token")) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.sign_in);
            // Make the signup link clickable
            TextView no_account = (TextView) findViewById(R.id.no_account);
            no_account.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            launchBoardListActivity();
        }
    }

    public void signIn(View v) {
        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);
        String theUsername = username.getText().toString();
        String thePassword = password.getText().toString();
        api.login(theUsername, thePassword);
    }

    @Override
    public void onSuccess(APIResponse response) {
        LoginResponse login = (LoginResponse) response;
        if (login.getAuthenticated()) {
            String token = login.getToken();
            prefs.edit().putString("auth_token", token);
            launchBoardListActivity();
        } else {
            invalidCredentials();
        }
    }

    @Override
    public void onNetworkError(String err) {
        // display error message about network error
    }

    @Override
    public void onServerError(int err, ResponseBody response)
    {
        // display error message about server error
    }

    public void invalidCredentials() {
        // display error message about credentials error
    }

    public void launchBoardListActivity() {
        // launch board list activity
    }
}

