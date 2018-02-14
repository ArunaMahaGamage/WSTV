package itemloader.witellsolutions.tvadvertisement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.DigitalClock;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Sumudu on 3/30/2016.
 */
public class VideoPlayer extends AppCompatActivity {
    ViewPager viewPagerTop, viewPagerBottom;
    Timer timer, timer1;
    VideoView videoView;
    int Measuredwidth;
    String deviceID;
    int Measuredheight;
    int dealerID;
    static int netTimer = 0;
    TextView news;
    int videoNumber;
    ImageView tvBadge;
    static int dbCheckCount = 0;
    DBOperations dbo = new DBOperations();
    Context c;
     FragmentPageAdapter2 ft2;
    FragmentPageAdapter3 ft3;
    String[] newsArray = null;
    int imgIndex1 = 0, imgIndex2 = 0, imgIndex3 = 0, count1 = 0, count2 = 0, count3 = 0;
    String newsTextString;

    String userCategory = "";
    DigitalClock clock;
    boolean netStat;
    TextView chagre;
    LinearLayout newsBar;
    int newsBartextSize;
    NetworkStatChecker n = new NetworkStatChecker();

    String[] contentArray1 = null, contentArray2 = null, contentArray3 = null;
    String[] contentTypeArray1 = null, contentTypeArray2 = null, contentTypeArray3 = null;
    String[] transitTime1 = null, transitTime2 = null, transitTime3 = null;

