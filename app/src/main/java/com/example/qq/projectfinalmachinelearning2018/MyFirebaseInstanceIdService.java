package com.example.qq.projectfinalmachinelearning2018;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Sarayut on 9/6/2561.
 */
public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    public static final String TAG = "Notify";
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String token = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG, "Token: " + token);
    }
}
