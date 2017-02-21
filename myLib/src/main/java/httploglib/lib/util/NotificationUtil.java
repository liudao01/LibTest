package httploglib.lib.util;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.widget.Button;
import android.widget.RemoteViews;

import java.lang.reflect.Method;

import httploglib.lib.R;
import httploglib.lib.service.WindowService;

/**
 * @author liuml.
 * @explain 通知栏工具类 使用静态内部类单例模式
 * @time 2017/1/4 10:29
 */
public class NotificationUtil {

    private WindowService service;
    private Context mContext;
    /**
     * Notification管理
     */
    public NotificationManager mNotificationManager;
    /**
     * TAG
     */
    private final static String TAG = "CustomActivity";
    /**
     * 按钮：显示自定义带按钮的通知
     */
    private Button btn_show_custom_button;
    /**
     * NotificationCompat 构造器
     */
    Builder mBuilder;

    /**
     * 通知栏按钮广播
     */
    public ButtonBroadcastReceiver bReceiver;
    /**
     * 通知栏按钮点击事件对应的ACTION
     */
    public final static String ACTION_BUTTON = "com.notifications.intent.action.ButtonClick";


    public NotificationUtil() {
    }

    public static NotificationUtil getInstance() {
        return SingleonHolder.notifications;
    }

    private static class SingleonHolder {
        private static final NotificationUtil notifications = new NotificationUtil();
    }


    public void createNotification(Context context, WindowService service) {
        this.mContext = context;
        this.service = service;
        showButtonNotify();
        initButtonReceiver();
    }


    public final static String INTENT_BUTTONID_TAG = "ButtonId";
    /**
     * 网路 按钮点击 ID
     */
    public final static int BUTTON_NET_ID = 1;
    /**
     *  IP地址切换 按钮点击 ID
     */
    public final static int BUTTON_SERVICE_ID = 2;
    /**
     *  bug 按钮点击 ID
     */
    public final static int BUTTON_BUG_ID = 3;

    /**
     * ui 测试
     */
    public final static int BUTTON_UI_ID = 4;

    /**
     * 切换显示
     */
    public final static int BUTTON_CHANGE_ID = 5;

    private int notificationId = 200;

//    private Notification notify;

    /**
     * 带按钮的通知栏
     */
    public void showButtonNotify() {
        mNotificationManager = (NotificationManager) mContext
                .getSystemService(mContext.NOTIFICATION_SERVICE);
        Builder mBuilder = new Builder(mContext);
        RemoteViews mRemoteViews = new RemoteViews(mContext.getPackageName(),
                R.layout.notification_view);

        // 点击的事件处理
        Intent buttonIntent = new Intent(ACTION_BUTTON);
        /* 网络按钮 */
        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_NET_ID);
        // 这里加了广播，所及INTENT的必须用getBroadcast方法
        PendingIntent intent_prev = PendingIntent.getBroadcast(mContext, 1,
                buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_net, intent_prev);
        /* 服务 */
        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_SERVICE_ID);
        PendingIntent intent_paly = PendingIntent.getBroadcast(mContext, 2,
                buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_service, intent_paly);
        /* 崩溃 */
        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_BUG_ID);
        PendingIntent intent_next = PendingIntent.getBroadcast(mContext, 3,
                buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_bug, intent_next);
		/* UI测试 */
        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_UI_ID);
        PendingIntent intent_ui_test = PendingIntent.getBroadcast(mContext, 4,
                buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_ui_test, intent_ui_test);
		/* 切换 */
        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_CHANGE_ID);
        PendingIntent intent_change = PendingIntent.getBroadcast(mContext, 5,
                buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_change, intent_change);

        mBuilder.setTicker("通知标题6");
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContent(mRemoteViews);
        mBuilder.setOngoing(true)//设置为不可清除模式
                .setTicker("测试")//系统收到通知时，通知栏上面显示的文字。
                .setPriority(Notification.PRIORITY_DEFAULT);// 设置该通知优先级
//        mBuilder.setContent(mRemoteViews)//通知内容
//                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))//设置通知栏点击意图
//                .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
//                .setTicker("正在播放")//系统收到通知时，通知栏上面显示的文字。
//                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
//                .setOngoing(true) //设置为不可清除模式
//                .setSmallIcon(R.mipmap.ic_launcher);//显示在通知栏上的小图标
//        notify = mBuilder.build();
//        notify.flags = Notification.FLAG_ONGOING_EVENT;
        // 会报错，还在找解决思路
        // notify.contentView = mRemoteViews;
        // notify.contentIntent = PendingIntent.getActivity(this, 0, new
        // Intent(), 0);
        mNotificationManager.notify(notificationId, mBuilder.build()); //显示通知，
    }

    /**
     * 带按钮的通知栏点击广播接收
     */
    public void initButtonReceiver() {
        bReceiver = new ButtonBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_BUTTON);
        mContext.registerReceiver(bReceiver, intentFilter);
    }

    //代码实现关闭通知栏
    public static void collapseStatusBar(Context context) {
        try {
            Object statusBarManager = context.getSystemService("statusbar");
            Method collapse;
            if (Build.VERSION.SDK_INT <= 16) {
                collapse = statusBarManager.getClass().getMethod("collapse");
            } else {
                collapse = statusBarManager.getClass().getMethod("collapsePanels");
            }
            collapse.invoke(statusBarManager);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    /**
     * 广播监听按钮点击事件
     */
    public class ButtonBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            collapseStatusBar(mContext);
            String action = intent.getAction();
            if (action.equals(ACTION_BUTTON)) {
                // 通过传递过来的ID判断按钮点击属性或者通过getResultCode()获得相应点击事件
                int buttonId = intent.getIntExtra(INTENT_BUTTONID_TAG, 0);
                switch (buttonId) {
                    case BUTTON_NET_ID:
                        service.singleClick();
//                        Toast.makeText(mContext, "网络", Toast.LENGTH_SHORT).show();
                        break;
                    case BUTTON_SERVICE_ID:
//					isPlay = !isPlay;
//                        showButtonNotify();
                        service.startIpService();
//                        Toast.makeText(mContext, "服务", Toast.LENGTH_SHORT).show();
                        break;
                    case BUTTON_BUG_ID:
                        service.startCrash();
//                        Toast.makeText(mContext, "bug", Toast.LENGTH_SHORT).show();
                        break;
                    case BUTTON_UI_ID:
                        service.setView(true);
//                        Toast.makeText(mContext, "UI", Toast.LENGTH_SHORT).show();
                        break;
                    case BUTTON_CHANGE_ID:
                        service.windowVisible();
//                        Toast.makeText(mContext, "切换", Toast.LENGTH_SHORT).show();
                        clearNotify(notificationId);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * @获取默认的pendingIntent,为了防止2.3及以下版本报错
     * @flags属性: 在顶部常驻:Notification.FLAG_ONGOING_EVENT
     * 点击去除： Notification.FLAG_AUTO_CANCEL
     */
    public PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 1, new Intent(), flags);
        return pendingIntent;
    }

    /**
     * 清除当前创建的通知栏
     */
    public void clearNotify(int notifyId) {
        mNotificationManager.cancel(notifyId);//删除一个特定的通知ID对应的通知
//		mNotification.cancel(getResources().getString(R.string.app_name));
    }

    /**
     * 清除所有通知栏
     */
    public void clearAllNotify() {
        mNotificationManager.cancelAll();// 删除你发的所有通知
    }
}
