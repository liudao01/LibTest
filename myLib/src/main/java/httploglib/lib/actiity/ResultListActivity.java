package httploglib.lib.actiity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import httploglib.lib.R;
import httploglib.lib.been.HttpBeen;
import httploglib.lib.service.WindowService;
import lib.util.TestLibUtil;
import lib.util.Utils;

/**
 * @author liuml.
 * @explain 网络数据列表
 * @time 2016/12/13 21:50
 */
public class ResultListActivity extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    List<HttpBeen> beens;

    Button bt_clear, bt_close;
    Context context;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_list);
        context = this;

        ListView listview = (ListView) findViewById(R.id.listview);
        listview.setOnItemClickListener(this);
        listview.setOnItemLongClickListener(this);

        //获得Serializable方式传过来的值
        beens = WindowService.httpMoudleList;
        Collections.reverse(beens);//倒序刚发的在最前面
        myAdapter = new MyAdapter();
        listview.setAdapter(myAdapter);

        // Logger.d( beens.get(0).getJson());
        bt_clear = (Button) findViewById(R.id.bt_clear);
        bt_close = (Button) findViewById(R.id.bt_close);
        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestLibUtil.httpMoudleList.clear();
                beens.clear();
                myAdapter.notifyDataSetChanged();
            }
        });


        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Intent intent = new Intent();
        intent.setClass(this, ResultInfoActivity.class);
        intent.putExtra("javabean", i);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        HttpBeen httpBeen = WindowService.httpMoudleList.get(position);
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
            view = View.inflate(getApplicationContext(), R.layout.result_list_item, null);
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
