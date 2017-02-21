package httploglib.lib;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.Logger;

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

        /**
         * android 4.0及以上用户直接在application的onCreate中调用 com.wanjian.sak.LayoutManager.init(Application context) ,
         * 其他版本可以在activity的onResume中调用com.wanjian.sak.LayoutManager.init(Activity act)初始化。
         */
        Logger.d("开始调用");
//        if (!PRE_CUPCAKE) {
//            com.wanjian.sak.LayoutManager.init(this);
//            Logger.d("调用了初始化方式");
//        }
    }


    public static final int SDK_VERSION_ECLAIR = 5;
    public static final int SDK_VERSION_DONUT = 4;
    public static final int SDK_VERSION_CUPCAKE = 3;
    public static boolean PRE_CUPCAKE =
            getSDKVersionNumber() < SDK_VERSION_DONUT ? true : false;

    public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
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


}