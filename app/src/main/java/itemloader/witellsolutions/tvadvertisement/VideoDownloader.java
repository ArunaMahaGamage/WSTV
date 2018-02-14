package itemloader.witellsolutions.tvadvertisement;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Sumudu on 3/30/2016.
 */
public class VideoDownloader {
    static String connectionHost = "http://192.168.1.108/imageapp/";
    int downloadedSize = 0;
    int totalSize = 0;

    void downloadFile(String videoName){
        try {
            //URL url = new URL(connectionHost.trim() + videoName.trim());
            URL url = new URL(connectionHost + "rock.mp4");

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            urlConnection.connect();

            File SDCardRoot = Environment.getExternalStorageDirectory();
            File file = new File(SDCardRoot,"rock.mp4");

            FileOutputStream fileOutput = new FileOutputStream(file);
            InputStream inputStream = urlConnection.getInputStream();

            totalSize = urlConnection.getContentLength();

            byte[] buffer = new byte[1024];
            int bufferLength;

            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
            }
            fileOutput.close();

        } catch (final MalformedURLException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        catch (final Exception e) {
        }
    }
}
