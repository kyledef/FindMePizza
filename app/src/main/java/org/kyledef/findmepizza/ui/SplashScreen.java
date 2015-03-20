package org.kyledef.findmepizza.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import org.kyledef.findmepizza.R;

public class SplashScreen extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        final Context c = getApplicationContext();
        (new Thread(){
            public void run(){
                try{
                    sleep(2 * 1000); // Just to show the splash screen for 2 seconds
                    startActivity(new Intent(getBaseContext(), PizzaList.class)); // Start the Main
                }catch(Exception e){e.printStackTrace(); }
                finish();
            }
        }).start();
    }
}