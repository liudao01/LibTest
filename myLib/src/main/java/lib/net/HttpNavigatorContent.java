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
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;
import httploglib.lib.R;
import io.mattcarroll.hover.Content;
import lib.data.HttpTransaction;
import lib.theming.HoverTheme;
import lib.util.TestLibUtil;
import lib.util.Utils;

/**
 * {@link NavigatorContent} that displays an introduction to Hover.
 */

/**
 * @author liuml.
 * @explain http 界面
 * @time 2017/12/22 20:37
 */
public class HttpNavigatorContent extends FrameLayout implements Content, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

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

    private int clickPosition = 0;
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
    private Button btCopy;


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
        btCopy = (Button) findViewById(R.id.bt_copy);
        btClear = (Button) findViewById(R.id.bt_clear);
//        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
//        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerview.setAdapter(new HttpAdapter(TestLibUtil.httpMoudleList));
        //添加分割线
//        recyclerview.addItemXDecoration(new DividerItemDecoration(  getActivity(), DividerItemDecoration.HORIZONTAL_LIST));
//        btClear.setOnClickListener(this);


        listview = (ListView) findViewById(R.id.listview);
        listview.setOnItemClickListener(this);
        listview.setOnItemLongClickListener(this);

        httpTransactionList = TestLibUtil.httpMoudleList;

//        if (beens != null && beens.size() > 0) {
//            Collections.reverse(beens);//倒序刚发的在最前面
//        }
//        if (httpTransactionList != null && httpTransactionList.size() > 0) {
//            Collections.reverse(httpTransactionList);//倒序刚发的在最前面
//        }
        listHttpAdapter = new ListHttpAdapter(context, httpTransactionList);
        // myAdapter = new MyAdapter(context, beens);
//        listview.setAdapter(myAdapter);
        listview.setAdapter(listHttpAdapter);

        // Logger.d( beens.get(0).getJson());
        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestLibUtil.httpMoudleList.clear();
                httpTransactionList.clear();
//              myAdapter.setList(beens);
                listHttpAdapter.setList(httpTransactionList);
            }
        });

        //拷贝返回的数据
        btCopy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpTransaction httpTransaction = httpTransactionList.get(clickPosition);
                Utils.copy(context, "返回数据: " + httpTransaction.getFormattedResponseBody());
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
    public boolean isFullscreen() {
        return false;
    }

    @Override
    public void onShown() {
        Log.d(TAG, "onShown: ");

    }


    public static void setList() {

        if (TestLibUtil.httpMoudleList != null) {
            if (listHttpAdapter != null) {
                List<HttpTransaction> httpMoudleList = TestLibUtil.httpMoudleList;
                Message message = Message.obtain();
                message.obj = httpMoudleList;
                handler.sendMessage(message);

            }
        }
    }

    @Override
    public void onHidden() {
        Log.d(TAG, "onHidden: ");
    }

    public void onEventMainThread(@NonNull HoverTheme newTheme) {
        Log.d(TAG, "onEventMainThread: ");
        //点击导航按钮的时候调用这个

        if (TestLibUtil.httpMoudleList != null) {
            if (listHttpAdapter != null) {

                List<HttpTransaction> httpMoudleList = new ArrayList<>();
                httpMoudleList.addAll(TestLibUtil.httpMoudleList);
                if (httpMoudleList != null && httpMoudleList.size() > 0) {
                    Collections.reverse(httpMoudleList);//倒序刚发的在最前面
                }

                Message message = Message.obtain();
                message.obj = httpMoudleList;
                handler.sendMessage(message);

            }
        }
    }

    private List<HttpTransaction> getHttpTransactionList() {
        List<HttpTransaction> httpMoudleList = new ArrayList<>();
        httpMoudleList.addAll(TestLibUtil.httpMoudleList);
        if (httpMoudleList != null && httpMoudleList.size() > 0) {
            Collections.reverse(httpMoudleList);//倒序刚发的在最前面
        }
        return httpMoudleList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(context, "点击了"+position, Toast.LENGTH_SHORT).show();
        showResult();
        clickPosition = position;
        //setData();
        setHttpInfoData(getHttpTransactionList().get(position));
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


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        HttpTransaction httpTransaction = getHttpTransactionList().get(position);
        Utils.copy(context, "请求方式: " + httpTransaction.getMethod() + "\n" + "请求地址: " +
                httpTransaction.getUrl() + "\n" + "请求参数: " + httpTransaction.getRequestBody());
        return true;
    }


}
