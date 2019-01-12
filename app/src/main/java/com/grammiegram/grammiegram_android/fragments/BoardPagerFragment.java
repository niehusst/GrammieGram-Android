package com.grammiegram.grammiegram_android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.grammiegram.grammiegram_android.POJO.Gram;
import com.grammiegram.grammiegram_android.R;
import com.grammiegram.grammiegram_android.interfaces.OnGramFragmentClickListener;
import com.grammiegram.grammiegram_android.services.GramCarouselService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BoardPagerFragment extends Fragment {

    /*
    TODO: add a fragment interaction listener for sending read reciepts????
    if yes, add a class bool that dictates wheter a message has been read already or not so that
    repeated calls to the api are not made and add onCLickListener for text veiw
     */
    private String message;
    private String senderFirstName;
    private String expirationData;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    private long fragmentCreationTime;

    private ScheduledExecutorService pool;

    private OnGramFragmentClickListener mListener;

    //views
    @BindView(R.id.btn_left)
    Button left;
    @BindView(R.id.btn_right)
    Button right;
    @BindView(R.id.gram_message)
    TextView gramMessage;
    @BindView(R.id.message_from)
    TextView messageSender;

    //required empty constructor
    public BoardPagerFragment() {}

    /**
     * Returns a new instance of this fragment with the input gram string
     *
     * @param gram - the String to set as the gram in the fragment text view
     */
    public static BoardPagerFragment newInstance(Gram gram) {
        //TODO: what about images?? overload? ARE IMAGES EVEN IN API??
        //TODO: make a separate type of fragment that has an image view that this adapter can manage as well (link with interface?)
        BoardPagerFragment fragment = new BoardPagerFragment();
        //bundle up gram data
        Bundle args = new Bundle();
        args.putString("MESSAGE", gram.getMessage());
        args.putString("SENDER", gram.getSenderFirstName());
        args.putString("EXPIRATION", gram.getTill());
        args.putInt("YEAR", gram.getYear());
        args.putInt("MONTH", gram.getMonth());
        args.putInt("DAY", gram.getDay());
        args.putInt("HOUR", gram.getHour());
        args.putInt("MINUTE", gram.getMinute());

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle gramData) {
        //get fragment creation time
        this.fragmentCreationTime = System.currentTimeMillis();

        //inflate layout
        View rootView = inflater.inflate(R.layout.fragment_board, container, false);

        ButterKnife.bind(rootView);

        //unpack bundle
        this.senderFirstName = gramData.getString("SENDER");
        this.message = gramData.getString("MESSAGE");
        this.expirationData = gramData.getString("EXPIRATION");
        this.year = gramData.getInt("YEAR");
        this.month = gramData.getInt("MONTH");
        this.day = gramData.getInt("DAY");
        this.hour = gramData.getInt("HOUR");
        this.minute = gramData.getInt("MINUTE");

        //set text views
        gramMessage.setText(this.message);
        messageSender.setText(getString(R.string.from, senderFirstName, "last")); //TODO: add last getting when api update

        //async task to cycle through grams in adapter automatically
        GramCarouselService cycleService = new GramCarouselService(fragmentCreationTime, mListener);
        pool = Executors.newScheduledThreadPool(1);
        pool.scheduleAtFixedRate(cycleService, 0, 1, TimeUnit.SECONDS); //check every second for cycling

        return rootView;
    }

    /**
     * Handle gram traversal click for next gram
     */
    @OnClick(R.id.btn_right)
    public void rightClick() {
        if(mListener != null) {
            mListener.onRightClick();
        }
    }

    /**
     * Handle gram traversal click for previous gram
     */
    @OnClick(R.id.btn_left)
    public void leftClick() {
        if(mListener != null) {
            mListener.onLeftClick();
        }
    }

    /**
     * Set the context of BoardActivity as the OnGramFragmentListener required
     * to handle adapter traversal clicks.
     *
     * @param context - the BoardActivity context that implements OnGramFragmentListener
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnGramFragmentClickListener) {
            this.mListener = (OnGramFragmentClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnGramFragmentClickListener");
        }
    }

    /**
     * Prevent memory leaks by releasing the context reference to be garbage collected
     */
    @Override
    public void onDetach() {
        //clean up async thread pool service for this fragment
        if(pool != null) {
            pool.shutdownNow();
        }
        super.onDetach();
        //release reference to parent BoardActivity
        mListener = null;
    }
}

