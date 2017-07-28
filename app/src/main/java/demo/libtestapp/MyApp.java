package demo.libtestapp;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import lib.util.TestLibUtil;

/**
 * @author liuml
 * @explain
 * @time 2017/3/13 15:09
 */

public class MyApp extends Application {
    private RefWatcher refWatcher;
    public static RefWatcher getRefWatcher(Context context) {
        MyApp application = (MyApp) context.getApplicationContext();
        return application.refWatcher;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        TestLibUtil.getInstance().startUtil(this);
        refWatcher = LeakCanary.install(this);
    }


}
