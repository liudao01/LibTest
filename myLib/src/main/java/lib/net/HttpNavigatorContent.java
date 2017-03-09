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
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import httploglib.lib.service.WindowService;
import httploglib.lib.util.TestLibUtil;
import httploglib.lib.util.Utils;
import io.mattcarroll.hover.Navigator;
import io.mattcarroll.hover.NavigatorContent;
import lib.theming.HoverTheme;

/**
 * {@link NavigatorContent} that displays an introduction to Hover.
 */
public class HttpNavigatorContent extends FrameLayout implements NavigatorContent, View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private final EventBus mBus;
    private View mLogo;
    //    private HoverMotion mHoverMotion;
    private Button btClear;
    private RecyclerView recyclerview;
    List<HttpBeen> beens;
    Button bt_clear;
    Context context;
    MyAdapter myAdapter;
    private LinearLayout httpResult;
    private LinearLayout httpResultList;

    private TextView tvUrl;
    private TextView tvJsonSize;
    private TextView tvJson;
    private Button btClose;
    private TextView tvJsonHeader;

    private int clickPosition;


    public HttpNavigatorContent(@NonNull Context context, @NonNull EventBus bus) {
        super(context);
        mBus = bus;
        this.context = context;
        init();
        initResult();
    }

    private void initResult() {
        tvUrl = (TextView) findViewById(R.id.tv_url);
        tvJsonSize = (TextView) findViewById(R.id.tv_json_size);
        tvJson = (TextView) findViewById(R.id.tv_json);
        tvJsonHeader = (TextView) findViewById(R.id.tv_json_header);
        btClose = (Button) findViewById(R.id.bt_close);


        HttpBeen httpBeen = TestLibUtil.httpMoudleList.get(clickPosition);
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
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResultList();
            }
        });
    }


    private  void showResult(){
        httpResult.setVisibility(View.VISIBLE);
        httpResultList.setVisibility(View.GONE);
    }
    private void showResultList(){
        httpResultList.setVisibility(View.VISIBLE);
        httpResult.setVisibility(View.GONE);

    }
    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.result_list, this, true);
        mLogo = findViewById(R.id.imageview_logo);
//        mHoverMotion = new HoverMotion();
        httpResult = (LinearLayout) findViewById(R.id.http_result);
        httpResultList = (LinearLayout) findViewById(R.id.http_result_list);

        btClear = (Button) findViewById(R.id.bt_clear);
//        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
//        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerview.setAdapter(new HttpAdapter(TestLibUtil.httpMoudleList));
        //添加分割线
//        recyclerview.addItemDecoration(new DividerItemDecoration(  getActivity(), DividerItemDecoration.HORIZONTAL_LIST));
        btClear.setOnClickListener(this);


        ListView listview = (ListView) findViewById(R.id.listview);
        listview.setOnItemClickListener(this);
        listview.setOnItemLongClickListener(this);

        //获得Serializable方式传过来的值
        beens = TestLibUtil.httpMoudleList;
        if (beens != null && beens.size() > 0) {
            Collections.reverse(beens);//倒序刚发的在最前面
            myAdapter = new MyAdapter();
            listview.setAdapter(myAdapter);

        }

        // Logger.d( beens.get(0).getJson());
        bt_clear = (Button) findViewById(R.id.bt_clear);
        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WindowService.clearList();
                beens.clear();
                myAdapter.notifyDataSetChanged();
            }
        });


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
//        mHoverMotion.start(mLogo);
    }

    @Override
    public void onHidden() {
//        mHoverMotion.stop();
    }

    public void onEventMainThread(@NonNull HoverTheme newTheme) {
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        clickPosition = position;
        showResult();
        //context.startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        HttpBeen httpBeen = TestLibUtil.httpMoudleList.get(position);
        Utils.copy(context, httpBeen.getUrl());
        return true;
    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (beens != null) return beens.size();
            else return 0;

        }

        @Override
        public Object getItem(int i) {
            return beens.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = View.inflate(context, R.layout.result_list_item, null);
            TextView tv = (TextView) view.findViewById(R.id.list_item_text);
            HttpBeen httpBeen = beens.get(i);
            tv.setText(httpBeen.getUrl());
            if (!TextUtils.isEmpty(httpBeen.getJson())) {
                tv.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            } else {
                tv.setTextColor(context.getResources().getColor(R.color.colorAccent));
            }
            return view;
        }
    }


}
