package com.grammiegram.grammiegram_android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grammiegram.grammiegram_android.R;

public class BoardPagerFragment extends Fragment {

    //required empty constructor
    public BoardPagerFragment() {}

    /**
     * Returns a new instance of this fragment with the input gram string
     *
     * @param gram - the String to set as the gram in the fragment text view
     */
    public static BoardPagerFragment newInstance(String gram) { //TODO: what about images??
        BoardPagerFragment fragment = new BoardPagerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_board, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText("Your Gram");
        return rootView;
    }
}

