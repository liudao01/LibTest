package demo.libtestapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import httploglib.lib.been.IpConfigBeen;
import httploglib.lib.util.TestLibUtil;

public class MainActivity extends Activity {
    public Context context;
    private TextView tvIpList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;


        setContentView(R.layout.activity_main);

        TestLibUtil.getInstance().startUtil(MainActivity.this);
        tvIpList = (TextView) findViewById(R.id.tv_ip_list);

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

        TestLibUtil.getInstance().sendmessage("", url, json);

        //ip 测试
        IPinit();

        List<IpConfigBeen> ipList = TestLibUtil.getInstance().getIpList();
        String list = "";
        for (IpConfigBeen ipConfigBeen : ipList) {
            list = list + ipConfigBeen.toString()+"\n";
        }
        tvIpList.setText(list);

        /**
         * 开始的时候 的权限 判断是否可以让悬浮窗 悬浮到所有应用的前面
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent drawOverlaysSettingsIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                drawOverlaysSettingsIntent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(drawOverlaysSettingsIntent);
            }
        }
    }

    public void IPinit() {
        List<IpConfigBeen> list = new ArrayList<>();

        list.add(new IpConfigBeen("http://192.168.1.103:8080/", "测试地址1", false));
        list.add(new IpConfigBeen("http://192.168.1.102:8080/truck/api/", "测试地址2", false));
        list.add(new IpConfigBeen("http://192.168.1.157:6060/truck/api/", "测试地址3", false));
        list.add(new IpConfigBeen("http://192.168.1.154:8080/truck/api/", "测试地址4", false));
        list.add(new IpConfigBeen("http://xxx.xxx.xxx.229:8008/truck/api/", "测试地址5", false));
        list.add(new IpConfigBeen("http://xxx.175.xxx.242:8001/truck/api/", "测试地址6", false));
        list.add(new IpConfigBeen("http://58.58.62.227:81/truck/api/", "测试地址7", false));
        list.add(new IpConfigBeen("http://58.xx.xx.xxx:8008/truck/api/", "测试地址8", false));
        list.add(new IpConfigBeen("http://www.xxxxxxxx.com:8008/truck/api/", "正式接口", true));
        TestLibUtil.getInstance().initIpSwitchs(this.getApplicationContext(), list);
    }

}
