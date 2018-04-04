/*
* Copyright (C) 2015 Square, Inc, 2017 Jeff Gilfelt.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package lib.http;

import android.content.Context;
import android.util.Log;

import java.io.EOFException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import httploglib.lib.R;
import lib.data.HttpTransaction;
import lib.support.RetentionManager;
import lib.util.JsonFormatUtil;
import lib.util.TestLibUtil;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;

/**
 * An OkHttp Interceptor which persists and displays HTTP activity in your application for later inspection.
 */
public final class ChuckInterceptor implements Interceptor {

    private String requestStrLast;
    private String responseStrLast;

    public enum Period {
        /**
         * Retain data for the last hour.
         */
        ONE_HOUR,
        /**
         * Retain data for the last day.
         */
        ONE_DAY,
        /**
         * Retain data for the last week.
         */
        ONE_WEEK,
        /**
         * Retain data forever.
         */
        FOREVER
    }

    private static final String LOG_TAG = "ChuckInterceptor";
    private static final Period DEFAULT_RETENTION = Period.ONE_WEEK;
    private static final Charset UTF8 = Charset.forName("UTF-8");

    private Context context;
    private RetentionManager retentionManager;
    private long maxContentLength = 250000L;

    /**
     * @param context The current Context.
     */
    public ChuckInterceptor(Context context) {
        this.context = context.getApplicationContext();
        retentionManager = new RetentionManager(this.context, DEFAULT_RETENTION);
    }


    /**
     * Set the maximum length for request and response content before it is truncated.
     * Warning: setting this value too high may cause unexpected results.
     *
     * @param max the maximum length (in bytes) for request/response content.
     * @return The {@link ChuckInterceptor} instance.
     */
    public ChuckInterceptor maxContentLength(long max) {
        this.maxContentLength = max;
        return this;
    }

