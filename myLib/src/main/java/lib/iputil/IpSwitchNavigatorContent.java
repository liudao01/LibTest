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
package lib.iputil;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.List;

import de.greenrobot.event.EventBus;
import httploglib.lib.R;
import httploglib.lib.been.IpConfigBeen;
import io.mattcarroll.hover.Content;
import lib.theming.HoverTheme;
import lib.util.TestLibUtil;

/**
 * ip 选择 导航
 * {@link NavigatorContent} that displays an introduction to Hover.
 */
public class IpSwitchNavigatorContent extends FrameLayout implements Content, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    private final EventBus mBus;
    private View mLogo;
    private Context mContext;
    private List<IpConfigBeen> dataList;
    private IpSwitchAdapter ipSwitchAdapter;
    private ListView listview;

    private Button btClearAll;
//    private HoverMotion mHoverMotion;

    public IpSwitchNavigatorContent(@NonNull Context context, @NonNull EventBus bus) {
        super(context);
        mContext = context;
        mBus = bus;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.result_ip_list, this, true);

//        mHoverMotion = new HoverMotion();
        btClearAll = (Button) findViewById(R.id.bt_clear_all);
        listview = (ListView) findViewById(R.id.listview_ip);
        listview.setOnItemClickListener(this);

        btClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestLibUtil.getInstance().DelIpAll();
                System.exit(0);
            }
        });


        ipSwitchAdapter = new IpSwitchAdapter();
        listview.setAdapter(ipSwitchAdapter);
        listview.setOnItemLongClickListener(this);


        dataList = TestLibUtil.getInstance().getIpList();
    }

    private void setListSelect(int position) {

        for (int i = 0; i < dataList.size(); i++) {
            IpConfigBeen ipConfigBeen = dataList.get(i);
            if (i == position) {
                ipConfigBeen.setSelect(true);
            } else {
                ipConfigBeen.setSelect(false);
            }
            dataList.set(i, ipConfigBeen);
        }
        for (IpConfigBeen ipConfigBeen : dataList) {

            Logger.d("打印 " + ipConfigBeen.toString());

        }

        ipSwitchAdapter.notifyDataSetChanged();
        //保存
        TestLibUtil.getInstance().setDataList(dataList);
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

    }


    @Override
    public void onHidden() {
//        mHoverMotion.stop();
    }

    public void onEventMainThread(@NonNull HoverTheme newTheme) {
        //切换界面时候调用的
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //选择一个选中的
        setListSelect(position);
        System.exit(0);
    }

    class IpSwitchAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (dataList != null) return dataList.size();
            else return 0;

        }

        @Override
        public Object getItem(int i) {
            return dataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            IpConfigBeen ipConfigBeen = dataList.get(i);
            view = View.inflate(mContext, R.layout.result_ip_list_item, null);
            TextView list_item_url_text = (TextView) view.findViewById(R.id.list_item_url_text);
            TextView list_item_dec = (TextView) view.findViewById(R.id.list_item_dec);
            RadioButton radioButton = (RadioButton) view.findViewById(R.id.radioButton);
            list_item_url_text.setText(ipConfigBeen.getUrl());
            list_item_dec.setText(ipConfigBeen.getUrlName());
            if (ipConfigBeen.isSelect()) {
                radioButton.setVisibility(View.VISIBLE);
            } else {
                radioButton.setVisibility(View.GONE);
            }
            return view;
        }
    }
}
