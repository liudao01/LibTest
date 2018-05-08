package lib.util;

import android.app.Application;
import android.content.Context;

import java.util.List;

/**
 * @author liuml
 * @explain 测试库工具 关于工具库所有的操作都通过这个类
 * @time 2016/12/7 10:04
 */

public class TestLibUtil {
    public static List httpMoudleList;
    private Application context;

    //使用静态单例模式
    private static class InnerInstance {
        public static TestLibUtil instance = new TestLibUtil();
    }

    public static TestLibUtil getInstance() {
        return InnerInstance.instance;
    }


    /**
     * 1.1版本初始化
     *
     * @param context
     */
    public void startUtil(Application context) {
    }


    /**
     * 发送网路哦请求
     *
     * @param header
     * @param url
     * @param json
     */
    public void sendmessage(String header, String url, String json) {
    }
    /**
     * 发送网路哦请求
     */
    public void sendmessage(HttpTransaction httpBeen) {
    }


    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        return isWork;
    }


}
