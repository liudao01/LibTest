/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lib.net;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;
import httploglib.lib.R;
import httploglib.lib.been.HttpBeen;
import io.mattcarroll.hover.Navigator;
import io.mattcarroll.hover.NavigatorContent;
import lib.adapter.AutoAdapter;
import lib.adapter.ViewHolder;
import lib.data.HttpTransaction;
import lib.theming.HoverTheme;
import lib.util.TestLibUtil;
import lib.util.Utils;

/**
 * {@link NavigatorContent} that displays an introduction to Hover.
 */
public class HttpNavigatorContent extends FrameLayout implements NavigatorContent, View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final String TAG = "HttpNavigatorContent";
    private final EventBus mBus;
    private View mLogo;
    //    private HoverMotion mHoverMotion;
    private Button btClear;
    //    private RecyclerView recyclerview;
//    List<HttpBeen> beens;
    List<HttpTransaction> httpTransactionList;
    Button bt_clear;
    Context context;
    //    static MyAdapter myAdapter;
    private LinearLayout httpResult;
    private LinearLayout httpResultList;
    private ListView listview;

    private TextView tvUrl;
    private TextView tvJsonSize;
    private TextView tvJson;
    private Button btClose;
    private TextView tvJsonHeader;

    private int clickPosition;
    static ListHttpAdapter listHttpAdapter;

    TextView url;
    TextView method;
    TextView protocol;
    TextView status;
    TextView response;
    TextView ssl;
    TextView requestTime;
    TextView responseTime;
    TextView duration;
    TextView requestSize;
    TextView responseSize;
    TextView totalSize;
    private TextView requestHeaders;
    private TextView requestBody;
    private TextView responseHeaders;
    private TextView reponseBody;


    static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<HttpTransaction> httpTransactionList = (List<HttpTransaction>) msg.obj;
            listHttpAdapter.setList(httpTransactionList);
        }
    };

    public HttpNavigatorContent(@NonNull Context context, @NonNull EventBus bus) {
        super(context);
        mBus = bus;
        this.context = context;
        init();
        initResult();
    }


    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.result_list, this, true);
//        mLogo = findViewById(R.id.imageview_logo);
//        mHoverMotion = new HoverMotion();
        httpResult = (LinearLayout) findViewById(R.id.http_result);
        httpResultList = (LinearLayout) findViewById(R.id.http_result_list);

        btClear = (Button) findViewById(R.id.bt_clear);
//        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
//        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerview.setAdapter(new HttpAdapter(TestLibUtil.httpMoudleList));
        //添加分割线
//        recyclerview.addItemXDecoration(new DividerItemDecoration(  getActivity(), DividerItemDecoration.HORIZONTAL_LIST));
        btClear.setOnClickListener(this);


        listview = (ListView) findViewById(R.id.listview);
        listview.setOnItemClickListener(this);
        listview.setOnItemLongClickListener(this);

        httpTransactionList = TestLibUtil.httpMoudleList;

//        if (beens != null && beens.size() > 0) {
//            Collections.reverse(beens);//倒序刚发的在最前面
//        }
        if (httpTransactionList != null && httpTransactionList.size() > 0) {
            Collections.reverse(httpTransactionList);//倒序刚发的在最前面
        }
        listHttpAdapter = new ListHttpAdapter(context, httpTransactionList);
        // myAdapter = new MyAdapter(context, beens);
//        listview.setAdapter(myAdapter);
        listview.setAdapter(listHttpAdapter);

        // Logger.d( beens.get(0).getJson());
        bt_clear = (Button) findViewById(R.id.bt_clear);
        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestLibUtil.httpMoudleList.clear();
                httpTransactionList.clear();
