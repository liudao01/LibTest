package lib.http;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author liuml
 * @explain
 * @time 2018/5/8 20:45
 */

public final class ChuckInterceptor implements Interceptor {

    public ChuckInterceptor(Context context) {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return null;
    }
}
