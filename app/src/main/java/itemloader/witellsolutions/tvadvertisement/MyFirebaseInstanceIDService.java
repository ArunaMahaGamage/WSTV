package itemloader.witellsolutions.tvadvertisement;

/**
 * Created by Sumudu on 6/22/2016.
 */
import android.content.SharedPreferences;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


//Class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Displaying token on logcat
        System.out.println(refreshedToken);
        SharedPreferences logcatStart = getSharedPreferences("sync", 0);
        SharedPreferences.Editor logcatEditor = logcatStart.edit();
        logcatEditor.putString("token", refreshedToken);
        logcatEditor.commit();
    }

    @Override
    public void onDestroy() {
    }
}