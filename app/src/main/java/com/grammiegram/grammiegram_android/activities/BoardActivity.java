package com.grammiegram.grammiegram_android.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
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
    //TODO: before release, board MUST have picture messages and a way to handle interactive grams
    //TODO: must be able to detect active grams missed during a period of momentary wifi loss. Skip checknew, just always get grams and compare through?
    //TODO: tablet performance is significantly worse...
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
    @BindView(R.id.no_grams)
    TextView noGramsText;
    @BindView(R.id.no_grams_logo)
    ImageView logo;

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

        // Set up the ViewPager with the board fragment adapter.
        mViewPager = (ViewPager) findViewById(R.id.gram_container);
        // Create the adapter that will return a fragment for each gram
        pagerAdapter = new BoardFragmentPagerAdapter(getSupportFragmentManager(), mViewPager);
        mViewPager.setAdapter(pagerAdapter);

        //do initial fill of adapter
        this.initialLoad = true;
        GrammieGramService api = new GrammieGramService(this);
        api.getGrams(prefs.getString("auth_token", "DEFAULT"), board.getBoardDisplayName());

        // Set up runnable tasks to update board and grams
        BoardUpdateService gramFetchService = new BoardUpdateService(pagerAdapter, this,
                board.getBoardDisplayName(), prefs);
        //ScheduledThreadPoolExecutor to periodically check for new grams
        pool = Executors.newScheduledThreadPool(1);
        pool.scheduleAtFixedRate(gramFetchService, 0, BoardUpdateService.CHECK_RATE_SECONDS, TimeUnit.SECONDS);
        //update clock on UI thread
        handler.post(dateTimeService);
    }

    /**
     * Prevent back press from destroying open grams fragment; when back is pressed,
     * it opens a dialog window that that gives user the option to close the board activity
     * or take no action.
     */
    @Override
    public void onBackPressed() {
        //popup message asking if they really want to close board.
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked, launch boardlist and finish board
                        launchBoardListActivity();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked, do nothing
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dialogue_text)).setPositiveButton(getString(R.string.dialogue_positive),
                dialogClickListener).setNegativeButton(getString(R.string.dialogue_negative), dialogClickListener).show();
    }


    /**
     * Cycle to the previous gram from the fragment state pager adapter
     */
    @Override
    public void onLeftClick() {
        //update viewPager
        mViewPager.setCurrentItem(pagerAdapter.getPrev());
    }

    /**
     * Cycle to the next gram from the fragment state pager adapter
     */
    @Override
    public void onRightClick() {
        //update ViewPager
        mViewPager.setCurrentItem(pagerAdapter.getNext());
    }

    /**
     * Launch a BoardListActivity and finish the board activity.
     */
    private void launchBoardListActivity() {
        Intent intent = new Intent(this, BoardListActivity.class);
        startActivity(intent);
        finish();
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

    /*
    TODO: when adding grams to adapter, set bg views to invisible/gone
    TODO: when remove grams, check if adapter is now empty. if so, bring back no grams views
     */
    /**
     * Load grams that don't already exist in the adapter into it. Play the users preferred
     * notification media when not the initial load of grams into adapter.
     */
    @Override
    public void onSuccess(APIResponse response) {
        //add grams from response to the adapter if grams are not already present
        GramsListResponse gramList = (GramsListResponse) response;
        this.pagerAdapter.addNewGrams(gramList.getGrams());

        //dont play a notification if this is the initial load of existing grams (not new grams)
        if(!initialLoad) { //TODO: notification sounds are broken: happen too often when not new grams
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
                    //dont play a sound when pref is "None"
            }
        }
        //handle UI setup depending on whether or not there are grams to show
        if (pagerAdapter.getCount() == 0) {
            //make no grams views visible
            noGramsText.setVisibility(View.VISIBLE);
            logo.setVisibility(View.VISIBLE);
            date.setVisibility(View.VISIBLE);
            time.setVisibility(View.VISIBLE);
        } else {
            //set no-grams views to gone
            noGramsText.setVisibility(View.GONE);
            logo.setVisibility(View.GONE);
            date.setVisibility(View.GONE);
            time.setVisibility(View.GONE);
        }
        this.initialLoad = false;
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
            DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
            Calendar cal = Calendar.getInstance();
            this.date.setText(dateFormat.format(cal.getTime()));
            this.time.setText(timeFormat.format(cal.getTime()));

            //set to run again in 1 second
            handler.postDelayed(this, 1000);
        }
    }
}
