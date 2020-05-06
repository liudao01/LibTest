package demo.libtestapp;

import android.app.Application;

import lib.util.TestLibUtil;

/**
 * @author liuml
 * @explain
 * @time 2017/3/13 15:09
 */

public class MyApp extends Application {
    public  static  MyApp myApp ;
    @Override
    public void onCreate() {
        super.onCreate();
        myApp  = this;
        TestLibUtil.getInstance().startUtil(this);
    }
}
