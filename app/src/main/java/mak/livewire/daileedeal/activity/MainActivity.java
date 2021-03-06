package mak.livewire.daileedeal.activity;
import android.Manifest;
import android.app.Application;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.widget.ArrayAdapter;
import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.ImageButton;
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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.zxing.integration.android.IntentIntegrator;
//import com.google.zxing.integration.android.IntentResult;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.*;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mak.livewire.daileedeal.CustomSwipeRefreshLayout;
//import mak.livewire.daileedeal.Jsongetter;
import mak.livewire.daileedeal.gcm.QuickstartPreferences;
import mak.livewire.daileedeal.R;
import mak.livewire.daileedeal.gcm.RegistrationIntentService;

public class MainActivity extends AppCompatActivity implements CustomSwipeRefreshLayout.CanChildScrollUpCallback {
    //public ProgressBar progress;
    private JSONArray jsonArray;
    private static final int REQUEST_CODE_SOME_FEATURES_PERMISSIONS=138;
    //private FragmentDrawer drawerFragment;
    private WebView mWebView;
  //  private ListView mDrawerList;
  //  private ArrayAdapter<String> mAdapter;
  //  private ActionBarDrawerToggle mDrawerToggle;  dep
 //   private DrawerLayout mDrawerLayout;
 //   private String mActivityTitle;
  //  private final int links[] = new int[]{116, 60, 61, 62, 63, 64, 65, 66};
  //  private final String base_link = "https://daileedeal.com/index.php?route=product/category&path=";
  //  private Toolbar mToolbar;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    private Intent intent;
    private String noti_addr;
    private CustomSwipeRefreshLayout mRefreshLayout;
   // private ImageButton button;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//barcode scanner

jsonArray=new JSONArray();
        //new Jsongetter(this).getTags();
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
        mWebView = (WebView) findViewById(R.id.activity_main_webview); // for snackbar ref
// barcode
   /*    button=(ImageButton)findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(android.os.Build.VERSION.SDK_INT>=23) {
                    int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);
                    //int hasSMSPermission = checkSelfPermission( Manifest.permission.SEND_SMS );
                    List<String> permissions = new ArrayList<String>();
                    if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                        permissions.add(Manifest.permission.CAMERA);
                    }




                    if (!permissions.isEmpty()) {
                        requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_CODE_SOME_FEATURES_PERMISSIONS);
                    }
                    else
                    { IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);

                        integrator.initiateScan();}
                }
                else
                { IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);

                integrator.initiateScan();}


            }
        });

*/
//material toolbar
        //toolbar material view
       /* mToolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
*/
        // fragment drawer
       /* drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout_new), mToolbar);
        drawerFragment.setDrawerListener(this);*/

//contactsender
//boolean contactsentflag=false;
/*
        final SharedPreferences setting= getSharedPreferences("settings",0); // get preferences
        final SharedPreferences.Editor editor = setting.edit(); // to change preference
        boolean permission_requested=setting.getBoolean("permission",false); // assign preference to subs

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            int hasContactPermission = checkSelfPermission(Manifest.permission.READ_CONTACTS);
            //int hasSMSPermission = checkSelfPermission( Manifest.permission.SEND_SMS );
            List<String> permissions = new ArrayList<String>();
            if (hasContactPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_CONTACTS);
            }


            if (!permissions.isEmpty() && !permission_requested) {
                //Snackbar.make(mWebView,"DaileeDeal needs access to your contacts info to serve you better",Snackbar.LENGTH_LONG).show();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        MainActivity.this);

                // set title
                alertDialogBuilder.setTitle("Request for contacts information !");

                // set dialog message
                alertDialogBuilder
                        .setMessage("DaileeDeal requires your contacts info for Ad campaign and to serve you better. Please read our privacy policy for more information.")
                        .setCancelable(false)
                        .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity
                                if(android.os.Build.VERSION.SDK_INT >= 23)
                                    requestPermissions(new String[] {Manifest.permission.READ_CONTACTS},REQUEST_CODE_SOME_FEATURES_PERMISSIONS);
                                editor.putBoolean("permission",true);
                                editor.apply();
                            }
                        })
                        ;

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();



                //requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_CODE_SOME_FEATURES_PERMISSIONS);
            }


        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            //contactsentflag=true;
            sendContacts();

        }


    }

*/
        //end of contacts sender

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
        //mActivityTitle = getTitle().toString();

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_SOME_FEATURES_PERMISSIONS: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d("Permissions", "Permission Granted: " + permissions[i]);
                        //IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);

                       // integrator.initiateScan();
                        //do stuff here
                        sendContacts();



                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.d("Permissions", "Permission Denied: " + permissions[i]);


                    }
                }
            }
            break;
            default: {

               // Snackbar.make(mWebView,"DaileeDeal wont be able to proceed without Contacts permission",Snackbar.LENGTH_LONG).show();


                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

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

    //@Override
   /* protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Snackbar.make((RelativeLayout)findViewById(R.id.drawer_layout), "No Bar Code Read.", Snackbar.LENGTH_SHORT).show();

                //Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                Snackbar.make((RelativeLayout)findViewById(R.id.drawer_layout), "Loading Item.", Snackbar.LENGTH_SHORT).show();
                mWebView.loadUrl("https://daileedeal.com/index.php?route=product/product&product_id="+result.getContents());
               // Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
*/
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

   /* @Override
    public void onDrawerItemSelected(View view, int position) {
        if (position == 0) mWebView.loadUrl("https://daileedeal.com/");
        else mWebView.loadUrl(base_link + links[position - 1]);
    }
    //no menu inflater
*/
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

    void sendContacts()
    {
int len=0;
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            //Log.e("contacts", name + "-" + phoneNumber);
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", name);
                jsonObject.put("phone", phoneNumber);
                jsonArray.put(jsonObject);
                len++;
                //


            }
            catch (JSONException e) {}

        }
        JSONObject packed_contacts=new JSONObject();
       try { packed_contacts.put("msg",jsonArray);
        packed_contacts.put("len",Integer.toString(len)); }
       catch (JSONException e) {}

        vollyContacts(packed_contacts);
        Log.i("contact","volly");
        //Toast.makeText(MainActivity.this,packed_contacts.toString(),Toast.LENGTH_LONG).show();
        phones.close();

    }
    void vollyContacts(final JSONObject info)
    {    //Toast.makeText(MainActivity.this,info.toString(),Toast.LENGTH_LONG).show();
        final RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

        String url = "https://daileedeal.com/contact/";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
               // Toast.makeText(MainActivity.this,response.toString(),Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("a", info.toString()); //Add the data you'd like to send to the server.
                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);



    }
}
