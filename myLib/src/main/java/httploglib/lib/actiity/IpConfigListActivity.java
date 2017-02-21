package httploglib.lib.actiity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.List;

import httploglib.lib.R;
import httploglib.lib.been.IpConfigBeen;
import httploglib.lib.util.CommonDialog;
import httploglib.lib.util.TestLibUtil;


/**
 * @author liuml.
 * @explain 切换ip页面
 * @time 2016/12/7 16:45
 */
public class IpConfigListActivity extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    Context mContext;
    List<IpConfigBeen> dataList;
    Button bt_close;
    MyAdapter myAdapter;
    ListView listview;

    private Button btClearAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.result_ip_list);
        btClearAll = (Button) findViewById(R.id.bt_clear_all);
        listview = (ListView) findViewById(R.id.listview_ip);
        listview.setOnItemClickListener(this);

        btClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDelAllDialog();
            }
        });


        //Collections.reverse(dataList);//倒序刚发的在最前面
        myAdapter = new MyAdapter();
        listview.setAdapter(myAdapter);
        listview.setOnItemLongClickListener(this);
        bt_close = (Button) findViewById(R.id.bt_close);


        dataList = TestLibUtil.getInstance().getIpList();
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        SwitchIPDialog(i);

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
        Logger.d("打印 " + dataList.toArray().toString());

        myAdapter.notifyDataSetChanged();
        //保存
        TestLibUtil.getInstance().setDataList(dataList);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        showMyDialog(i);
        return true;
    }

    /**
     * 是否删除ip dialog   暂时没作用了 因为做了判断 假如有ip数据就不会更新  只有删除全部才会更新数据
     *
     * @param position
     */
    private void showMyDialog(final int position) {
        final CommonDialog confirmDialog = new CommonDialog(this,"是否删除ip");
        confirmDialog.show();
        confirmDialog.setClicklistener(new CommonDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                TestLibUtil.getInstance().DelIp(position);
                dataList.remove(position);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void doCancle() {
            }
        });
    }

    /**
     * 切换ip 提示dialog
     *
     * @param position
     */
    private void SwitchIPDialog(final int position) {
        final CommonDialog confirmDialog = new CommonDialog(this,"是否切换ip,切换ip将会关闭程序");
        confirmDialog.show();
        confirmDialog.setClicklistener(new CommonDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                //选择一个选中的
                setListSelect(position);
                System.exit(0);
            }

            @Override
            public void doCancle() {
            }
        });
    }

    /**
     * 清除所有ip 提示dialog
     *
     */
    private void showDelAllDialog() {
        final CommonDialog confirmDialog = new CommonDialog(this,"是否清除所有ip,清除将会关闭程序");
        confirmDialog.show();
        confirmDialog.setClicklistener(new CommonDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                TestLibUtil.getInstance().DelIpAll();
                System.exit(0);
            }

            @Override
            public void doCancle() {
            }
        });
    }

    class MyAdapter extends BaseAdapter {

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
            view = View.inflate(getApplicationContext(), R.layout.result_ip_list_item, null);
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
