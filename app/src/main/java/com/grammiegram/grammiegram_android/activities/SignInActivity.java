package com.grammiegram.grammiegram_android.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.grammiegram.grammiegram_android.services.GrammieGramService;
import com.grammiegram.grammiegram_android.POJO.LoginResponse;
import com.grammiegram.grammiegram_android.R;
import com.grammiegram.grammiegram_android.interfaces.APIResponse;
import com.grammiegram.grammiegram_android.interfaces.CallBack;

import okhttp3.ResponseBody;

public class SignInActivity extends AppCompatActivity implements CallBack {
    private GrammieGramService api = new GrammieGramService(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("grammiegram", MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        // If we already have an authentication token, jump to the board list
        if (prefs.contains("auth_token")) {
            launchBoardListActivity();
        } else {
            // Make the signup link clickable
            TextView no_account = (TextView) findViewById(R.id.no_account);
            no_account.setMovementMethod(LinkMovementMethod.getInstance());
            // Make error text clear when going back to re-enter username/password
            EditText username = (EditText) findViewById(R.id.username);
            EditText password = (EditText) findViewById(R.id.password);
            View.OnFocusChangeListener clrErrTxt = new ClearErrorText();
            username.setOnFocusChangeListener(clrErrTxt);
            password.setOnFocusChangeListener(clrErrTxt);
        }
    }

    public class ClearErrorText implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            TextView errorText = (TextView) findViewById(R.id.error);
            String err = errorText.getText().toString();
            if (hasFocus && !(err.equals(null) || err.equals(""))) {
                errorText.setText("");
            }
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
        SharedPreferences prefs = getSharedPreferences("grammiegram", MODE_PRIVATE);
        LoginResponse login = (LoginResponse) response;
        if (login.getAuthenticated()) {
            String token = login.getToken();
            prefs.edit().putString("auth_token", token).apply();
            launchBoardListActivity();
        } else {
            invalidCredentials();
        }
    }

    @Override
    public void onNetworkError(String err) {
        TextView errorText = (TextView) findViewById(R.id.error);
        errorText.setText(getString(R.string.wifi_error));
    }

    @Override
    public void onServerError(int err, ResponseBody response)
    {
        TextView errorText = (TextView) findViewById(R.id.error);
        errorText.setText(getString(R.string.server_error));
    }

    public void invalidCredentials() {
        TextView errorText = (TextView) findViewById(R.id.error);
        errorText.setText(getString(R.string.error_invalid_login));
    }

    public void launchBoardListActivity() {
        Intent intent = new Intent(this, BoardListActivity.class);
        startActivity(intent);
        finish();
    }
}
