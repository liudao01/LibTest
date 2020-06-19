package demo.libtestapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import httploglib.lib.been.IpConfigBeen;
import lib.data.HttpTransaction;
import lib.util.TestLibUtil;
import lib.http.ChuckInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {
    public Context context;
    private TextView tvIpList;
    private Button btStart;
    private Button btCrash;
    private Button btStartOne;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        // TestLibUtil.getInstance().startUtil(getApplication());

        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                Intent drawOverlaysSettingsIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                drawOverlaysSettingsIntent.setData(Uri.parse("package:" + context.getPackageName()));
                context.startActivity(drawOverlaysSettingsIntent);
            }
        }
        btStart = (Button) findViewById(R.id.bt_start);

        tvIpList = (TextView) findViewById(R.id.tv_ip_list);

        btCrash = (Button) findViewById(R.id.bt_crash);
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doHttpActivity();
            }
        });

        btCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = 1 / 0;
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


        btStartOne = findViewById(R.id.bt_start_one);

        HttpTransaction httpTransaction = new HttpTransaction();
        httpTransaction.setUrl(url);
        httpTransaction.setResponseBody(json);
        TestLibUtil.getInstance().sendmessage(httpTransaction);

        btStartOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpTransaction httpTransaction = new HttpTransaction();
                httpTransaction.setUrl("www.google.com__" + (count++));
                httpTransaction.setResponseBody(json);

                TestLibUtil.getInstance().sendmessage(httpTransaction);
            }
        });
//        tvIpList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TestLibUtil.getInstance().sendmessage("", url, json);
//            }
//        });
        //ip 测试
        IPinit();

        List<IpConfigBeen> ipList = TestLibUtil.getInstance().getIpList();
        String list = "";
        for (IpConfigBeen ipConfigBeen : ipList) {
            list = list + ipConfigBeen.toString() + "\n";
        }
        tvIpList.setText(list);


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


    private OkHttpClient getClient(Context context) {
        return new OkHttpClient.Builder()
                // Add a ChuckInterceptor instance to your OkHttp client
                .addInterceptor(new ChuckInterceptor(context))
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    private void doHttpActivity() {
        SampleApiService.HttpbinApi api = SampleApiService.getInstance(getClient(this));
        Callback<Void> cb = new Callback<Void>() {
            @Override
            public void onResponse(Call call, Response response) {
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
            }
        };
        api.get().enqueue(cb);
        api.post(new SampleApiService.Data("posted_____sssssssssssssssss_post请求")).enqueue(cb);
        api.patch(new SampleApiService.Data("patched")).enqueue(cb);
        api.put(new SampleApiService.Data("put")).enqueue(cb);
        api.delete().enqueue(cb);
        api.status(201).enqueue(cb);
        api.status(401).enqueue(cb);
        api.status(500).enqueue(cb);
        api.delay(9).enqueue(cb);
        api.delay(15).enqueue(cb);
        api.redirectTo("https://http2.akamai.com").enqueue(cb);
        api.redirectTo("https://http2.akamai2.com").enqueue(cb);
        api.redirect(3).enqueue(cb);
        api.redirectRelative(2).enqueue(cb);
        api.redirectAbsolute(4).enqueue(cb);
        api.stream(500).enqueue(cb);
        api.streamBytes(2048).enqueue(cb);
        api.image("image/png").enqueue(cb);
        api.gzip().enqueue(cb);
        api.xml().enqueue(cb);
        api.utf8().enqueue(cb);
        api.deflate().enqueue(cb);
        api.cookieSet("v").enqueue(cb);
        api.basicAuth("me", "pass").enqueue(cb);
        api.drip(512, 5, 1, 200).enqueue(cb);
        api.deny().enqueue(cb);
        api.cache("Mon").enqueue(cb);
        api.cache(30).enqueue(cb);
    }

}
