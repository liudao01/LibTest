package httploglib.lib.actiity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import httploglib.lib.R;
import httploglib.lib.been.IpConfigBeen;
import httploglib.lib.util.IpLibConfig;
import httploglib.lib.util.ListDataSave;
import httploglib.lib.util.TestLibUtil;

/**
 * @explain 这是用于测试的类  demo 样例
 * @author liuml.
 * @time 2016/12/11 23:24
 */
public class DemoTest extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button start = (Button) findViewById(R.id.start);
        final TextView tv_ip = (TextView) findViewById(R.id.tv_ip);
        Button bt_select_ip = (Button) findViewById(R.id.bt_select_ip);
        Button bt_add_ip = (Button) findViewById(R.id.bt_add_ip);
        Button pop = (Button) findViewById(R.id.pop);
        Button send = (Button) findViewById(R.id.send);
        Button button_broken = (Button) findViewById(R.id.button_broken);

        //初始化

        //崩溃测试
        button_broken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.d(null);
            }
        });

        //网络测试
        final String url = "www.baidu.com";
        final String json = "{\n" +
                "    \"result\": \"success\",\n" +
                "    \"arr\": [\n" +
                "        {\n" +
                "            \"examid\": 11,\n" +
                "            \"class\": \"情感测试\",\n" +
                "            \"subclass\": \"家庭关系\",\n" +
                "            \"title\": \"家庭情感表达测试\",\n" +
                "            \"updatetime\": \"2013-12-04 17:14:17\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"examid\": 12,\n" +
                "            \"class\": \"情感测试\",\n" +
                "            \"subclass\": \"亲子关系\",\n" +
                "            \"title\": \"亲子关系测试\",\n" +
                "            \"updatetime\": \"2013-12-04 17:14:17\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                TestLibUtil.getInstance().sendmessage("", url, json);
            }
        });


        //切换服务器ip 地址 测试
        final ListDataSave instance = ListDataSave.getInstance(getApplicationContext(), ListDataSave.ListDataSave);
        //instance.setDataList(ListDataSave.listTag, getData());
        final IpLibConfig libConfig = new IpLibConfig(getApplicationContext());
        //存入
        libConfig.initIpConfig(getData());

        bt_select_ip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                List<IpConfigBeen> list = instance.getDataList(ListDataSave.listTag);
                for (IpConfigBeen ipConfigBeen : list) {
                    if (ipConfigBeen.isSelect()) {
                        tv_ip.setText(ipConfigBeen.toString());
                    }
                }
            }
        });

        //服务器地址添加测试

        bt_add_ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //测试添加
                libConfig.AddIp(new IpConfigBeen("www.google.com", "谷歌", false));
            }
        });



        //小米悬浮窗权限
        pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断小米手机是否有悬浮窗权限
                Context context = getApplicationContext();
                if (isMiuiFloatWindowOpAllowed(DemoTest.this)) {
                    Toast.makeText(context, "已经开启了", Toast.LENGTH_SHORT).show();
                } else {
                    openMiuiPermissionActivity(DemoTest.this);
                }
            }
        });
    }

    private List<IpConfigBeen> getData() {
        List<IpConfigBeen> list = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            list.add(new IpConfigBeen("www.baidu.com/" + i, "第" + i + "个ip服务地址", false));
        }
        return list;
    }


    /*****************************************************小米悬浮窗权限相关start******************************************************/

    /**
     * 经测试V5版本是有区别的
     *
     * @param context
     */
    public void openMiuiPermissionActivity(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");

        if ("V5".equals(getProperty())) {
            PackageInfo pInfo = null;
            try {
                pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("canking", "error");
            }
            intent.setClassName("com.miui.securitycenter", "com.miui.securitycenter.permission.AppPermissionsEditor");
            intent.putExtra("extra_package_uid", pInfo.applicationInfo.uid);
        } else {
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            intent.putExtra("extra_pkgname", context.getPackageName());
        }

        if (isActivityAvailable(context, intent)) {
            if (context instanceof Activity) {
                Activity a = (Activity) context;
                a.startActivityForResult(intent, 2);
            }
        } else {
            Log.e("canking", "Intent is not available!");
        }
    }

    public static boolean isActivityAvailable(Context cxt, Intent intent) {
        PackageManager pm = cxt.getPackageManager();
        if (pm == null) {
            return false;
        }
        List<ResolveInfo> list = pm.queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list != null && list.size() > 0;
    }

    public static String getProperty() {
        String property = "null";
        if (!"Xiaomi".equals(Build.MANUFACTURER)) {
            return property;
        }
        try {
            Class<?> spClazz = Class.forName("android.os.SystemProperties");
            Method method = spClazz.getDeclaredMethod("get", String.class, String.class);
            property = (String) method.invoke(spClazz, "ro.miui.ui.version.name", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return property;
    }

    /**
     * 判断MIUI的悬浮窗权限
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isMiuiFloatWindowOpAllowed(Context context) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            return checkOp(context, 24);  // AppOpsManager.OP_SYSTEM_ALERT_WINDOW
        } else {
            if ((context.getApplicationInfo().flags & 1 << 27) == 1 << 27) {
                return true;
            } else {
                return false;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean checkOp(Context context, int op) {
        final int version = Build.VERSION.SDK_INT;

        if (version >= 19) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {

                Class<?> spClazz = Class.forName(manager.getClass().getName());
                Method method = manager.getClass().getDeclaredMethod("checkOp", int.class, int.class, String.class);
                int property = (Integer) method.invoke(manager, op,
                        Binder.getCallingUid(), context.getPackageName());
                Logger.e(AppOpsManager.MODE_ALLOWED + " invoke " + property);

                if (AppOpsManager.MODE_ALLOWED == property) {  //这儿反射就自己写吧
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                Logger.e(e.getMessage());
            }
        } else {
            Logger.e("Below API 19 cannot invoke!");
        }
        return false;
    }
    /****************************************************小米悬浮窗权限相关end**********************************************************/


    /***********ip 地址改变测试代码**************/

    public static void IPinit() {
        List<IpConfigBeen> list = new ArrayList<>();

        list.add(new IpConfigBeen("http://192.168.1.103:8080/truck/api/", "贺刚", false));
        list.add(new IpConfigBeen("http://192.168.1.102:8080/truck/api/", "汝军", false));
        list.add(new IpConfigBeen("http://192.168.1.157:6060/truck/api/", "梦阳", false));
        list.add(new IpConfigBeen("http://192.168.1.154:8080/truck/api/", "赵哲 阿里云", false));
        list.add(new IpConfigBeen("http://58.58.62.229:8008/truck/api/", "预上线版本", false));
        list.add(new IpConfigBeen("http://222.175.171.242:8001/truck/api/", "外网242", false));
        list.add(new IpConfigBeen("http://58.58.62.227:81/truck/api/", "外网227", false));
        list.add(new IpConfigBeen("http://58.58.62.229:8008/truck/api/", "预上线版本", false));
        list.add(new IpConfigBeen("http://www.woniuhuoche.com:8008/truck/api/", "正式接口", true));


        //注册广播
//        BroadcastUtil.registerReceiver(MyApplication.getContext(), new BroadcastUtil.IReceiver() {
//            @Override
//            public void onReceive(Context ctx, Intent intent) {
//                if (intent.getAction() == BroadcastUtil.IP) {//网络请求
//                    String url = intent.getStringExtra(CommonData.PARAMS_URL);
//                    HttpUrls.switchs = url;
//                    LogUtil.d("接受的url     " + url);
//                    String url1 = getSwitchs();
//                    LogUtil.d("接受的url通过sp     " + url);
//                    switchs = url1;
//                }
//            }
//        }, BroadcastUtil.IP);
    }




}