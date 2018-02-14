package itemloader.witellsolutions.tvadvertisement;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.VideoView;

/**
 * Created by Sumudu on 3/30/2016.
 */
public class Splash extends Activity {
    VideoView splashVideo;
    SharedPreferences settings = null;
    String stat;
    NetworkStatChecker n = new NetworkStatChecker();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        //new Net().execute();
        splashVideo = (VideoView) findViewById(R.id.splashVideoPlayer);
        String UrlPath = "android.resource://" + getPackageName() + "/" + R.raw.wcvid;
        final Uri uri = Uri.parse(UrlPath);
        splashVideo.setVideoURI(uri);
        splashVideo.start();
        splashVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                logReader();
            }
        });
        Intent intent1 = new Intent(Splash.this, MyFirebaseInstanceIDService.class);
        Intent intent2 = new Intent(Splash.this, MyFirebaseMessagingService.class);
        if(!isMyServiceRunning(MyFirebaseInstanceIDService.class)){
            startService(intent1);
        }
        if(!isMyServiceRunning(MyFirebaseMessagingService.class)){
            startService(intent2);
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

    class Net extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... urls) {
                    stat = n.isConnected(getApplicationContext());
            return stat;
        }

        @Override
        protected void onPostExecute(String result) {
                if(stat.equalsIgnoreCase("")){

                }
            else{
                    netError(stat);
                }
        }
    }

    public void netError(String msg){
        final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setCancelable(false);
        dlgAlert.setMessage("Please check your network connection" + '\n' + "Error Code: " + msg);
        dlgAlert.setTitle("No network connection");
        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });
        dlgAlert.create().show();
    }

    public void logReader() {
        settings = getSharedPreferences("usertype", 0);
        if (settings.getString("usertype", "").toString().equals("")) {
            finish();
            Intent i = new Intent(Splash.this, Login.class);
            i.putExtra("stat", "");
            startActivity(i);
        } else {
            Intent intent = new Intent(Splash.this, Loading.class);
            intent.putExtra("usertype", settings.getString("usertype", ""));
            startActivity(intent);
            finish();
        }
    }
}