package com.grammiegram.grammiegram_android.activities;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.grammiegram.grammiegram_android.services.BoardUpdateService;
import com.grammiegram.grammiegram_android.POJO.Board;
import com.grammiegram.grammiegram_android.POJO.GramsListResponse;
import com.grammiegram.grammiegram_android.R;
import com.grammiegram.grammiegram_android.adapters.BoardFragmentPagerAdapter;
import com.grammiegram.grammiegram_android.interfaces.APIResponse;
import com.grammiegram.grammiegram_android.interfaces.CallBack;
import com.grammiegram.grammiegram_android.interfaces.OnGramFragmentClickListener;
import com.grammiegram.grammiegram_android.services.GrammieGramService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

/**
 * The activity that displays a board's current grams.
 * Utilizes fragments containing autoresizeing text views to display a gram. A pager
 * adapter is used to manage gram holding fragments.
 */
public class BoardActivity extends AppCompatActivity implements CallBack, OnGramFragmentClickListener {
    //TODO: does it lanuch first frag if it starts with no grams??
    //TODO: add api call for get current settings
    /*
     * The {@link android.support.v4.app.FragmentStatePagerAdapter} that will provide
     * fragments for each of the sections that hold grams.
     */
    private BoardFragmentPagerAdapter pagerAdapter;

    //The {@link ViewPager} that will host the section contents.
    private ViewPager mViewPager;

    //thread pool and handler for background service runnables
    private ScheduledExecutorService pool;
    private Handler handler = new Handler();
    private Runnable dateTimeService;

    //flag to mark
    private boolean initialLoad;

    //no grams views
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.time)
    TextView time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        //prevent the device from falling asleep while BoardActivity is open
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ButterKnife.bind(this);
        SharedPreferences prefs = getSharedPreferences("grammiegram", MODE_PRIVATE);
        dateTimeService = new DateTimeUpdateService();

        // Get board data to load from intent
        final Board board = (Board) getIntent().getParcelableExtra("BOARD");

        // Create the adapter that will return a fragment for each gram
        pagerAdapter = new BoardFragmentPagerAdapter(getSupportFragmentManager());
        //do initial fill of adapter
        GrammieGramService api = new GrammieGramService(this);
        api.getGrams(prefs.getString("auth_token", "DEFAULT"), board.getBoardDisplayName());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(pagerAdapter);

        initialLoad = true;
        //handle UI setup depending on whether or not there are grams to show
        if(pagerAdapter.getCount() == 0) {
            //TODO: set up no grams views to visible??

        } else {
            //TODO: set up grams in fragment pager (other views to gone?) would that mean we have to set them back to visible whenever adapter empties?
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                    R.anim.slide_in_right, R.anim.slide_out_right);
            transaction.replace(R.id.board_list_container, pagerAdapter.getItem(0));
            transaction.addToBackStack(null);
            transaction.commit();
        }
        initialLoad = false;

        // Set up runnable tasks to update board and grams
        BoardUpdateService gramFetchService = new BoardUpdateService(pagerAdapter, this,
                board.getBoardDisplayName(), prefs);
        //ScheduledThreadPoolExecutor to periodically check for new grams
        pool = Executors.newScheduledThreadPool(2);
        pool.scheduleAtFixedRate(gramFetchService, 0, BoardUpdateService.CHECK_RATE_SECONDS, TimeUnit.SECONDS);
        //update clock on UI thread (TODO: check if too blocking of user interaction)
        handler.post(dateTimeService);
    }

    /**
     * Prevent back press from destroying open grams fragment
     */
    @Override
    public void onBackPressed() {
        //TODO: popup message asking if they really want to close board? launch board list
        Toast.makeText(this, "Are you sure you want to leave the board?", Toast.LENGTH_SHORT).show();
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

    /**
     * Clean up this activity by stopping all pending Runnables in thread pools
     */
    @Override
    protected void onDestroy() {
        if(pool != null) {
            pool.shutdownNow();
            pool = null;
        }
        super.onDestroy();
    }

    /**
     * Pause the date and time updates to the UI thread when activity
     * leaves focus.
     */
    @Override
    protected void onPause() {
        handler.removeCallbacks(dateTimeService);
        super.onPause();
    }

    /**
     * Restart date time updates to the UI thread when activity comes back into focus.
     */
    @Override
    protected void onResume() {
        handler.post(dateTimeService);
        super.onResume();
    }

    /**
     * Load grams that don't already exist in the adapter into it. Play the users preferred
     * notification media when not the initial load of grams into adapter.
     */
    @Override
    public void onSuccess(APIResponse response) {
        //dont play a notification if this is the initial load of existing grams (not new grams)
        if(!initialLoad) {
            //play notification for new grams if audio preference is activated
            SharedPreferences prefs = getSharedPreferences("grammiegram", MODE_PRIVATE);

            MediaPlayer notification;
            switch (prefs.getString("audio_notifications", "None")) {
                case "cardinal":
                    notification = MediaPlayer.create(this, R.raw.cardinal);
                    notification.start();
                    break;
                case "turkey":
                    notification = MediaPlayer.create(this, R.raw.turkey);
                    notification.start();
                    break;
                case "bells":
                    notification = MediaPlayer.create(this, R.raw.bells);
                    notification.start();
                    break;
                default:
                    //dont play a sound when pref is None
            }
        }

        //add grams from response to the adapter if grams are not already present
        GramsListResponse gramList = (GramsListResponse) response;
        this.pagerAdapter.addNewGrams(gramList.getGrams());
    }

    /**
     * Notify user that there is a wifi error.
     * @param error - String with information on the error (likely technical)
     */
    @Override
    public void onNetworkError(String error) {
        Toast.makeText(this, getString(R.string.wifi_error), Toast.LENGTH_SHORT).show();
    }

    /**
     * Notify user that there is a server error.
     * @param code - html error code
     * @param body - body of the error response
     */
    @Override
    public void onServerError(int code, ResponseBody body) {
        Toast.makeText(this, code + getString(R.string.server_error), Toast.LENGTH_SHORT).show();
    }


    /**
     * Inner class for updating date and time TextViews. Must be an inner class to have access
     * to the views it needs to update on UI thread.
     */
    class DateTimeUpdateService implements Runnable {

        //text views that must be updated
        TextView date;
        TextView time;

        DateTimeUpdateService() {
            this.date = findViewById(R.id.date);
            this.time = findViewById(R.id.time);
        }

        /**
         * Work to do in a separate background thread. Computes the Strings for
         * time and date, saving them in class fields.
         */
        @Override
        public void run() {
            //update clock text views
            DateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.US);
            DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d yyyy", Locale.US);
            Calendar cal = Calendar.getInstance();
            this.date.setText(dateFormat.format(cal.getTime()));
            this.time.setText(timeFormat.format(cal.getTime()));

            //set to run again in 1 second
            handler.postDelayed(this, 1000);
        }
    }
}
