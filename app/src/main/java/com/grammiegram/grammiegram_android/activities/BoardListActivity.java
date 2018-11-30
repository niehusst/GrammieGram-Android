package com.grammiegram.grammiegram_android.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.grammiegram.grammiegram_android.GrammieGramService;
import com.grammiegram.grammiegram_android.POJO.BoardListResponse;
import com.grammiegram.grammiegram_android.R;
import com.grammiegram.grammiegram_android.adapters.BoardListRecyclerAdapter;
import com.grammiegram.grammiegram_android.fragments.SettingsFragment;
import com.grammiegram.grammiegram_android.interfaces.APIResponse;
import com.grammiegram.grammiegram_android.interfaces.CallBack;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

public class BoardListActivity extends AppCompatActivity implements CallBack {

    private GrammieGramService api;

    //success views
    @BindView(R.id.board_recycler)
    public RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    //loading views
    @BindView(R.id.progress_dialogue)
    public ProgressBar dialogue;

    //err views
    @BindView(R.id.error_text)
    public TextView errText;
    @BindView(R.id.error_image)
    public ImageView errImg;
    @BindView(R.id.retry_button)
    public Button errRetryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_list);

        //setup api to use this activities callbacks
        api = new GrammieGramService(this);

        //bind views for this activity
        ButterKnife.bind(this);

        //set custom action bar
        setSupportActionBar(toolbar);

        //put spacing between rv items
        recyclerView.addItemDecoration(new DividerItemDecoration(BoardListActivity.this,
                DividerItemDecoration.VERTICAL));


        //define onclick listeners

    }

    /**
     * inflates the toolbar menu at top of activity
     *
     * @param menuParam - the menu
     * @return - true (correctly inflated)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menuParam) {
        //inflate toolbar menu
        getMenuInflater().inflate(R.menu.tool_bar, menuParam);
        return true;
    }

    /**
     * Handle user click on menu item
     * @param item - item clicked by user
     * @return - boolean, true if hancled correctly
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_button:
                // User chose the "Settings" item, launch settings fragment
                launchSettingsFragment();
                return true;

            case R.id.logout_button:
                // User chose the "logout" action, go back to login page
                launchLoginActivity();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    /**
     * Launches the board activity of the selected board item from recycler view
     */
    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        //TODO: delete saved data from preferences?
        startActivity(intent);
        finish();
    }

    /**
     * Launches the board activity of the selected board item from recycler view
     */
    private void launchSettingsFragment() {
        Intent intent = new Intent(this, SettingsFragment.class);
        startActivity(intent);
    }

    /**
     * Call the GrammieGram API to fill in the recycler view
     */
    public void getBoards() {
        //start progress dialogue at start of loading sequence
        dialogue.setVisibility(View.VISIBLE);

        //api call (handling goes to overridden on___ methods)
        api.getBoards();

        //finished loading, make dialogue invisible again
        dialogue.setVisibility(View.GONE);
    }

    /**
     * Handle successful api response
     * @param response - BoardListResponse object containing api response data
     */
    @Override
    public void onSuccess(APIResponse response) {
        BoardListResponse bl = (BoardListResponse) response;

        //set response data into the adapter
        BoardListRecyclerAdapter adapter = new BoardListRecyclerAdapter(bl, this);

        //set recycler view w/ adapter
        recyclerView.setAdapter(adapter);
    }

    /**
     * Handle no api response, no wifi connection
     * @param error - Throwable converted to string
     */
    @Override
    public void onNetworkError(String error) {
        //set error views visible
        errImg.setVisibility(View.VISIBLE);
        errText.setVisibility(View.VISIBLE);
        errRetryBtn.setVisibility(View.VISIBLE);

        errText.setText(R.string.wifi_error);

        //make toast
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    /**
     * Handle failed api response
     * @param code - the error code
     * @param body - error text
     */
    @Override
    public void onServerError(int code, ResponseBody body) {
        //set error views visible
        errImg.setVisibility(View.VISIBLE);
        errText.setVisibility(View.VISIBLE);
        errRetryBtn.setVisibility(View.VISIBLE);

        errText.setText(R.string.server_error);

        //make toast
        Toast.makeText(this, code + " Server Error", Toast.LENGTH_SHORT).show();
    }
}
