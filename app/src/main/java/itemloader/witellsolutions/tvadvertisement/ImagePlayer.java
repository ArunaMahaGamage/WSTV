package itemloader.witellsolutions.tvadvertisement;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Sumudu on 3/30/2016.
 */
public class ImagePlayer extends AppCompatActivity {
    int c1 = 0, c2 = 0, c3 = 0;
    Timer timer1, timer2, timer3, timerNews;
    TextView news;
    LinearLayout sideLayout;
    ViewPager imagePlayer, sideImageTopPlayer, sideImageBottomPlayer;
    String newsTextString = "", newsTextStringUpdated = "";
    NetworkStatChecker n = new NetworkStatChecker();
    FragmentPageAdapter1 ft1;
    FragmentPageAdapter2 ft2;
    FragmentPageAdapter3 ft3;
    int newsBartextSize;
    DBOperations dbo = new DBOperations();
    boolean netStat;
    static int logoutTimer = 0;
    static int DIVIDING_COUNT = 10;
    int dealerID;
    String version;
    String versionCode, verURL;
    boolean startSync = false;
    boolean loginAvailable = true, userAvailable = true;
    DigitalClock clock;
    TextView chagre;
    ImageView tvBadge;
    Context c;
    String deviceName;
    static int netTimer = 0;
    int counting = 1;

    String[] contentNameUpdated1 = null, contentTypeUpdated1 = null, contentTimeUpdated1 = null;
    String[] contentNameUpdated2 = null, contentTypeUpdated2 = null, contentTimeUpdated2 = null;
    String[] contentNameUpdated3 = null, contentTypeUpdated3 = null, contentTimeUpdated3 = null;

    boolean completeSync1 = true, completeSync2 = true, completeSync3 = true, completeSyncNews = true;
    Data d = new Data();
    TextView refreshText;
    LinearLayout newsBar;
    String userCategory = "";
    int content1Index = 0, content2Index = 0, content3Index = 0;
    int content1Time = 0, content2Time = 0, content3Time = 0;
    int Measuredwidth;
    SharedPreferences settings5;
    SharedPreferences settings;
    String deviceID;
    int Measuredheight;

    String[] contentName1, contentType1, contentTime1;
    String[] contentName2, contentType2, contentTime2;
    String[] contentName3, contentType3, contentTime3;

