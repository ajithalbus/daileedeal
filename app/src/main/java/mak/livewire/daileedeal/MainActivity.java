package mak.livewire.daileedeal;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.widget.ArrayAdapter;
import android.content.res.Configuration;
import android.widget.AdapterView;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import 	android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.view.View;
import android.content.*;
import android.app.*;
import android.content.pm.ActivityInfo;
import android.widget.Toast;
import android.os.Handler;
import android.view.Window;
public class MainActivity extends ActionBarActivity {
   public ProgressBar progress;
    private WebView mWebView;
    private ListView mDrawerList;
   private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private final int links[]=new int[] {59,115,60,61,62,63,64,65,66};
    private final String base_link="https://daileedeal.com/index.php?route=product/category&path=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


       // ActionBar actionBar = getActionBar();

       // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        setupDrawer();

        mDrawerList = (ListView)findViewById(R.id.left_drawer);
        addDrawerItems();
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, position+"", Toast.LENGTH_SHORT).show();
if(position==0) mWebView.loadUrl("https://daileedeal.com/");
          else      mWebView.loadUrl(base_link+links[position-1]);
                mDrawerLayout.closeDrawers();

            }
        });


        //webview code start here
        mWebView = (WebView) findViewById(R.id.activity_main_webview);
        mWebView.setWebViewClient(new MyWebViewClient());// to load page locally
        mWebView.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = mWebView.getSettings(); //enables js
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);


        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);


        //webSettings.setAllowContentAccess(true);
       // webSettings.setAllowFileAccessFromFileURLs(true);
       // webSettings.setAllowUniversalAccessFromFileURLs(true);

         progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setVisibility(View.GONE);


mWebView.loadUrl("file:///android_asset/splash.html");
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                mWebView.loadUrl("https://daileedeal.com/");
            }
        }, 500);



    }



    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Categories");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    private void addDrawerItems() {
        String[] osArray = {"Home", "Fruits", "Vegetables", "Beverages", "Grocery & Staples", "Baby Care","Packaged Food","Household", "Bread and Dairy","Personal Care"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

    }




    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mDrawerToggle.onConfigurationChanged(newConfig);

        super.onConfigurationChanged(newConfig);
        //Toast.makeText(getApplicationContext(),"woking",Toast.LENGTH_SHORT).show();
        //here you can handle orientation change
    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            {
                if (doubleBackToExitPressedOnce) {
                    finish();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
             // copied code for back button exit
                /*new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                        .setMessage("Are you sure?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                finish();

                            }
                        }).setNegativeButton("no", null).show();

            */
            }

        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }



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
        /*if (id == R.id.action_settings) {
            return true;
        }*/
String url="https://daileedeal.com/index.php?route=";
        switch (id)
        {case R.id.myaccount : url=url+"account/account";break;
            case R.id.wishlist : url=url+"account/wishlist";break;
            case R.id.shoppingcart : url=url+"checkout/cart";break;
            case R.id.checkout : url=url+"checkout/checkout";break;

default:url=null;
        }if(url!=null)
        mWebView.loadUrl(url);
if(id==R.id.refresh)
    mWebView.reload();


        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //no menu inflater

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        view.loadUrl("file:///android_asset/error.html");
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
