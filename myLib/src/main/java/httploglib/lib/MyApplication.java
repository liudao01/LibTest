package httploglib.lib;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.orhanobut.logger.Logger;

import lib.Bus;
import lib.appstate.AppStateTracker;
import lib.theming.HoverTheme;
import lib.theming.HoverThemeManager;

/**
 * MyApplication
 *
 * @author liuml
 * @time 2016-6-1 下午4:52:19
 */
public class MyApplication extends Application {
    private final static String TAG = "MyApplication";
    public static Context applicationContext;
    private static MyApplication instance;

    public MyApplication() {

    }

    /**
     * 开始调用
     */
    @Override
    public void onCreate() {
        applicationContext = this;
        setupTheme();
        setupAppStateTracking();
        /**
         * android 4.0及以上用户直接在application的onCreate中调用 com.wanjian.sak.LayoutManager.init(Application context) ,
         * 其他版本可以在activity的onResume中调用com.wanjian.sak.LayoutManager.init(Activity act)初始化。
         */
        Logger.d("开始调用");

    }


    public static final int SDK_VERSION_ECLAIR = 5;
    public static final int SDK_VERSION_DONUT = 4;
    public static final int SDK_VERSION_CUPCAKE = 3;
    public static boolean PRE_CUPCAKE =
            getSDKVersionNumber() < SDK_VERSION_DONUT ? true : false;

    public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;
    }

    // 单例模式中获取唯一的MyApplication实例
    public static MyApplication getInstance() {
        if (null == instance) {
            synchronized (MyApplication.class) {
                instance = new MyApplication();
            }
        }
        return instance;

    }

    /**
     * 主题样式 颜色
     */
    private void setupTheme() {
        HoverTheme defaultTheme = new HoverTheme(
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorGray));
        HoverThemeManager.init(Bus.getInstance(), defaultTheme);
    }

    private void setupAppStateTracking() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        //整个app 的状态跟踪器
        AppStateTracker.init(this, Bus.getInstance());

        //获取栈顶的activity 的id 以及名字
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (activityManager.getAppTasks().size() > 0) {
                AppStateTracker.getInstance().trackTask(activityManager.getAppTasks().get(0).getTaskInfo());
            }
        }
    }


}