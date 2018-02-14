package itemloader.witellsolutions.tvadvertisement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Sumudu on 4/4/2016.
 */
public class ContentDownloader {
    String[] newsArray = null;
    String[] contentName1 = null;
    String[] contentType1 = null;

    static String[] contentNameUpdated1 = null;
    static String[] contentTypeUpdated1 = null;
    Context context;
    NetworkStatChecker n = new NetworkStatChecker();
    DBOperations dbo = new DBOperations();
    FileDeleter fileDeleter = new FileDeleter();
    String newsText = "";
    static int count=0;
    Data d = new Data();
    boolean netStat;
    String userCategory = "";

    public String[][] ContentDownloader(Context context, String userCategory, String[] contentName1, String[] contentType1, String[] newsArray, boolean netStat){
        this.context = context;
        this.userCategory = userCategory;
        this.contentName1 = contentName1;
        this.contentType1 = contentType1;
        this.newsArray = newsArray;
        this.netStat = netStat;
        String[][] content;

            String[][] contentDetails1 = dbo.getContentDetails1(userCategory);
            // String[][] contentDetails2 = dbo.getContentDetails2(userCategory);
            // String[][] contentDetails3 = dbo.getContentDetails3(userCategory);
            newsArray = dbo.getNews();

            try {
                contentNameUpdated1 = contentDetails1[0];
                contentTypeUpdated1 = contentDetails1[1];
                boolean fAvailable;
                for (int i = 0; i < contentTypeUpdated1.length; i++) {
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

            } catch (Exception e) {
                Toast.makeText(context, "No data in database", Toast.LENGTH_SHORT).show();
            }

            /*  try{
            contentNameUpdated2 = contentDetails2[0];
            contentTypeUpdated2 = contentDetails2[1];
            boolean fAvailable;
            for(int i = 0; i<contentTypeUpdated2.length ; i++) {
                fAvailable = checkFileIsAvailable(contentNameUpdated2[i]);
                System.out.println(contentNameUpdated2[i]);
                if(fAvailable){
                }
                else{
                    if(contentTypeUpdated2[i].equals("img")){
                        new ImageDownloader(contentNameUpdated2[i]).execute();
                    }
                    else{
                        new FileDownloader().downloadFile(contentNameUpdated2[i]);
                    }
                }
            }
        }
        catch(Exception e){
            Toast.makeText(context, "No data in database.", Toast.LENGTH_SHORT).show();
        }*/

       /* try{
            contentNameUpdated3 = contentDetails3[0];
            contentTypeUpdated3 = contentDetails3[1];
            boolean fAvailable;
            for(int i = 0; i<contentTypeUpdated3.length ; i++) {
                fAvailable = checkFileIsAvailable(contentNameUpdated3[i]);
                System.out.println(contentNameUpdated3[i]);
                if(fAvailable){
                }
                else{
                    if(contentTypeUpdated3[i].equals("img")){
                        new ImageDownloader(contentNameUpdated3[i]).execute();
                    }
                    else{
                        new FileDownloader().downloadFile(contentNameUpdated3[i]);
                    }
                }
            }
        }
        catch(Exception e){
            Toast.makeText(context, "No data in database.", Toast.LENGTH_SHORT).show();
        }*/

            ArrayList<String> n = fileDeleter.listAllFiles();

            FIRST_LOOP: for(int i=0; i < contentNameUpdated1.length; i++){
                String name = contentNameUpdated1[i];
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

       /* FIRST_LOOP: for(int i=0; i < contentNameUpdated2.length; i++){
            String name = contentNameUpdated2[i];
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

        FIRST_LOOP: for(int i=0; i < contentNameUpdated3.length; i++){
            String name = contentNameUpdated3[i];
            SECOND_LOOP: for(int p = 0; p < n.size(); p++){
                if(n.size() == 0){}
                else {
                    if (name.equals(n.get(p))) {
                        n.remove(p);
                        break SECOND_LOOP;
                    }
                }

            }
        }*/

            if(n.size() == 0){}

            else{
                for(int check = 0; check<n.size(); check++){
                    fileDeleter.deleteFile(n.get(check));
                }
            }
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
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse("file://" +  Environment.getExternalStorageDirectory())));
            }

            if(contentTypeUpdated1.length == 0 || contentNameUpdated1.length == 0 || newsArray.length == 0){
                content = new String[][]{contentName1, contentType1, newsArray};
                return content;
            }
            else{
                content = new String[][]{contentNameUpdated1, contentTypeUpdated1, newsArray};
                return content;
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
                   // Toast.makeText(context, "Download Error.", Toast.LENGTH_SHORT).show();
                    return null;
                }
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            if(result == null){
                Toast.makeText(context, "Unable to download the image" + '\n' + "Error code: 0xE0884731", Toast.LENGTH_LONG).show();
            }
            else{
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
            Toast.makeText(context, "Compress Error" + '\n' + "Error code: 0xE0542139", Toast.LENGTH_LONG).show();
        }
        return d.getSTORAGE_PATH().getAbsolutePath();
    }

}
