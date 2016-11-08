package org.kyledef.findmepizza.helper;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

import org.kyledef.findmepizza.R;

public class AccountUtils {

    public static final String LOGIN_TAG= "LOGIN_ACTIVITY";
    public static final int RC_SIGN_IN = 0;
    public static final String AUTH_SCOPES[] = {
            Scopes.PLUS_LOGIN,
            Scopes.DRIVE_APPFOLDER,
            "https://www.googleapis.com/auth/plus.profile.emails.read"};
    static final String AUTH_TOKEN_TYPE;
    static {
        StringBuilder sb = new StringBuilder();
        sb.append("oauth2:");
        for (String scope : AUTH_SCOPES) {
            sb.append(scope);
            sb.append(" ");
        }
        AUTH_TOKEN_TYPE = sb.toString();
    }
    private static final String TAG = "AccountUtils";
    private static final String PREF_ACTIVE_ACCOUNT = "user_account";
    private static final String PROFILE_IMG_URL = "account_cover_image";
    private static final String PROF_ID = "profile";
    private static final String PLUS_NAME = "plus_name";
    private static final String AUTH_TOKEN = "token";

    public static boolean hasActiveAccount(Context context){
       return (getActiveAccountName(context) != null);
    }

    public static String getActiveAccountName(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_ACTIVE_ACCOUNT, null);
    }

    public static void setActiveAccountName(final Context context, final String accountName){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_ACTIVE_ACCOUNT, accountName).apply();
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
        String url = sp.getString(PROFILE_IMG_URL, null);
        if (url != null) {
            Log.d(TAG, "Attempting to Load profile image from: " + url);
            // Load with URL stored in preferences
            Glide.with(context)
                .load(url)
                .asBitmap()
                .animate(R.anim.image_fade_in)
                .into(imageView);
        }else{
            Log.d(TAG, "No URL for image was found for the account");
            // Use Default, however it should have been set within the XML setting again just incase
            imageView.setImageResource(R.drawable.account_image);
        }
    }

    public static void setPlusName(Context context, String accountName, String displayName) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PLUS_NAME, displayName);
    }

    public static String getPlusName(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PLUS_NAME, null);
    }

    public static void setPlusImageUrl(Context context, String accountName, String imageUrl) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PROFILE_IMG_URL, imageUrl);
    }

    public static void setPlusProfileId(Context context, String accountName, String id) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PROF_ID, id);
    }


    public static void signInUser(final Activity activity, String accountName, SignInCallback callback) {
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(activity.getApplicationContext());
        GAuthHandler gHandler = new GAuthHandler(activity, accountName);

        GoogleApiClient client = builder.addApi(Plus.API)
                .addConnectionCallbacks(gHandler)
                .addOnConnectionFailedListener(gHandler)
                .setAccountName(accountName)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();

        gHandler.setClient(client);
        gHandler.setCallback(callback);

        client.connect();
    }

    public static void setAuthToken(final Activity activity, String token){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        sp.edit().putString(AUTH_TOKEN, token).commit();
    }

    public static interface SignInCallback{
        public void onSignInSuccess();
        public void onSignInFailure(String message);
    }

    public static class GAuthHandler implements GAuthListeners{

        private final String accountName;
        private GoogleApiClient client;
        private Activity activity;
        private boolean mIntentInProgress;
        private SignInCallback callback;


        public GAuthHandler(Activity context, String accountName){
            this.activity = context;
            this.accountName = accountName;
        }

        @Override
        public void onConnected(Bundle bundle) {
            Log.d(LOGIN_TAG, "Successfully Logged In to Google API");
            PendingResult<People.LoadPeopleResult> result = Plus.PeopleApi.load(client, "me");
            result.setResultCallback(this);
        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(ConnectionResult result) {
            if (!mIntentInProgress && result.hasResolution()) {
                try {
                    mIntentInProgress = true;
                    activity.startIntentSenderForResult(result.getResolution().getIntentSender(),RC_SIGN_IN, null, 0, 0, 0);
                } catch (IntentSender.SendIntentException e) {
                    // The intent was canceled before it was sent.  Return to the default
                    // state and attempt to connect to get an updated ConnectionResult.
                    mIntentInProgress = false;
                    client.connect();
                }
            }
        }

        @Override
        public void onResult(People.LoadPeopleResult loadPeopleResult) {
            Log.d(LOGIN_TAG,  "onPeopleLoaded, status=" + loadPeopleResult.getStatus().toString());
            if (loadPeopleResult.getStatus().isSuccess()) {
                PersonBuffer personBuffer = loadPeopleResult.getPersonBuffer();
                if (personBuffer != null && personBuffer.getCount() > 0) {
                    Person currentUser = personBuffer.get(0);
                    Log.d(LOGIN_TAG, "Got plus profile for account " + currentUser.getDisplayName());
                    personBuffer.close();

                    AccountUtils.setActiveAccountName(activity, accountName);

                    AccountUtils.setPlusProfileId(activity, accountName, currentUser.getId());
                    String imageUrl = currentUser.getImage().getUrl();
                    if (imageUrl != null) {
                        imageUrl = Uri.parse(imageUrl)
                                .buildUpon().appendQueryParameter("sz", "256").build().toString();
                    }
                    Log.d(LOGIN_TAG, "Saving plus image URL: " + imageUrl);
                    AccountUtils.setPlusImageUrl(activity, accountName, imageUrl);
                    Log.d(LOGIN_TAG, "Saving plus display name: " + currentUser.getDisplayName());
                    AccountUtils.setPlusName(activity, accountName, currentUser.getDisplayName());

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final String token = GoogleAuthUtil.getToken(activity, accountName, AUTH_TOKEN_TYPE);
                                if (token != null) setAuthToken(activity, token);
                                else
                                    Log.d(LOGIN_TAG, "Did not receive authentication token from google");
                            }catch (Exception e) { e.printStackTrace(); }
                        }
                    }).start();
                    client.disconnect();
                    if (callback != null)callback.onSignInSuccess();
                }
            }else{
                Log.d(LOGIN_TAG, "Unable to retrieve people information from Google +");
                if (callback != null)callback.onSignInFailure("Unable to retrieve people information from Google");
            }

        }

        public void setClient(GoogleApiClient client) {
            this.client = client;
        }

        public void setActivity(Activity activity) {
            this.activity = activity;
        }

        public void setCallback(SignInCallback callback) {
            this.callback = callback;
        }
    }


}
