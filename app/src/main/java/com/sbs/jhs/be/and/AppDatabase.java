package com.sbs.jhs.be.and;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppDatabase {
    private static Application application;
    private static SharedPreferences sp;
    private static SharedPreferences.Editor spEditor;

    public static void init(Application application) {
        AppDatabase.application = application;
        sp = PreferenceManager.getDefaultSharedPreferences(application);
        spEditor = sp.edit();
    }

    public static void saveLoginAuthKey(String loginAuthKey) {
        spEditor.putString("loginAuthKey", loginAuthKey);
        spEditor.commit();
    }

    public static String getLoginAuthKey() {
        return sp.getString("loginAuthKey", "");
    }

    public static void removeLoginAuthKey() {
        spEditor.remove("loginAuthKey");
        spEditor.commit();
    }

    public static void saveLoginId(String loginId) {
        spEditor.putString("loginId", loginId);
        spEditor.commit();
    }

    public static String getLoginId() {
        return sp.getString("loginId", "");
    }

    public static void removeLoginId() {
        spEditor.remove("loginId");
        spEditor.commit();
    }
}