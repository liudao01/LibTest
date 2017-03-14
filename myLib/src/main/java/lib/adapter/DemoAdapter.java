package lib.adapter;

import android.content.Context;
import android.view.View;

import java.util.List;

import httploglib.lib.R;

/**
 * 测试adpater
 * @author liuml
 *
 */
public class DemoAdapter extends AutoAdapter {

	public DemoAdapter(Context context, List<?> list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getLayoutID(int position) {
		// TODO Auto-generated method stub
		return R.layout.carsh_result_item;
	}

	@Override
	public void baseGetView(int position, View v, ViewHolder vh) {

		String item = (String) list.get(position);

		//vh.getTextView(R.id.textView1).setText(item);

	}

}
