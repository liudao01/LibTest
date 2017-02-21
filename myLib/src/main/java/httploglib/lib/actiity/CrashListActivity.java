package httploglib.lib.actiity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import httploglib.lib.R;
import httploglib.lib.util.FileUtils;


/**
 * @author liuml.
 * @explain crash 列表页面
 * @time 2016/12/7 16:45
 */
public class CrashListActivity extends Activity implements AdapterView.OnItemClickListener {
    List<String> lists;

    Button bt_clear, bt_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_list);

        ListView  listview = (ListView) findViewById(R.id.listview);
        listview.setOnItemClickListener(this);
        //获得Serializable方式传过来的值
        lists = new ArrayList<>();

        File dir = FileUtils.getCrashFile();
        File[] files = dir.listFiles();
        for(File mCurrentFile:files){
            lists.add(mCurrentFile.getName());
        }
        Collections.reverse(lists);//倒序刚发的在最前面
        final MyAdapter myAdapter = new MyAdapter();
        listview.setAdapter(myAdapter);
        // Logger.d( beens.get(0).getJson());
        bt_clear = (Button) findViewById(R.id.bt_clear);
        bt_close = (Button) findViewById(R.id.bt_close);
        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lists.clear();
                myAdapter.notifyDataSetChanged();
                try {
                    FileUtils.deleteFolderFile(FileUtils.getCrashPath(),false);
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
        File file = new File(FileUtils.getCrashPath()+"/"+lists.get(i));
        FileUtils.openFile(file,getApplicationContext());
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (lists != null) return lists.size();
            else return 0;

        }

        @Override
        public Object getItem(int i) {
            return lists.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = View.inflate(getApplicationContext(), R.layout.result_list_item, null);
            TextView tv = (TextView) view.findViewById(R.id.list_item_text);
            tv.setText(lists.get(i));
            return view;
        }
    }
}
