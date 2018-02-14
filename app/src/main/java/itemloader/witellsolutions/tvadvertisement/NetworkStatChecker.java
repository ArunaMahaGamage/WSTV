package itemloader.witellsolutions.tvadvertisement;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.StrictMode;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Sumudu on 3/30/2016.
 */
public class NetworkStatChecker {
    //static String connectionHost = "http://rdamsapp.witellsolutions.com";
    static Data d = new Data();
    public String isConnected(Context context) {
        String returningString = "";
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        if(connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()){
            try{
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                returningString = "";

                URL url = new URL(d.getSERVER_PHP_PATH().trim() + "getnews.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.connect();
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    returningString = "";
                   // conn.disconnect();
                }
                else{
                    Toast.makeText(context, String.valueOf(conn.getResponseCode()), Toast.LENGTH_LONG).show();
                    returningString = "0xE0225792";
                    //conn.disconnect();
                }
            } catch (Exception e) {
//                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                //System.out.println("Except :" + String.valueOf(e));
                returningString = "0xE0225792";
            }
        }
        else {
            returningString = "0xE0417527";
        }
        return returningString;
    }
    public Boolean isWifiConnected(Context c){
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI);
    }

    public Boolean isEthernetConnected(Context c){
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_ETHERNET);
    }
}
