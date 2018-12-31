package com.grammiegram.grammiegram_android.activities;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.TextView;

import com.grammiegram.grammiegram_android.POJO.Board;
import com.grammiegram.grammiegram_android.POJO.Gram;
import com.grammiegram.grammiegram_android.POJO.GramsListResponse;
import com.grammiegram.grammiegram_android.R;
import com.grammiegram.grammiegram_android.adapters.BoardFragmentPagerAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * The activity that displays a board's current grams.
 * Utilizes fragments containing autoresizeing text views to display a gram. A pager
 * adapter is used to manage gram holding fragments.
 */
public class BoardActivity extends AppCompatActivity {
    //TODO: make set landscape rotation and prevent falling asleep
    //TODO: make pager automatically rotate every 30? seconds
    //TODO: implement logic of gram traversal

    /*
     * The {@link android.support.v4.app.FragmentStatePagerAdapter} that will provide
     * fragments for each of the sections that hold grams.
     */
    private FragmentStatePagerAdapter pagerAdapter;

    //The {@link ViewPager} that will host the section contents.
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get board data to load from intent
        Board board = (Board) getIntent().getParcelableExtra("BOARD");

        //TODO: while(1) { if(apiresponse.boardUpdateNeeded()) { add new grams to adapter?, create new adapter? how wil that look UI side?
        //TODO: create a service that runs async in bg checking for/doing updates to board grams
        // Create the adapter that will return a fragment for each gram
        GramsListResponse g = new GramsListResponse(); //TODO: delete this debugging stuff
        g.setGrams(new ArrayList<Gram>()); //still delet this
        pagerAdapter = new BoardFragmentPagerAdapter(g, getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(pagerAdapter);

        if(pagerAdapter.getCount() == 0) {
            //set up no grams views
            //TODO: set date and time for no grams views (make these TextViewCompat!) (Or TextClock????)
            TextView date = (TextView) findViewById(R.id.date);
            TextView time = (TextView) findViewById(R.id.time);
            date.setText(getString(R.string.date, "date date"));
            time.setText(getString(R.string.time, "2am"));
        } else {
            //TODO: set up grams in fragment pager
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //add handling of button click here if we decide to add buttons to app bar
        return super.onOptionsItemSelected(item);
    }

}
