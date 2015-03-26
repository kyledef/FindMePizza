package org.kyledef.findmepizza.helper;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.kyledef.findmepizza.R;


/**
 * The Google Analytics Helper will provide a wrapper for ensuring that the functionality defined can be resued across the various application states of the program
 */
public class GAnalyticsHelper {

    private static final String TAG = "GAnalytics";
    private Tracker tracker;

    private static GAnalyticsHelper instance = null;

    private GAnalyticsHelper(Context context){
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
        this.tracker = analytics.newTracker(R.xml.ganalytics);
        tracker.enableAdvertisingIdCollection(true);
    }

    private boolean canSend(){
        return true;
    }
    // Make sense to keep a singleton class to make sure trackers are not reinitialized every time its called
    public static GAnalyticsHelper getInstance(Context context){
        if (instance == null)instance = new GAnalyticsHelper(context);
        return instance;
    }

    public Tracker getTracker(){
        return this.tracker;
    }

    public void sendUserEvent(String eventName, String action){
        if (canSend()) {
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("userevent")
                    .setAction("action")
                    .setLabel(eventName)
                    .setValue(1)
                    .build());
        }
    }

    public void sendScreenView(String screenName){
        if (canSend()) {
            Log.d(TAG, "Sending Screen View " + screenName);
            tracker.setScreenName(screenName);
            tracker.send(new HitBuilders.AppViewBuilder().build());
        }
    }

}
