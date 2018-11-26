package com.grammiegram.grammiegram_android.interfaces;

import com.grammiegram.grammiegram_android.POJO.BoardListResponse;

public interface BoardListCallBack extends CallBack {
    void onSuccess(BoardListResponse response);
}
