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
 * Created by Sumudu on 3/31/2016.
 */
public class FileDownloader {
        int downloadedSize = 0;
        int totalSize = 0;
       Data d = new Data();

        void downloadFile(String fileName){
            try {
                //URL url = new URL(connectionHost.trim() + videoName.trim());
                URL url = new URL(d.getIMAGE_DOWNLOADER_PATH().trim() + fileName);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);

                urlConnection.connect();

                File file = new File(d.getSTORAGE_PATH(), fileName);

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
                System.out.println(e);
            } catch (final IOException e) {
                System.out.println(e);
            }
            catch (final Exception e) {
                System.out.println(e);
            }
        }


}
