package com.grammiegram.grammiegram_android.adapters;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.grammiegram.grammiegram_android.POJO.Gram;
import com.grammiegram.grammiegram_android.fragments.BoardPagerFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
/*
    TODO: make the adapter move which frag is current if deleting it
    TODO: for some reason, doesn't recive new grams after the 1st one??
    TODO: figure out why the log.v for remove grams is never called

    -the gram removal ~kind of works~, it removes the correct gram, but on removal the screen goes white until
     swipe (then allowing the correct grams to appear)
    -the notification sound is repeatedly going off, even on the initial load (likely due to the constant refreshes
     of the adapter caused by POSITION_NONE
    -after any gram receval/removal, incoming grams can't be received??
     */
/**
 * A {@link FragmentStatePagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class BoardFragmentPagerAdapter extends PagerAdapter {
    private static final boolean DEBUG = true; //TODO: turn off debug
    private static final String TAG = "BoardFragPagerAdapter";

    private LinkedList<Gram> grams;
    private int index;
    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem = null;
    private ViewPager mViewPager;

    /**
     * Create a fresh adapter and starts stuff
     *
     * @param fm - the fragment manager to manage fragments in this adapter
     */
    public BoardFragmentPagerAdapter(FragmentManager fm, ViewPager vp) {
        this.mFragmentManager = fm;
        this.index = 0; //the index of the current primary item
        this.grams = new LinkedList<>();
        this.mViewPager = vp;
    }


    /**
     * Check for expired grams in the adapter. If expired grams exist,
     * they are removed from the adapter
     */
    public void removeExpiredGrams() {
        boolean removedGrams = false;

        //iterate grams in adapter
        Iterator<Gram> iter = this.grams.iterator();
        while(iter.hasNext()) {
            Gram gram = iter.next();
            //if current time is passed til, delete gram
            if(getTime() >= convertTillToTime(gram.getTill())) {
                //remove from adapter list
                iter.remove();
                if(DEBUG) Log.v(TAG, "removed gram: " + gram.getMessage());
                removedGrams = true;
            }
        }

        if(removedGrams) {
            this.notifyDataSetChanged();
            if(DEBUG) Log.v(TAG, "removed grams");
        }
    }

    /**
     * Add new grams from an api response to the gram fragment adapter
     *
     * @param responseGrams - list of grams from and api getGrams response
     */
    public void addNewGrams(List<Gram> responseGrams) {
        boolean addedGrams = false;

        //get hashset of grams in the adapter
        HashSet<Gram> adapterGrams = new HashSet<>(this.getGrams());

        for(Gram gram : responseGrams) {
            if(!adapterGrams.contains(gram)) {
                //add new gram to front of adapter list
                this.grams.addFirst(gram);
                addedGrams = true;
            }
        }
        //TODO: add api call for sending "recieved" info back to mark not new
        if(addedGrams) {
            this.notifyDataSetChanged();
            mViewPager.setCurrentItem(0);
            if(DEBUG) Log.v(TAG, "added grams");
        }
    }

    /**
     * Convert the till filed of a gram into a more usable format
     *
     * @param till - the expiration date of a gram in String form
     * @return - the till string converted to a usable int
     */
    private long convertTillToTime(String till) {
        //strip non-numbers and get value of string
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
     * Tell the pager adapter whether or not it needs to update the position of object
     *
     * @param object - fragment that needs its position in the adapter checked
     * @return - the position of object in the adapter, or POSITION_NONE if it's not present
     */
    @Override
    public int getItemPosition(@NonNull Object object) {
        /*int pos = grams.indexOf(((BoardPagerFragment)object).getGram());
        if(pos != -1) {
            return pos;
        } else {*/
            return POSITION_NONE; // force adapter to update
        //} //TODO: if this doesnt' work, try reinstantiating the viewpager with this as the adapter
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

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment)object).getView() == view;
    }

    /**
     * Called when a change in the shown pages is going to start being made.
     *
     * @param container The containing View which is displaying this adapter's page views.
     */
    @Override
    public void startUpdate(ViewGroup container) {
        //TODO: make the transaction manager move to a different page if primary is being deltetd? here?
    }


    /**
     * Build/rebuild the fragment at position in the adapter
     *
     * @param container - the ViewGroup to build fragment in
     * @param position - index of adapter to load/create state from
     * @return - the newly built fragment
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        if(DEBUG) Log.v(TAG, "instantiating item #" + position + ": " + grams.get(position).getMessage());

        Fragment fragment = BoardPagerFragment.newInstance(grams.get(position));

        fragment.setMenuVisibility(false);
        mCurTransaction.add(container.getId(), fragment);
        return fragment;
    }

    /**
     * Remove fragment object from view
     *
     * @param container - ViewGroup to destroy from
     * @param position - index in adapter of fragment to destroy
     * @param object - the fragment to destroy/save state for
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment)object;
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        if(DEBUG) Log.v(TAG, "Removing frag trans item #" + position + ": f=" + object
                + " m=" + ((BoardPagerFragment)object).getGram().getMessage());

        mCurTransaction.remove(fragment);
    }

    /**
     * Update the primary (visible) fragment
     *
     * @param container - viewgroup containing object
     * @param position - position of object?
     * @param object - fragment to become visible
     */
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment)object;
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
            }
            mCurrentPrimaryItem = fragment;
        }
    }

    /**
     * Called after instantiate and destroy items
     *
     * @param container - viewgroup containing object
     */
    @Override
    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
            mFragmentManager.executePendingTransactions();
        }
    }

    /**
     * Save state of adapter
     *
     * @return - parcel of saved state of all fragments
     */
    @Override
    public Parcelable saveState() {
        Bundle state = null;
        if (grams.size() > 0) {
            state = new Bundle();
            Gram[] gramArray = new Gram[grams.size()];
            grams.toArray(gramArray);
            state.putParcelableArray("grams", gramArray);
        }

        return state;
    }

    /**
     * Rebuild state of entire adapter from a parcel
     *
     * @param state - saved fragment states and tags
     * @param loader - loads state
     */
    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        if (state != null) {
            Bundle bundle = (Bundle)state;
            bundle.setClassLoader(loader);
            Parcelable[] temp_grams = bundle.getParcelableArray("grams");

            if(this.grams == null) this.grams = new LinkedList<>();
            if(temp_grams != null) {
                for(int i = 0; i < temp_grams.length; i++) {
                    this.grams.add((Gram) temp_grams[i]);
                }
            }
        }
    }

    /**
     * Get the adapter index following the current one. Circularly loops from back
     * to front if end of adapter list is reached.
     *
     * @return - the next index in adapter (or 1st if current index is the last)
     */
    public int getNext() {
        if(this.index >= this.grams.size()-1)
            index = 0;
        else
            index++;

        return index;
    }

    /**
     * Get the adapter index prior to the current one. Circularly loops from front
     * to back if start of adapter list is reached.
     *
     * @return - the previous index in adapter (or last if current index is first)
     */
    public int getPrev() {
        if(this.index <= 0)
            index = this.grams.size()-1;
        else
            index--;

        return index;
    }

}

