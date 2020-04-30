package lib.ui.content;

import android.content.Context;
import androidx.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import httploglib.lib.R;
import io.mattcarroll.hover.Content;
import io.reactivex.functions.Consumer;
import lib.data.HttpTransaction;
import lib.listener.HoverChangeListener;
import lib.net.ListHttpAdapter;
import lib.util.RxBus;
import lib.util.TestLibUtil;
import lib.util.Utils;

/**
 * @author liuml
 * @explain
 * @time 2018/11/2 14:14
 */
public class NetHoverMenuScreen implements Content, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private static final String TAG = "HttpNavigatorContent";
    private final Context mContext;
    private final String mPageTitle;
    private final View mWholeScreen;


    private Button btClear;
    List<HttpTransaction> httpTransactionList;
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

    private HoverChangeListener hoverChangeListener;

//
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            List<HttpTransaction> httpTransactionList = (List<HttpTransaction>) msg.obj;
//            listHttpAdapter.setList(httpTransactionList);
//        }
//    };
    private View view;


    public NetHoverMenuScreen(@NonNull Context context, @NonNull String pageTitle) {
        mContext = context.getApplicationContext();
        mPageTitle = pageTitle;
        mWholeScreen = initView();
    }

    public NetHoverMenuScreen(@NonNull Context context, @NonNull String pageTitle, HoverChangeListener hoverChangeListener) {
        mContext = context.getApplicationContext();
        mPageTitle = pageTitle;
        mWholeScreen = initView();
        this.hoverChangeListener = hoverChangeListener;
    }

////    private View createScreenView() {
////        TextView wholeScreen = new TextView(mContext);
////        wholeScreen.setText("Screen: " + mPageTitle);
////        wholeScreen.setGravity(Gravity.CENTER);
////        return wholeScreen;
////    }    @NonNull


    private View initView() {
        view = LayoutInflater.from(mContext).inflate(R.layout.result_list, null);
        httpResult = (LinearLayout) view.findViewById(R.id.http_result);
        httpResultList = (LinearLayout) view.findViewById(R.id.http_result_list);
        btCopy = (Button) view.findViewById(R.id.bt_copy);
        btClear = (Button) view.findViewById(R.id.bt_clear);


        listview = (ListView) view.findViewById(R.id.listview);
        listview.setOnItemClickListener(this);
        listview.setOnItemLongClickListener(this);

        httpTransactionList = TestLibUtil.httpMoudleList;

        if (httpTransactionList != null && httpTransactionList.size() > 0) {
            Collections.reverse(httpTransactionList);//倒序刚发的在最前面
        }
        listHttpAdapter = new ListHttpAdapter(mContext, httpTransactionList);
        listview.setAdapter(listHttpAdapter);

        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestLibUtil.httpMoudleList.clear();
                httpTransactionList.clear();
                listHttpAdapter.setList(httpTransactionList);
            }
        });

        //拷贝返回的数据
        btCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpTransaction httpTransaction = httpTransactionList.get(clickPosition);
                Utils.copy(mContext, "返回数据: " + httpTransaction.getFormattedResponseBody());
            }
        });

        return view;
    }

    private void initResult() {
        //数据详情的控件

        url = (TextView) view.findViewById(R.id.url);
        method = (TextView) view.findViewById(R.id.method);
        protocol = (TextView) view.findViewById(R.id.protocol);
        status = (TextView) view.findViewById(R.id.status);
        response = (TextView) view.findViewById(R.id.response);
        ssl = (TextView) view.findViewById(R.id.ssl);
        requestTime = (TextView) view.findViewById(R.id.request_time);
        responseTime = (TextView) view.findViewById(R.id.response_time);
        duration = (TextView) view.findViewById(R.id.duration);
        requestSize = (TextView) view.findViewById(R.id.request_size);
        responseSize = (TextView) view.findViewById(R.id.response_size);
        totalSize = (TextView) view.findViewById(R.id.total_size);

        requestHeaders = (TextView) view.findViewById(R.id.request_headers);
        requestBody = (TextView) view.findViewById(R.id.request_body);
        responseHeaders = (TextView) view.findViewById(R.id.response_headers);
        reponseBody = (TextView) view.findViewById(R.id.reponse_body);

        tvUrl = (TextView) view.findViewById(R.id.tv_url);
        tvJsonSize = (TextView) view.findViewById(R.id.tv_json_size);
        tvJson = (TextView) view.findViewById(R.id.tv_json);
        tvJsonHeader = (TextView) view.findViewById(R.id.tv_json_header);
        btClose = (Button) view.findViewById(R.id.bt_close);


        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResultList();
            }
        });

        RxBus.getInstance().doSubscribe(this,List.class,new Consumer<List>(){

            @Override
            public void accept(List list) throws Exception {
                httpTransactionList = list;
                listHttpAdapter.setList(httpTransactionList);
//                hoverChangeListener.onChange();
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

    public static void setList() {
        if (TestLibUtil.httpMoudleList != null) {
            if (listHttpAdapter != null) {
                List<HttpTransaction> httpMoudleList = TestLibUtil.httpMoudleList;
                if (httpMoudleList != null && httpMoudleList.size() > 0) {
                    Collections.reverse(httpMoudleList);//倒序刚发的在最前面
                }
                RxBus.getInstance().post(httpMoudleList);
//                Message message = Message.obtain();
//                message.obj = httpMoudleList;
//                handler.sendMessage(message);
            }
        }
    }

    // Make sure that this method returns the SAME View.  It should NOT create a new View each time
    // that it is invoked.
    @NonNull
    @Override
    public View getView() {
        return mWholeScreen;
    }

    @Override
    public boolean isFullscreen() {
        return true;
    }

    @Override
    public void onShown() {
        // No-op.
        initResult();
    }

    @Override
    public void onHidden() {
        // No-op.
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
            textView.setText(mContext.getString(R.string.chuck_body_omitted));
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
        HttpTransaction httpTransaction = (HttpTransaction) TestLibUtil.httpMoudleList.get(position);
        Utils.copy(mContext, "请求方式: " + httpTransaction.getMethod() + "\n" + "请求地址: " +
                httpTransaction.getUrl() + "\n" + "请求参数: " + httpTransaction.getRequestBody());
        return true;
    }

}

