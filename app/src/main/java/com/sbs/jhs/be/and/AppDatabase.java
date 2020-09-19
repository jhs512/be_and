package com.sbs.jhs.be.and;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class AppDatabase {
    private static Application application;
    private static SharedPreferences sp;
    private static SharedPreferences.Editor spEditor;
    private static List<String> todos;

    static {
        todos = new ArrayList<>();
    }

    public static void init(Application application) {
        AppDatabase.application = application;
        sp = PreferenceManager.getDefaultSharedPreferences(application);
        spEditor = sp.edit();
    }

    public static void saveLoginAuthKey(String authKey) {
        spEditor.putString("loginAuthKey", authKey);
        spEditor.commit();
    }

    public static String getLoginAuthKey() {
        return sp.getString("loginAuthKey", "");
    }

    public static boolean isLogined() {
        return getLoginAuthKey().equals("") == false;
    }

    public static void removeLoginAuthKey() {
        spEditor.remove("loginAuthKey");
        spEditor.commit();
    }
}