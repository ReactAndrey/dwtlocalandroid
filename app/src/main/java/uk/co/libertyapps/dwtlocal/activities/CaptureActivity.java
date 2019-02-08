package uk.co.libertyapps.dwtlocal.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.result.UserProfile;

import uk.co.libertyapps.dwtlocal.R;
import uk.co.libertyapps.dwtlocal.WeeksActivity;
import uk.co.libertyapps.dwtlocal.application.App;


public class CaptureActivity extends AppCompatActivity {

    private UserProfile mUserProfile;
    private Auth0 mAuth0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth0 = new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));
        // The process to reclaim an UserProfile is preceded by an Authentication call.
        AuthenticationAPIClient aClient = new AuthenticationAPIClient(mAuth0);
        aClient.tokenInfo(App.getInstance().getUserCredentials().getIdToken())
                .start(new BaseCallback<UserProfile, AuthenticationException>() {
                    @Override
                    public void onSuccess(final UserProfile payload) {
                        CaptureActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                mUserProfile = payload;
                                refreshScreenInformation();


                                Intent intented = new Intent(CaptureActivity.this, WeeksActivity.class);
                                startActivity(intented);
                                finish();

                            }
                        });
                    }

                    @Override
                    public void onFailure(AuthenticationException error) {
                        CaptureActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(CaptureActivity.this, "Profile Request Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    private void refreshScreenInformation() {
        Log.d("bakery", mUserProfile.getName());
        Log.d("Email", mUserProfile.getEmail());

        SharedPreferences settings = getSharedPreferences("USER", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("EMAIL",  mUserProfile.getEmail());
        editor.putString("getName",  mUserProfile.getName());
        editor.putBoolean("ACCESS", true);

        Log.d("EDITOR 1", editor.toString());

        // Commit the edits!
        editor.commit();


        Log.d("EDITOR 2", editor.toString());
    }
}