    boolean updateAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Point size = new Point();
        WindowManager w = getWindowManager();
        newsBar = (LinearLayout)findViewById(R.id.newsBar);
        refreshText = (TextView)findViewById(R.id.rtext);
        sideLayout = (LinearLayout) findViewById(R.id.sideLayout);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)    {
            w.getDefaultDisplay().getSize(size);
            newsBartextSize = newsBar.getHeight();
            Measuredwidth = size.x;
            Measuredheight = size.y;
        }else{
            Display d = w.getDefaultDisplay();
            Measuredwidth = d.getWidth();
            Measuredheight = d.getHeight();
        }

        if (Build.VERSION.SDK_INT < 16) { //ye olde method
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            // Jellybean and up, new hotness
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            ActionBar actionBar = getActionBar();
            if(actionBar != null){actionBar.hide();}
        }
        try{
            settings = getSharedPreferences("exit", 0);
        }
        catch (Exception e){

        }

        imagePlayer = (ViewPager)findViewById(R.id.imagePlayer);
        sideImageTopPlayer = (ViewPager)findViewById(R.id.sideImageTopPlayer);
        sideImageBottomPlayer = (ViewPager)findViewById(R.id.sideImageBottomPlayer);

        tvBadge = (ImageView)findViewById(R.id.tvbadge);

        deviceName = android.os.Build.MANUFACTURER + " " + android.os.Build.PRODUCT;
        tvBadge.setLayoutParams(new RelativeLayout.LayoutParams((Measuredwidth/6), (Measuredheight/6)));
        news = (TextView)findViewById(R.id.newsText);
        news.setFocusable(true);
        news.setTextSize((float) (Measuredheight/40));
        final LinearLayout loadingImageView = (LinearLayout)findViewById(R.id.netErrorMessage);
        loadingImageView.setVisibility(View.GONE);
        loadingImageView.bringToFront();
        final LinearLayout loadingImageView111 = (LinearLayout)findViewById(R.id.updateReminder);
        loadingImageView111.setVisibility(View.GONE);
        final TextView txtUpdate = (TextView)findViewById(R.id.gahusgduj);

        chagre = (TextView)findViewById(R.id.textClock);// TextView to display Time
        clock = (DigitalClock)findViewById(R.id.digitalClock);// DigitalClock With visibility=gone

        clock.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                chagre.setText(s.toString());//will be called when system clock updataes
            }
        });

        c = this;
        Intent i = getIntent();
        if(i != null){
            dealerID = i.getIntExtra("dealerID", 0);
            deviceID = i.getStringExtra("devID");
            userCategory = i.getStringExtra("userCategory");
        }

        try{
            SharedPreferences settings1 = getSharedPreferences("content1", 0);
            String contentName1String = settings1.getString("contentName1", "");
            String contentType1String = settings1.getString("contentType1", "");
            String contentTime1String = settings1.getString("contentTime1", "");
            newsTextString = settings1.getString("news", "");
            news.setText(newsTextString);
            news.setSelected(true);

            contentName1 = contentName1String.split("#");
            contentType1 = contentType1String.split("#");
            contentTime1 = contentTime1String.split("#");

            SharedPreferences settings2 = getSharedPreferences("content2", 0);
            String contentName2String = settings2.getString("contentName2", "");
            String contentType2String = settings2.getString("contentType2", "");
            String contentTime2String = settings2.getString("contentTime2", "");

            contentName2 = contentName2String.split("#");
            contentType2 = contentType2String.split("#");
            contentTime2 = contentTime2String.split("#");

            SharedPreferences settings3 = getSharedPreferences("content3", 0);
            String contentName3String = settings3.getString("contentName3", "");
            String contentType3String = settings3.getString("contentType3", "");
            String contentTime3String = settings3.getString("contentTime3", "");

            contentName3 = contentName3String.split("#");
            contentType3 = contentType3String.split("#");
            contentTime3 = contentTime3String.split("#");
        }
        catch(Exception e){

        }
            ft1 = new FragmentPageAdapter1(getSupportFragmentManager(),contentName1, contentType1, contentTime1, imagePlayer, getApplicationContext());
            ft1.notifyDataSetChanged();
            imagePlayer.setAdapter(ft1);
            imagePlayer.setOnTouchListener(null);
            imagePlayer.setCurrentItem(0, true);

          /*  ft2 = new FragmentPageAdapter2(getSupportFragmentManager(),contentName2, contentType2, contentTime2, sideImageTopPlayer, getApplicationContext());
            ft2.notifyDataSetChanged();
            sideImageTopPlayer.setAdapter(ft2);
            sideImageTopPlayer.setOnTouchListener(null);
            sideImageTopPlayer.setCurrentItem(0, true);*/

         /*   ft3 = new FragmentPageAdapter3(getSupportFragmentManager(),contentName3, contentType3, contentTime3, sideImageBottomPlayer, getApplicationContext());
            ft3.notifyDataSetChanged();
            sideImageBottomPlayer.setAdapter(ft3);
            sideImageBottomPlayer.setOnTouchListener(null);
            sideImageBottomPlayer.setCurrentItem(0, true);*/

            final Handler handler1 = new Handler();
            timer1 = new Timer();
            TimerTask doAsynchronousTask1 = new TimerTask() {
                @Override
                public void run() {
                    handler1.post(new Runnable() {
                        public void run() {
                            if((60 - netTimer) >= 20){
                                refreshText.setText("Connecting "+ String.valueOf(60 - (netTimer+20)) + " seconds");
                            }
                            else{
                                refreshText.setText("Trying to connect...");
                            }
                            if((60 - netTimer) < 20){
                                if(startSync){}
                                else{
                                    SharedPreferences logcatStart = getSharedPreferences("logtime", 0);
                                    boolean functionEnable = logcatStart.getBoolean("ping", true);
                                    if(functionEnable){
                                        new NetChecker().execute();
                                    }
                                    else{
                                        //new LoginChecker().execute();
                                    }
                                }
                            }
                                if(netTimer == 60){
                                    netTimer = 0;
                                }
                                else{
                                    netTimer++;
                                }

                            if(logoutTimer == 5){
                                SharedPreferences deviceLoginChecker = getSharedPreferences("devicelogin", 0);
                                boolean deactivateDevice = deviceLoginChecker.getBoolean("deactivate", false);
                                if(deactivateDevice){
                                    loginAvailable = false;
                                }
                                else {
                                    loginAvailable = true;
                                }

                                SharedPreferences userLoginChecker = getSharedPreferences("cred", 0);
                                boolean available = userLoginChecker.getBoolean("unblock", true);
                                if(available){
                                    userAvailable = true;
                                }
                                else {
                                    userAvailable = false;
                                }

                                SharedPreferences sideCheck = getSharedPreferences("hideSide", 0);
                                boolean side = sideCheck.getBoolean("hideSide", false);
                                if(side){
                                    sideLayout.setVisibility(View.GONE);
                                }
                                else{
                                    sideLayout.setVisibility(View.VISIBLE);
                                }
                                logoutTimer = 0;
                            }
                            else{
                                logoutTimer ++;
                            }

                            try{
                                if(content1Time == Integer.parseInt(contentTime1[content1Index])){
                                    if(content1Index == (contentName1.length - 1)){
                                        if(completeSync1){
                                            imagePlayer.setAdapter(null);
                                            ft1 = new FragmentPageAdapter1(getSupportFragmentManager(),contentName1, contentType1, contentTime1, imagePlayer, getApplicationContext());
                                            ft1.notifyDataSetChanged();
                                            if(loginAvailable && userAvailable){
                                                if(ft1 == null){
                                                    ft1 = new FragmentPageAdapter1(getSupportFragmentManager(),new String[]{"end"}, new String[]{"img"}, new String[]{"10"}, imagePlayer, getApplicationContext());
                                                    imagePlayer.setAdapter(ft1);
                                                    imagePlayer.setCurrentItem(0, true);
                                                }
                                                else{
                                                    imagePlayer.setAdapter(ft1);
                                                    imagePlayer.setCurrentItem(0, true);
                                                }
                                            }
                                            else{ // blocked user or device
                                                ft1 = new FragmentPageAdapter1(getSupportFragmentManager(),new String[]{"end"}, new String[]{"img"}, new String[]{"10"}, imagePlayer, getApplicationContext());
                                                imagePlayer.setAdapter(ft1);
                                                imagePlayer.setCurrentItem(0, true);
                                            }
                                        }
                                        else{
                                            imagePlayer.setAdapter(ft1);
                                            imagePlayer.setCurrentItem(0, true);
                                        }
                                        content1Index = 0;
                                    }

                                    else if(content1Index == (contentName1.length - 2)){
                                        SharedPreferences settings = getSharedPreferences("sync1", 0);
                                        boolean syncStart = settings.getBoolean("synchronize", false);
                                        if(syncStart){
                                            new Downloader1().execute();
                                        }
                                        imagePlayer.setCurrentItem(++content1Index, true);
                                    }
                                    else{
                                        imagePlayer.setCurrentItem(++content1Index, true);
                                    }
                                    content1Time =  0;
                                }
                                else{
                                    content1Time ++ ;
                                }
                            }
                            catch(Exception e){
                                e.printStackTrace();
                                content1Index = 0;
                                content1Time =  0;
                                System.out.println("Exception: " + e.toString());
                                imagePlayer.setAdapter(null);
                                ft1 = new FragmentPageAdapter1(getSupportFragmentManager(),contentName1, contentType1, contentTime1, imagePlayer, getApplicationContext());
                                ft1.notifyDataSetChanged();
                                if(loginAvailable && userAvailable){
                                    if(ft1 == null){
                                        ft1 = new FragmentPageAdapter1(getSupportFragmentManager(),new String[]{"end"}, new String[]{"img"}, new String[]{"10"}, imagePlayer, getApplicationContext());
                                        imagePlayer.setAdapter(ft1);
                                        imagePlayer.setCurrentItem(0, true);
                                    }
                                    else{
                                        imagePlayer.setAdapter(ft1);
                                        imagePlayer.setCurrentItem(0, true);
                                    }
                                }
                                else{ // blocked user or device
                                    ft1 = new FragmentPageAdapter1(getSupportFragmentManager(),new String[]{"end"}, new String[]{"img"}, new String[]{"10"}, imagePlayer, getApplicationContext());
                                    imagePlayer.setAdapter(ft1);
                                    imagePlayer.setCurrentItem(0, true);
                                }
                                /*ft1 = new FragmentPageAdapter1(getSupportFragmentManager(),new String[]{"end"}, new String[]{"img"}, new String[]{"10"}, imagePlayer, getApplicationContext());
                                imagePlayer.setAdapter(ft1);
                                imagePlayer.setCurrentItem(0, true);

                                if(c1 == 10){
                                    c1 = 0;
                                    new Downloader2().execute();
                                }
                                else c1++;*/
                            }
                        }
                    });
                }
            };
            timer1.schedule(doAsynchronousTask1, 0, 1000);

      /*  final Handler handler2 = new Handler();
        timer2 = new Timer();
        TimerTask doAsynchronousTask2 = new TimerTask() {
            @Override
            public void run() {
                handler2.post(new Runnable() {
                    public void run() {
                        if(logoutTimer == 5){
                            SharedPreferences deviceLoginChecker = getSharedPreferences("devicelogin", 0);
                            boolean deactivateDevice = deviceLoginChecker.getBoolean("deactivate", false);
                            if(deactivateDevice){
                                loginAvailable = false;
                            }
                            else{
                                loginAvailable = true;
                            }
                            SharedPreferences userLoginChecker = getSharedPreferences("cred", 0);
                            boolean available = userLoginChecker.getBoolean("unblock", true);
                            if(available){
                                userAvailable = true;
                            }
                            else{
                                userAvailable = false;
                            }
                            logoutTimer = 0;
                        }
                        else{
                            logoutTimer ++;
                        }

                        try{
                            if(content2Time == Integer.parseInt(contentTime2[content2Index])){
                                if(content2Index == (contentName2.length - 1)){
                                    if(completeSync2){
                                        sideImageTopPlayer.setAdapter(null);
                                        ft2 = new FragmentPageAdapter2(getSupportFragmentManager(),contentName2, contentType2, contentTime2, sideImageTopPlayer, getApplicationContext());
                                        ft2.notifyDataSetChanged();
                                        if(loginAvailable && userAvailable){
                                            if(ft2 == null){
                                                ft2 = new FragmentPageAdapter2(getSupportFragmentManager(),new String[]{"end"}, new String[]{"img"}, new String[]{"10"}, sideImageTopPlayer, getApplicationContext());
                                                sideImageTopPlayer.setAdapter(ft2);
                                                sideImageTopPlayer.setCurrentItem(0, true);
                                            }
                                            else{
                                                sideImageTopPlayer.setAdapter(ft2);
                                                sideImageTopPlayer.setCurrentItem(0, true);
                                            }
                                        }
                                        else{ // blocked user or device
                                            ft2 = new FragmentPageAdapter2(getSupportFragmentManager(),new String[]{"end"}, new String[]{"img"}, new String[]{"10"}, sideImageTopPlayer, getApplicationContext());
                                            sideImageTopPlayer.setAdapter(ft2);
                                            sideImageTopPlayer.setCurrentItem(0, true);
                                        }
                                    }

                                    else{
                                        sideImageTopPlayer.setCurrentItem(0, true);
                                    }
                                    content2Index = 0;
                                }

                                else if(content2Index == (contentName2.length - 2)){
                                    SharedPreferences settings = getSharedPreferences("sync2", 0);
                                    boolean syncStart = settings.getBoolean("synchronize", false);
                                    if(syncStart){
                                        new Downloader2().execute();
                                    }
                                    sideImageTopPlayer.setCurrentItem(++content2Index, true);
                                }
                                else{
                                    sideImageTopPlayer.setCurrentItem(++content2Index, true);
                                }
                                content2Time =  0;
                            }
                            else{
                                content2Time ++ ;
                            }
                        }
                        catch(Exception e){
                           /* ft2 = new FragmentPageAdapter2(getSupportFragmentManager(),new String[]{"end"}, new String[]{"img"}, new String[]{"10"}, sideImageTopPlayer, getApplicationContext());
                            sideImageTopPlayer.setAdapter(ft2);
                            sideImageTopPlayer.setCurrentItem(0, true);

                            if(c2 == 10){
                                c2 = 0;
                                new Downloader2().execute();
                            }
                            else c2++;*/
                      /*  }
                    }
                });
            }
        };
        timer2.schedule(doAsynchronousTask2, 0, 1000);

        final Handler handler3 = new Handler();
        timer3 = new Timer();
        TimerTask doAsynchronousTask3 = new TimerTask() {
            @Override
            public void run() {
                handler3.post(new Runnable() {
                    public void run() {
                        if(logoutTimer == 5){
                            SharedPreferences deviceLoginChecker = getSharedPreferences("devicelogin", 0);
                            boolean deactivateDevice = deviceLoginChecker.getBoolean("deactivate", false);
                            if(deactivateDevice){
                                loginAvailable = false;
                            }
                            else{
                                loginAvailable = true;
                            }
                            SharedPreferences userLoginChecker = getSharedPreferences("cred", 0);
                            boolean available = userLoginChecker.getBoolean("unblock", true);
                            if(available){
                                userAvailable = true;
                            }
                            else{
                                userAvailable = false;
                            }
                            logoutTimer = 0;
                        }
                        else{
                            logoutTimer ++;
                        }

                        try{
                            if(content3Time == Integer.parseInt(contentTime3[content3Index])){
                                if(content3Index == (contentName3.length - 1)){
                                    if(completeSync3){
                                        sideImageBottomPlayer.setAdapter(null);
                                        ft3 = new FragmentPageAdapter3(getSupportFragmentManager(),contentName3, contentType3, contentTime3, sideImageBottomPlayer, getApplicationContext());
                                        ft3.notifyDataSetChanged();
                                        if(loginAvailable && userAvailable){
                                            if(ft3 == null){
                                                ft3 = new FragmentPageAdapter3(getSupportFragmentManager(),new String[]{"end"}, new String[]{"img"}, new String[]{"10"}, sideImageBottomPlayer, getApplicationContext());
                                                sideImageBottomPlayer.setAdapter(ft3);
                                                sideImageBottomPlayer.setCurrentItem(0, true);
                                            }
                                            else{
                                                sideImageBottomPlayer.setAdapter(ft3);
                                                sideImageBottomPlayer.setCurrentItem(0, true);
                                            }
                                        }
                                        else{ // blocked user or device
                                            ft3 = new FragmentPageAdapter3(getSupportFragmentManager(),new String[]{"end"}, new String[]{"img"}, new String[]{"10"}, sideImageBottomPlayer, getApplicationContext());
                                            sideImageBottomPlayer.setAdapter(ft3);
                                            sideImageBottomPlayer.setCurrentItem(0, true);
                                        }
                                    }

                                    else{
                                        sideImageBottomPlayer.setCurrentItem(0, true);
                                    }
                                    content3Index = 0;
                                }

                                else if(content3Index == (contentName3.length - 2)){
                                    SharedPreferences settings = getSharedPreferences("sync3", 0);
                                    boolean syncStart = settings.getBoolean("synchronize", false);
                                    if(syncStart){
                                        new Downloader3().execute();
                                    }
                                    sideImageBottomPlayer.setCurrentItem(++content3Index, true);
                                }
                                else{
                                    sideImageBottomPlayer.setCurrentItem(++content3Index, true);
                                }
                                content3Time =  0;
                            }
                            else{
                                content3Time ++ ;
                            }
                        }
                        catch(Exception e){*/
                            /*ft3 = new FragmentPageAdapter3(getSupportFragmentManager(),new String[]{"end"}, new String[]{"img"}, new String[]{"10"}, sideImageBottomPlayer, getApplicationContext());
                            sideImageBottomPlayer.setAdapter(ft3);
                            sideImageBottomPlayer.setCurrentItem(0, true);

                            if(c3 == 10){
                                c3 = 0;
                                new Downloader3().execute();
                            }
                            else c3++;*/
                   /*     }
                    }
                });
            }
        };
        timer3.schedule(doAsynchronousTask3, 0, 1000);*/

            final Handler handlerNews = new Handler();
            timerNews = new Timer();
            TimerTask doAsynchronousTaskNews = new TimerTask() {
                @Override
                public void run() {
                    handlerNews.post(new Runnable() {
                        public void run() {
                            SharedPreferences settings = getSharedPreferences("syncNews", 0);
                            boolean syncStart = settings.getBoolean("synchronize", false);
                            if(syncStart){
                                new DownloaderNews().execute();
                            }
                        }
                    });
                }
            };
            timerNews.schedule(doAsynchronousTaskNews, 0, 3000); //1000*60*60*4 = 4 hours*/

    }

    @Override
    public void onBackPressed() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String currentDateandTime = sdf.format(new Date());
        SharedPreferences settings = getSharedPreferences("exit", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("exit", currentDateandTime);
        editor.commit();

        SharedPreferences settings2 = getSharedPreferences("offlinestart", 0);
        SharedPreferences.Editor editor111 = settings2.edit();
        editor111.putString("offlinestart", currentDateandTime);
        editor111.commit();

        SharedPreferences settings1 = getSharedPreferences("dev", 0);
        SharedPreferences.Editor editor12 = settings1.edit();
        editor12.putString("dev", deviceID);
        editor12.commit();
        System.exit(0);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //Toast.makeText(getApplicationContext(), "f c", Toast.LENGTH_SHORT).show();
        if(!hasFocus) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            String currentDateandTime = sdf.format(new Date());
            SharedPreferences settings = getSharedPreferences("exit", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("exit", currentDateandTime);
            editor.commit();

            SharedPreferences settings2 = getSharedPreferences("offlinestart", 0);
            SharedPreferences.Editor editor111 = settings2.edit();
            editor111.putString("offlinestart", currentDateandTime);
            editor111.commit();

            SharedPreferences settings1 = getSharedPreferences("dev", 0);
            SharedPreferences.Editor editor12 = settings1.edit();
            editor12.putString("dev", deviceID);
            editor12.commit();

            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
            System.exit(0);
        }
    }

    public void logout(View v){
        try{
            SharedPreferences settings = getSharedPreferences("logger", 0);
            String t= settings.getString("logger", "");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            String currentDateandTime = sdf.format(new Date());

            SharedPreferences settings1 = getSharedPreferences("offlinestart", 0);
            SharedPreferences.Editor editor = settings1.edit();
            editor.putString("offlinestart", currentDateandTime);
            editor.commit();

            boolean result = dbo.putLogOutTime(deviceID, String.valueOf(dealerID).trim(), t, currentDateandTime);
            if(result){
                timer1.cancel();
///                timer2.cancel();
                //timer3.cancel();
                timerNews.cancel();
                deleteLog();
                Intent i = new Intent(c ,Login.class);
                i.putExtra("stat", true);
                startActivity(i);
                finish();
            }
            else{
                String netStat = n.isConnected(c);
                if(netStat.equals("0xE0417527")){
                    errorDialog("Please check your internet connection" + '\n' + "Error code: " + netStat);
                }
                else if(netStat.equals("0xE0225792")){
                    errorDialog("Server is not responding" + '\n' + "Error code: "  +netStat);
                }
            }
        }
        catch(Exception ex){
                ex.printStackTrace();
        }
    }

    public void update(View v){
        Uri uri = Uri.parse(verURL); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void deleteLog(){
        SharedPreferences settings = c.getSharedPreferences("usertype", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("usertype", "");
        editor.commit();
    }
    public boolean checkFileIsAvailable(String imgName){
        boolean availableFile;
        try {
            File f=new File(d.getSTORAGE_PATH(), imgName);
            availableFile = f.exists();
        }
        catch (Exception e) {
            availableFile = false;
        }
        return availableFile;
    }

    class Downloader1 extends AsyncTask<String, Void, String[][]> {
        String[][] content;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            completeSync1 = false;
        }

        @Override
        protected String[][] doInBackground(String... urls) {
            String netStat = n.isConnected(c);
            if (netStat.equals("")) {
                String[][] contentDetails1 = dbo.getContentDetails1(userCategory);

                try {
                    if(contentDetails1[0] == null || contentDetails1[1] == null){
                        SharedPreferences settings1 = getSharedPreferences("content1", 0);
                        SharedPreferences.Editor editor111 = settings1.edit();
                        editor111.putString("contentName1", "");
                        editor111.putString("contentType1", "");
                        editor111.putString("contentTime1", "");
                        editor111.commit();

                        contentNameUpdated1 = new String[]{"end"};
                        contentTypeUpdated1 = new String[]{"img"};
                        contentTimeUpdated1 = new String[]{"10"};
                    }
                    else{
                        contentNameUpdated1 = contentDetails1[0];
                        contentTypeUpdated1 = contentDetails1[1];
                        contentTimeUpdated1 = contentDetails1[2];
                    }

                    boolean fAvailable;

                    for (int i = 0; i < contentNameUpdated1.length; i++) {
                        fAvailable = checkFileIsAvailable(contentNameUpdated1[i]);
                        System.out.println(contentNameUpdated1[i]);
                        if (fAvailable) {
                        } else {
                            if (contentTypeUpdated1[i].equals("img")) {
                                new ImageDownloader(contentNameUpdated1[i]).execute();
                            } else {
                                new FileDownloader().downloadFile(contentNameUpdated1[i]);
                            }
                        }
                    }

                   /* SharedPreferences settings1 = getSharedPreferences("content1", 0);
                    SharedPreferences.Editor editor111 = settings1.edit();
                    editor111.putString("contentName1", "");
                    editor111.putString("contentType1", "");
                    editor111.putString("contentTime1", "");
                    editor111.commit();*/

                } catch (Exception e) {

                    contentNameUpdated1 = new String[]{"end"};
                    contentTypeUpdated1 = new String[]{"img"};
                    contentTimeUpdated1 = new String[]{"10"};

                    SharedPreferences settings1 = getSharedPreferences("content1", 0);
                    SharedPreferences.Editor editor111 = settings1.edit();
                    editor111.putString("contentName1", "");
                    editor111.putString("contentType1", "");
                    editor111.putString("contentTime1", "");
                    editor111.commit();
                }

                if(contentNameUpdated1[0].equalsIgnoreCase("end")){
                    /*content = new String[][]{contentName1, contentType1, contentTime1};
                    return content;*/
                    return null;
                }
                else{
                    content = new String[][]{contentNameUpdated1, contentTypeUpdated1, contentTimeUpdated1};
                    return content;
                }
            } else {
               /* content = new String[][]{contentName1, contentType1, contentTime1};
                return content;*/
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[][] result) {
            if (result == null) {
                System.out.println("return null");
            } else {
                contentName1 = result[0];
                contentType1 = result[1];
                contentTime1 = result[2];
                System.out.println("assigned");
            }

            SharedPreferences logcatStart = getSharedPreferences("sync1", 0);
            SharedPreferences.Editor logcatEditor = logcatStart.edit();
            logcatEditor.putBoolean("synchronize", false);
            logcatEditor.commit();
            completeSync1 = true;
            System.out.println("flushed");
        }
    }

    class DownloaderNews extends AsyncTask<String, Void, String[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            completeSyncNews = false;
        }

        @Override
        protected String[] doInBackground(String... urls) {
            String netStat = n.isConnected(c);
            if (netStat.equals("")) {
                String[] newsArray = dbo.getNews();
                    SharedPreferences settings1 = getSharedPreferences("content1", 0);
                    SharedPreferences.Editor editor111 = settings1.edit();
                    editor111.putString("news", "");
                    editor111.commit();

                return newsArray;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] result) {
            newsTextStringUpdated = "";
            if (result == null) {

            }
            else{
                try{
                    for(int i = 0; i < result.length; i ++){
                        newsTextStringUpdated += "  ●  " + result[i];
                    }
                    newsTextString = newsTextStringUpdated;
                    news.setText(newsTextString);
                }
                catch(Exception e){

                }
            }

            SharedPreferences logcatStart = getSharedPreferences("syncNews", 0);
            SharedPreferences.Editor logcatEditor = logcatStart.edit();
            logcatEditor.putBoolean("synchronize", false);
            logcatEditor.commit();
            completeSyncNews = true;
        }
    }


    class Downloader2 extends AsyncTask<String, Void, String[][]> {
        String[][] content;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            completeSync2 = false;
        }

        @Override
        protected String[][] doInBackground(String... urls) {
            String netStat = n.isConnected(c);
            if (netStat.equals("")) {
                String[][] contentDetails2 = dbo.getContentDetails2(userCategory);

                try {
                    if(contentDetails2[0] == null || contentDetails2[1] == null){
                        SharedPreferences settings1 = getSharedPreferences("content2", 0);
                        SharedPreferences.Editor editor111 = settings1.edit();
                        editor111.putString("contentName2", "");
                        editor111.putString("contentType2", "");
                        editor111.putString("contentTime2", "");
                        editor111.commit();

                        contentNameUpdated2 = new String[]{"end"};
                        contentTypeUpdated2 = new String[]{"img"};
                        contentTimeUpdated2 = new String[]{"10"};
                    }
                    else{
                        contentNameUpdated2 = contentDetails2[0];
                        contentTypeUpdated2 = contentDetails2[1];
                        contentTimeUpdated2 = contentDetails2[2];
                    }

                    boolean fAvailable;
                    System.out.println(contentNameUpdated2[0]);
                    for (int i = 0; i < contentNameUpdated2.length; i++) {
                        fAvailable = checkFileIsAvailable(contentNameUpdated2[i]);
                        if (fAvailable) {
                        } else {
                            if (contentTypeUpdated2[i].equals("img")) {
                                new ImageDownloader(contentNameUpdated2[i]).execute();
                            } else {
                                new FileDownloader().downloadFile(contentNameUpdated2[i]);
                            }
                        }
                    }

                    SharedPreferences settings1 = getSharedPreferences("content2", 0);
                    SharedPreferences.Editor editor111 = settings1.edit();
                    editor111.putString("contentName2", "");
                    editor111.putString("contentType2", "");
                    editor111.putString("contentTime2", "");
                    editor111.commit();

                } catch (Exception e) {

                    contentNameUpdated2 = new String[]{"end"};
                    contentTypeUpdated2 = new String[]{"img"};
                    contentTimeUpdated2 = new String[]{"10"};

                    SharedPreferences settings1 = getSharedPreferences("content2", 0);
                    SharedPreferences.Editor editor111 = settings1.edit();
                    editor111.putString("contentName2", "");
                    editor111.putString("contentType2", "");
                    editor111.putString("contentTime2", "");
                    editor111.commit();
                }

                if(contentNameUpdated2[0].equalsIgnoreCase("end")){
                    content = new String[][]{contentName2, contentType2, contentTime2};
                    return content;
                }
                else{
                    content = new String[][]{contentNameUpdated2, contentTypeUpdated2, contentTimeUpdated2};
                    return content;
                }
            } else {
                content = new String[][]{contentName2, contentType2, contentTime2};
                return content;
            }
        }

        @Override
        protected void onPostExecute(String[][] result) {
            if (result == null) {
            } else {
                contentName2 = result[0];
                contentType2 = result[1];
                contentTime2 = result[2];
            }

            SharedPreferences logcatStart = getSharedPreferences("sync2", 0);
            SharedPreferences.Editor logcatEditor = logcatStart.edit();
            logcatEditor.putBoolean("synchronize", false);
            logcatEditor.commit();
            completeSync2 = true;
        }
    }

    class Downloader3 extends AsyncTask<String, Void, String[][]> {
        String[][] content;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            completeSync3 = false;
        }

        @Override
        protected String[][] doInBackground(String... urls) {
            String netStat = n.isConnected(c);
            if (netStat.equals("")) {
                String[][] contentDetails3 = dbo.getContentDetails3(userCategory);

                try {
                    if(contentDetails3[0] == null || contentDetails3[1] == null){
                        SharedPreferences settings1 = getSharedPreferences("content3", 0);
                        SharedPreferences.Editor editor111 = settings1.edit();
                        editor111.putString("contentName3", "");
                        editor111.putString("contentType3", "");
                        editor111.putString("contentTime3", "");
                        editor111.commit();

                        contentNameUpdated3 = new String[]{"end"};
                        contentTypeUpdated3 = new String[]{"img"};
                        contentTimeUpdated3 = new String[]{"10"};
                    }
                    else{
                        contentNameUpdated3 = contentDetails3[0];
                        contentTypeUpdated3 = contentDetails3[1];
                        contentTimeUpdated3 = contentDetails3[2];
                    }

                    boolean fAvailable;
                    System.out.println(contentNameUpdated3[0]);
                    for (int i = 0; i < contentNameUpdated3.length; i++) {
                        fAvailable = checkFileIsAvailable(contentNameUpdated3[i]);
                        if (fAvailable) {
                        } else {
                            if (contentTypeUpdated3[i].equals("img")) {
                                new ImageDownloader(contentNameUpdated3[i]).execute();
                            } else {
                                new FileDownloader().downloadFile(contentNameUpdated3[i]);
                            }
                        }
                    }

                    SharedPreferences settings1 = getSharedPreferences("content3", 0);
                    SharedPreferences.Editor editor111 = settings1.edit();
                    editor111.putString("contentName3", "");
                    editor111.putString("contentType3", "");
                    editor111.putString("contentTime3", "");
                    editor111.commit();

                } catch (Exception e) {

                    contentNameUpdated3 = new String[]{"end"};
                    contentTypeUpdated3 = new String[]{"img"};
                    contentTimeUpdated3 = new String[]{"10"};

                    SharedPreferences settings1 = getSharedPreferences("content3", 0);
                    SharedPreferences.Editor editor111 = settings1.edit();
                    editor111.putString("contentName3", "");
                    editor111.putString("contentType3", "");
                    editor111.putString("contentTime3", "");
                    editor111.commit();
                }

                if(contentNameUpdated3[0].equalsIgnoreCase("end")){
                    content = new String[][]{contentName3, contentType3, contentTime3};
                    return content;
                }
                else{
                    content = new String[][]{contentNameUpdated3, contentTypeUpdated3, contentTimeUpdated3};
                    return content;
                }
            } else {
                content = new String[][]{contentName3, contentType3, contentTime3};
                return content;
            }
        }

        @Override
        protected void onPostExecute(String[][] result) {
            if (result == null) {
            } else {
                    contentName3 = result[0];
                    contentType3 = result[1];
                    contentTime3 = result[2];
            }

            SharedPreferences logcatStart = getSharedPreferences("sync3", 0);
            SharedPreferences.Editor logcatEditor = logcatStart.edit();
            logcatEditor.putBoolean("synchronize", false);
            logcatEditor.commit();
            completeSync3 = true;
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String fileName){
        File mypath=new File(d.getSTORAGE_PATH(),fileName);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();
        } catch (Exception e) {
            errorDialog("Compress error" + '\n' + "Error code: 0xE0542139");
        }
        return d.getSTORAGE_PATH().getAbsolutePath();
    }

    class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        String name;
        @Override
        public void onPreExecute(){
        }

        public ImageDownloader(String fileName) {
            this.name = fileName;
        }
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap bitmap;
                try {
                    InputStream in = new java.net.URL(d.getIMAGE_DOWNLOADER_PATH() + name).openStream();
                    bitmap = BitmapFactory.decodeStream(in);
                    return bitmap;
                }catch(Exception e){
                    System.out.println("error1");
                    return null;
                }
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            if(result == null){
               System.out.println("error");
            }
            else{
                saveToInternalStorage(result, name);
            }
        }
    }

    class NetChecker extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            startSync = true;
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            String currentDateandTime = sdf.format(new Date());
            String updates[] = dbo.getVersion();

            try{
                String token = settings5.getString("token", "");
                if(token.equals("")){}
                else{
                    dbo.addToken(token, deviceID);
                }
            }
            catch(Exception e){}

            try{
                String exitTime = settings.getString("exit", "");
                if(exitTime.equals("")){}
                else{
                    boolean res =  dbo.putExitTime(deviceID, exitTime);
                    if(res){
                        SharedPreferences settings = getSharedPreferences("exit", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("exit", "");
                        editor.commit();
                    }
                }
            }
            catch(Exception e){}

            if(updates == null){}
            else{
                versionCode = updates[0];
                verURL = updates[1];
            }
            return dbo.pinging(deviceID, currentDateandTime);
        }

        @Override
        protected void onPostExecute(Boolean result) {
        if(result){
            try{
                PackageManager manager = c.getPackageManager();
                PackageInfo info = manager.getPackageInfo(c.getPackageName(), 0);
                version = info.versionName;
                if(versionCode.equalsIgnoreCase("")){}
                else{
                    if(versionCode.equalsIgnoreCase(version)){
                        final LinearLayout loadingImageView111 = (LinearLayout)findViewById(R.id.updateReminder);
                        loadingImageView111.setVisibility(View.GONE);
                        updateAvailable = false;
                        loadingImageView111.clearAnimation();
                    }
                    else{
                        final LinearLayout loadingImageView111 = (LinearLayout)findViewById(R.id.updateReminder);
                        loadingImageView111.setVisibility(View.VISIBLE);
                        updateAvailable = true;
                    }
                }
            }
            catch(Exception ex){

            }

            final LinearLayout loadingImageView = (LinearLayout)findViewById(R.id.netErrorMessage);
            loadingImageView.setVisibility(View.GONE);
            if(updateAvailable){
                final LinearLayout loadingImageView111 = (LinearLayout)findViewById(R.id.updateReminder);
                loadingImageView111.setVisibility(View.VISIBLE);
            }
            loadingImageView.bringToFront();
            loadingImageView.clearAnimation();
            try{
                SharedPreferences settings2 = getSharedPreferences("offlinestart", 0);
                String startTimeOffile = settings2.getString("offlinestart", "");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                String currentDateandTime = sdf.format(new Date());
                if(startTimeOffile.equals("")){

                }
                else{
                    boolean ofUpdated = dbo.addOfflineTimeRange(String.valueOf(dealerID), userCategory, deviceID, startTimeOffile, currentDateandTime);
                    if(ofUpdated){
                        SharedPreferences settings1 = getSharedPreferences("offlinestart", 0);
                        SharedPreferences.Editor editor111 = settings1.edit();
                        editor111.putString("offlinestart", "");
                        editor111.commit();
                    }
                    else{}
                }
            }
            catch(Exception e){

            }
            netStat = true;
        }
            else{
            final LinearLayout loadingImageView = (LinearLayout)findViewById(R.id.netErrorMessage);
            loadingImageView.setVisibility(View.VISIBLE);
            final LinearLayout loadingImageView111 = (LinearLayout)findViewById(R.id.updateReminder);
            loadingImageView111.setVisibility(View.GONE);
            loadingImageView.bringToFront();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            String currentDateandTime = sdf.format(new Date());
            try{
                SharedPreferences settings2 = getSharedPreferences("offlinestart", 0);
                String startTimeOffile = settings2.getString("offlinestart", "");
                if(startTimeOffile.equalsIgnoreCase("")){
                    SharedPreferences settings1 = getSharedPreferences("offlinestart", 0);
                    SharedPreferences.Editor editor111 = settings1.edit();
                    editor111.putString("offlinestart", currentDateandTime);
                    editor111.commit();
                }
                else{

                }
            }
            catch(Exception ex){
                SharedPreferences settings1 = getSharedPreferences("offlinestart", 0);
                SharedPreferences.Editor editor111 = settings1.edit();
                editor111.putString("offlinestart", currentDateandTime);
                editor111.commit();
            }
            netStat = false;
        }
            startSync = false;
        }
    }

    class LoginChecker extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            startSync = true;
        }

        @Override
        protected Boolean doInBackground(String... urls) {
           return dbo.connectToTheServer();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                try{
                    PackageManager manager = c.getPackageManager();
                    PackageInfo info = manager.getPackageInfo(c.getPackageName(), 0);
                    version = info.versionName;
                    if(versionCode.equalsIgnoreCase("")){}
                    else{
                        if(versionCode.equalsIgnoreCase(version)){
                            final LinearLayout loadingImageView111 = (LinearLayout)findViewById(R.id.updateReminder);
                            loadingImageView111.setVisibility(View.GONE);
                            updateAvailable = false;
                            loadingImageView111.clearAnimation();
                        }
                        else{
                            final LinearLayout loadingImageView111 = (LinearLayout)findViewById(R.id.updateReminder);
                            loadingImageView111.setVisibility(View.VISIBLE);
                            updateAvailable = true;
                        }
                    }
                }
                catch(Exception ex){

                }
                final LinearLayout loadingImageView = (LinearLayout)findViewById(R.id.netErrorMessage);
                loadingImageView.setVisibility(View.GONE);
                if(updateAvailable){
                    final LinearLayout loadingImageView111 = (LinearLayout)findViewById(R.id.updateReminder);
                    loadingImageView111.setVisibility(View.VISIBLE);
                }
                loadingImageView.bringToFront();
                loadingImageView.clearAnimation();
                try{
                    SharedPreferences settings2 = getSharedPreferences("offlinestart", 0);
                    String startTimeOffile = settings2.getString("offlinestart", "");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                    String currentDateandTime = sdf.format(new Date());
                    if(startTimeOffile.equals("")){

                    }
                    else{
                        boolean ofUpdated = dbo.addOfflineTimeRange(String.valueOf(dealerID), userCategory, deviceID, startTimeOffile, currentDateandTime);
                        if(ofUpdated){
                            SharedPreferences settings1 = getSharedPreferences("offlinestart", 0);
                            SharedPreferences.Editor editor111 = settings1.edit();
                            editor111.putString("offlinestart", "");
                            editor111.commit();
                        }
                        else{}
                    }
                }
                catch(Exception e){

                }
                netStat = true;
            }
            else{
                final LinearLayout loadingImageView = (LinearLayout)findViewById(R.id.netErrorMessage);
                loadingImageView.setVisibility(View.VISIBLE);
                final LinearLayout loadingImageView111 = (LinearLayout)findViewById(R.id.updateReminder);
                loadingImageView111.setVisibility(View.GONE);
                loadingImageView.bringToFront();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                String currentDateandTime = sdf.format(new Date());
                try{
                    SharedPreferences settings2 = getSharedPreferences("offlinestart", 0);
                    String startTimeOffile = settings2.getString("offlinestart", "");

                    if(startTimeOffile.equalsIgnoreCase("")){
                        SharedPreferences settings1 = getSharedPreferences("offlinestart", 0);
                        SharedPreferences.Editor editor111 = settings1.edit();
                        editor111.putString("offlinestart", currentDateandTime);
                        editor111.commit();
                    }
                    else{

                    }
                }
                catch(Exception ex){
                    SharedPreferences settings1 = getSharedPreferences("offlinestart", 0);
                    SharedPreferences.Editor editor111 = settings1.edit();
                    editor111.putString("offlinestart", currentDateandTime);
                    editor111.commit();
                }
                netStat = false;
            }
            startSync = false;
        }
    }

    public void errorDialog(String msg){
        final Dialog searchingCategoryDialogView1 = new Dialog(getApplicationContext(), android.R.style.Theme_Translucent_NoTitleBar);
        searchingCategoryDialogView1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        searchingCategoryDialogView1.setContentView(R.layout.error);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(searchingCategoryDialogView1.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        searchingCategoryDialogView1.getWindow().setAttributes(lp);

        Button OK = (Button) searchingCategoryDialogView1.findViewById(R.id.btn);
        TextView txt = (TextView)searchingCategoryDialogView1.findViewById(R.id.errorMSG);
        txt.setText(msg);
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchingCategoryDialogView1.dismiss();
            }
        });
        //searchingCategoryDialogView1 = new AlertDialog.Builder(Login.this).create();
        searchingCategoryDialogView1.setCancelable(false);
        searchingCategoryDialogView1.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });

        searchingCategoryDialogView1.show();
    }

    public void netDown(String msg){
        final LinearLayout loadingImageView = (LinearLayout)findViewById(R.id.netErrorMessage);
        loadingImageView.setVisibility(View.VISIBLE);
        final LinearLayout loadingImageView111 = (LinearLayout)findViewById(R.id.updateReminder);
        loadingImageView111.setVisibility(View.GONE);

        TextView netMSG = (TextView)findViewById(R.id.netdownmsg);
        loadingImageView.bringToFront();
        netMSG.setText(msg);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String currentDateandTime = sdf.format(new Date());
        try{
            SharedPreferences settings2 = getSharedPreferences("offlinestart", 0);
            String startTimeOffile = settings2.getString("offlinestart", "");
            if(startTimeOffile.equalsIgnoreCase("")){
                SharedPreferences settings1 = getSharedPreferences("offlinestart", 0);
                SharedPreferences.Editor editor111 = settings1.edit();
                editor111.putString("offlinestart", currentDateandTime);
                editor111.commit();
            }
            else{

            }
        }
        catch(Exception ex){
            SharedPreferences settings1 = getSharedPreferences("offlinestart", 0);
            SharedPreferences.Editor editor111 = settings1.edit();
            editor111.putString("offlinestart", currentDateandTime);
            editor111.commit();
        }
        netStat = false;
    }
}