    String deviceName;
    Data d = new Data();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Point size = new Point();
        WindowManager w = getWindowManager();
        newsBar = (LinearLayout)findViewById(R.id.newsBar);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            w.getDefaultDisplay().getSize(size);
            newsBartextSize = newsBar.getHeight();
            Measuredwidth = size.x;
            Measuredheight = size.y;
        }else{
            Display d = w.getDefaultDisplay();
            Measuredwidth = d.getWidth();
            Measuredheight = d.getHeight();
        }

        viewPagerTop = (ViewPager)findViewById(R.id.sideImageTopPlayer);
        tvBadge = (ImageView)findViewById(R.id.tvbadge);
        deviceName = android.os.Build.MANUFACTURER + " " + android.os.Build.PRODUCT;
        tvBadge.setLayoutParams(new RelativeLayout.LayoutParams((Measuredwidth/6), (Measuredheight/6)));
        viewPagerBottom = (ViewPager)findViewById(R.id.sideImageBottomPlayer);
        videoView = (VideoView)findViewById(R.id.videoPlayer);
        news = (TextView)findViewById(R.id.newsText);
        news.setFocusable(true);
        news.setTextSize((float) (Measuredheight/40));
        final TextView loadingImageView = (TextView)findViewById(R.id.netErrorMessage);
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

        loadingImageView.setVisibility(View.GONE);
        c = this;
        Intent i = getIntent();
        if(i != null){
            dealerID = i.getIntExtra("uid", 0);

            contentArray1 = i.getStringArrayExtra("content1");
             contentArray2 = i.getStringArrayExtra("content2");
             contentArray3 = i.getStringArrayExtra("content3");

            transitTime1 = i.getStringArrayExtra("transit1");
            transitTime2 = i.getStringArrayExtra("transit2");
            transitTime3 = i.getStringArrayExtra("transit3");

            contentTypeArray1 = i.getStringArrayExtra("contenttypearray1");
            contentTypeArray2 = i.getStringArrayExtra("contenttypearray2");
              contentTypeArray3 = i.getStringArrayExtra("contenttypearray3");

            newsTextString = i.getStringExtra("newstext");
            newsArray = i.getStringArrayExtra("newsArray");
            imgIndex1 = i.getIntExtra("index1", 0);
            deviceID = i.getStringExtra("devID");
            userCategory = i.getStringExtra("userCategory");
              imgIndex2 = i.getIntExtra("index2", 0);
             imgIndex3 = i.getIntExtra("index3", 0);
        }

        news.setText(newsTextString);
        news.setSelected(true);

        if(contentArray1 == null){
        }
        else{
            videoView.setVideoPath(d.getSTORAGE_PATH() + contentArray1[0]);
            videoView.start();
            videoNumber = 0;
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    if(videoNumber == (contentArray1.length - 1)){
                        videoView.setVideoPath(d.getSTORAGE_PATH() + contentArray1[0]);
                        videoView.start();
                        videoNumber = 0;
                    }
                    else{
                        videoNumber += 1;
                        videoView.setVideoPath(d.getSTORAGE_PATH() + contentArray1[videoNumber]);
                        videoView.start();
                    }

                }
            });

           /* ft2 = new FragmentPageAdapter2(getSupportFragmentManager(),contentArray2, contentTypeArray2, viewPagerTop);
            ft2.notifyDataSetChanged();
            viewPagerTop.setAdapter(ft2);
            viewPagerTop.setOnTouchListener(null);
            viewPagerTop.setCurrentItem(imgIndex2, true);*/

           /* ft3 = new FragmentPageAdapter3(getSupportFragmentManager(),contentArray3, contentTypeArray3, viewPagerBottom);
            ft3.notifyDataSetChanged();
            viewPagerBottom.setAdapter(ft3);
            viewPagerBottom.setOnTouchListener(null);
            viewPagerBottom.setCurrentItem(imgIndex3, true);*/

            // int[] lengthOfArrays = new int[]{contentArray1.length, contentArray2.length, contentArray3.length};
            dbCheckCount = contentArray1.length;

            System.out.println(dbCheckCount);
            final Handler handler = new Handler();
            timer = new Timer();
            TimerTask doAsynchronousTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            if(netTimer == 6){
                                new NetChecker().execute();
                                netTimer = 0;
                            }
                            else{
                                netTimer++;
                            }
                           /* if(imgIndex1 == 0){
                                ft1 = new FragmentPageAdapter1(getSupportFragmentManager(),contentArray1, contentTypeArray1, viewPagerBig);
                                ft1.notifyDataSetChanged();
                                viewPagerBig.setAdapter(ft1);
                                viewPagerBig.setOnTouchListener(null);
                                viewPagerBig.setCurrentItem(imgIndex1, true);
                            }*/
                            if(imgIndex2 == 0){
                                /*ft2 = new FragmentPageAdapter2(getSupportFragmentManager(),contentArray2, contentTypeArray2, viewPagerTop);
                                ft2.notifyDataSetChanged();
                                viewPagerTop.setAdapter(ft2);
                                viewPagerTop.setOnTouchListener(null);
                                viewPagerTop.setCurrentItem(imgIndex2, true);*/
                            }

                            if(imgIndex3 == 0){
                                /*ft3 = new FragmentPageAdapter3(getSupportFragmentManager(),contentArray3, contentTypeArray3, viewPagerBottom);
                                ft3.notifyDataSetChanged();
                                viewPagerBottom.setAdapter(ft3);
                                viewPagerBottom.setOnTouchListener(null);
                                viewPagerBottom.setCurrentItem(imgIndex3, true);*/
                            }

                            if(imgIndex2 == 1){
                                if(netStat){
                                    new Downloader().execute();
                                }
                            }

                           /* if(imgIndex1 == (dbCheckCount-1)){
                                viewPagerBig.setCurrentItem(imgIndex1, true);
                                viewPagerBig.setOnTouchListener(null);
                                imgIndex1 = 0;
                            }
                            else{
                                viewPagerBig.setCurrentItem(imgIndex1, true);
                                viewPagerBig.setOnTouchListener(null);
                                imgIndex1++;
                            }*/

                            if(imgIndex2 == (contentArray2.length-1)){
                                viewPagerTop.setCurrentItem(imgIndex2, true);
                                viewPagerTop.setOnTouchListener(null);
                                imgIndex2 = 0;
                            }
                            else{
                                viewPagerTop.setCurrentItem(imgIndex2, true);
                                viewPagerTop.setOnTouchListener(null);
                                imgIndex2++;
                            }

                            if(imgIndex3 == (contentArray3.length-1)){
                                viewPagerBottom.setCurrentItem(imgIndex3, true);
                                viewPagerBottom.setOnTouchListener(null);
                                imgIndex3 = 0;
                                count3 = 0;
                            }
                            else{
                                viewPagerBottom.setCurrentItem(imgIndex3, true);
                                viewPagerBottom.setOnTouchListener(null);
                                imgIndex3++;
                            }
                            count1 ++;
                        }
                    });
                }
            };
            timer.schedule(doAsynchronousTask, 0, 1000);

            final Handler handler1 = new Handler();
            timer1 = new Timer();
            TimerTask doAsynchronousTask1 = new TimerTask() {
                @Override
                public void run() {
                    handler1.post(new Runnable() {
                        public void run() {
                            news.setText(newsTextString);
                        }
                    });
                }
            };
            timer1.schedule(doAsynchronousTask1, 60000, 1800000); //1000*60*60*4 = 4 hours
            //1000*60*30
        }
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

    class Downloader extends AsyncTask<String, Void, String[][]> {
        String[][] content;

        @Override
        protected String[][] doInBackground(String... urls) {
            String netStat = n.isConnected(c);
            if (netStat.equals("")) {
                try {
                    ContentDownloader contentDownloader = new ContentDownloader();
                    content = contentDownloader.ContentDownloader(c, userCategory, contentArray1, contentTypeArray1, newsArray, true);
                } catch (Exception e) {
                    content = null;
                }
                return content;
            } else {
                return content;
            }
        }

        @Override
        protected void onPostExecute(String[][] result) {
            String newsText = "";
            if (result == null) {
            } else {
                newsArray = result[2];
                boolean fAvailable;
                String[] cc = result[0];
                int counting = 1;
                for (int i = 0; i < result[0].length; i++) {
                    fAvailable = checkFileIsAvailable(cc[i]);
                    if (fAvailable) {
                        counting++;
                    }
                }
                if (counting == cc.length) {
                    if (imgIndex1 <= 1) {
                        contentArray1 = result[0];
                        contentTypeArray1 = result[1];
                        dbCheckCount = contentArray1.length;
                    }


                    for (int k = 0; k < contentArray1.length; k++) {
                        System.out.print("Array " + contentArray1[k]);
                    }
                }


             /*   contentArray2 = result[2];
                contentTypeArray2 = result[3];
                contentArray3 = result[4];
                contentTypeArray3 = result[5];*/


                if (newsArray.length > 0) {
                    for (int newsSizeCheck = 0; newsSizeCheck < newsArray.length; newsSizeCheck++) {
                        System.out.println(newsArray[newsSizeCheck]);
                        if (newsSizeCheck == newsArray.length - 1) {
                            newsText += newsArray[newsSizeCheck];
                        } else {
                            newsText += newsArray[newsSizeCheck] + "  ●  ";
                        }
                    }
                } else {
                    newsText = "";
                }

                if (newsTextString.equals(newsText)) {

                } else {
                    newsTextString = newsText;
                }
            }
        }
    }

    public void logout(View v){
        try{
            SharedPreferences settings = getSharedPreferences("logger", 0);
            String t= settings.getString("logger", "");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            String currentDateandTime = sdf.format(new Date());

            boolean result = dbo.putLogOutTime(deviceID, String.valueOf(dealerID), t, currentDateandTime);
            if(result){
                timer.cancel();
                deleteLog();
                Intent i = new Intent(c ,Login.class);
                i.putExtra("stat", true);
                startActivity(i);
                finish();
            }
            else{
                String netStat = n.isConnected(c);
                if(netStat.equals("0xE0417527")){
                    Toast.makeText(c, "Please check your internet connection" + '\n' + "Error code: " + netStat, Toast.LENGTH_LONG).show();
                }
                else if(netStat.equals("0xE0225792")){
                    Toast.makeText(c, "Server is not responding" + '\n' + "Error code: "  +netStat, Toast.LENGTH_LONG).show();
                }

            }
        }
        catch(Exception ex){

        }
    }
    public void deleteLog(){
        SharedPreferences settings = c.getSharedPreferences("usertype", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("usertype", "");
        editor.commit();
    }

    class NetChecker extends AsyncTask<String, Void, Boolean> {
        private String mc;

        @Override
        protected void onPreExecute() {
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            mc = telephonyManager.getDeviceId();
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            String currentDateandTime = sdf.format(new Date());
            //return dbo.pinging("2", mc, currentDateandTime);

            return dbo.pinging(mc, currentDateandTime);
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                final TextView loadingImageView = (TextView)findViewById(R.id.netErrorMessage);
                loadingImageView.setVisibility(View.GONE);
                loadingImageView.clearAnimation();
                try{
                    String t = new CheckTextFile().texting(c, "offlineStart");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                    String currentDateandTime = sdf.format(new Date());
                    if(t.equals("") || mc.equals("") || deviceName.equals("")){

                    }
                    else{
                        SharedPreferences settings2 = getSharedPreferences("offlinestart", 0);
                        String startTimeOffile = settings2.getString("offlinestart", "");

                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                        String currentDateandTime1 = sdf1.format(new Date());
                        String zone = TimeZone.getDefault().getDisplayName();
                        //System.out.println("cat " + userCategory + " mac: " + mc + " start: " + t + " end: " + currentDateandTime + " device: " + deviceName);
                        boolean ofUpdated = dbo.addOfflineTimeRange(String.valueOf(dealerID), userCategory, deviceID, startTimeOffile, currentDateandTime);
                        if(ofUpdated){
                            new FileDeleter().deleteFile("offlineStart.txt");
                        }
                        else{}
                    }
                }
                catch(Exception e){

                }
                netStat = true;
            }
            else{
                final TextView loadingImageView = (TextView)findViewById(R.id.netErrorMessage);
                loadingImageView.setVisibility(View.VISIBLE);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                String zone = TimeZone.getDefault().getDisplayName();
                String currentDateandTime = sdf.format(new Date());
                try{
                    String t = new CheckTextFile().texting(c, "offlineStart");
                    if(t.equals("")){
                        writer(currentDateandTime, "offlineStart");
                    }
                    else{
                    }
                }
                catch(Exception e){
                    writer(currentDateandTime, "offlineStart");
                }
                netStat = false;
            }
        }
    }

    public void writer(String content, String fileName) {
        try {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(d.getSTORAGE_PATH(), fileName + ".txt")));
                bufferedWriter.write(content);
                bufferedWriter.flush();
                bufferedWriter.close();
            } catch (Exception e) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
