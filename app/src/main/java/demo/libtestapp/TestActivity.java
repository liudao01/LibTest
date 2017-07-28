package demo.libtestapp;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * @author liuml
 * @explain
 * @time 2017/7/28 11:26
 */

public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
    }
}
