package com.example.qq.projectfinalmachinelearning2018;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by Sarayut on 23/5/2561.
 */
public class UserManage {
    private final String Key_username = "YUT";

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    public UserManage(Context context) {
        sharedPref = context.getSharedPreferences(Key_username, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public boolean checkLoginValidate(String email) {
        String realEmail = sharedPref.getString(Key_username, "");


        if ((!TextUtils.isEmpty(email)) && email.equalsIgnoreCase(realEmail)) {
            return true;
        }
        return false;
    }

    public boolean registerUser(String email) {

        if (TextUtils.isEmpty(email)) {
            return false;
        }

        editor.putString(Key_username, email);
        return editor.commit();
    }

    public void removeKey() {
        editor.remove(Key_username);
        editor.apply();
    }
}
