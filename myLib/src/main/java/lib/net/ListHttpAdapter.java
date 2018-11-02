package lib.net;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import httploglib.lib.R;
import lib.adapter.AutoAdapter;
import lib.adapter.ViewHolder;
import lib.data.HttpTransaction;
import lib.util.TestLibUtil;
import lib.util.Utils;

/**
 * @author liuml
 * @explain
 * @time 2017/3/28 11:11
 */

public class ListHttpAdapter extends AutoAdapter {
    HttpTransaction transaction;
    Context context;
    int colorDefault;
    int colorRequested;
    int colorError;
    int color500;
    int color400;
    int color300;


    public ListHttpAdapter(Context context, List<?> list) {
        super(context, list);
        this.context = context;
        final Resources res = context.getResources();
        colorDefault = res.getColor(R.color.colorWhite);
        colorRequested = res.getColor(R.color.chuck_status_requested);
        colorError = res.getColor(R.color.chuck_status_error);
        color500 = res.getColor(R.color.chuck_status_500);
        color400 = res.getColor(R.color.chuck_status_400);
        color300 = res.getColor(R.color.chuck_status_300);
    }

    @Override
    public int getLayoutID(int position) {
        return R.layout.chuck_list_item_transaction;
    }

    @Override
    public void baseGetView(final int position, View v, ViewHolder vh) {
        if (list == null || list.size() <= 0) {
            return;
        }
        HttpTransaction transaction = (HttpTransaction) list.get(position);
//        Logger.d(" 每个item 的值 = "+transaction.toString());
        TextView code;
        TextView path;
        TextView host;
        TextView start;
        TextView duration;
        TextView size;
        ImageView ssl;
        code = (TextView) vh.getTextView(R.id.code);
        path = (TextView) vh.getTextView(R.id.path);
        host = (TextView) vh.getTextView(R.id.host);
        start = (TextView) vh.getTextView(R.id.start);
        duration = (TextView) vh.getTextView(R.id.duration);
        size = (TextView) vh.getTextView(R.id.size);
        ssl = (ImageView) vh.getImageView(R.id.ssl);
        Button mBtCopyData;

        mBtCopyData =vh.getButton(R.id.bt_copy_data);
        path.setText(transaction.getMethod() + " " + transaction.getPath());
        host.setText(transaction.getHost());
        start.setText(transaction.getRequestStartTimeString());
        ssl.setVisibility(transaction.isSsl() ? View.VISIBLE : View.GONE);
        if (transaction.getStatus() == HttpTransaction.Status.Complete) {
            code.setText(String.valueOf(transaction.getResponseCode()));
            duration.setText(transaction.getDurationString());
            size.setText(transaction.getTotalSizeString());
        } else {
            code.setText(null);
            duration.setText(null);
            size.setText(null);
        }
        if (transaction.getStatus() == HttpTransaction.Status.Failed) {
            code.setText("!!!");
        }
        mBtCopyData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpTransaction httpTransaction = (HttpTransaction) TestLibUtil.httpMoudleList.get(position);
                Utils.copy(context, "请求方式: " + httpTransaction.getMethod() + "\n" + "请求地址: " +
                        httpTransaction.getUrl() + "\n" + "请求参数: " + httpTransaction.getRequestBody()
                        + "\n请求结果:" + httpTransaction.getFormattedResponseBody());
            }
        });
        setStatusColor(vh, transaction);
    }

    private void setStatusColor(ViewHolder holder, HttpTransaction transaction) {
        int color;
        if (transaction.getStatus() == HttpTransaction.Status.Failed) {
            color = colorError;
        } else if (transaction.getStatus() == HttpTransaction.Status.Requested) {
            color = colorRequested;
        } else if (transaction.getResponseCode() >= 500) {
            color = color500;
        } else if (transaction.getResponseCode() >= 400) {
            color = color400;
        } else if (transaction.getResponseCode() >= 300) {
            color = color300;
        } else {
            color = colorDefault;
        }
        holder.getTextView(R.id.code).setTextColor(color);
        holder.getTextView(R.id.path).setTextColor(color);
    }
}
