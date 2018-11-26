package com.grammiegram.grammiegram_android.interfaces;

import com.grammiegram.grammiegram_android.POJO.GramsListResponse;

public interface GramsCallBack extends CallBack {
    void onSuccess(GramsListResponse response);
}