    /**
     * Set the retention period for HTTP transaction data captured by this interceptor.
     * The default is one week.
     *
     * @param period the peroid for which to retain HTTP transaction data.
     * @return The {@link ChuckInterceptor} instance.
     */
    public ChuckInterceptor retainDataFor(Period period) {
        retentionManager = new RetentionManager(context, period);
        return this;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        HttpTransaction transaction = new HttpTransaction();
        HttpTransaction transactionView = new HttpTransaction();
        transaction.setRequestDate(new Date());//时间

        transaction.setMethod(request.method());//请求方法
        transaction.setUrl(request.url().toString());//请求url

        transaction.setRequestHeaders(request.headers());//请求头
        if (hasRequestBody) {
            if (requestBody.contentType() != null) {
                transaction.setRequestContentType(requestBody.contentType().toString());
            }
            if (requestBody.contentLength() != -1) {
                transaction.setRequestContentLength(requestBody.contentLength());
            }
        }

        transaction.setRequestBodyIsPlainText(!bodyHasUnsupportedEncoding(request.headers()));//请求体是否明文
        if (hasRequestBody && transaction.requestBodyIsPlainText()) {
            BufferedSource source = getNativeSource(new Buffer(), bodyGzipped(request.headers()));
            Buffer buffer = source.buffer();
            requestBody.writeTo(buffer);
            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            if (isPlaintext(buffer)) {
                if (!contentType.toString().contains("multipart/form-data")) {
                    try {
                        String s = readFromBuffer(buffer, charset);
                        String requestStr = decode(s);
                        String string = converStr(requestStr);
                        requestStrLast = JsonFormatUtil.formatJson(string);
                    } catch (Exception e) {
                        requestStrLast = readFromBuffer(buffer, charset);
                    }
                } else {
                    requestStrLast = "";
                }

                transaction.setRequestBody(readFromBuffer(buffer, charset));
            } else {
                transaction.setResponseBodyIsPlainText(false);
            }
        }

//        Uri transactionUri = create(transaction);

        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            transaction.setError(e.toString());
            try {
                transactionView = transaction;
                transactionView.setRequestBody(requestStrLast);
                TestLibUtil.getInstance().sendmessage(transactionView);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
//            update(transaction, transactionUri);
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();

        transaction.setRequestHeaders(response.request().headers()); // includes headers added later in the chain
        transaction.setResponseDate(new Date());
        transaction.setTookMs(tookMs);
        transaction.setProtocol(response.protocol().toString());
        transaction.setResponseCode(response.code());
        transaction.setResponseMessage(response.message());

        transaction.setResponseContentLength(responseBody.contentLength());
        if (responseBody.contentType() != null) {
            transaction.setResponseContentType(responseBody.contentType().toString());
        }
        transaction.setResponseHeaders(response.headers());

        transaction.setResponseBodyIsPlainText(!bodyHasUnsupportedEncoding(response.headers()));
        if (HttpHeaders.hasBody(response) && transaction.responseBodyIsPlainText()) {
            BufferedSource source = getNativeSource(response);
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    //update(transaction, transactionUri);
                    try {
                        transactionView = transaction;
                        transactionView.setRequestBody(requestStrLast);
                        TestLibUtil.getInstance().sendmessage(transactionView);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    return response;
                }
            }
            if (isPlaintext(buffer)) {
                try {

                    responseStrLast = JsonFormatUtil.formatJson(readFromBuffer(buffer.clone(), charset));
                } catch (Exception e) {
                    responseStrLast = readFromBuffer(buffer.clone(), charset);
                }
                transaction.setResponseBody(readFromBuffer(buffer.clone(), charset));
            } else {
                transaction.setResponseBodyIsPlainText(false);
            }
            transaction.setResponseContentLength(buffer.size());
        }
        //update(transaction, transactionUri);

        try {
            transactionView = transaction;
            transactionView.setRequestBody(requestStrLast);
            transactionView.setResponseBody(responseStrLast);
            TestLibUtil.getInstance().sendmessage(transactionView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

//    private Uri create(HttpTransaction transaction) {
//        ContentValues values = LocalCupboard.getInstance().withEntity(HttpTransaction.class).toContentValues(transaction);
//        Uri uri = context.getContentResolver().insert(ChuckContentProvider.TRANSACTION_URI, values);
//        transaction.setId(Long.valueOf(uri.getLastPathSegment()));
//        if (showNotification) {
//            notificationHelper.show(transaction);
//        }
//        retentionManager.doMaintenance();
//        return uri;
//    }
//
//    private int update(HttpTransaction transaction, Uri uri) {
//        ContentValues values = LocalCupboard.getInstance().withEntity(HttpTransaction.class).toContentValues(transaction);
//        int updated = context.getContentResolver().update(uri, values, null, null);
//        if (showNotification && updated > 0) {
//            notificationHelper.show(transaction);
//        }
//        return updated;
//    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyHasUnsupportedEncoding(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null &&
                !contentEncoding.equalsIgnoreCase("identity") &&
                !contentEncoding.equalsIgnoreCase("gzip");
    }

    private boolean bodyGzipped(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return "gzip".equalsIgnoreCase(contentEncoding);
    }

    private String readFromBuffer(Buffer buffer, Charset charset) {
        long bufferSize = buffer.size();
        long maxBytes = Math.min(bufferSize, maxContentLength);
        String body = "";
        try {
            body = buffer.readString(maxBytes, charset);
        } catch (EOFException e) {
            body += context.getString(R.string.chuck_body_unexpected_eof);
        }
        if (bufferSize > maxContentLength) {
            body += context.getString(R.string.chuck_body_content_truncated);
        }
        return body;
    }

    private BufferedSource getNativeSource(BufferedSource input, boolean isGzipped) {
        if (isGzipped) {
            GzipSource source = new GzipSource(input);
            return Okio.buffer(source);
        } else {
            return input;
        }
    }


    private BufferedSource getNativeSource(Response response) throws IOException {
        if (bodyGzipped(response.headers())) {
            BufferedSource source = response.peekBody(maxContentLength).source();
            if (source.buffer().size() < maxContentLength) {
                return getNativeSource(source, true);
            } else {
                Log.w(LOG_TAG, "gzip encoded response was too long");
            }
        }
        return response.body().source();
    }


    public String converStr(String str) {
        if (null == str || "".equals(str)) {
            return "";
        }
        StringBuilder newStr = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            String substring = str.substring(i, i + 1);
            if ("&".equals(substring)) {
                newStr.append("\n");
            }
            newStr.append(str.charAt(i));
        }
        return newStr.toString();
    }

    public String decode(String url)

    {

        try {

            String prevURL = "";

            String decodeURL = url;

            while (!prevURL.equals(decodeURL))

            {

                prevURL = decodeURL;

                decodeURL = URLDecoder.decode(decodeURL, "UTF-8");

            }

            return decodeURL;

        } catch (UnsupportedEncodingException e) {

            return "Issue while decoding" + e.getMessage();

        }

    }
}
