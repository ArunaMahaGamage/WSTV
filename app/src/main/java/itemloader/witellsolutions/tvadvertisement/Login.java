package itemloader.witellsolutions.tvadvertisement;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Sumudu on 3/30/2016.
 */
public class Login extends Activity{
    AlertDialog searchingCategoryDialogView, searchingCategoryDialogView1;
    EditText userNameField, passwordField;
    DBOperations dbo = new DBOperations();
    NetworkStatChecker n = new NetworkStatChecker();
    Context context;
    Data d = new Data();
    String dealerID = "";
    String wlanMac, eMac;
    boolean res;
    String deviceID;
    String registered = "", registered2 = "";
    String currentDateandTime;
    String userName = "", password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        userNameField = (EditText)findViewById(R.id.usernamefield);
        passwordField = (EditText)findViewById(R.id.passwordfield);
        context = this;
    }

    public void loadingDialog() {
        LayoutInflater factory = LayoutInflater.from(Login.this);
        View profileDialog;
        profileDialog = factory.inflate(R.layout.login_loading, null);
        ImageView rrr = (ImageView)profileDialog.findViewById(R.id.r);
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.infiniterotate);
        rrr.startAnimation(anim);

        searchingCategoryDialogView = new AlertDialog.Builder(Login.this).create();
        searchingCategoryDialogView.setCancelable(false);
        searchingCategoryDialogView.setView(profileDialog);
        searchingCategoryDialogView.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });

        searchingCategoryDialogView.show();
        new LoginChecker(userName, password).execute();
    }

    public void netError(String msg){
        searchingCategoryDialogView.dismiss();
        final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setCancelable(false);
        dlgAlert.setMessage("Please check your network connection" + '\n' + "Error Code: " + msg);
        dlgAlert.setTitle("No network connection");
        dlgAlert.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });
        dlgAlert.create().show();
    }
    public Boolean isWifiConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI);
    }

    public Boolean isEthernetConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_ETHERNET);
    }

    protected void forceWrapContent(View v) {
        // Start with the provided view
        View current = v;

        // Travel up the tree until fail, modifying the LayoutParams
        do {
            // Get the parent
            ViewParent parent = current.getParent();

            // Check if the parent exists
            if (parent != null) {
                // Get the view
                try {
                    current = (View) parent;
                } catch (ClassCastException e) {
                    // This will happen when at the top view, it cannot be cast to a View
                    break;
                }

                // Modify the layout
                current.getLayoutParams().width = FrameLayout.LayoutParams.WRAP_CONTENT;
            }
        } while (current.getParent() != null);

        // Request a layout to be re-done
        current.requestLayout();
    }

    class IMEIChecker extends AsyncTask<String, Void, Boolean> {
        String userType;

        IMEIChecker(String userType){
            this.userType = userType;
        }
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... urls) {
            String re = n.isConnected(context);
            if (re.equals("")) {
                if(wlanMac.equalsIgnoreCase("") || wlanMac.equalsIgnoreCase(null)){
                    if(eMac.equalsIgnoreCase("")){
                        res = false;
                    }
                    else{
                        res = dbo.checkImei(dealerID, eMac);
                        if(res){
                            deviceID = dbo.getDeviceID(eMac);
                        }
                        else res = false;
                    }
                }
                else {
                    res = dbo.checkImei(dealerID, wlanMac);
                    if(res){
                        deviceID = dbo.getDeviceID(wlanMac);
                    }
                    else res = false;
                }
                return res;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            searchingCategoryDialogView.dismiss();
                if(result){
                    dbo.putLoginTime(deviceID, String.valueOf(dealerID), currentDateandTime, "");
                    SharedPreferences logcatStart = getSharedPreferences("logger", 0);
                    SharedPreferences.Editor logcatEditor = logcatStart.edit();
                    logcatEditor.putString("logger", currentDateandTime);
                    logcatEditor.commit();

                    SharedPreferences settings = getSharedPreferences("usertype", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("usertype", userType);
                    editor.commit();

                    SharedPreferences uid = getSharedPreferences("dealerID", 0);
                    SharedPreferences.Editor editoruid = uid.edit();
                    editoruid.putString("dealerID", dealerID);
                    editoruid.commit();

                    SharedPreferences devID = getSharedPreferences("dev", 0);
                    SharedPreferences.Editor devIDEditor = devID.edit();
                    devIDEditor.putString("dev", deviceID);
                    devIDEditor.commit();

                        finish();
                        Intent intent = new Intent(Login.this, Loading.class);
                        intent.putExtra("usertype", userType);
                        intent.putExtra("devID", deviceID);
                        intent.putExtra("dealerID", dealerID);
                        startActivity(intent);
                }
                else{
                    errorDialog("Your device not belongs to this login credentials" + '\n' + "Error code: 0xE0175486");
                }
        }
    }

    public void errorDialog(String msg){
        try {
           /* if (searchingCategoryDialogView.isShowing()) {
               // searchingCategoryDialogView.dismiss();
            } else {

            }*/
            final Dialog searchingCategoryDialogView1 = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
            searchingCategoryDialogView1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            searchingCategoryDialogView1.setContentView(R.layout.error);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(searchingCategoryDialogView1.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            searchingCategoryDialogView1.getWindow().setAttributes(lp);

            Button OK = (Button) searchingCategoryDialogView1.findViewById(R.id.btn);
            TextView txt = (TextView) searchingCategoryDialogView1.findViewById(R.id.errorMSG);
            txt.setText(msg);
            OK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchingCategoryDialogView1.dismiss();
                }
            });
            //searchingCategoryDialogView1 = new AlertDialog.Builder(Login.this).create();
            searchingCategoryDialogView1.setCancelable(false);
            searchingCategoryDialogView1.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            });

            searchingCategoryDialogView1.show();
        } catch (Exception e) {}
    }

    public String getMacAddress(){
        try {
            return loadFileAsString("/sys/class/net/eth0/address").toUpperCase().substring(0, 17);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public String getWLANMacAddress(){
        try {
            return loadFileAsString("/sys/class/net/wlan0/address").toUpperCase().substring(0, 17);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String loadFileAsString(String filePath) throws java.io.IOException{
        StringBuffer data = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead;
        while((numRead = reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            data.append(readData);
        }
        reader.close();
        return data.toString();
    }

    public void loginCheck(View v){
        InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        userName = userNameField.getText().toString();
        password = passwordField.getText().toString();

        if(userName.equals("") || password.equals("")){
            errorDialog("Please fill all fields");
        }
        else{
            if(isValidEmail(userName)){
                loadingDialog();
            }
            else{
                errorDialog("Invalid email address");
            }
        }
    }
    public boolean isValidEmail(CharSequence target) {
        if (target == null)return false;
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    class LoginChecker extends AsyncTask<String, String, String> {
        String uname = "", pw = "";
        boolean isLoginAvailable = false;
        String[][] category_id_and_user_id = null;
        public LoginChecker(String uname, String pw){
            this.pw = pw;
            this.uname = uname;
        }

        @Override
        protected String doInBackground(String... params) {
            String res = n.isConnected(context);
            if(res.equals("")){
                isLoginAvailable = dbo.checkLoginCredentials(uname, pw);
                if(isLoginAvailable){
                    category_id_and_user_id = dbo.getUserType(uname, pw);
                    String[] category_id = category_id_and_user_id[0];
                    String[] user_id = category_id_and_user_id[1];
                    dealerID = user_id[0];
                    try{
                        eMac = getMacAddress();
                    }
                    catch(Exception e){
                        eMac = "";
                    }
                    try{
                        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                        WifiInfo info = manager.getConnectionInfo();
                        wlanMac = info.getMacAddress();
                        if(wlanMac.equals("") || wlanMac.equals(null)){
                            try{
                                wlanMac = getWLANMacAddress();
                            }
                            catch(Exception e){

                            }
                        }
                    }
                    catch(Exception ex){
                        wlanMac = getWLANMacAddress();
                    }
                    System.out.println("WLAN:  " + wlanMac + '\n' + "E: " + eMac);
                    return category_id[0];
                }
                else{
                    return "incorrect";
                }
            }
            else{
                return res;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            currentDateandTime = sdf.format(new Date());
            try {
                if(s.equals("0xE0225792") || s.equals("0xE0417527")){
                    netError(s);
                }
                else if(s.equals("incorrect")){
                    searchingCategoryDialogView.dismiss();
                    errorDialog("Invalid login credentials");
                }
                else {
                    if(s.equals("2")){
                        try{
                            if(wlanMac.equalsIgnoreCase("") || wlanMac.equalsIgnoreCase(null)){
                                if(eMac.equalsIgnoreCase("")){
                                    errorDialog("Please turn on wifi first");
                                }
                                else{
                                    registered2 = dbo.checkMAC(eMac);
                                }
                            }
                            else {
                                registered = dbo.checkMAC(wlanMac);
                            }
                            System.out.print("reg1: " + registered + "   reg2: " + registered2);
                            if (registered.equalsIgnoreCase("0") || registered2.equalsIgnoreCase("0")) {
                                //pending one
                                successDialog("This device is deactivated");
                            } else if (registered.equalsIgnoreCase("1") || registered2.equalsIgnoreCase("1")) {
                                // registered one
                                successDialog("This device is already registered");
                            } else if (registered.equalsIgnoreCase("2") || registered2.equalsIgnoreCase("2")) {
                                //pending one
                                successDialog("Registration pending");
                            } else if (registered.equalsIgnoreCase("3") || registered2.equalsIgnoreCase("3")) {
                                //blocked one
                                errorDialog("This device is blocked");
                            } else {
                                finish();
                                Intent intent = new Intent(Login.this, UserRegister.class);
                                intent.putExtra("wlanMacAddress", wlanMac);
                                intent.putExtra("ethernetMacAddress", eMac);
                                intent.putExtra("adminEmail", userName);
                                startActivity(intent);
                            }
                        }
                        catch(Exception e){
                            errorDialog("Please turn on WIFI first");
                            e.printStackTrace();
                        }
                    }
                    else if(s.equals("1") || s.equals("3")){
                        searchingCategoryDialogView.dismiss();
                        errorDialog("Access denied");
                    }
                    else{
                        new IMEIChecker(s).execute();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                searchingCategoryDialogView.dismiss();
            }
        }
    }

    public void successDialog(String message){
        searchingCategoryDialogView.dismiss();
        final Dialog searchingCategoryDialogView1 = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        searchingCategoryDialogView1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        searchingCategoryDialogView1.setContentView(R.layout.success);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(searchingCategoryDialogView1.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        searchingCategoryDialogView1.getWindow().setAttributes(lp);



        TextView WIFIMAC = (TextView)searchingCategoryDialogView1.findViewById(R.id.wlanMACAddressDisplay);
        TextView ethernetMAC = (TextView)searchingCategoryDialogView1.findViewById(R.id.eMACAddressDisplay);
        TextView displayMSG = (TextView)searchingCategoryDialogView1.findViewById(R.id.dis);
        displayMSG.setText(message);

        WIFIMAC.setText("WIFI: " + wlanMac);
        ethernetMAC.setText("Ethernet: " + eMac);

        Button OK = (Button) searchingCategoryDialogView1.findViewById(R.id.btn);
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchingCategoryDialogView1.dismiss();
            }
        });
        //searchingCategoryDialogView1 = new AlertDialog.Builder(Login.this).create();
        searchingCategoryDialogView1.setCancelable(false);
        searchingCategoryDialogView1.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });

        searchingCategoryDialogView1.show();
    }

    public void writer(String content, String fileName) {
        try {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new
                        File(d.getSTORAGE_PATH(), fileName + ".txt")));
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
