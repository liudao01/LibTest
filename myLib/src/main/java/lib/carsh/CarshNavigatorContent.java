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
package lib.carsh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;
import httploglib.lib.R;
import httploglib.lib.util.FileUtils;
import io.mattcarroll.hover.Navigator;
import io.mattcarroll.hover.NavigatorContent;
import lib.theming.HoverTheme;

/**
 * 错误carsh导航
 */
public class CarshNavigatorContent extends FrameLayout implements NavigatorContent, AdapterView.OnItemClickListener {

    private static final String TAG = "CarshNavigatorContent";
    private final EventBus mBus;
    private View mLogo;
    //    private HoverMotion mHoverMotion;
    public List<String> lists;
    private Button bt_clear, bt_close;
    private ListView listview;
    private LinearLayout carshResult;
    private LinearLayout carshResultList;
    private TextView tvCarsh;
    Context context;
    private CarshAdapter carshAdapter;


    public CarshNavigatorContent(@NonNull Context context, @NonNull EventBus bus) {
        super(context);
        mBus = bus;
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.carsh_result_list, this, true);
        mLogo = findViewById(R.id.imageview_logo);
        listview = (ListView) findViewById(R.id.listview);
        listview.setOnItemClickListener(this);
        //获得Serializable方式传过来的值
        lists = new ArrayList<>();

        selectCarsh();//获取carsh列表
        carshResult = (LinearLayout) findViewById(R.id.carsh_result);
        carshResultList = (LinearLayout) findViewById(R.id.carsh_result_list);
        tvCarsh = (TextView) findViewById(R.id.tv_carsh);

        carshAdapter = new CarshAdapter();
        listview.setAdapter(carshAdapter);


        bt_clear = (Button) findViewById(R.id.bt_clear);
        bt_close = (Button) findViewById(R.id.bt_close);
        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lists.clear();
                //myAdapter.notifyDataSetChanged();
                try {
                    FileUtils.deleteFolderFile(FileUtils.getCrashPath(), false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                carshAdapter.notifyDataSetChanged();
            }
        });


        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showResultList();

            }
        });
    }

    public void selectCarsh() {
        File dir = FileUtils.getCrashFile();
        File[] files = dir.listFiles();
        lists.clear();
        for (File mCurrentFile : files) {
            lists.add(mCurrentFile.getName());
        }
        Collections.reverse(lists);//倒序刚发的在最前面

    }

    private void showResult() {
        carshResult.setVisibility(View.VISIBLE);
        carshResultList.setVisibility(View.GONE);
    }

    private void showResultList() {
        carshResultList.setVisibility(View.VISIBLE);
        carshResult.setVisibility(View.GONE);

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
//        mHoverTitleTextView.setTextColor(newTheme.getAccentColor());
//        mGoalsTitleTextView.setTextColor(newTheme.getAccentColor());

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //Toast.makeText(context,"点击了",Toast.LENGTH_SHORT).show();
        showResult();
//        String read = read(context, FileUtils.getCrashPath() + "/" + lists.get(position));
        String read = readFileFromSDCard(FileUtils.getCrashPath() + "/" + lists.get(position));
        Log.d(TAG, "onItemClick: " + read);
        tvCarsh.setText(read);
    }

    /**
     * @param fileName
     * @return
     * @desc 读取文件内容
     */
    public String readFileFromSDCard(String fileName) {
        byte[] buffer = null;
        try {
            String filePaht = fileName;
            FileInputStream fin = new FileInputStream(filePaht);
            int length = fin.available();
            buffer = new byte[length];
            fin.read(buffer);
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String s = new String((buffer));
        return s;
    }

    /**
     * @param fileName
     * @return
     * @desc 读取文件内容
     */
    public static String read(Context context, String fileName) {

        try {
            FileInputStream fis = context.openFileInput(fileName);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = fis.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            byte[] data = bos.toByteArray();
            fis.close();
            bos.close();
            return new String(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    class CarshAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (lists != null) {
                return lists.size();
            } else {
                return 0;
            }

        }

        @Override
        public Object getItem(int i) {
            if (lists != null && lists.size() > 0) {
                return lists.get(i);
            }
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = View.inflate(context, R.layout.carsh_result_item, null);
            TextView tv = (TextView) view.findViewById(R.id.tv_carsh_item);
            if (lists != null && lists.size() > 0) {
                tv.setText(lists.get(i));
            }
            return view;
        }
    }
}
