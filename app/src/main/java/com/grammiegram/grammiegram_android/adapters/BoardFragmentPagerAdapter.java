package com.grammiegram.grammiegram_android.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.grammiegram.grammiegram_android.POJO.Gram;
import com.grammiegram.grammiegram_android.POJO.GramsListResponse;
import com.grammiegram.grammiegram_android.fragments.BoardPagerFragment;

import java.util.List;

/**
 * A {@link FragmentStatePagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class BoardFragmentPagerAdapter extends FragmentStatePagerAdapter {
    //TODO: adapter must implement the logic of destroying grams when they expire!
    //TODO: lazily get grams from adapter so as not to crash app (already happens? dont forget to destroy fragments)

    private List<Gram> grams;

    public BoardFragmentPagerAdapter(GramsListResponse grams, FragmentManager fm) {
        super(fm);

        this.grams = grams.getGrams();
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a BoardPagerFragment.
        Gram gram = this.grams.get(position);
        return BoardPagerFragment.newInstance(gram);
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
}

