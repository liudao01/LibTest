package httploglib.lib.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import httploglib.lib.R;
import httploglib.lib.actiity.CrashListActivity;
import httploglib.lib.actiity.IpConfigListActivity;
import httploglib.lib.actiity.ResultListActivity;
import httploglib.lib.been.HttpBeen;
import httploglib.lib.util.NotificationUtil;


/**
 * @author liuml
 * @explain
 * @time 2016/12/6 22:53
 */

public class WindowService extends Service implements AdapterView.OnItemClickListener {

    WindowManager mWindowManager;
    WindowManager.LayoutParams wmParams;
    LinearLayout mFloatLayout;
    Button mFloatView, bt_close;
    public static List<HttpBeen> httpMoudleList;
    ArrayList<String> mArrayList;
    WindowMyAdapter myAdapter;
    ListView listview;
    public WindowService instance;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        instance = this;

        //默认使用通知栏
        createFloatView();
        windowGone();
        createNoticefication();

        httpMoudleList = new ArrayList<>();

    }

    //创建通知栏
    private void createNoticefication() {

        NotificationUtil notification = NotificationUtil.getInstance();
        notification.createNotification(getApplicationContext(), instance);

    }

    public void windowVisible() {
        mFloatLayout.setVisibility(View.VISIBLE);
    }

    private void windowGone() {
        mFloatLayout.setVisibility(View.GONE);
    }

    //创建悬浮窗
    private void createFloatView() {
        //获取LayoutParams对象
        wmParams = new WindowManager.LayoutParams();

        //获取的是LocalWindowManager对象
        mWindowManager = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        // mWindowManager = getApplicationContext().getWindowManager();
        //mWindowManager = getWindow().getWindowManager();
        //获取的是CompatModeWrapper对象
        //mWindowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        wmParams.format = PixelFormat.RGBA_8888;
        ;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.x = 0;
        wmParams.y = 0;
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = LayoutInflater.from(getApplication());//LayoutInflater.from(getApplication());

        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout, null);
        mWindowManager.addView(mFloatLayout, wmParams);

        mFloatView = (Button) mFloatLayout.findViewById(R.id.float_id);
        //绑定触摸移动监听
        mFloatView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                wmParams.x = (int) event.getRawX() - mFloatLayout.getWidth() / 2;
                //25为状态栏高度
//                wmParams.y = (int) event.getRawY() - mFloatLayout.getHeight() / 2 - 40;
                wmParams.y = (int) event.getRawY() - 100;

                mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                return true;
            }
        });


        myAdapter = new WindowMyAdapter(getApplicationContext(), getData());
        //listview 设置
        listview = (ListView) mFloatLayout.findViewById(R.id.listview);
        listview.setAdapter(myAdapter);

        bt_close = (Button) mFloatLayout.findViewById(R.id.bt_close);
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int visibility = listview.getVisibility();
                if (visibility == View.VISIBLE) {
                    listview.setVisibility(View.GONE);
                    bt_close.setText("显");
                    bt_close.setWidth(30);
                    bt_close.setHeight(30);
                } else {
                    bt_close.setText("隐藏");
                    listview.setVisibility(View.VISIBLE);
                }
            }
        });

        listview.setOnItemClickListener(this);


    }

    public static void clearList() {
        if (httpMoudleList != null && httpMoudleList.size() > 0) {
            httpMoudleList.clear();
        }
    }


    private boolean waitDouble = true;
    private static final int DOUBLE_CLICK_TIME = 500; //两次单击的时间间隔


    private ArrayList<String> getData() {
        mArrayList = new ArrayList<>();
        mArrayList.add("网络");
        mArrayList.add("服务");
        mArrayList.add("崩溃");
        mArrayList.add("UI测试");
//        mArrayList.add("内存泄露");
        mArrayList.add("切换");
        mArrayList.add("关闭");
        return mArrayList;
    }

    // 单击响应事件  查看网络请求
    public void singleClick() {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), ResultListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // 双击响应事件
    private void doubleClick() {
        WindowService.this.stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatLayout != null) {
            mWindowManager.removeView(mFloatLayout);
        }
//        BroadcastUtil.unRegisterReceiver(getApplicationContext());
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent;
        switch (i) {
            case 0://网络
                Logger.d("打印 网络");
                singleClick();
                break;
            case 1://服务
                startIpService();
                break;
            case 2://崩溃
                startCrash();

                break;
            case 3://ui测试开关
                setView(true);
                break;
//            case 4://内存泄露查看开关
//
//                break;
            case 4://切换
                //WindowService.this.stopSelf();
                // windowGone();
                windowGone();
                createNoticefication();
            case 5://关闭
                windowGone();
                break;
        }
    }


    public void startIpService() {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), IpConfigListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(intent);
    }

    public void startCrash() {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), CrashListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(intent);
    }

    public static void setView(boolean b) {
       // LayoutManager.setTempViewVisible(b);
    }

    class WindowMyAdapter extends BaseAdapter {

        List<String> list;
        Context context;

        public WindowMyAdapter(Context context, List list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list != null) return list.size();
            else return 0;

        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = View.inflate(getApplicationContext(), R.layout.window_list_item, null);
            TextView tv_window_item = (TextView) view.findViewById(R.id.tv_window_item);
            tv_window_item.setText(list.get(i));
            return view;
        }
    }
}
