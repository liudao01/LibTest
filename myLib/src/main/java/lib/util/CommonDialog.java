package lib.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import httploglib.lib.R;


/**
 * @author liuml
 * @explain
 * @time 2016/12/8 16:05
 */

public class CommonDialog extends Dialog {
    private Context context;
    private String titleStr;
    private ClickListenerInterface clickListenerInterface;

    public CommonDialog(Context context) {
        super(context, R.style.MyDialogStyle);
        this.context = context;
    }
    public CommonDialog(Context context,String title) {
        super(context, R.style.MyDialogStyle);
        this.context = context;
        this.titleStr = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_common, null);
        setContentView(view);
        TextView tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(titleStr);
        tv_ok.setOnClickListener(new clickListener());
        tv_cancel.setOnClickListener(new clickListener());
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }

    public interface ClickListenerInterface {
        public void doConfirm();
        public void doCancle();
    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.tv_ok) {
                clickListenerInterface.doConfirm();
                dismiss();

            } else if (id == R.id.tv_cancel) {
                clickListenerInterface.doCancle();
                dismiss();

            }
        }
    }

    ;
}
