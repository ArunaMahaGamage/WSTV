package itemloader.witellsolutions.tvadvertisement;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sumudu on 4/26/2016.
 */
public class UserRegister extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {
    TextView WIFIMAC, EthernetMAC, GPSLatitude, GPSLongitude;
    EditText mEditText;
    AlertDialog searchingCategoryDialogView1, searchingCategoryDialogView;
    ListView users;
    Button regButton;
    String dealerEmailAddress;
    SimpleAdapter adapter;
    String[] emailsList, storesList;
    TextView mailtext, shopNameDisplay;
    NetworkStatChecker n = new NetworkStatChecker();
    DBOperations dbo = new DBOperations();
    String gpsLatitude = "", gpsLongitude = "";
    String wlanMAC, eMac, adminEmail;

    LocationRequest
            mLocationRequest;

    GoogleApiClient
            mGoogleApiClient;

    LatLng latLng;

    DecimalFormat
            df = new DecimalFormat("#.0000");

    private static final long
            INTERVAL = 1000 * 1;

    private static final long
            FASTEST_INTERVAL = 10 * 5;

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.macregister);
        Intent i = getIntent();
        context = UserRegister.this;
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();
        if(i != null){
            wlanMAC = i.getStringExtra("wlanMacAddress");
            eMac = i.getStringExtra("ethernetMacAddress");
            adminEmail = i.getStringExtra("adminEmail");
        }

        WIFIMAC = (TextView)findViewById(R.id.wlanMACDisplay);
        EthernetMAC = (TextView)findViewById(R.id.eMACDisplay);
        GPSLatitude = (TextView)findViewById(R.id.latDisplay);
        GPSLongitude = (TextView)findViewById(R.id.lngDisplay);
        users = (ListView)findViewById(R.id.dealerSelector);
        mailtext = (TextView)findViewById(R.id.mailAddress);
        shopNameDisplay = (TextView)findViewById(R.id.shopName);

        new UserEmails().execute();

        regButton = (Button)findViewById(R.id.regButton);
        regButton.setClickable(false);

        WIFIMAC.setText(wlanMAC);
        EthernetMAC.setText(eMac);
    }

    public void cancel(View v){
        finish();
        Intent i = new Intent(UserRegister.this, Login.class);
        startActivity(i);
    }

    public void registerDevice(View v){
       dealerEmailAddress = mailtext.getText().toString().trim();
        if(dealerEmailAddress.equalsIgnoreCase("")){
            Toast.makeText(getApplicationContext(), "Please select an email address first", Toast.LENGTH_SHORT).show();
        }
        else {
            regButton.setEnabled(false);
            loadingDialog("Registering");
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        System.out.println("connected");
        startLocationUpdates();
    }
    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new com.google.android.gms.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                System.out.println("Updated: " + df.format(location.getLatitude()) + " " + df.format(location.getLongitude()));
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                gpsLatitude = df.format(location.getLatitude());
                gpsLongitude = df.format(location.getLongitude());
                GPSLatitude.setText(gpsLatitude);
                GPSLongitude.setText(gpsLongitude);
                regButton.setClickable(true);
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    class DBSynchronization1 extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            String connectionStat = n.isConnected(getApplication());
            if (connectionStat.equals("")) {
                boolean res = dbo.addDevice(mailtext.getText().toString().trim(), wlanMAC, eMac, GPSLatitude.getText().toString().trim(), GPSLongitude.getText().toString().trim(), adminEmail);
                return res;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            regButton.setEnabled(true);
            searchingCategoryDialogView.dismiss();
            if(result){
                successDialog();
            }
            else{
                Toast.makeText(getApplication(), "Failed to register", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void loadingDialog(String msg) {
        LayoutInflater factory = LayoutInflater.from(UserRegister.this);
        View profileDialog;
        profileDialog = factory.inflate(R.layout.login_loading, null);
        TextView checkingText = (TextView) profileDialog.findViewById(R.id.checkingText);
        checkingText.setText(msg);
        ImageView rrr = (ImageView)profileDialog.findViewById(R.id.r);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.infiniterotate);
        rrr.startAnimation(anim);
        searchingCategoryDialogView = new AlertDialog.Builder(UserRegister.this).create();
        searchingCategoryDialogView.setCancelable(false);
        searchingCategoryDialogView.setView(profileDialog);
        searchingCategoryDialogView.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
        searchingCategoryDialogView.show();
        new DBSynchronization1().execute();
    }

    class UserEmails extends AsyncTask<String, Void, String[]> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String[] doInBackground(String... urls) {
            String connectionStat = n.isConnected(getApplication());
            if (connectionStat.equals("")) {
                String[][] data = dbo.getEmails();
                if(data == null){
                    return null;
                }
                else{
                    emailsList = data[0];
                    storesList = data[1];
                    return emailsList;
                }
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] result) {
            regButton.setEnabled(true);
            if(result == null){
                Toast.makeText(getApplication(), "No users in the database", Toast.LENGTH_LONG).show();
              users.setEmptyView(findViewById(R.id.emptyview));
            }
            else{
                List<HashMap<String, String>> aList = new ArrayList<>();
                for (int i = 0; i < emailsList.length; i++) {
                    HashMap<String, String> hm = new HashMap<>();
                    String email = emailsList[i].replaceAll("null", "");
                    String stores = storesList[i].replaceAll("null", "");
                    hm.put("email", email);
                    hm.put("store", stores);
                    aList.add(hm);
                }
                String[] from = {"email", "store"};
                int[] to = {R.id.mail, R.id.store};
                adapter = new SimpleAdapter(getApplicationContext(), aList, R.layout.email_display, from, to);
                adapter.notifyDataSetChanged();
                users.setAdapter(adapter);
                users.setTextFilterEnabled(true);
                mEditText = (EditText) findViewById(R.id.filteringText);
                mEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                        adapter.getFilter().filter(arg0);
                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {

                    }
                });
                AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View container, int position, long id) {
                        LinearLayout linearLayoutParent = (LinearLayout) container;
                        TextView mailAdd = (TextView) linearLayoutParent.getChildAt(0);
                        TextView storeName = (TextView) linearLayoutParent.getChildAt(1);
                        mailtext.setText(mailAdd.getText().toString());
                        shopNameDisplay.setText(storeName.getText().toString());
                    }
                };
                users.setOnItemClickListener(itemClickListener);
            }
        }
    }

    public void successDialog() {
        final Dialog searchingCategoryDialogView2 = new Dialog(UserRegister.this, android.R.style.Theme_Translucent_NoTitleBar);
        searchingCategoryDialogView2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        searchingCategoryDialogView2.setContentView(R.layout.registered);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(searchingCategoryDialogView2.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        searchingCategoryDialogView2.getWindow().setAttributes(lp);

        Button OK = (Button) searchingCategoryDialogView2.findViewById(R.id.btn);
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchingCategoryDialogView2.dismiss();
                finish();
                Intent i = new Intent(UserRegister.this, Login.class);
                startActivity(i);
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
}