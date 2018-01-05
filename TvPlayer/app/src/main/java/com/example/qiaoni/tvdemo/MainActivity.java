package com.example.qiaoni.tvdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
    // 全屏视频布局对象
    private FrameLayout videoview;
    // 测试全部按钮
    // webview
    private WebView videowebview = null;
    // 自定义视频view
    private View xCustomView;
    // google webview client
    private xWebChromeClient xwebchromeclient;
    // 频道id
    private String channelId = null;
    // 测试地址
    // private String baseUrl = "http://10.63.0.149:8000/mobile/channel/";
    // 回调函数
    private WebChromeClient.CustomViewCallback xCustomViewCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        setContentView(R.layout.activity_main);
        // 初始控件
        initwidget();
    }

    /**
     * 控件初始化
     */
    private void initwidget() {
        videoview = (FrameLayout) findViewById(R.id.video_view);
        videowebview = (WebView) findViewById(R.id.video_webview);
        WebSettings ws = videowebview.getSettings();
        ws.setBuiltInZoomControls(true);
        ws.setDefaultTextEncodingName("UTF-8");
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        ws.setUseWideViewPort(false);
        ws.setLoadWithOverviewMode(true);
        ws.setSaveFormData(true);
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
        xwebchromeclient = new xWebChromeClient();
        videowebview.setWebChromeClient(xwebchromeclient);
        videowebview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:(function() { var videos = document.getElementsByTagName('video'); for(var i=0;i<videos.length;i++){videos[i].play();}})()");
            }
        });
        videowebview.addJavascriptInterface(new HtmlObj(), "android");
        /**
         * 在html中直接android.outOfFullScreen(xxxxxx)
         */
    }
    @Override
    public void onBackPressed() {
        if (inCustomView()) {
            hideCustomView();
        } else {
            //videowebview.loadUrl("about:blank");
            finish();
            super.onBackPressed();
        }
    }
    public void  goSettings(){
        Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
        MainActivity.this.startActivity(intent);
    }
    public void loadWebView(String url){
        videowebview.loadUrl(url);
    }
    @Override
    protected void onResume() {
        super.onResume();
        channelId = FileIOHelper.getString("channelId");
        if("" != channelId){
            loadWebView(baseUrl + channelId);
        }else{
            goSettings();
        }
    }
    public boolean inCustomView() {
        return (xCustomView != null);
    }
    public void hideCustomView() {
        xwebchromeclient.onHideCustomView();
    }
    public class xWebChromeClient extends WebChromeClient {
        private Bitmap xdefaltvideo = null;
        private View xprogressvideo = null;
        @Override
        public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            videowebview.setVisibility(View.GONE);
            if (xCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            videoview.addView(view);
            xCustomView = view;
            xCustomViewCallback = callback;
            videoview.setVisibility(View.VISIBLE);
        }
        @Override
        public void onHideCustomView() {
            if (xCustomView == null)
                return;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            xCustomView.setVisibility(View.GONE);
            videoview.removeView(xCustomView);
            xCustomView = null;
            videoview.setVisibility(View.GONE);
            xCustomViewCallback.onCustomViewHidden();
            videowebview.setVisibility(View.VISIBLE);
        }
        @Override
        public Bitmap getDefaultVideoPoster() {
            if (xdefaltvideo == null) {
                xdefaltvideo = BitmapFactory.decodeResource(
                        getResources(), R.mipmap.ic_launcher);
            }
            return xdefaltvideo;
        }
        @Override
        public View getVideoLoadingProgressView() {
            if (xprogressvideo == null) {
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                xprogressvideo = inflater.inflate(R.layout.video_loading_progress, null);
            }
            return xprogressvideo;
        }
        @Override
        public void onReceivedTitle(WebView view, String title) {
            (MainActivity.this).setTitle(title);
        }
    }
    /**
     * 接收当前Activity跳转后，目标Activity关闭后的回传值
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode){
            case RESULT_OK:{//接收并显示Activity传过来的值
                Bundle bundle = data.getExtras();
                channelId = bundle.getString("channelId");
                if(null != channelId) {
                    loadWebView(baseUrl + channelId);
                }
                break;
            }
            default:
                break;
        }
    }
    public class xWebViewClientent extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            (MainActivity.this).setTitle(view.getTitle());
            super.onPageFinished(view, url);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //islandport = newConfig.orientation == Configuration.ORIENTATION_PORTRAIT;
    }
    public class HtmlObj extends Object {
        @JavascriptInterface
        public void outOfFullScreen(final String nextVideoUrl) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onBackPressed();
                }
            });
        }
        @JavascriptInterface
        public void  toast(){
            Toast.makeText(getApplicationContext(),"exec java action",Toast.LENGTH_LONG).show();
        }
    }
    public boolean onKeyDown(int keyCode,KeyEvent event) {
        switch(keyCode)

        {
            case KeyEvent.KEYCODE_MENU:

//                Toast.makeText(getApplicationContext(),"你按下菜单键",Toast.LENGTH_LONG).show();
                goSettings();
                break;

            case KeyEvent.KEYCODE_DPAD_CENTER:

                Toast.makeText(getApplicationContext(), "你按下中间键", Toast.LENGTH_LONG).show();

                break;

            case KeyEvent.KEYCODE_DPAD_DOWN:

                Toast.makeText(getApplicationContext(), "你按下下方向键", Toast.LENGTH_LONG).show();
                break;

            case KeyEvent.KEYCODE_DPAD_LEFT:

                Toast.makeText(getApplicationContext(), "你按下左方向键", Toast.LENGTH_LONG).show();
                break;

            case KeyEvent.KEYCODE_DPAD_RIGHT:

                Toast.makeText(getApplicationContext(), "你按下右方向键", Toast.LENGTH_LONG).show();
                break;

            case KeyEvent.KEYCODE_DPAD_UP:

                Toast.makeText(getApplicationContext(), "你按下上方向键", Toast.LENGTH_LONG).show();
                break;

            default:
                break;

        }

        return super.onKeyDown(keyCode, event);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            goSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
