package com.grammiegram.grammiegram_android.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.grammiegram.grammiegram_android.GrammieGramService;
import com.grammiegram.grammiegram_android.POJO.Board;
import com.grammiegram.grammiegram_android.POJO.BoardListResponse;
import com.grammiegram.grammiegram_android.R;
import com.grammiegram.grammiegram_android.adapters.BoardListRecyclerAdapter;
import com.grammiegram.grammiegram_android.fragments.SettingsFragment;
import com.grammiegram.grammiegram_android.interfaces.APIResponse;
import com.grammiegram.grammiegram_android.interfaces.CallBack;
import com.grammiegram.grammiegram_android.interfaces.ItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;

public class BoardListActivity extends AppCompatActivity implements
        CallBack, SettingsFragment.OnFragmentInteractionListener, ItemClickListener {

    private GrammieGramService api;

    //success views
    @BindView(R.id.board_recycler)
    public RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    private BoardListRecyclerAdapter adapter;

    //loading progress views
    @BindView(R.id.progress_dialogue)
    public ProgressBar dialogue;

    //err views
    @BindView(R.id.error_text)
    public TextView errText;
    @BindView(R.id.error_image)
    public ImageView errImg;
    @BindView(R.id.retry_button)
    public Button errRetryBtn;

    //fragment container
    @BindView(R.id.board_list_container)
    public FrameLayout settingsFrag;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(BoardListActivity.this,
                DividerItemDecoration.VERTICAL));

        //load up boards into recycler view
        getBoards();
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
     * When errRetryBtn is clicked, retry the getBoards API call
     */
    @OnClick(R.id.retry_button)
    public void onClickRetry() {
        //start spinner and make err views disappear
        dialogue.setVisibility(View.VISIBLE);
        errRetryBtn.setVisibility(View.GONE);
        errImg.setVisibility(View.GONE);
        errText.setVisibility(View.GONE);

        //retry api call for boards
        getBoards();
    }

    /**
     * Define behavior when back button is pressed
     */
    @Override
    public void onBackPressed() {
        if(settingsFrag.getVisibility() == View.VISIBLE) {
            settingsFrag.setVisibility(View.GONE);

        } else {
            BoardListActivity.super.onBackPressed();
        }
    }

    /**
     * Launches the board activity of the selected board item from recycler view
     */
    private void launchLoginActivity() {
        // delete saved token data from preferences
        getSharedPreferences("grammiegram", MODE_PRIVATE)
                .edit()
                .remove("auth_token")
                .apply();

        //launch login activity with empty intent
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Launches the board activity of the selected board item from recycler view
     */
    private void launchSettingsFragment() {
        settingsFrag.setVisibility(View.VISIBLE);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.animator.slide_in, R.animator.slide_out, R.animator.slide_in, R.animator.slide_out);
        transaction.replace(R.id.board_list_container, new SettingsFragment());
        transaction.addToBackStack(null);
        transaction.commit();


        /*
        TODO: add line to pop off fragments that are built up? but replace should prevent build up????
        getFragmentManager().popBackStack();
         */
    }

    /**
     * Call the GrammieGram API to fill in the recycler view
     */
    public void getBoards() {
        //start progress dialogue at start of loading sequence
        dialogue.setVisibility(View.VISIBLE);

        //api call (handling goes to callback interface methods)
        SharedPreferences sharedPreferences = getSharedPreferences("grammiegram", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", "DEFAULT");
        api.getBoards(token);

        //finished loading, make dialogue invisible again
        dialogue.setVisibility(View.GONE);
    }

    /**
     * Handle successful api response
     * @param response - BoardListResponse object containing api response data
     */
    @Override
    public void onSuccess(APIResponse response) {
        settingsFrag.setVisibility(View.GONE);
        errImg.setVisibility(View.GONE);
        errText.setVisibility(View.GONE);
        errRetryBtn.setVisibility(View.GONE);
        BoardListResponse bl = (BoardListResponse) response;

        //set response data into the adapter
        this.adapter = new BoardListRecyclerAdapter(bl, this, this);

        if(this.adapter.getItemCount() == 0) {
            //no boards to display
            errText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

            //display help text
            errText.setText(R.string.no_boards);
        } else {
            recyclerView.setVisibility(View.VISIBLE);

            //set recycler view w/ adapter
            recyclerView.setAdapter(adapter);
        }
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
        recyclerView.setVisibility(View.GONE);
        settingsFrag.setVisibility(View.GONE);

        errText.setText(R.string.wifi_error);

        //make toast
        Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show();
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
        recyclerView.setVisibility(View.GONE);
        settingsFrag.setVisibility(View.GONE);

        errText.setText(R.string.server_error);

        //make toast
        Toast.makeText(this, code + " Server Error", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
        //required by Fragment class parent activity
    }

    /**
     * Listener for launching the BoardActivity with data about the board that was clicked
     *
     * @param view - view that was clicked
     * @param pos - position in adapter of view that was clicked
     */
    @Override
    public void onItemClick(View view, int pos) {
        Intent intent = new Intent(this, BoardActivity.class);

        //add board data to intent
        Board board = adapter.getItem(pos);
        intent.putExtra("BOARD", board);

        startActivity(intent);
        finish();
    }
}
