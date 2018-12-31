package com.grammiegram.grammiegram_android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grammiegram.grammiegram_android.POJO.Gram;
import com.grammiegram.grammiegram_android.R;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle gramData) {
        View rootView = inflater.inflate(R.layout.fragment_board, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.gram_message);
        //unpack bundle
        this.senderFirstName = gramData.getString("SENDER");
        this.message = gramData.getString("MESSAGE");
        this.expirationData = gramData.getString("EXPIRATION");
        this.year = gramData.getInt("YEAR");
        this.month = gramData.getInt("MONTH");
        this.day = gramData.getInt("DAY");
        this.hour = gramData.getInt("HOUR");
        this.minute = gramData.getInt("MINUTE");

        textView.setText(this.message);
        return rootView;
    }
}

