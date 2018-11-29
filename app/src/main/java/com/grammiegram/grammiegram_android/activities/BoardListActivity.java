package com.grammiegram.grammiegram_android.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.grammiegram.grammiegram_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BoardListActivity extends AppCompatActivity {

    //success views
    @BindView(R.id.board_recycler)
    private RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    private Toolbar toolbar;

    //loading views
    @BindView(R.id.progress_dialogue)
    private ProgressBar dialogue;
    @BindView(R.id.loading_text)
    private TextView loadText;

    //err views
    @BindView(R.id.error_text)
    private TextView errText;
    @BindView(R.id.error_image)
    private ImageView errImg;
    @BindView(R.id.retry_button)
    private Button errRetryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_list);

        //bind views for this activity
        ButterKnife.bind(this);

        //set custom action bar
        setSupportActionBar(toolbar);

        //put spacing between rv items
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
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
    private void launchBoardActivity(int position) {
        Intent intent = new Intent(this, BoardActivity.class);

        startActivity(intent);
        finish();
    }

    /**
     * Launches the board activity of the selected board item from recycler view
     */
    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        //TODO: (delete saved data from preferences?)
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
}
