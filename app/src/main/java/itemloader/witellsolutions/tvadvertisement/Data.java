package itemloader.witellsolutions.tvadvertisement;

import android.os.Environment;

import java.io.File;

/**
 * Created by Sumudu on 4/12/2016.
 */
public class Data {
    private File STORAGE_PATH = new File(Environment.getExternalStorageDirectory()+"/tv");
    private String SERVER_PHP_PATH = "http://wstv.wirelessshoponline.com/app/";
    private String IMAGE_DOWNLOADER_PATH = "http://wstv.wirelessshoponline.com/app/img/";
        //private String SERVER_PHP_PATH = "http://192.168.1.163/advert/"; in Localhost
        //private String IMAGE_DOWNLOADER_PATH = "http://192.168.1.163/tv/"; in Localhost
    public File getSTORAGE_PATH() {
        return STORAGE_PATH;
    }
    public String getSERVER_PHP_PATH() {
        return SERVER_PHP_PATH;
    }
    public String getIMAGE_DOWNLOADER_PATH() {
        return IMAGE_DOWNLOADER_PATH;
    }
}
