package com.example.iotmakerandroidsdktest;

import android.content.Context;
import android.content.SharedPreferences;

public class ApplicationPreference {
    public static final String PREF_NAME = "gigaiot.pref";
    public static final String PREF_ACCOUNT_ID = "pref.account_id";
    public static final String PREF_ACCOUNT_MEMBER_SEQ = "pref.account_mbrseq";
    public static final String PREF_ACCESS_TOKEN = "pref.access_token";
    public static ApplicationPreference applicationPref;

    SharedPreferences pref;

    SharedPreferences.Editor editor;

    public static boolean isInitilized = false;

    public ApplicationPreference(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();
        isInitilized = true;
    }

    public static void init(Context context) {
        if (applicationPref == null)
            applicationPref = new ApplicationPreference(context);
    }

    public static ApplicationPreference getInstance() {
        return applicationPref;
    }

    public void setPrefAccountId(String data) {
        editor.putString(PREF_ACCOUNT_ID, data);
        editor.commit();
    }

    public String getPrefAccountId() {
        return pref.getString(PREF_ACCOUNT_ID, "");
    }

    public String getPrefAccountMbrSeq() {
        return pref.getString(PREF_ACCOUNT_MEMBER_SEQ, "");
    }

    public void setPrefAccountMbrSeq(String data) {
        editor.putString(PREF_ACCOUNT_MEMBER_SEQ, data);
        editor.commit();
    }


    public void setPrefAccessToken(String data) {
        editor.putString(PREF_ACCESS_TOKEN, data);
        editor.commit();
    }

    public String getPrefAccessToken() {
        return pref.getString(PREF_ACCESS_TOKEN, "");
    }
}
