package org.kyledef.findmepizza.helper;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.BuildConfig;
import com.firebase.ui.auth.ui.ResultCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;


public class AccountUtils {

    public static final int RC_SIGN_IN = 215;
    private static AccountUtils instance = null;

    private final FirebaseAuth auth;

    private AccountUtils(){
        auth = FirebaseAuth.getInstance();
    }

    public static AccountUtils getInstance(){
        if (instance == null)instance = new AccountUtils();
        return instance;
    }

    public void signInUser(final Activity activity) {
        if (auth.getCurrentUser() == null){
            activity.startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                            ))
                            .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                            .build(),
                    RC_SIGN_IN);
        }
    }

    public void handleSignInResponse(final SignInCallback callback, final int resultCode){
        if (resultCode == Activity.RESULT_OK){
            callback.onSignInSuccess();
        }
        // Sign in canceled
        else if (resultCode == Activity.RESULT_CANCELED) {
            callback.onSignInFailure("Sign-in was cancelled");
        }
        // No network
        else if (resultCode == ResultCodes.RESULT_NO_NETWORK) {
            callback.onSignInFailure("No Internet detected - Sign-in was unsuccessful");
        }
        else{
            callback.onSignInFailure("Sign-in was unsuccessful");
        }
    }


    public void signOut(final Activity activity, final SignInCallback callback){
        AuthUI.getInstance().signOut(activity)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            callback.onSignOutSuccess();
                        else
                            callback.onSignInFailure("Unable to Sign Out User");
                    }
                });
    }

}
