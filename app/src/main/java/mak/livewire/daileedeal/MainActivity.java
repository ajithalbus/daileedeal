package mak.livewire.daileedeal;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import 	android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.view.View;
import android.content.*;
import android.app.*;
import android.content.pm.ActivityInfo;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
   public ProgressBar progress;
    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

       // ActionBar actionBar = getActionBar();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = (WebView) findViewById(R.id.activity_main_webview);
        mWebView.setWebViewClient(new MyWebViewClient());// to load page locally
        WebSettings webSettings = mWebView.getSettings(); //enables js
        webSettings.setJavaScriptEnabled(true);

         progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setVisibility(View.GONE);



        mWebView.loadUrl("https://daileedeal.com/");
    }

    @Override


    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //Toast.makeText(getApplicationContext(),"woking",Toast.LENGTH_SHORT).show();
        //here you can handle orientation change
    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            { // copied code for back button exit
                new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                        .setMessage("Are you sure?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                finish();

                            }
                        }).setNegativeButton("no", null).show();
            }

        }
    }


/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
    //no menu inflater

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progress.setVisibility(View.GONE);
            MainActivity.this.progress.setProgress(100);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progress.setVisibility(View.VISIBLE);
            MainActivity.this.progress.setProgress(0);
            super.onPageStarted(view, url, favicon);
        }
    }

}
