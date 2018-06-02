package com.lbrand.githubclient.common;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferenceManager {
    private SharedPreferences sharedPrefs;
    private static PreferenceManager sInstance;
    private SharedPreferences.Editor editor;
    private static Context context;

    public interface PreferenceKeys {
        String SHARED_PREFS = "l_brands";
        String IS_FIRST_LAUNCH = "is_first_launch";
        String ACCESS_TOKEN = "access_token";
        String NOTIFICATION_STATUS = "notification_status";
    }

    public static PreferenceManager getsInstance(Context ctx) {
        context = ctx;
        if (sInstance == null) {
            synchronized (PreferenceManager.class) {
                if (sInstance == null) {
                    sInstance = new PreferenceManager(ctx);
                }
            }
        }
        return sInstance;
    }


    private PreferenceManager(Context ctx) {
        sharedPrefs = ctx.getSharedPreferences(PreferenceKeys.SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
    }


    public void setSharedPreferencesListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        sharedPrefs.registerOnSharedPreferenceChangeListener(listener);
    }

    public void setFirstLaunch(boolean isFirstLaunch) {
        editor.putBoolean(PreferenceKeys.IS_FIRST_LAUNCH, isFirstLaunch).commit();
    }

    public boolean isFirstLaunch() {
        return sharedPrefs.getBoolean(PreferenceKeys.IS_FIRST_LAUNCH, true);
    }


    public String getAccessToken() {
        return sharedPrefs.getString(PreferenceKeys.ACCESS_TOKEN, "");
    }


    public void setAccessToken(String accessToken) {
        editor.putString(PreferenceKeys.ACCESS_TOKEN, accessToken);
        editor.commit();
    }

    public void setNotificationStatus(String status) {
        editor.putString(PreferenceKeys.NOTIFICATION_STATUS, status).commit();
    }

    public String getNotificationStatus() {
        return sharedPrefs.getString(PreferenceKeys.NOTIFICATION_STATUS, "0");
    }
}
