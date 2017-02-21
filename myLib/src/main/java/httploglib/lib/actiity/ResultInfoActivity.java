package httploglib.lib.actiity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import httploglib.lib.R;
import httploglib.lib.been.HttpBeen;
import httploglib.lib.service.WindowService;


public class ResultInfoActivity extends Activity {
    private TextView tvUrl;
    private TextView tvJsonSize;
    private TextView tvJson;
    private Button btClose;
    private TextView tvJsonHeader;


    /**
     * jsonObject.put("phoneModel", SystemUtils.getMobileModel());//机型
     jsonObject.put("sysVersion", SystemUtils.getMobileSysVersion());//系统版本
     jsonObject.put("w_and_h", SystemUtils.getScreenWidth(null)+"*"+SystemUtils.getScreenHeight(null));//宽高
     jsonObject.put("version", SystemUtils.getVersionName());//app版本

     JSONObject addressJson=new JSONObject();
     addressJson.put("province", sharedUtils.getLocationProvince());//省
     addressJson.put("city", sharedUtils.getLocationCity());//市
     addressJson.put("district", sharedUtils.getLocationDistrict());//县区
     addressJson.put("detailsAddress", sharedUtils.getLocationAddress());//详细地址
     jsonObject.put("userAddress",addressJson);
     jsonObject.put("network",SystemUtils.getNetworkType());//网络制式
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        tvUrl = (TextView) findViewById(R.id.tv_url);
        tvJsonSize = (TextView) findViewById(R.id.tv_json_size);
        tvJson = (TextView) findViewById(R.id.tv_json);
    tvJsonHeader = (TextView) findViewById(R.id.tv_json_header);
        btClose = (Button) findViewById(R.id.bt_close);


        int position = getIntent().getIntExtra("javabean", 0);
        HttpBeen httpBeen = WindowService.httpMoudleList.get(position);
        tvUrl.setText(httpBeen.getUrl());
        tvJson.setText(httpBeen.getJson());

        if (!TextUtils.isEmpty(httpBeen.getJson())) {
            byte[] buff = httpBeen.getJson().getBytes();
            int f = buff.length;
            double kb = f / 1024.0;
            kb = (double)(Math.round(kb*100)/100.0);
            tvJsonSize.setText(kb + " kb");
        }

        tvJsonHeader.setText(httpBeen.getHttpHeader());
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
