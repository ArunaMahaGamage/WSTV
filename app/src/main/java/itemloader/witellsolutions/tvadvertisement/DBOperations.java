package itemloader.witellsolutions.tvadvertisement;

import android.net.Uri;
import android.os.StrictMode;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sumudu on 3/30/2016.
 */
public class DBOperations {
    InputStream is = null;
    Data d = new Data();
    public boolean checkLoginCredentials(String userName, String passWord){
        String line, result = "";
        boolean res;

        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL url = new URL(d.getSERVER_PHP_PATH().trim()+"login.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username",  userName.toString().trim())
                        .appendQueryParameter("pass", passWord.toString().trim());
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                try{
                    conn.connect();
                    is = conn.getInputStream();
                }
                catch(Exception e){}
                finally {
                    //conn.disconnect();
                }

                /*HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(d.getSERVER_PHP_PATH().trim()+"login.php?username="+userName.toString().trim()+"&pass="+passWord.toString().trim());
                HttpResponse response = httpclient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();*/
            }
            catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
                res = false;
                //System.out.println(ex.toString());
            }
        }
        catch(Exception ex){
            System.out.println("Ex " + ex.toString());
            res = false;
            //System.out.println(ex.toString());
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
            is.close();
        }
        catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            res = false;
            //System.out.println(ex.toString());
        }
        try {
            JSONArray jArray = new JSONArray(result);
            int count = jArray.length();
            if(count == 0){
                res = false;
            }
            else{
                res = true;
            }
        } catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            res = false;
        }
        return res;
    }

    public boolean checkActive(String deviceID){
        String line, result = "";
        boolean res;
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL url = new URL(d.getSERVER_PHP_PATH().trim()+"checkDeviceActive.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("dev",  deviceID);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                try{
                    conn.connect();
                    is = conn.getInputStream();
                }
                catch(Exception e){
                }
                finally {
                    //conn.disconnect();
                }

                /*HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(d.getSERVER_PHP_PATH().trim()+"checkDeviceActive.php");

                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("dev", deviceID));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();*/
            }
            catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
                res = false;
            }
        }
        catch(Exception ex){
            System.out.println("Ex " + ex.toString());
            res = false;
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
            is.close();
        }
        catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            res = false;
        }
        try {
            JSONArray jArray = new JSONArray(result);
            int count = jArray.length();
            if(count == 0){
                res = false;
            }
            else{
                res = true;
            }
        } catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            res = false;
        }
        return res;
    }

    public boolean checkImei(String userID, String mac){
        String line, result = "";
        boolean res;
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {

                URL url = new URL(d.getSERVER_PHP_PATH().trim()+"checkimei.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("id",  userID.toString().trim())
                        .appendQueryParameter("mac", mac.toString().trim());
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                try{
                    conn.connect();
                    is = conn.getInputStream();
                }
                catch(Exception e){}
                finally {
                   // conn.disconnect();
                }

                /*HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(d.getSERVER_PHP_PATH().trim()+"checkimei.php");

                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("id", userID.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("mac", mac.toString().trim()));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();*/
            }
            catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
                res = false;
                //System.out.println(ex.toString());
            }
        }
        catch(Exception ex){
            System.out.println("Ex " + ex.toString());
            //System.out.println(ex.toString());
            res = false;
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
            is.close();
        }
        catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            res = false;
            //System.out.println(ex.toString());
        }
        try {
            JSONArray jArray = new JSONArray(result);
            int count = jArray.length();
            if(count == 0){
                res = false;
            }
            else{
                res = true;
            }
        } catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            res = false;
        }
        return res;
    }

    public String checkMAC(String mac){
        String line, result = "";
        String res = "";
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL url = new URL(d.getSERVER_PHP_PATH().trim()+"checkMACAddress.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("mac",  mac.toString().trim());
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                try{
                    conn.connect();
                    is = conn.getInputStream();
                }
                catch(Exception e){}
                finally {
                    //conn.disconnect();
                }


                /*HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(d.getSERVER_PHP_PATH().trim()+"checkMACAddress.php");
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("mac", mac.toString().trim()));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();*/
            }
            catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
                res = "4";
            }
        }
        catch(Exception ex){
            System.out.println("Ex " + ex.toString());
            res = "4";
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
            is.close();
        }
        catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            res = "4";
        }
        try {
            JSONArray jArray = new JSONArray(result);
            int count = jArray.length();

            for (int i = 0; i < count; i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                res += jObject.getString("device_active");
            }

            if(res.equalsIgnoreCase("")){
                res = "4";
            }

        } catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
           res = "4";
        }
        return res;
    }

    public String[][] getContentDetails1(String categoryId){
        String line = "", result = "", contentName = "", contentType = "", transit_time = "";
        String[][] imageDetails;
        String[] content;
        String[] type;
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL url = new URL(d.getSERVER_PHP_PATH().trim()+"contentdetails1.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("category",  categoryId);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                is = conn.getInputStream();

                try{
                    conn.connect();
                    is = conn.getInputStream();
                }
                catch(Exception e){
                }
                finally {
                    //conn.disconnect();
                }


              /*  HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(d.getSERVER_PHP_PATH().trim()+"contentdetails3.php?category=" + categoryId);
                HttpResponse response = httpclient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();*/
            }
            catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
                return null;
            }
        }
        catch(Exception ex){
            System.out.println("Ex " + ex.toString());
            return null;
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
            is.close();
        }
        catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            return null;
        }
        try {
            JSONArray jArray = new JSONArray(result);
            int count = jArray.length();
            for (int i = 0; i < count; i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                contentName += jObject.getString("content_path") + "#";
                contentType += jObject.getString("content_type") + "#";
                transit_time += jObject.getString("transit_time") + "#";
            }

            content = contentName.split("#");
            type = contentType.split("#");

            imageDetails = new String[][]{ content, type, transit_time.split("#")};

        } catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            return null;
        }

        return imageDetails;
    }

    public String[][] getContentDetails2(String categoryId){
        String line = "", result = "", contentName = "", contentType = "", transit_time = "";
        String[][] imageDetails;
        String[] content;
        String[] type;
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL url = new URL(d.getSERVER_PHP_PATH().trim()+"contentdetails2.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("category",  categoryId);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                is = conn.getInputStream();

                try{
                    conn.connect();
                    is = conn.getInputStream();
                }
                catch(Exception e){
                }
                finally {
                    //conn.disconnect();
                }


              /*  HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(d.getSERVER_PHP_PATH().trim()+"contentdetails3.php?category=" + categoryId);
                HttpResponse response = httpclient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();*/
            }
            catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
                return null;
            }
        }
        catch(Exception ex){
            System.out.println("Ex " + ex.toString());
            return null;
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
            is.close();
        }
        catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            return null;
        }
        try {
            JSONArray jArray = new JSONArray(result);
            int count = jArray.length();
            for (int i = 0; i < count; i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                contentName += jObject.getString("content_path") + "#";
                contentType += jObject.getString("content_type") + "#";
                transit_time += jObject.getString("transit_time") + "#";
            }

            content = contentName.split("#");
            type = contentType.split("#");

            imageDetails = new String[][]{ content, type, transit_time.split("#")};

        } catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            return null;
        }
        return imageDetails;
    }

    public String[][] getContentDetails3(String categoryId){
        String line = "", result = "", contentName = "", contentType = "", transit_time = "";
        String[][] imageDetails;
        String[] content;
        String[] type;
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL url = new URL(d.getSERVER_PHP_PATH().trim()+"contentdetails3.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("category",  categoryId);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                is = conn.getInputStream();

                try{
                    conn.connect();
                    is = conn.getInputStream();
                }
                catch(Exception e){
                }
                finally {
                    //conn.disconnect();
                }


              /*  HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(d.getSERVER_PHP_PATH().trim()+"contentdetails3.php?category=" + categoryId);
                HttpResponse response = httpclient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();*/
            }
            catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
                return null;
            }
        }
        catch(Exception ex){
            System.out.println("Ex " + ex.toString());
            return null;
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
            is.close();
        }
        catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            return null;
        }
        try {
            JSONArray jArray = new JSONArray(result);
            int count = jArray.length();
            for (int i = 0; i < count; i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                contentName += jObject.getString("content_path") + "#";
                contentType += jObject.getString("content_type") + "#";
                transit_time += jObject.getString("transit_time") + "#";
            }

            content = contentName.split("#");
            type = contentType.split("#");

            imageDetails = new String[][]{ content, type, transit_time.split("#")};

        } catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            return null;
        }
        return imageDetails;
    }

    public String[][] getUserType(String userName, String password){
        String line = "", result = "", userType = "", userID = "";
        String[] userTypeArray = null, userIDArray = null;
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL url = new URL(d.getSERVER_PHP_PATH().trim()+"getusertype.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username",  userName)
                        .appendQueryParameter("pass",  password);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                try{
                    conn.connect();
                    is = conn.getInputStream();
                }
                catch(Exception e){}
                finally {
                    //conn.disconnect();
                }


                /*HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(d.getSERVER_PHP_PATH().trim() + "getusertype.php?username=" + userName + "&pass=" + password);
                HttpResponse response = httpclient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();*/
            }
            catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
            }
        }
        catch(Exception ex){
            System.out.println("Ex " + ex.toString());
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
            is.close();
        }
        catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
        }
        try {
            JSONArray jArray = new JSONArray(result);
            int count = jArray.length();
            for (int i = 0; i < count; i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                userType += jObject.getString("category_id") + "#";
                userID += jObject.getString("uid") + "#";
            }
            userTypeArray = userType.split("#");
            userIDArray = userID.split("#");

        } catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
        }

        return new String[][]{ userTypeArray, userIDArray};
    }

    public String[] getNews(){
        String line = "", result = "", newsText = "";
        String[] newstextArray = null;
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL url = new URL(d.getSERVER_PHP_PATH().trim() + "getnews.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                try{
                    conn.connect();
                    is = conn.getInputStream();
                }
                catch(Exception e){}
                finally {
                    //conn.disconnect();
                }

                /*HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(d.getSERVER_PHP_PATH().trim() + "getnews.php");
                HttpResponse response = httpclient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();*/
            }
            catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
            }
        }
        catch(Exception ex){
            System.out.println("Ex " + ex.toString());
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
            is.close();
        }
        catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
        }
        try {
            JSONArray jArray = new JSONArray(result);
            int count = jArray.length();
            for (int i = 0; i < count; i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                newsText += jObject.getString("news_text") + "#";
            }
            newstextArray = newsText.split("#");

        } catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
        }
        return newstextArray;
    }

    public boolean addOfflineTimeRange(String dealerID, String categoryID, String deviceID, String startTime, String endTime){
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //macAddress = macAddress.replaceAll(":", "#");
            try {
                URL url = new URL(d.getSERVER_PHP_PATH().trim() + "offlineRange.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("id",  dealerID)
                        .appendQueryParameter("cat",  categoryID)
                        .appendQueryParameter("dev",  deviceID)
                        .appendQueryParameter("start",  startTime)
                        .appendQueryParameter("end", endTime);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                try{
                    conn.connect();
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                catch(Exception e){
                    return false;
                }
                finally {
                    //conn.disconnect();
                }

                /*HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(d.getSERVER_PHP_PATH().trim() + "offlineRange.php");
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("id", dealerID));
                nameValuePairs.add(new BasicNameValuePair("cat", categoryID));
                nameValuePairs.add(new BasicNameValuePair("dev", deviceID));
                nameValuePairs.add(new BasicNameValuePair("start", startTime));
                nameValuePairs.add(new BasicNameValuePair("end", endTime));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpClient.execute(httpPost);*/
               /* if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    return true;
                }
                else{
                    return false;
                }*/
            }
            catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
               return false;
            }
        }
        catch(Exception ex){
            System.out.println("Ex " + ex.toString());
            return false;
        }
    }

    public boolean pinging(String deviceID, String time){
        try{
            try {
                URL url = new URL(d.getSERVER_PHP_PATH().trim() + "ping.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(20000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("dev",  deviceID)
                        .appendQueryParameter("time", time);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                try{
                    conn.connect();
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                catch(Exception e){
                    return false;
                }
                finally {
                    //conn.disconnect();
                }

                /*HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(d.getSERVER_PHP_PATH().trim() + "ping.php");
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("dev", deviceID));
                nameValuePairs.add(new BasicNameValuePair("time", time));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpClient.execute(httpPost);*/
               /* if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    return true;
                }
                else{
                    return false;
                }*/
            }
            catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
                return false;
            }
        }
        catch(Exception ex){
            System.out.println("Ex " + ex.toString());
            return false;
        }
    }

    public boolean addDevice(String dealerEmail, String wifiMAC, String ethernetMAC, String gpsLAT, String gpsLng, String adminEmail){
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL url = new URL(d.getSERVER_PHP_PATH().trim() + "addDevice.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("dealerEmail",  dealerEmail)
                        .appendQueryParameter("wifiMAC",  wifiMAC)
                        .appendQueryParameter("ethernetMAC",  ethernetMAC)
                        .appendQueryParameter("gpsLAT",  gpsLAT)
                        .appendQueryParameter("gpsLng",  gpsLng)
                        .appendQueryParameter("adminEmail", adminEmail);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();


                try{
                    conn.connect();
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                catch(Exception e){
                    return false;
                }
                finally {
                    //conn.disconnect();
                }

               /* HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(d.getSERVER_PHP_PATH().trim() + "addDevice.php");
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("dealerEmail", dealerEmail));
                nameValuePairs.add(new BasicNameValuePair("wifiMAC", wifiMAC));
                nameValuePairs.add(new BasicNameValuePair("ethernetMAC", ethernetMAC));
                nameValuePairs.add(new BasicNameValuePair("gpsLAT", gpsLAT));
                nameValuePairs.add(new BasicNameValuePair("gpsLng", gpsLng));
                nameValuePairs.add(new BasicNameValuePair("adminEmail", adminEmail));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpClient.execute(httpPost);*/
               /* if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    return true;
                }
                else{
                    return false;
                }*/
            }
            catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
                return false;
            }
        }
        catch(Exception ex){
            System.out.println("Ex " + ex.toString());
            return false;
        }
    }

    public boolean putLoginTime(String deviceID, String dealerID, String loginTime, String logoutTime){
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL url = new URL(d.getSERVER_PHP_PATH().trim() + "logtime.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("dev",  deviceID)
                        .appendQueryParameter("logintime",  loginTime)
                        .appendQueryParameter("logouttime",  logoutTime)
                        .appendQueryParameter("id", dealerID);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                try{
                    conn.connect();
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                catch(Exception e){
                    return false;
                }
                finally {
                    //conn.disconnect();
                }

                /*HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(d.getSERVER_PHP_PATH().trim() + "logtime.php");
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("dev", deviceID));
                nameValuePairs.add(new BasicNameValuePair("logintime", loginTime));
                nameValuePairs.add(new BasicNameValuePair("logouttime", logoutTime));
                nameValuePairs.add(new BasicNameValuePair("id", dealerID));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpClient.execute(httpPost);*/
             /*   if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    return true;
                }
                else{
                    return false;
                }*/
            }
            catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
                return false;
            }
        }
        catch(Exception ex){
            System.out.println("Ex " + ex.toString());
            return false;
        }
    }

    public boolean putLogOutTime(String deviceID, String dealerID, String loginTime, String logoutTime){
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {
                URL url = new URL(d.getSERVER_PHP_PATH().trim() + "logouttime.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("dev",  deviceID)
                        .appendQueryParameter("login",  loginTime)
                        .appendQueryParameter("logout",  logoutTime)
                        .appendQueryParameter("id", dealerID);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                try{
                    conn.connect();
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                catch(Exception e){
                    return false;
                }
                finally {
                    //conn.disconnect();
                }

               /* HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(d.getSERVER_PHP_PATH().trim() + "logouttime.php");
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("dev", deviceID));
                nameValuePairs.add(new BasicNameValuePair("login", loginTime));
                nameValuePairs.add(new BasicNameValuePair("logout", logoutTime));
                nameValuePairs.add(new BasicNameValuePair("id", dealerID));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpClient.execute(httpPost);*/
               /* if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    return true;
                }
                else{
                    return false;
                }*/
            }
            catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
                return false;
            }
        }
        catch(Exception ex){
            System.out.println("Ex " + ex.toString());
            return false;
        }
    }

    public boolean putExitTime(String deviceID, String time){
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {

                URL url = new URL(d.getSERVER_PHP_PATH().trim() + "exittime.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("dev",  deviceID)
                        .appendQueryParameter("ext", time);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                try{
                    conn.connect();
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                catch(Exception e){
                    return false;
                }
                finally {
                    //conn.disconnect();
                }

                /*HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(d.getSERVER_PHP_PATH().trim() + "exittime.php");
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("dev", deviceID));
                nameValuePairs.add(new BasicNameValuePair("ext", time));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpClient.execute(httpPost);*/
                /*if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    return true;
                }
                else{
                    return false;
                }*/
            }
            catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
                return false;
            }
        }
        catch(Exception ex){
            System.out.println("Ex " + ex.toString());
            return false;
        }
    }

    public String getDeviceID(String mac){
        String line, result, devID = "";
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL url = new URL(d.getSERVER_PHP_PATH().trim()+"getdeviceid.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("mac", mac);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                try{
                    conn.connect();
                    is = conn.getInputStream();
                }
                catch(Exception e){}
                finally {
                    //conn.disconnect();
                }

                /*HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(d.getSERVER_PHP_PATH().trim()+"getdeviceid.php");
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("mac", mac));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();*/
            }
            catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
                return null;
            }
        }
        catch(Exception ex){
            System.out.println("Ex " + ex.toString());
            return null;
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
            is.close();
        }
        catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            return null;
        }
        try {
            JSONArray jArray = new JSONArray(result);
            int count = jArray.length();
            for (int i = 0; i < count; i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                devID += jObject.getString("device_id");
            }

        } catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            return null;
        }
        return devID;
    }

    public String[] getVersion(){
        String line, result, verID = "", url = "";
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL urls = new URL(d.getSERVER_PHP_PATH().trim()+"getversion.php");
                HttpURLConnection conn = (HttpURLConnection) urls.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                try{
                    conn.connect();
                    is = conn.getInputStream();
                }
                catch(Exception e){}
                finally {
                    //conn.disconnect();
                }

                /*HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(d.getSERVER_PHP_PATH().trim()+"getversion.php");
                HttpResponse response = httpclient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();*/
            }
            catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
                return null;
            }
        }
        catch(Exception ex){
            System.out.println("Ex " + ex.toString());
            return null;
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
            is.close();
        }
        catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            return null;
        }
        try {
            JSONArray jArray = new JSONArray(result);
            int count = jArray.length();
            for (int i = 0; i < count; i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                verID += jObject.getString("ver");
                url += jObject.getString("url");
            }

        } catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            return null;
        }
        return new String[]{verID, url};
    }

    public String[][] getEmails(){
        String line, result, emailString = "", storeNameString = "";
        String[] emailAddresses, storeNameArray;
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL url = new URL(d.getSERVER_PHP_PATH().trim()+"getemails.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                try{
                    conn.connect();
                    is = conn.getInputStream();
                }
                catch(Exception e){}
                finally {
                    //conn.disconnect();
                }

                /*HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(d.getSERVER_PHP_PATH().trim()+"getemails.php");
                HttpResponse response = httpclient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();*/
            }
            catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
                return null;
            }
        }
        catch(Exception ex){
            System.out.println("Ex " + ex.toString());
            return null;
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
            is.close();
        }
        catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            return null;
        }
        try {
            JSONArray jArray = new JSONArray(result);
            int count = jArray.length();
            for (int i = 0; i < count; i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                emailString += jObject.getString("username") + "#";
                storeNameString += jObject.getString("store_name") + "#";
            }

            emailAddresses = emailString.split("#");
            storeNameArray = storeNameString.split("#");

            return new String[][]{emailAddresses, storeNameArray};

        } catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            return null;
        }
    }

    public boolean addToken(String token, String deviceID) {
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {

                URL url = new URL(d.getSERVER_PHP_PATH().trim()+"addtoken.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("token", token)
                        .appendQueryParameter("deviceID", deviceID);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                try{
                    conn.connect();
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                catch(Exception e){
                    return false;
                }
                finally {
                    //conn.disconnect();
                }
                /*HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(d.getSERVER_PHP_PATH().trim()+"getemails.php");
                HttpResponse response = httpclient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();*/

            }
            catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
                return false;
            }
        }
        catch(Exception ex){
            System.out.println("Ex " + ex.toString());
            return false;
        }
    }

    public boolean connectToTheServer(){
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL url = new URL(d.getSERVER_PHP_PATH().trim() + "connecting.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                try{
                    conn.connect();
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                catch(Exception e){
                    return false;
                }
                finally {
                    //conn.disconnect();
                }
                /*HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(d.getSERVER_PHP_PATH().trim() + "connecting.php");
                HttpResponse response = httpClient.execute(httpPost);*/
               /* if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    return true;
                }
                else{
                    return false;
                }*/
            }
            catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
                return false;
            }
        }
        catch(Exception ex){
            System.out.println("Ex " + ex.toString());
            return false;
        }
    }
}
