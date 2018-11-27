package com.grammiegram.grammiegram_android.interfaces;

import com.grammiegram.grammiegram_android.POJO.LoginResponse;

public interface LoginCallBack extends CallBack {
    void onSuccess(LoginResponse response);
}
