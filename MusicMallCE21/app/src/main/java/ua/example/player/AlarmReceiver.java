package ua.example.player;

import java.lang.reflect.Method;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MYTAG", "RECEIVED getMobileDataEnabled: " + getMobileDataEnabled(context));  
        
        if (!isOnline(context)) {
            Log.d("MYTAG", "NO INET");
            if (turnOnInet(context)) {
                Log.d("MYTAG", "INET IS ON");
            }
        }
/*
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://xxx.xxx.xxx.xxx/ping/pong/moto/");
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("short_code", "ROFL"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                httpclient.execute(httppost);
                Log.d("MYTAG", "POST FINISHED");
            }
            catch (Exception e) {
                Log.e("MYTAG", "MYTAG", e);
            }*/
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null){
            Log.d("MYTAG", "isAvailable: "+netInfo.isAvailable());
        }
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public boolean turnOnInet(Context context) {
        ConnectivityManager mgr = (ConnectivityManager)context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mgr == null) {
            Log.d("MYTAG", "ConnectivityManager == NULL");
            return false;
        }
        try {
            Method setMobileDataEnabledMethod = mgr.getClass().getDeclaredMethod("setMobileDataEnabled", boolean.class);
            if (null == setMobileDataEnabledMethod) {
                Log.d("MYTAG", "setMobileDataEnabledMethod == null");
                return false;
            }    
            setMobileDataEnabledMethod.invoke(mgr, true);
        }
        catch(Exception e) {
            Log.e("MYTAG", "MYTAG", e);
            return false;
        }
        return true;
    }   


    private boolean getMobileDataEnabled(Context context) {
        ConnectivityManager mgr = (ConnectivityManager)context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mgr == null) {
            Log.d("MYTAG", "getMobileDataEnabled ConnectivityManager == null");
            return false;
        }
        try {
            Method method = mgr.getClass().getMethod("getMobileDataEnabled");
            return (Boolean) method.invoke(mgr);
        } catch (Exception e) {
            Log.e("MYTAG", "MYTAG", e);
            return false;
        }
    }


}