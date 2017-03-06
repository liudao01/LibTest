package demo.libtestapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import httploglib.lib.util.TestLibUtil;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TestLibUtil.getInstance().startUtil(MainActivity.this);



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
}