//              myAdapter.setList(beens);
                listHttpAdapter.setList(httpTransactionList);
            }
        });


        //数据详情的控件

        url = (TextView) findViewById(R.id.url);
        method = (TextView) findViewById(R.id.method);
        protocol = (TextView) findViewById(R.id.protocol);
        status = (TextView) findViewById(R.id.status);
        response = (TextView) findViewById(R.id.response);
        ssl = (TextView) findViewById(R.id.ssl);
        requestTime = (TextView) findViewById(R.id.request_time);
        responseTime = (TextView) findViewById(R.id.response_time);
        duration = (TextView) findViewById(R.id.duration);
        requestSize = (TextView) findViewById(R.id.request_size);
        responseSize = (TextView) findViewById(R.id.response_size);
        totalSize = (TextView) findViewById(R.id.total_size);

        requestHeaders = (TextView) findViewById(R.id.request_headers);
        requestBody = (TextView) findViewById(R.id.request_body);
        responseHeaders = (TextView) findViewById(R.id.response_headers);
        reponseBody = (TextView) findViewById(R.id.reponse_body);

    }

    private void initResult() {
        tvUrl = (TextView) findViewById(R.id.tv_url);
        tvJsonSize = (TextView) findViewById(R.id.tv_json_size);
        tvJson = (TextView) findViewById(R.id.tv_json);
        tvJsonHeader = (TextView) findViewById(R.id.tv_json_header);
        btClose = (Button) findViewById(R.id.bt_close);


        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResultList();
            }
        });
    }


    private void showResult() {
        httpResult.setVisibility(View.VISIBLE);
        httpResultList.setVisibility(View.GONE);
    }

    private void showResultList() {
        httpResultList.setVisibility(View.VISIBLE);
        httpResult.setVisibility(View.GONE);

    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mBus.registerSticky(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        mBus.unregister(this);
        super.onDetachedFromWindow();
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onShown(@NonNull Navigator navigator) {
//        if (TestLibUtil.httpMoudleList != null) {
//            if (myAdapter != null) {
//                myAdapter.setList(TestLibUtil.httpMoudleList);
//            }
//        }
    }

    public static void oldsetList() {

//        if (TestLibUtil.httpMoudleList != null) {
//            if (myAdapter != null) {
//                List<HttpBeen> httpMoudleList = TestLibUtil.httpMoudleList;
//                if (httpMoudleList != null && httpMoudleList.size() > 0) {
//                    Collections.reverse(httpMoudleList);//倒序刚发的在最前面
//                }
//                myAdapter.setList(httpMoudleList);
//            }
//        }
    }

    public static void setList() {

        if (TestLibUtil.httpMoudleList != null) {
            if (listHttpAdapter != null) {
                List<HttpTransaction> httpMoudleList = TestLibUtil.httpMoudleList;
                if (httpMoudleList != null && httpMoudleList.size() > 0) {
                    Collections.reverse(httpMoudleList);//倒序刚发的在最前面
                }
                Message message = Message.obtain();
                message.obj = httpMoudleList;
                handler.sendMessage(message);

            }
        }
    }

    @Override
    public void onHidden() {

    }

    public void onEventMainThread(@NonNull HoverTheme newTheme) {
        //点击导航按钮的时候调用这个
//        if (TestLibUtil.httpMoudleList != null) {
//            if (myAdapter == null) {
//                myAdapter = new MyAdapter(context, TestLibUtil.httpMoudleList);
//            } else {
//                myAdapter.setList(TestLibUtil.httpMoudleList);
//
//            }
//        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//        Toast.makeText(context,"点击了",Toast.LENGTH_SHORT).show();
        showResult();
        clickPosition = position;
        //setData();
        setHttpInfoData((HttpTransaction) httpTransactionList.get(position));
    }

    private void setHttpInfoData(HttpTransaction transaction) {
        url.setText(transaction.getUrl());
        method.setText(transaction.getMethod());
        protocol.setText(transaction.getProtocol());
        status.setText(transaction.getStatus().toString());
        response.setText(transaction.getResponseSummaryText());
        ssl.setText((transaction.isSsl() ? R.string.chuck_yes : R.string.chuck_no));
        requestTime.setText(transaction.getRequestDateString());
        responseTime.setText(transaction.getResponseDateString());
        duration.setText(transaction.getDurationString());
        requestSize.setText(transaction.getRequestSizeString());
        responseSize.setText(transaction.getResponseSizeString());
        totalSize.setText(transaction.getTotalSizeString());

        requestHeaders.setVisibility((TextUtils.isEmpty(transaction.getRequestHeadersString(true)) ? View.GONE : View.VISIBLE));
        responseHeaders.setVisibility((TextUtils.isEmpty(transaction.getResponseHeadersString(true)) ? View.GONE : View.VISIBLE));

        requestHeaders.setText("requestHeaders  " + Html.fromHtml(transaction.getRequestHeadersString(true)));
        responseHeaders.setText("responseHeaders  " + Html.fromHtml(transaction.getResponseHeadersString(true)));

        setText(1, requestBody, transaction.getFormattedRequestBody(), transaction.requestBodyIsPlainText());
        setText(2, reponseBody, transaction.getFormattedResponseBody(), transaction.responseBodyIsPlainText());
    }

    private void setText(int type, TextView textView, String bodyString, boolean isPlainText) {
        if (!isPlainText) {
            textView.setText(context.getString(R.string.chuck_body_omitted));
        } else {
            if (type == 1) {
                textView.setText("requestBody " + bodyString);
            } else {
                textView.setText("reponseBody " + bodyString);
            }
        }
    }

    private void setData() {
        HttpBeen httpBeen = (HttpBeen) TestLibUtil.httpMoudleList.get(clickPosition);
        tvUrl.setText(httpBeen.getUrl());
        tvJson.setText(httpBeen.getJson());

        if (!TextUtils.isEmpty(httpBeen.getJson())) {
            byte[] buff = httpBeen.getJson().getBytes();
            int f = buff.length;
            double kb = f / 1024.0;
            kb = (double) (Math.round(kb * 100) / 100.0);
            tvJsonSize.setText(kb + " kb");
        }

        tvJsonHeader.setText(httpBeen.getHttpHeader());
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//        HttpBeen httpBeen = (HttpBeen) TestLibUtil.httpMoudleList.get(position);
        HttpTransaction httpTransaction = (HttpTransaction) TestLibUtil.httpMoudleList.get(position);
        // String str = TextUtils.isEmpty(httpTransaction.getRequestBody()) ? "" : "请求参数: " + httpTransaction.getRequestBody();//请求参数
        Utils.copy(context, "请求方式: " + httpTransaction.getMethod() + "\n" + "请求地址: " + httpTransaction.getUrl());
        return true;
    }


    class MyAdapter extends AutoAdapter {
        public MyAdapter(Context context, List<?> list) {
            super(context, list);
            // TODO Auto-generated constructor stub
        }


        @Override
        public int getLayoutID(int position) {
            return R.layout.result_list_item;
        }

        @Override
        public void baseGetView(int position, View v, ViewHolder vh) {

            TextView tv = vh.getTextView(R.id.list_item_text);
            HttpBeen httpBeen = (HttpBeen) list.get(position);
            tv.setText(httpBeen.getUrl());
            if (!TextUtils.isEmpty(httpBeen.getJson())) {
                tv.setTextColor(context.getResources().getColor(R.color.colorWhite));
            } else {
                tv.setTextColor(context.getResources().getColor(R.color.colorAccent));
            }
        }
    }


}
