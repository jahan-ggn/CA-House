package com.example.tjg.cahouseclient.Storage;

import android.content.Context;
import android.content.SharedPreferences;


public class MyPreference {
    public static final String KEY_ID = "user_id";
    SharedPreferences sharedPreferences;
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_EMAIL_ID = "email_id";
    public static final String GCM_TOKEN_ID = "token_id";
    public static final String KEY_USER_MOB = "mobile";


    public void setKeyUserMob(String mobile){
        editor.putString(KEY_USER_MOB, mobile);
        editor.commit();
    }

    public String getKeyUserMob(){
        return sharedPreferences.getString(KEY_USER_MOB, null);
    }

    public String getGcmTokenId() {
        return sharedPreferences.getString(GCM_TOKEN_ID, null);
    }

    public void setGcmTokenId(String tokenId){
        editor.putString(GCM_TOKEN_ID, tokenId);
        editor.commit();
    }

    public void setEmailId(String email) {
        editor.putString(KEY_EMAIL_ID, email);
        editor.commit();
    }

    public String getEmailId() {
        return sharedPreferences.getString(KEY_EMAIL_ID,null);
    }

    SharedPreferences.Editor editor;

    public MyPreference(Context context) {
        sharedPreferences = context.getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void CreateSession(String user_id) {
        editor.putString(KEY_ID, user_id);
        editor.commit();
    }

    public String GetSession() {
        return sharedPreferences.getString(KEY_ID, null);
    }

    public void setName(String name) {
        editor.putString(KEY_USER_NAME, name);
        editor.commit();
    }

    public String getName() {
        return sharedPreferences.getString(KEY_USER_NAME, null);
    }
    public void Logout() {
        editor.clear();
        editor.commit();
    }
}
