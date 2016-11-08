package org.kyledef.findmepizza.helper;


public interface SignInCallback{
	void onSignInSuccess();
	void onSignInFailure(String message);
	void onSignOutSuccess();
	void onSignOutFailure(String message);
}