package itemloader.witellsolutions.tvadvertisement;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Sumudu on 6/22/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        // Log.d(TAG, "From: " + remoteMessage.getFrom());
        //Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        String msgBody = remoteMessage.getData().get("title");
        Log.d(TAG, "Notification Message Body: " + msgBody.toString().trim());

        switch (msgBody.toString().trim()){
            case "login" :sendNotificationDeactivateLogin(); break;
            case "active" :sendNotificationActivateLogin(); break;
            case "sync1" :sendNotificationStartSynchronization(); break;
            case "sync2" :sendNotificationStartSynchronization(); break;
            case "sync3" :sendNotificationStartSynchronization(); break;
            case "blocked" :sendNotificationBlocked(); break;
            case "unblocked" :sendNotificationUnblocked(); break;
            case "pingenable" :ping(true); break;
            case "pingdisable" :ping(false); break;
            case "syncNews" : sendNotificationStartSynchronizationNews(); break;
            case "hideSide" : hideSide();
            //case "rightToLeft" : layoutNumber(1); break;
           // case "leftToRight" : layoutNumber(2); break;
        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void sendNotificationStartSynchronization() {
        SharedPreferences logcatStart = getSharedPreferences("sync1", 0);
        SharedPreferences.Editor logcatEditor = logcatStart.edit();
        logcatEditor.putBoolean("synchronize", true);
        logcatEditor.commit();
    }

    private void sendNotificationStartSynchronizationNews() {
        SharedPreferences logcatStart = getSharedPreferences("syncNews", 0);
        SharedPreferences.Editor logcatEditor = logcatStart.edit();
        logcatEditor.putBoolean("synchronize", true);
        logcatEditor.commit();
    }
    @Override
    public void onDestroy() {
        if(!isMyServiceRunning(MyFirebaseMessagingService.class)){
            Intent myIntent = new Intent(getApplication(), MyFirebaseMessagingService.class);
            getApplication().startService(myIntent);
        }
    }

    private void sendNotificationDeactivateLogin() {
        SharedPreferences logcatStart = getSharedPreferences("devicelogin", 0);
        SharedPreferences.Editor logcatEditor = logcatStart.edit();
        logcatEditor.putBoolean("deactivate", true);
        logcatEditor.commit();
    }

    private void sendNotificationBlocked() {
        SharedPreferences logcatStart = getSharedPreferences("cred", 0);
        SharedPreferences.Editor logcatEditor = logcatStart.edit();
        logcatEditor.putBoolean("unblock", false);
        logcatEditor.commit();
    }

    private void sendNotificationUnblocked() {
        SharedPreferences logcatStart = getSharedPreferences("cred", 0);
        SharedPreferences.Editor logcatEditor = logcatStart.edit();
        logcatEditor.putBoolean("unblock", true);
        logcatEditor.commit();
    }


    private void hideSide() {
        SharedPreferences logcatStart = getSharedPreferences("hideSide", 0);
        SharedPreferences.Editor logcatEditor = logcatStart.edit();
        logcatEditor.putBoolean("hideSide", true);
        logcatEditor.commit();
    }

    private void ping(boolean active) {
        SharedPreferences logcatStart = getSharedPreferences("logtime", 0);
        SharedPreferences.Editor logcatEditor = logcatStart.edit();
        logcatEditor.putBoolean("ping", active);
        logcatEditor.commit();
    }

    private void sendNotificationActivateLogin() {
        SharedPreferences logcatStart = getSharedPreferences("devicelogin", 0);
        SharedPreferences.Editor logcatEditor = logcatStart.edit();
        logcatEditor.putBoolean("deactivate", false);
        logcatEditor.commit();
       /* if(isMyServiceRunning(ChatHeadService.class)){
            try{
                Intent i = new Intent(MyFirebaseMessagingService.this, ChatHeadService.class);
                stopService(i);
            }
            catch(Exception e){

            }
        }
        if(!isMyServiceRunning(ChatHeadService.class)){
            Intent ii = new Intent(MyFirebaseMessagingService.this, ChatHeadService.class);
            startService(ii);
        }*/

       /* try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
            //   MediaPlayer mp = MediaPlayer.create(ChatHeadService.this, R.raw.alert);
            //  mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, MenuViewPager.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Dealer Portal Notification")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());*/
    }
}
