package org.kyledef.findmepizza.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;

import org.kyledef.findmepizza.R;
import org.kyledef.findmepizza.model.PizzaModelManager;

public class SplashScreen extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        // Perform Check for Internet Access if first Use
        final Context c = getApplicationContext();
        if (isFirstUse(c)){
            if (!isOnline(c)){
                retryDialog(this);
                return;
            }
        }
        loadData(c);
    }

    private void loadData(final Context context){
        (new Thread(){
            public void run(){
                try{
                    sleep(2 * 1000); // Just to show the splash screen for 2 seconds
                    PizzaModelManager.getInstance(getApplicationContext()).loadAllData(); // Load data into singleton
                    setFirstUse(context);
                    Intent intent = new Intent(context, PizzaList.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Prevent Use from going back to home screen
                    startActivity(intent); // Start the Main
                }catch(Exception e){e.printStackTrace(); }
                finish();
            }
        }).start();
    }

    private boolean isOnline(final Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isFirstUse(final Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("first_use", true);
    }

    private boolean setFirstUse(final Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.edit().putBoolean("first_use", false).commit();
    }

    private void retryDialog(final Activity context){
       new AlertDialog.Builder(context)
            .setTitle(context.getResources().getString(R.string.no_conn_dialog))
            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(context, SplashScreen.class));
                    finish();
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            })
            .show();
    }
}