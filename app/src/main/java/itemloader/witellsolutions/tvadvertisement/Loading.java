package itemloader.witellsolutions.tvadvertisement;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Sumudu on 3/30/2016.
 */
public class Loading extends Activity {
    ProgressBar pBar;
    String[] newsArray = null;
    Data d = new Data();
    String newsText = "";
    int count=0;
    Button logout;
    int dealerID;
    NetworkStatChecker n = new NetworkStatChecker();
    Context context;
    DBOperations dbo = new DBOperations();
    FileDeleter fileDeleter = new FileDeleter();
    ImageView rotatingImageView;
    String currentDateandTime;
    LinearLayout rotate;
    String userCategory = "";
    Animation anim;
    String offlineStartTime;
    String deviceID;
    String exitTime;
    SharedPreferences settings5;
    TextView pStatText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        pBar = (ProgressBar)findViewById(R.id.progressBar);
        pStatText = (TextView)findViewById(R.id.progressText);
        pBar.setMax(100);
        pBar.setScaleY(4f);
        int color = Color.parseColor("#ff0000");
        pBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        pBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        logout = (Button)findViewById(R.id.outButton);
        rotate = (LinearLayout)findViewById(R.id.rotate);
        rotate.setVisibility(View.VISIBLE);

        context = this;
        rotatingImageView = (ImageView)findViewById(R.id.loadingRotateImageView);
        anim = AnimationUtils.loadAnimation(context, R.anim.infiniterotate);
        rotatingImageView.startAnimation(anim);
        SharedPreferences settings = getSharedPreferences("exit", 0);
        SharedPreferences settings4 = getSharedPreferences("offlinestart", 0);
        SharedPreferences settings1 = getSharedPreferences("dev", 0);
        SharedPreferences settings2 = getSharedPreferences("dealerID", 0);
        SharedPreferences settings3 = getSharedPreferences("usertype", 0);
        settings5 = getSharedPreferences("sync", 0);
        try{
            exitTime = settings.getString("exit", "");
            deviceID = settings1.getString("dev", "");
            dealerID = Integer.parseInt(settings2.getString("dealerID", "").trim());
            userCategory = settings3.getString("usertype", "");
            offlineStartTime = settings4.getString("offlinestart", "");
        }
        catch(Exception e){
            exitTime = "";
            offlineStartTime = "";
        }

