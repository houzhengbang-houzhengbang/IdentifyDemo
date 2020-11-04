package com.pad.rzjys;

import android.app.Application;
import android.content.Context;

/**
 * FileName: MyApplication
 * Author: houzhengbang
 * Date: 2020/11/3 1:11 PM
 * Description:
 */
public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();


        mContext = getApplicationContext();
    }


    public static Context getContext() {
        return mContext;
    }
}