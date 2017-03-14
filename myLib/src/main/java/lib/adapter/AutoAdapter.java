package lib.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class AutoAdapter extends MBaseAdapter {

	public AutoAdapter(Context context, List<?> list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	public abstract int getLayoutID(int position);

	@Override
	public View baseGetView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater
					.inflate(getLayoutID(position), parent, false);
		}
		baseGetView(position, convertView,
				ViewHolder.getViewHolder(convertView));
		return convertView;
	}

	public abstract void baseGetView(int position, View v, ViewHolder vh);

}