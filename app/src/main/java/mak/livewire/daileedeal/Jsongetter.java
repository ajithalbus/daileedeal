package mak.livewire.daileedeal;

/**
 * Created by mak on 14/7/16.
 */
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;




public class Jsongetter {

    private final String url="http://mak.wtf.im/js.json";
    HttpClient client;
    JSONArray responce;
    JSONObject json;
    Context con;

   public Jsongetter(Context c)
   {this.con=c;}


    public String[] getTags()
    { new Tasker().execute(0);
        try {String tem=responce.getJSONObject(0).getString("tag");
            Toast.makeText(con, tem,Toast.LENGTH_SHORT ).show();
        }
        catch (Exception e){}
            return null;
    }

    public  String[] getLinks()
    {

        return null;
    }

    //copied from mblood
    public JSONArray updater()throws ClientProtocolException,IOException,JSONException
    {
        HttpGet get=new HttpGet(url);
        HttpResponse r=client.execute(get);
        int status=r.getStatusLine().getStatusCode();

        if(status==200)
        {
            HttpEntity e=r.getEntity();
            String data= EntityUtils.toString(e);
            //Toast.makeText(parent,data,Toast.LENGTH_SHORT).show();
            //Log.d("data", data);
            JSONArray result=new JSONArray(data);
            return result;
        }
        else
        {
            //Toast.makeText(parent,"Error Updating",Toast.LENGTH_SHORT).show();
            return null;
        }

    }// end of updater function

    public class Tasker extends AsyncTask<Integer,Integer,JSONArray>
    {
        int flag,count,i;
        public Tasker()
        {}


        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected JSONArray doInBackground(Integer... params) {
            JSONArray jsonArray=new JSONArray();

            try {
                jsonArray=updater();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

responce=jsonArray;

            return jsonArray;


            //return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
           }
    }



}
