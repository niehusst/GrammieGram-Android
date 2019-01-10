package com.grammiegram.grammiegram_android.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.grammiegram.grammiegram_android.POJO.Gram;
import com.grammiegram.grammiegram_android.POJO.GramsListResponse;
import com.grammiegram.grammiegram_android.fragments.BoardPagerFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * A {@link FragmentStatePagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class BoardFragmentPagerAdapter extends FragmentStatePagerAdapter {
    //TODO: have a list of Fragments so that one doesn't have to be built every time??

    private List<Gram> grams;
    private int index;

    public BoardFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        this.index = 0;
        this.grams = new ArrayList<>();
    }

    /**
     * Add a new gram to the adapter
     *
     * @param gram - Gram object to add
     */
    public void addGram(Gram gram) {
        grams.add(gram);
    }

    /**
     * Delete a Gram from the pager adapter.
     * Primarily for deleting expired grams.
     *
     * @param gram - Gram object to delete
     */
    public void removeGram(Gram gram) {
        grams.remove(gram);
    }

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
            if(getTime() >= convertTillToTime(gram.getTill())) {
                iter.remove();
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
     * @return -
     */
    private int convertTillToTime(String till) {
        //TODO: stub
        return 0;
    }


    /**
     * Use java.time to get the current date and time in a usable format
     *
     * @return - the current date and time in one variable
     */
    private int getTime() {
        //TODO: stub
        return 0;
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
     * @return - the fragment at index position
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
     * @return - number of grams in this board
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
        if(this.index == this.grams.size()-1)
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
        if(this.index == 0)
            index = this.grams.size()-1;
        else
            index--;

        return getItem(index);
    }
}

