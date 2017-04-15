package com.amazonaws.mobilehelper.auth;
//
// Copyright 2017 Amazon.com, Inc. or its affiliates (Amazon). All Rights Reserved.
//
// Code generated by AWS Mobile Hub. Amazon gives unlimited permission to 
// copy, distribute and modify it.
//
// Source code generated from template: aws-my-sample-app-android v0.16
//

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.amazonaws.mobilehelper.R;

/**
 * A default base class easing the work required for implementing the SignInResultHandler for
 * {@link IdentityManager dot signInOrSignUp(Context, SignInResultHandler)} by providing default
 * behavior in the case that the user cancels signing in or encounters an error. The default for
 * canceling is to toast that sign-in was canceled. The default for a sign-in error is to show
 * an alert dialog specifying the error message.
 */
public abstract class DefaultSignInResultHandler implements SignInResultHandler {
    private static final String LOG_TAG = DefaultSignInResultHandler.class.getSimpleName();

    /**
     * User cancelled signing in with a provider on the sign-in activity.
     * Note: The user is still on the sign-in activity when this call is made.
     * @param provider the provider the user canceled with.
     */
    public void onIntermediateProviderCancel(Activity callingActivity, IdentityProvider provider) {
        Toast.makeText(callingActivity, String.format(
            callingActivity.getString(R.string.sign_in_canceled_message_format),
            provider.getDisplayName()), Toast.LENGTH_LONG).show();
    }

    /**
     * User encountered an error when attempting to sign-in with a provider.
     * Note: The user is still on the sign-in activity when this call is made.
     * @param provider the provider the user attempted to sign-in with that encountered an error.
     * @param ex the exception that occurred.
     */
    public void onIntermediateProviderError(Activity callingActivity, IdentityProvider provider, Exception ex) {
        final String failureFormatString = callingActivity.getString(R.string.sign_in_failure_message_format);
        Log.e(LOG_TAG, String.format(failureFormatString,
            provider.getDisplayName(), ex.getMessage()), ex);
        final AlertDialog.Builder errorDialogBuilder = new AlertDialog.Builder(callingActivity);
        errorDialogBuilder.setTitle(callingActivity.getString(R.string.sign_in_failure_dialog_title));
        errorDialogBuilder.setMessage(
            String.format(failureFormatString, provider.getDisplayName(), ex.getMessage()));
        errorDialogBuilder.setNeutralButton(android.R.string.ok, null);
        errorDialogBuilder.show();
    }
}
