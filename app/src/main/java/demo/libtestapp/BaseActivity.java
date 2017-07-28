package demo.libtestapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * @author liuml
 * @explain
 * @time 2017/7/28 11:21
 */

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApp.getRefWatcher(this);
    }
}
