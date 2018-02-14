package itemloader.witellsolutions.tvadvertisement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by Sumudu on 4/4/2016.
 */
public class WifiReceiver extends BroadcastReceiver {
    public static boolean wifiON = false;

    @Override
    public void onReceive(Context context, Intent intent) {

        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if(info != null && info.isConnected()) {

            wifiON = true;
            // e.g. To check the Network Name or other info:
            /*WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();*/
        }
        else{
            wifiON = false;
        }
    }
}