package com.party.funhunt.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Ermike on 4/4/2017.
 */

public class Pref {

    Context context;
    SharedPreferences preferences;

    public Pref(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getPreference(String key) {
        return preferences.getString(key, "");
    }

    public void setPreferences(String key, String value) {
        preferences.edit().putString(key, value).commit();
    }
}
