package uk.co.libertyapps.dwtlocal.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.auth0.android.Auth0;
import com.auth0.android.lock.AuthenticationCallback;
import com.auth0.android.lock.Lock;
import com.auth0.android.lock.LockCallback;
import com.auth0.android.lock.utils.LockException;
import com.auth0.android.result.Credentials;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import uk.co.libertyapps.dwtlocal.R;
import uk.co.libertyapps.dwtlocal.SearchActivity;
import uk.co.libertyapps.dwtlocal.WeeksActivity;
import uk.co.libertyapps.dwtlocal.application.App;


public class LoginActivity extends Activity {

    private Lock mLock;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics());

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        SharedPreferences user = getSharedPreferences("USER", 0);
        boolean access = user.getBoolean("ACCESS", true);
        if (!access) {

            Auth0 auth0 = new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));
            mLock = Lock.newBuilder(auth0, mCallback)
                    //Add parameters to the build
                    .build(this);
            startActivity(mLock.newIntent(this));
        } else {
            // skip data collection
            Intent intented = new Intent(LoginActivity.this, WeeksActivity.class);
            startActivity(intented);
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Your own Activity code
        if (mLock != null) {
            mLock.onDestroy(this);
            mLock = null;
        }
    }

    private final LockCallback mCallback = new AuthenticationCallback() {
        @Override
        public void onAuthentication(Credentials credentials) {
                Log.d("outcome", "Log In - Success");
                App.getInstance().setUserCredentials(credentials);
                startActivity(new Intent(LoginActivity.this, CaptureActivity.class));
                finish();

        }

        @Override
        public void onCanceled() {
            Log.d("outcome", "Log In - onCanceled");
        }

        @Override
        public void onError(LockException error) {
            Log.d("outcome", "Log In - Error Occurred");
        }
    };

}


