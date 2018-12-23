package com.grammiegram.grammiegram_android.interfaces;

import android.view.View;

/**
 * inner interface for the parent BoardListActivity to implement in order to
 * hear onClick actions
 */
public interface ItemClickListener {
    void onItemClick(View view, int pos);
}