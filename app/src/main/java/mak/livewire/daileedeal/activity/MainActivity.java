package mak.livewire.daileedeal.activity;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.widget.ArrayAdapter;
import android.graphics.Bitmap;
import android.widget.ListView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import 	android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.os.Handler;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.*;
import android.util.Log;

import mak.livewire.daileedeal.CustomSwipeRefreshLayout;
import mak.livewire.daileedeal.gcm.QuickstartPreferences;
import mak.livewire.daileedeal.R;
import mak.livewire.daileedeal.gcm.RegistrationIntentService;

public class MainActivity extends AppCompatActivity implements CustomSwipeRefreshLayout.CanChildScrollUpCallback,FragmentDrawer.FragmentDrawerListener {
    //public ProgressBar progress;
    private FragmentDrawer drawerFragment;
    private WebView mWebView;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
  //  private ActionBarDrawerToggle mDrawerToggle;  dep
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private final int links[] = new int[]{116, 60, 61, 62, 63, 64, 65, 66};
    private final String base_link = "https://daileedeal.com/index.php?route=product/category&path=";
    private Toolbar mToolbar;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    private Intent intent;
    private String noti_addr;
    private CustomSwipeRefreshLayout mRefreshLayout;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        noti_addr = "https://daileedeal.com";
        intent = getIntent();
        if (intent.getIntExtra("from_noti", 0) == 1)
            noti_addr = intent.getStringExtra("page");

        //Toast.makeText(this,intent.getStringExtra("page"),Toast.LENGTH_SHORT).show();
        //gcm
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
              /*  if (sentToken) {
                    Toast.makeText(MainActivity.this,"success",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this,"error",Toast.LENGTH_SHORT).show();}

            */
            }
        };

        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }


        // ActionBar actionBar = getActionBar();

        // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//material toolbar
        //toolbar material view
        mToolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // fragment drawer
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout_new), mToolbar);
        drawerFragment.setDrawerListener(this);



//refresh

        mRefreshLayout = (CustomSwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mRefreshLayout.setColorSchemeResources(R.color.blue, R.color.green);
        mRefreshLayout.setCanChildScrollUpCallback(this);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWebView.reload();

            }
        });


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        //mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout); dep
        mActivityTitle = getTitle().toString();

       /* setupDrawer();

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        addDrawerItems();
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, position+"", Toast.LENGTH_SHORT).show();
                if (position == 0) mWebView.loadUrl("https://daileedeal.com/");
                else mWebView.loadUrl(base_link + links[position - 1]);
                mDrawerLayout.closeDrawers();

            }
        }); */    //dep


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

        //progress = (ProgressBar) findViewById(R.id.progressBar);
       // progress.setVisibility(View.GONE);


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

                mWebView.loadUrl(noti_addr);


            }
        }, 500);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

  /*  private void setupDrawer() {  dep
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            // Called when a drawer has settled in a completely open state.
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Categories");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            // Called when a drawer has settled in a completely closed state.
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
        String[] osArray = {"Home", "Organic Products", "Beverages", "Grocery & Staples", "Baby Care", "Packaged Food", "Household", "Bread and Dairy", "Personal Care"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

    }
*/

   /* @Override dep
    public void onConfigurationChanged(Configuration newConfig) {
        mDrawerToggle.onConfigurationChanged(newConfig);

        super.onConfigurationChanged(newConfig);
        //Toast.makeText(getApplicationContext(),"woking",Toast.LENGTH_SHORT).show();
        //here you can handle orientation change
    }*/

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        WebBackForwardList mWebBackForwardList = mWebView.copyBackForwardList();
        String historyUrl = mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex()-1).getUrl();
        if (mWebView.canGoBack()&&((historyUrl.compareToIgnoreCase("file:///android_asset/splash.html")!=0)&&(historyUrl.compareToIgnoreCase("file:///android_asset/error.html")!=0))) {
            mWebView.goBack();

        } else {
            {
                if (doubleBackToExitPressedOnce) {
                    finish();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Snackbar.make((RelativeLayout)findViewById(R.id.drawer_layout), "Please click BACK again to exit", Snackbar.LENGTH_SHORT).show();
                //Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

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
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("MAIN", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


  /*  @Override dep
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    } */


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
        String url = "https://daileedeal.com/index.php?route=";
        switch (id) {
            case R.id.myaccount:
                url = url + "account/account";
                break;
            case R.id.wishlist:
                url = url + "account/wishlist";
                break;
            case R.id.shoppingcart:
                url = url + "checkout/cart";
                break;
            case R.id.checkout:
                url = url + "checkout/checkout";
                break;

            default:
                url = null;
        }
        if (url != null)
            mWebView.loadUrl(url);
        /*if (id == R.id.refresh)
            mWebView.reload();
*/

        /*if (mDrawerToggle.onOptionsItemSelected(item)) { dep
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    public boolean canSwipeRefreshChildScrollUp() {
        return mWebView.getScrollY() > 0;
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        if (position == 0) mWebView.loadUrl("https://daileedeal.com/");
        else mWebView.loadUrl(base_link + links[position - 1]);
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
          //  progress.setVisibility(View.GONE);
           // MainActivity.this.progress.setProgress(100);
            MainActivity.this.mRefreshLayout.setRefreshing(false);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
           // progress.setVisibility(View.VISIBLE);
           // MainActivity.this.progress.setProgress(0);
            MainActivity.this.mRefreshLayout.setRefreshing(false);
            MainActivity.this.mRefreshLayout.setRefreshing(true);
            super.onPageStarted(view, url, favicon);
        }
    }

}
