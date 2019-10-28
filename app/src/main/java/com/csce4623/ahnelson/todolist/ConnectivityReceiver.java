package com.csce4623.ahnelson.todolist;

        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.net.ConnectivityManager;
        import android.widget.Toast;

public class ConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive( Context context,  Intent intent) {
        ConnectivityManager conMan = (ConnectivityManager) context .getSystemService(Context.CONNECTIVITY_SERVICE);

        android.net.NetworkInfo wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        android.net.NetworkInfo mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (!wifi.isAvailable() && !mobile.isAvailable()) {
            Toast.makeText(context.getApplicationContext(),"You are offline. Changes will be stored locally.",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(context.getApplicationContext(),"You are online. Changes will be synced.",Toast.LENGTH_LONG).show();
        }
    }

}