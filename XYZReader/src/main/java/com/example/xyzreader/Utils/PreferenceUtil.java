package com.example.xyzreader.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by elshaarawy on 07-May-17.
 */

public class PreferenceUtil {
    private Context context;
    private String SharedPreferenceName;
    private SharedPreferences sharedPreference;


    public PreferenceUtil(Context context, String sharedPreferenceName) {
        this.context = context;
        this.SharedPreferenceName = sharedPreferenceName;
        this.sharedPreference = context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE);
    }

    public boolean editValue(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putBoolean(key, value);
        boolean back = editor.commit();
        return back;
    }

    public boolean editValue(String key, String value) {
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString(key, value);
        boolean back = editor.commit();
        return back;
    }

    public boolean editValue(String key, float value) {
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putFloat(key, value);
        boolean back = editor.commit();
        return back;
    }

    public boolean editValue(String key, int value) {
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putInt(key, value);
        boolean back = editor.commit();
        return back;
    }

    public boolean editValue(String key, long value) {
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putLong(key, value);
        boolean back = editor.commit();
        return back;
    }

    public boolean getBoolean(String key) {
        return sharedPreference.getBoolean(key, false);
    }

    public String getString(String key) {
        return sharedPreference.getString(key, null);
    }

    public float getFloat(String key) {
        return sharedPreference.getFloat(key, 0.0f);
    }

    public int getInt(String key) {
        return sharedPreference.getInt(key, 0);
    }

    public long getLong(String key) {
        return sharedPreference.getLong(key, 0);
    }

    public Set<String> getStringSet(String key) {
        return sharedPreference.getStringSet(key, null);
    }

    public boolean clear(){
        SharedPreferences.Editor editor = sharedPreference.edit();
        return editor.clear().commit();
    }

    public static final class DefaultKeys {
        public static final String DEFAULT_SHARED_PREFERENCE = "DefaultPreference";

        public static final String PREF_IS_FIRST_TIME = "isFirstTime";
    }

}
