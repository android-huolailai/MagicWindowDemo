package com.wingsofts.magicwindowdemo;

import android.app.Application;
import com.zxinsight.Session;

/**
 * Created by wing on 16/7/29.
 */
public class MyApp extends Application {
  @Override public void onCreate() {
    super.onCreate();

    Session.setAutoSession(this);
  }
}
