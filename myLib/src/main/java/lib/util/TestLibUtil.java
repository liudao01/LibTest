package lib.util;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import httploglib.lib.R;
import httploglib.lib.been.HttpBeen;
import httploglib.lib.been.IpConfigBeen;
import httploglib.lib.crash.CrashHandler;
import httploglib.lib.service.WindowService;
import lib.Bus;
import lib.DemoHoverMenuService;
import lib.appstate.AppStateTracker;
import lib.data.HttpTransaction;
import lib.net.HttpNavigatorContent;
import lib.theming.HoverTheme;
import lib.theming.HoverThemeManager;

/**
 * @author liuml
 * @explain 测试库工具 关于工具库所有的操作都通过这个类
 * @time 2016/12/7 10:04
 */

public class TestLibUtil {
    public static List httpMoudleList;
    private Application context;
    IpLibConfig libConfig;

    //使用静态单例模式
    private static class InnerInstance {
        public static TestLibUtil instance = new TestLibUtil();
    }

    public static TestLibUtil getInstance() {
        return InnerInstance.instance;
    }


    /**
     * 初始化
     *
     * @param context
     */
    public void startUtil(Application context) {
        this.context = context;

        DemoHoverMenuService.showFloatingMenu(context);
        if (httpMoudleList == null) {
            httpMoudleList = new ArrayList<>();
        }
        //崩溃工具初始化
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(context);

        setupTheme();
        setupAppStateTracking();


    }

    /**
     * 1.0版本初始化
     *
     * @param context
     */
    public void initWindows(Application context) {
        this.context = context;
        if (!isServiceWork(context, WindowService.class.getName())) {
            Intent intent = new Intent(context, WindowService.class);
            context.startService(intent);
            //崩溃工具初始化
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(context);
            //UI
           // LayoutManager.init(context);
            //


        }

    }


    /**
     * 发送网路哦请求
     *
     * @param header
     * @param url
     * @param json
     */
    public void sendmessage(String header, String url, String json) {
        Intent intent1 = new Intent();
//        不能发送大量数据
        //BroadcastUtil.send(context, intent1, BroadcastUtil.windows);
        //直接操作静态变量
        Logger.d("打印数据 url=     \n" + url);
        HttpBeen been = new HttpBeen(url, json, header);
        //最大条数  0条避免数量过多溢出
        if (httpMoudleList != null) {

            if (httpMoudleList.size() > 30) {
                httpMoudleList.remove(30);
                httpMoudleList.add(been);
            } else {
                httpMoudleList.add(been);
            }
            HttpNavigatorContent.setList();
        }
    }

    /**
     * 发送网路哦请求
     */
    public void sendmessage(HttpBeen httpBeen) {
        //最大条数  0条避免数量过多溢出
        if (httpMoudleList != null) {

            if (httpMoudleList.size() > 30) {
                httpMoudleList.remove(30);
                httpMoudleList.add(httpBeen);
            } else {
                httpMoudleList.add(httpBeen);
            }
            HttpNavigatorContent.setList();
        }
    }
    /**
     * 发送网路哦请求
     */
    public void sendmessage(HttpTransaction httpBeen) {
        //最大条数  0条避免数量过多溢出
        if (httpMoudleList != null) {

            if (httpMoudleList.size() > 30) {
                httpMoudleList.remove(30);
                httpMoudleList.add(httpBeen);
            } else {
                httpMoudleList.add(httpBeen);
            }
            HttpNavigatorContent.setList();
        }
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
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    /**********************************************ip 操作******************************************************/

    /**
     * 这里逻辑判断  除非sp内 没有IP地址的数据才会去更新
     *
     * @param context
     * @param list
     */
    public void initIpSwitchs(Context context, List<IpConfigBeen> list) {
        libConfig = IpLibConfig.getInstance(context);
        //存入之前先判断是否已经有了
        String str = getSwitchs((context));
        if (TextUtils.isEmpty(str)) {
            //存入
            libConfig.initIpConfig(list);
        }
    }

    /**
     * 获取当前ip
     *
     * @param context
     * @return
     */
    public String getSwitchs(Context context) {
        String url = null;
        //切换服务器ip 地址 测试
        ListDataSave instance = ListDataSave.getInstance(context, ListDataSave.ListDataSave);
        List<IpConfigBeen> list = instance.getDataList(ListDataSave.listTag);
        for (IpConfigBeen ipConfigBeen : list) {
            if (ipConfigBeen.isSelect()) {
                url = ipConfigBeen.getUrl();
            }
        }
        return url;
    }

    /**
     * 添加ip
     *
     * @param ipConfigBeen
     */
    public void AddIp(IpConfigBeen ipConfigBeen) {

        IpLibConfig.getInstance(context).AddIp(ipConfigBeen);
    }

    /**
     * 删除ip
     *
     * @param position
     */
    public void DelIp(int position) {
        IpLibConfig.getInstance(context).DelIp(position);
    }

    /**
     * 删除全部ip
     */
    public void DelIpAll() {
        IpLibConfig.getInstance(context).DelIpAll();
    }

    /**
     * 设置ip 的数据
     *
     * @param dataList
     */
    public void setDataList(List<IpConfigBeen> dataList) {
        IpLibConfig.getInstance(context).setListSelect(dataList);
    }

    /**
     * 获取所有ip地址
     *
     * @return
     */
    public List<IpConfigBeen> getIpList() {
        List<IpConfigBeen> ipList = IpLibConfig.getInstance(context).getIpList();
        return ipList;
    }

    /**
     *
     */


//    public void setAppTracking(Application appTracking){
//        setupTheme();
//        setupAppStateTracking(appTracking);
//    }

    /**
     * 主题样式 颜色
     */
    private void setupTheme() {
        HoverTheme defaultTheme = new HoverTheme(
                ContextCompat.getColor(context, R.color.colorAccent),
                ContextCompat.getColor(context, R.color.colorGray));
        HoverThemeManager.init(Bus.getInstance(), defaultTheme);
    }

    private void setupAppStateTracking() {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);

        //整个app 的状态跟踪器
        AppStateTracker.init(context, Bus.getInstance());

        //获取栈顶的activity 的id 以及名字
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (activityManager.getAppTasks().size() > 0) {
                AppStateTracker.getInstance().trackTask(activityManager.getAppTasks().get(0).getTaskInfo());
            }
        }
    }

}
