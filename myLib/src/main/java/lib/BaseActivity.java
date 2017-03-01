package lib;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author liuml
 * @explain
 * @time 2017/2/27 18:18
 */

public class BaseActivity extends Activity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        /**
//         * 开始的时候 的权限 判断是否可以让悬浮窗 悬浮到所有应用的前面
//         */
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!Settings.canDrawOverlays(this)) {
//                Intent drawOverlaysSettingsIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                drawOverlaysSettingsIntent.setData(Uri.parse("package:" + getPackageName()));
//                startActivity(drawOverlaysSettingsIntent);
//            }
//        }
    }
}
