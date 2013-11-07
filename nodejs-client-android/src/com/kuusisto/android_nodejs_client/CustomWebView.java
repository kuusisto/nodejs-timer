package com.kuusisto.android_nodejs_client;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CustomWebView extends WebView implements View.OnClickListener {

    private Activity mActivity;

    private EditText mTimerValueEditText;
    private Button mSetTimerButton;

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomWebView(Context context) {
        this(context, null);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        mActivity = (Activity) getContext();

        mTimerValueEditText = (EditText) getRootView().findViewById(R.id.timer_value);
        mSetTimerButton = (Button) getRootView().findViewById(R.id.set_timer_button);
        mSetTimerButton.setOnClickListener(this);

        init();

        loadUrl(Const.getServer());
    }

    private void init() {

        getSettings().setJavaScriptEnabled(true);

        setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                mActivity.setProgress(progress * 1000);
            }
        });
        setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(mActivity, "Oh no! " + description, Toast.LENGTH_SHORT).show();

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSetTimerButton.setEnabled(false);
                    }
                });
            }
        });
    }

    private String getRestTimerStr() {

        if (!TextUtils.isEmpty(mTimerValueEditText.getText().toString()) || mTimerValueEditText == null) {
            return Const.getServer() + "/timer/" + mTimerValueEditText.getText().toString();
        }
        else {
            return null;
        }
    }

    private void setTimerValue() {

        final String requestURL = getRestTimerStr();

        if (!TextUtils.isEmpty(requestURL)) {
            // TODO: create as a member and cancel previous requests as necessary..
            AsyncTask task = new DownloadWebpageTask();
            task.execute(requestURL);
        }
        else {
            Toast.makeText(mActivity, "Please set a value first", Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadWebpageTask extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... urlStr) {

            try {

                final String requestURL = urlStr[0].toString();

                Log.v("JK", "request URL = " + requestURL);

                if (!TextUtils.isEmpty(requestURL)) {
                    URL url = new URL(requestURL);

                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream in = urlConnection.getInputStream();

                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();
                    while (data != -1) {
                        char current = (char) data;
                        data = isw.read();
                        System.out.print(current);
                    }
                }
                else {
                    // TODO: handle error
                }
            }
            catch (Exception e) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mActivity, "request failed", Toast.LENGTH_SHORT).show();
                    }
                });

                e.printStackTrace();
            }

            return null;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mSetTimerButton) {
            setTimerValue();
        }
    }
}
