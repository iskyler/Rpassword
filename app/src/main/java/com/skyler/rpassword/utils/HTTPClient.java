package com.skyler.rpassword.utils;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

/**
 * Created by skyler on 2014/9/19.
 */
public class HTTPClient {
    private static final String BASE = "https://rpassword.sinaapp.com/";
    private static AsyncHttpClient client = new AsyncHttpClient();
    private final String TAG = "net";

    public static void post(Context context, String url, Header[] headers,
                            RequestParams params, AsyncHttpResponseHandler handler) {
        client.post(context, BASE + url, headers, params, RequestParams.APPLICATION_JSON, handler);
    }

    public static void post(String url, RequestParams params,
                            AsyncHttpResponseHandler handler) {
        client.post(BASE + url, params, handler);
    }

    public static void post(String url, RequestParams params,
                            JsonHttpResponseHandler handler) {
        client.post(BASE + url, params, handler);
    }
}
