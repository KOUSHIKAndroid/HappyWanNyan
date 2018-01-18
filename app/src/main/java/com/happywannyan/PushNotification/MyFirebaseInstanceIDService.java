package com.happywannyan.PushNotification;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.R;
import com.happywannyan.Utils.CustomJSONParser;

import java.util.HashMap;

/**
 * Created by apple on 23/05/17.
 */


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("@@ ", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    private void sendRegistrationToServer(String token) {
        HashMap<String, String> Params = new HashMap<>();
        Params.put("user_id", AppConstant.UserId);
        Params.put("anorid_device_id", token);

        new CustomJSONParser().APIForPostMethod2(getApplication(),AppConstant.BASEURL + "users_device_update", Params, new CustomJSONParser.JSONResponseInterface() {
            @Override
            public void OnSuccess(String Result) {

            }

            @Override
            public void OnError(String Error, String Response) {

            }

            @Override
            public void OnError(String Error) {
                if (Error.equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.please_check_your_internet_connection))){
                    Toast.makeText(getApplicationContext(),Error,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}