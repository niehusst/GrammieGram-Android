package com.grammiegram.grammiegram_android.interfaces;

import okhttp3.ResponseBody;

public interface CallBack {
    void onSuccess(APIResponse response);
    void onNetworkError(String error);
    void onServerError(int code, ResponseBody body);
}
