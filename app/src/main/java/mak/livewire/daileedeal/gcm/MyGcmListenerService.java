package mak.livewire.daileedeal.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.URLUtil;

import com.google.android.gms.gcm.GcmListenerService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import mak.livewire.daileedeal.R;
import mak.livewire.daileedeal.activity.MainActivity;

public class MyGcmListenerService extends GcmListenerService {
   private String[] contents;
    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        contents=message.split(",");
        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        try {
            sendNotification(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) throws IOException {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("page",contents[3]);
        intent.putExtra("from_noti",1);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        /*NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
*/


        Bitmap image=null;
        URL url=new URL(contents[2]);
        try {
            if (URLUtil.isValidUrl(url.toString())) {
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                if (image == null) {
                    image = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
                }
            }
        }
        catch (MalformedURLException exp)
        {Log.d("mak","exp");
            image = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        Bitmap icon=BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
        Notification notif = new Notification.Builder(this)
                .setContentTitle(contents[0])
                .setContentText(contents[1])
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(icon)
                .setStyle(new Notification.BigPictureStyle()
                        .bigPicture(image))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .build();

       // notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    notificationManager.notify(0,notif);
    }
}

