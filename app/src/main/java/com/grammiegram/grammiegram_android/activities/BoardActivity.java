package com.grammiegram.grammiegram_android.activities;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.grammiegram.grammiegram_android.BoardUpdateService;
import com.grammiegram.grammiegram_android.GrammieGramService;
import com.grammiegram.grammiegram_android.POJO.Board;
import com.grammiegram.grammiegram_android.POJO.Gram;
import com.grammiegram.grammiegram_android.POJO.GramsListResponse;
import com.grammiegram.grammiegram_android.R;
import com.grammiegram.grammiegram_android.adapters.BoardFragmentPagerAdapter;
import com.grammiegram.grammiegram_android.fragments.SettingsFragment;
import com.grammiegram.grammiegram_android.interfaces.APIResponse;
import com.grammiegram.grammiegram_android.interfaces.CallBack;
import com.grammiegram.grammiegram_android.interfaces.OnGramFragmentClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;

/**
 * The activity that displays a board's current grams.
 * Utilizes fragments containing autoresizeing text views to display a gram. A pager
 * adapter is used to manage gram holding fragments.
 */
public class BoardActivity extends AppCompatActivity implements CallBack, OnGramFragmentClickListener {
    //TODO: make set landscape rotation and prevent falling asleep
    //TODO: where is framgemtn manager that is acutally lanuching first frag??

    /*
     * The {@link android.support.v4.app.FragmentStatePagerAdapter} that will provide
     * fragments for each of the sections that hold grams.
     */
    private BoardFragmentPagerAdapter pagerAdapter;

    //The {@link ViewPager} that will host the section contents.
    private ViewPager mViewPager;

    //no grams views
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.time)
    TextView time;

    //gram fragment views
    @BindView(R.id.btn_left)
    Button left;
    @BindView(R.id.btn_right)
    Button right;
    @BindView(R.id.gram_message)
    TextView gramMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        // Get board data to load from intent
        Board board = (Board) getIntent().getParcelableExtra("BOARD");

        // Create the adapter that will return a fragment for each gram
        GramsListResponse g = new GramsListResponse(); //TODO: delete this debugging stuff
        pagerAdapter = new BoardFragmentPagerAdapter(g, getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(pagerAdapter);

        // Set up and start the async update service
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        BoardUpdateService service = new BoardUpdateService(pagerAdapter, this, board.getBoardDisplayName(), prefs);
        service.execute();

        if(pagerAdapter.getCount() == 0) {
            //set up no grams views
            //TODO: set date and time for no grams views (do in service update)
            date.setText(getString(R.string.date, "date date"));
            time.setText(getString(R.string.time, "2am"));
        } else {
            //TODO: set up grams in fragment pager (other views to gone?)
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                    R.anim.slide_in_right, R.anim.slide_out_right);
            transaction.replace(R.id.board_list_container, pagerAdapter.getItem(0));
            transaction.addToBackStack(null);
            transaction.commit();
        }

        //TODO: make text setting function for clock and date (while doing checknew service?)

        //TODO: make pager automatically rotate every 30? seconds. (try to do from prev rotate, not just time)
        //TODO: make another async task that cycles through grams???
    }


    /**
     * inflate app bar
     *
     * @param menu - the menu xml
     * @return - creation status
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_board, menu);
        return true;
    }

    /**
     * Handle clicks to items in appbar menu
     *
     * @param item - menu item that was clicked
     * @return - option selection
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //TODO: get rid of this?? or add a button to click on action bar
        int id = item.getItemId();
        //add handling of button click here if we decide to add buttons to app bar
        return super.onOptionsItemSelected(item);
    }

    /**
     * Load the previous gram from the fragment state pager adapter and
     * remove the current one.
     */
    @Override
    public void onLeftClick() {
        FragmentManager manager = getSupportFragmentManager();

        //delete old fragment
        manager.popBackStack();

        //setup new fragment
        Fragment prevGram = pagerAdapter.getPrev();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.container, prevGram);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Load the next gram from the fragment state pager adapter and
     * remove the current one.
     */
    @Override
    public void onRightClick() {
        FragmentManager manager = getSupportFragmentManager();

        //delete old fragment
        manager.popBackStack();

        //setup new fragment
        Fragment nextGram = pagerAdapter.getNext();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,
                R.anim.slide_in_left, R.anim.slide_out_right);
        transaction.replace(R.id.container, nextGram);
        transaction.addToBackStack(null);
        transaction.commit();
    }



    /**                API getGrams response callbacks                 */
    @Override
    public void onSuccess(APIResponse response) {
        //TODO: when a new gram comes in, do a linear check through the new messages, adding it to the adapter if it's new (job of update service?)
        //put the addNewGrams method in here?
    }

    @Override
    public void onNetworkError(String error) {
        Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServerError(int code, ResponseBody body) {
        Toast.makeText(this, code + "Server Error", Toast.LENGTH_SHORT).show();
    }

}
