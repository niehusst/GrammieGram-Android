package com.grammiegram.grammiegram_android.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.grammiegram.grammiegram_android.POJO.Gram;
import com.grammiegram.grammiegram_android.fragments.BoardPagerFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A {@link FragmentStatePagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class BoardFragmentPagerAdapter extends FragmentStatePagerAdapter {
    //TODO: fix rotation button click index ob error. index somehow exceded list size?

    private List<Gram> grams;
    private int index;

    public BoardFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        this.index = 0;
        this.grams = new ArrayList<>();
    }

    /*
    TODO: gram traversal is broken, also switch the direction that boardfragments enter and exit when button is clicked
     */
    /**
     * Check for expired grams in the adapter. If expired grams exist,
     * they are removed from the adapter
     */
    public void removeExpiredGrams() { //TODO: this removes from adapter, but does this actually count as destroying grams in db?? api?
        //iterate grams in adapter
        Iterator<Gram> iter = this.grams.iterator();
        while(iter.hasNext()) {
            Gram gram = iter.next();
            //if current time is passed til, delete gram
            Log.d("ADAPTCONT", getTime() + " >=? " + gram.getTill());
            if(getTime() >= convertTillToTime(gram.getTill())) {
                iter.remove();
                Log.d("ADAPTSIZE", "iter removed");
                this.notifyDataSetChanged();
            }
        }

    }


    /**
     * Add new grams from an api response to the gram fragment adapter
     *
     * @param responseGrams - list of grams from and api getGrams response
     */
    public void addNewGrams(List<Gram> responseGrams) {
        //get hashmap of adapter grams
        HashMap<Gram, Integer> adapterGrams = new HashMap<>();
        for(Gram gram : this.getGrams()) {
            adapterGrams.put(gram, 1); //arbitrary second value
        }

        for(Gram gram : responseGrams) {
            if(!adapterGrams.containsKey(gram)) {
                this.grams.add(gram);
            }
        }
    }

    /**
     * Convert the till filed of a gram into a more usable format
     *
     * @param till - the expiration date of a gram in String form
     * @return - the till string converted to a usable int
     */
    private long convertTillToTime(String till) {
        //strip punctuation and get value of string
        //(example of till) 2019-01-12 10:15:00+00:00
        till = till.replaceAll("[^0-9]", "");
        return Long.parseLong(till);
    }


    /**
     * Use java.time to get the current date and time in UTC in a usable format.
     * The time must be in UTC timezone because GrammieGram backend uses all UTC time for
     * settings expiration and sent dates.
     *
     * @return - the current UTC date and time in one variable
     */
    private long getTime() {
        DateFormat time = new SimpleDateFormat("yyyyMMddHHmmssSSSS", Locale.US);
        Calendar calendar = Calendar.getInstance();
        time.setTimeZone(TimeZone.getTimeZone("UTC"));
        return Long.parseLong(time.format(calendar.getTime()));
    }

    /**
     * Get the list of grams containted by this adapter
     * @return - list of grams
     */
    public List<Gram> getGrams() {
        return this.grams;
    }

    /**
     * Get the fragment from the adapter at index position
     *
     * @param position - index of fragment to get from adapter
     * @return - the fragment constructed from the gram at index position of List<gram>
     */
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a BoardPagerFragment that represents the gram at position.
        return BoardPagerFragment.newInstance(this.grams.get(position));
    }

    /**
     * Get the number of grams that the board has
     *
     * @return - number of grams stored in this adapter
     */
    @Override
    public int getCount() {
        if(this.grams == null)
            return 0;
        else
            return this.grams.size();
    }

    /**
     * Get the gram fragment following the current one. Circularly loops from back
     * to front if end of adapter list is reached.
     */
    public Fragment getNext() {
        if(this.index >= this.grams.size()-1)
            index = 0;
        else
            index++;

        return getItem(index);
    }

    /**
     * Get the gram fragment prior to the current one. Circularly loops from front
     * to back if start of adapter list is reached.
     */
    public Fragment getPrev() {
        if(this.index <= 0)
            index = this.grams.size()-1;
        else
            index--;

        return getItem(index);
    }


}

