package org.kyledef.findmepizza.helper;


import android.app.Activity;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

@SuppressWarnings("ConstantConditions")
public class AnalyticsHelper {

	private final FirebaseAnalytics analytics;
	private final boolean isEnabled = true;

	public AnalyticsHelper(Activity activity) {
		analytics = FirebaseAnalytics.getInstance(activity);
	}

	public void signIn(boolean b){
		if (isEnabled) {
			Bundle bundle = new Bundle();
			bundle.putBoolean("status", b);
			bundle.putString("direction", "in");
			analytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
		}
	}

	public void signOut(boolean b) {
		if (isEnabled) {
			Bundle bundle = new Bundle();
			bundle.putBoolean("status", b);
			bundle.putString("direction", "out");
			analytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
		}
	}

	public void performViewAction(String action, String item, String type){
		if (isEnabled) {
			Bundle bundle = new Bundle();
			bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, item);
			bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, type);
			bundle.putString("action", action);
			analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
		}
	}

	public void setScreen(String screenName) {
		if (isEnabled) {
			Bundle bundle = new Bundle();
			bundle.putString("screen", screenName);
			analytics.logEvent("select_screen", bundle);
		}
	}
}
