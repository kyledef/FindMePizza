package org.kyledef.findmepizza.helper;

import android.accounts.Account;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;

import org.kyledef.findmepizza.R;

public class AccountUtils {

    private static final String TAG = "AccountUtils";
    private static final String PREF_ACTIVE_ACCOUNT = "user_account";
    private static final String COVER_IMG_URL = "account_cover_image";

    public static boolean hasActiveAccount(Context context){
       return (getActiveAccountName(context) != null);
    }

    public static String getActiveAccountName(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_ACTIVE_ACCOUNT, null);
    }

    public static void setActiveAccountName(final Context context, final String accountName){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_ACTIVE_ACCOUNT, accountName);
    }

    public static Account getActiveAccount(final Context context) {
        String account = getActiveAccountName(context);
        if (account != null) {
            return new Account(account, GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        } else {
            return null;
        }
    }

    public static void loadAccountImage(final Context context, Account chosenAccount, ImageView imageView) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String url = sp.getString(COVER_IMG_URL, null);
        if (url != null) {
            // Load with URL stored in preferences
            Glide.with(context)
                .load(url)
                .asBitmap()
                .animate(R.anim.image_fade_in)
                .into(imageView);
        }else{
            // Use Default, however it should have been set within the XML setting again just incase
            imageView.setImageResource(R.drawable.account_image);
        }
    }


    public static void signInUser(final Context context, String accountName){
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(context);
        GAuthHandler gHandler = new GAuthHandler();
        builder.addApi(Plus.API)
                .addConnectionCallbacks(gHandler)
                .addOnConnectionFailedListener(gHandler)
                .setAccountName(accountName)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build()
                .connect();

    }


    public static class GAuthHandler implements GAuthListeners{

        @Override
        public void onConnected(Bundle bundle) {

        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {

        }

        @Override
        public void onResult(People.LoadPeopleResult loadPeopleResult) {

        }
    }
}