        boolean dirCheck = checkDirectoryAvailable();
        if(dirCheck){
            new DBSynchronization1(userCategory).execute();
           // new DBSynchronization2(userCategory).execute();
            //new DBSynchronization3(userCategory).execute();
        }
        else{
            createDirectory();
            new DBSynchronization1(userCategory).execute();
           // new DBSynchronization2(userCategory).execute();
           // new DBSynchronization3(userCategory).execute();
        }
    }

    private void createDirectory(){
        if(!d.getSTORAGE_PATH().exists()) {
            d.getSTORAGE_PATH().mkdirs();
        }
    }

    class DBSynchronization1 extends AsyncTask<String, Void, String[][]> {
        String userCategory, errorCode = "";
        String[] contentName1, contentType1, contentTime1;
        String contentName1AsString = "", contentType1AsString = "", contentTime1AsString = "";

        public DBSynchronization1(String userCategory) {
            this.userCategory = userCategory;
        }

        @Override
        protected void onPreExecute() {
            pBar.setProgress(20);
        }

        @Override
        protected String[][] doInBackground(String... urls) {
            String connectionStat = n.isConnected(context);
            if (connectionStat.equals("")) {
                String[][] contentDetails1 = dbo.getContentDetails1(userCategory);
                newsArray = dbo.getNews();
                String token = settings5.getString("token", "");
                if(token.equals("")){}
                else{
                    dbo.addToken(token, deviceID);
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                currentDateandTime = sdf.format(new Date());
                dbo.pinging(deviceID, currentDateandTime);

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

                if(offlineStartTime.equals("")){}

                else{
                    boolean ofUpdated = dbo.addOfflineTimeRange(String.valueOf(dealerID), userCategory, deviceID, offlineStartTime, currentDateandTime);
                    if(ofUpdated){
                        SharedPreferences settings1 = getSharedPreferences("offlinestart", 0);
                        SharedPreferences.Editor editor111 = settings1.edit();
                        editor111.putString("offlinestart", "");
                        editor111.commit();
                    }
                    else{}
                }
                    return contentDetails1;
            } else {
                errorCode = connectionStat;
                SharedPreferences settings1 = getSharedPreferences("content1", 0);
                String contentName1String = settings1.getString("contentName1", "");
                String contentType1String = settings1.getString("contentType1", "");
                String contentTime1String = settings1.getString("contentTime1", "");
                String newsDataString = settings1.getString("news", "");
                newsArray = newsDataString.split("  ●  ");

                if(contentName1String.equals("")){
                    contentName1 = new String[]{"end"};
                    contentType1 = new String[]{"img"};
                    contentTime1 = new String[]{"10"};
                }
                else{
                    contentName1 = contentName1String.split("#");
                    contentType1 = contentType1String.split("#");
                    contentTime1 = contentTime1String.split("#");
                }
                return new String[][]{contentName1, contentType1, contentTime1};
            }
        }
        @Override
        protected void onPostExecute(String[][] result) {
            if (result == null) {
                pBar.setProgress(30);
                pStatText.setText("Getting data...");
                try {
                    contentName1 = null;
                    contentType1 = null;
                    contentTime1 = null;
                    pBar.setProgress(40);

                    SharedPreferences settings1 = getSharedPreferences("content1", 0);
                    SharedPreferences.Editor editor111 = settings1.edit();
                    editor111.putString("contentName1", "end");
                    editor111.putString("contentType1", "img");
                    editor111.putString("contentTime1", "10");
                    editor111.commit();

                    count += 1;
                    check();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "No data in the database" + '\n' + "Error code: " + errorCode, Toast.LENGTH_LONG).show();
                }
            } else {
                pBar.setProgress(30);
                pStatText.setText("Getting data...");
                try {
                    contentName1 = result[0];
                    contentType1 = result[1];
                    contentTime1 = result[2];
                    boolean fAvailable;
                    pBar.setProgress(40);

                    if (contentName1[0].equals("end")){}
                    else {
                        for (int i = 0; i < contentType1.length; i++) {
                            fAvailable = checkFileIsAvailable(contentName1[i]);
                            System.out.println(contentName1[i]);
                            contentName1AsString += contentName1[i] + "#";
                            contentType1AsString += contentType1[i] + "#";
                            contentTime1AsString += contentTime1[i] + "#";
                            if (fAvailable) {
                            } else {
                                if (contentType1[i].equals("img")) {
                                    new ImageDownloader(contentName1[i]).execute();
                                } else {
                                    new FileDownloader().downloadFile(contentName1[i]);
                                }
                            }
                        }
                    }
                    SharedPreferences settings1 = getSharedPreferences("content1", 0);
                    SharedPreferences.Editor editor111 = settings1.edit();
                    editor111.putString("contentName1", contentName1AsString);
                    editor111.putString("contentType1", contentType1AsString);
                    editor111.putString("contentTime1", contentTime1AsString);
                    editor111.commit();

                    count += 1;
                    check();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "No data in the database" + '\n' + "Error code: " + errorCode, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

  /*  class DBSynchronization2 extends AsyncTask<String, Void, String[][]> {
        String userCategory, errorCode = "";
        String[] contentName2, contentType2, contentTime2;
        String contentName2AsString = "", contentType2AsString = "", contentTime2AsString = "";

        public DBSynchronization2(String userCategory) {
            this.userCategory = userCategory;
        }

        @Override
        protected void onPreExecute() {
            pBar.setProgress(20);
        }

        @Override
        protected String[][] doInBackground(String... urls) {
            String connectionStat = n.isConnected(context);
            if (connectionStat.equals("")) {
                String[][] contentDetails2 = dbo.getContentDetails2(userCategory);
                return contentDetails2;
            } else {
                errorCode = connectionStat;
                SharedPreferences settings1 = getSharedPreferences("content2", 0);

                String contentName2String = settings1.getString("contentName2", "");
                String contentType2String = settings1.getString("contentType2", "");
                String contentTime2String = settings1.getString("contentTime2", "");

                if(contentName2String.equals("")){
                    contentName2 = new String[]{"end"};
                    contentType2 = new String[]{"img"};
                    contentTime2 = new String[]{"10"};
                }
                else{
                    contentName2 = contentName2String.split("#");
                    contentType2 = contentType2String.split("#");
                    contentTime2 = contentTime2String.split("#");
                }
                return new String[][]{contentName2, contentType2, contentTime2};
            }
        }
        @Override
        protected void onPostExecute(String[][] result) {
            if (result == null) {
                pBar.setProgress(30);
                pStatText.setText("Getting data...");
                try {
                    contentName2 = null;
                    contentType2 = null;
                    contentTime2 = null;
                    pBar.setProgress(40);

                    SharedPreferences settings1 = getSharedPreferences("content2", 0);
                    SharedPreferences.Editor editor111 = settings1.edit();
                    editor111.putString("contentName2", "end");
                    editor111.putString("contentType2", "img");
                    editor111.putString("contentTime2", "10");
                    editor111.commit();

                    count += 1;
                    check();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "No data in the database" + '\n' + "Error code: " + errorCode, Toast.LENGTH_LONG).show();
                }
            } else {
                pBar.setProgress(30);
                pStatText.setText("Getting data...");
                try {
                    contentName2 = result[0];
                    contentType2 = result[1];
                    contentTime2 = result[2];
                    boolean fAvailable;
                    pBar.setProgress(40);

                    if (contentName2[0].equals("end")){}
                    else {
                        for (int i = 0; i < contentType2.length; i++) {
                            fAvailable = checkFileIsAvailable(contentName2[i]);
                            System.out.println(contentName2[i]);
                            contentName2AsString += contentName2[i] + "#";
                            contentType2AsString += contentType2[i] + "#";
                            contentTime2AsString += contentTime2[i] + "#";
                            if (fAvailable) {
                            } else {
                                if (contentType2[i].equals("img")) {
                                    new ImageDownloader(contentName2[i]).execute();
                                } else {
                                    new FileDownloader().downloadFile(contentName2[i]);
                                }
                            }
                        }
                    }
                    SharedPreferences settings1 = getSharedPreferences("content2", 0);
                    SharedPreferences.Editor editor111 = settings1.edit();
                    editor111.putString("contentName2", contentName2AsString);
                    editor111.putString("contentType2", contentType2AsString);
                    editor111.putString("contentTime2", contentTime2AsString);
                    editor111.commit();

                    count += 1;
                    check();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "No data in the database" + '\n' + "Error code: " + errorCode, Toast.LENGTH_LONG).show();
                }
            }
        }
    }*/

    /*class DBSynchronization3 extends AsyncTask<String, Void, String[][]> {
        String userCategory, errorCode = "";
        String[] contentName3, contentType3, contentTime3;
        String contentName3AsString = "", contentType3AsString = "", contentTime3AsString = "";

        public DBSynchronization3(String userCategory) {
            this.userCategory = userCategory;
        }

        @Override
        protected void onPreExecute() {
            pBar.setProgress(20);
        }

        @Override
        protected String[][] doInBackground(String... urls) {
            String connectionStat = n.isConnected(context);
            if (connectionStat.equals("")) {
                String[][] contentDetails3 = dbo.getContentDetails3(userCategory);
                return contentDetails3;
            } else {
                errorCode = connectionStat;
                SharedPreferences settings1 = getSharedPreferences("content3", 0);
                String contentName3String = settings1.getString("contentName3", "");
                String contentType3String = settings1.getString("contentType3", "");
                String contentTime3String = settings1.getString("contentTime3", "");

                if(contentName3String.equals("")){
                    contentName3 = new String[]{"end"};
                    contentType3 = new String[]{"img"};
                    contentTime3 = new String[]{"10"};
                }
                else{
                    contentName3 = contentName3String.split("#");
                    contentType3 = contentType3String.split("#");
                    contentTime3 = contentTime3String.split("#");
                }
                return new String[][]{contentName3, contentType3, contentTime3};
            }
        }
        @Override
        protected void onPostExecute(String[][] result) {
            if (result == null) {
                pBar.setProgress(30);
                pStatText.setText("Getting data...");
                try {
                    contentName3 = null;
                    contentType3 = null;
                    contentTime3 = null;
                    pBar.setProgress(40);

                    SharedPreferences settings1 = getSharedPreferences("content3", 0);
                    SharedPreferences.Editor editor111 = settings1.edit();
                    editor111.putString("contentName3", "end");
                    editor111.putString("contentType3", "img");
                    editor111.putString("contentTime3", "10");
                    editor111.commit();

                    count += 1;
                    check();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "No data in the database" + '\n' + "Error code: " + errorCode, Toast.LENGTH_LONG).show();
                }
            } else {
                pBar.setProgress(30);
                pStatText.setText("Getting data...");
                try {
                    contentName3 = result[0];
                    contentType3 = result[1];
                    contentTime3 = result[2];
                    boolean fAvailable;
                    pBar.setProgress(40);

                    if (contentName3[0].equals("end")){}
                    else {
                        for (int i = 0; i < contentType3.length; i++) {
                            fAvailable = checkFileIsAvailable(contentName3[i]);
                            System.out.println(contentName3[i]);
                            contentName3AsString += contentName3[i] + "#";
                            contentType3AsString += contentType3[i] + "#";
                            contentTime3AsString += contentTime3[i] + "#";
                            if (fAvailable) {
                            } else {
                                if (contentType3[i].equals("img")) {
                                    new ImageDownloader(contentName3[i]).execute();
                                } else {
                                    new FileDownloader().downloadFile(contentName3[i]);
                                }
                            }
                        }
                    }
                    SharedPreferences settings1 = getSharedPreferences("content3", 0);
                    SharedPreferences.Editor editor111 = settings1.edit();
                    editor111.putString("contentName3", contentName3AsString);
                    editor111.putString("contentType3", contentType3AsString);
                    editor111.putString("contentTime3", contentTime3AsString);
                    editor111.commit();

                    count += 1;
                    check();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "No data in the database" + '\n' + "Error code: " + errorCode, Toast.LENGTH_LONG).show();
                }
            }
        }
    }*/


    public void errorDialog(String msg){
        final Dialog searchingCategoryDialogView2 = new Dialog(Loading.this, android.R.style.Theme_Translucent_NoTitleBar);
        searchingCategoryDialogView2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        searchingCategoryDialogView2.setContentView(R.layout.error);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(searchingCategoryDialogView2.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        searchingCategoryDialogView2.getWindow().setAttributes(lp);
        Button OK = (Button) searchingCategoryDialogView2.findViewById(R.id.btn);
        TextView mmm = (TextView)searchingCategoryDialogView2.findViewById(R.id.errorMSG);
        mmm.setText(msg);
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchingCategoryDialogView2.dismiss();
            }
        });
        searchingCategoryDialogView2.setCancelable(false);
        searchingCategoryDialogView2.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
        searchingCategoryDialogView2.show();
    }

    public void check(){
        if(count == 1 ){
            new MahClass().execute();
          //  new FileDownloader().downloadFile("testvideo");
        }
    }

    public void out(View v){
        deleteLog();
        Intent i = new Intent(getApplicationContext() ,Login.class);
        i.putExtra("stat", true);
        startActivity(i);
        finish();
    }
    public void deleteLog(){
        SharedPreferences settings = getApplicationContext().getSharedPreferences("usertype", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("usertype", "");
        editor.commit();
    }

    private boolean checkDirectoryAvailable(){
        if(!d.getSTORAGE_PATH().exists()){
            return false;
        }
        else return true;
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

    public class MahClass extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            String[] contentName1, contentName2, contentName3;
            try{
                SharedPreferences settings1 = getSharedPreferences("content1", 0);
                String contentName1String = settings1.getString("contentName1", "");

                SharedPreferences settings2 = getSharedPreferences("content2", 0);
                String contentName2String = settings2.getString("contentName2", "");

                SharedPreferences settings3 = getSharedPreferences("content3", 0);
                String contentName3String = settings3.getString("contentName3", "");

                contentName1 = contentName1String.split("#");
                contentName2 = contentName2String.split("#");
                contentName3 = contentName3String.split("#");

                ArrayList<String> n = fileDeleter.listAllFiles();
                if(contentName1 != null){
                    FIRST_LOOP: for(int i=0; i < contentName1.length; i++){
                        String name = contentName1[i];
                        SECOND_LOOP: for(int p = 0; p < n.size(); p++){
                            if(n.size() == 0){}
                            else {
                                if (name.equals(n.get(p))) {
                                    n.remove(p);
                                    break SECOND_LOOP;
                                }
                            }
                        }
                    }
                }

                if(contentName3 != null){
                    FIRST_LOOP: for(int i=0; i < contentName3.length; i++){
                        String name = contentName3[i];
                        SECOND_LOOP: for(int p = 0; p < n.size(); p++){
                            if(n.size() == 0){}
                            else {
                                if (name.equals(n.get(p))) {
                                    n.remove(p);
                                    break SECOND_LOOP;
                                }
                            }
                        }
                    }
                }

                if(contentName2 != null){
                    FIRST_LOOP: for(int i=0; i < contentName2.length; i++){
                        String name = contentName2[i];
                        SECOND_LOOP: for(int p = 0; p < n.size(); p++){
                            if(n.size() == 0){}
                            else {
                                if (name.equals(n.get(p))) {
                                    n.remove(p);
                                    break SECOND_LOOP;
                                }
                            }
                        }
                    }
                }

                if(newsArray.length>0){
                    for(int newsSizeCheck = 0; newsSizeCheck<newsArray.length; newsSizeCheck++){
                        if(newsSizeCheck == newsArray.length - 1){
                            newsText += newsArray[newsSizeCheck];
                        }
                        else{
                            newsText += "  ●  " + newsArray[newsSizeCheck];
                        }
                    }
                }
                else{
                    newsText = "";
                }


                SharedPreferences settings4 = getSharedPreferences("content1", 0);
                SharedPreferences.Editor editor111 = settings4.edit();
                editor111.putString("news", newsText);
                editor111.commit();

                if(n.size() == 0){}

                else{
                    for(int check = 0; check<n.size(); check++){
                        fileDeleter.deleteFile(n.get(check));
                    }
                }

                while (pBar.getProgress() < 100) {
                    publishProgress();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            catch(Exception e){

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            pBar.incrementProgressBy(1);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentapiVersion >= Build.VERSION_CODES.KITKAT){
                MediaScannerConnection.scanFile(context, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
            } else{
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse("file://" +  Environment.getExternalStorageDirectory())));
            }
            rotate.setVisibility(View.GONE);
            rotatingImageView.clearAnimation();

            pStatText.setText("Successfully loaded");
                Intent ii = new Intent(Loading.this, ImagePlayer.class);
                ii.putExtra("dealerID", dealerID);
                ii.putExtra("devID", deviceID);
                ii.putExtra("userCategory", userCategory);
                startActivity(ii);
                finish();
        }
    }

    class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        String name;

        @Override
        public void onPreExecute() {
        }

        public ImageDownloader(String fileName) {
            this.name = fileName;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap bitmap;
            String connectionStat = n.isConnected(context);
            if (connectionStat.equals("")) {
                try {
                    InputStream in = new java.net.URL(d.getIMAGE_DOWNLOADER_PATH() + name).openStream();
                    bitmap = BitmapFactory.decodeStream(in);
                    return bitmap;

                } catch (Exception e) {
                    return null;
                }
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result == null) {
                Toast.makeText(context, "Unable to download the image" + '\n' + "Error code: 0xE0884731", Toast.LENGTH_LONG).show();
            } else {
                saveToInternalStorage(result, name);
            }
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
            Toast.makeText(context, "Compress error" + '\n' + "Error code: 0xE0542139", Toast.LENGTH_LONG).show();
        }
        return d.getSTORAGE_PATH().getAbsolutePath();
    }
}
