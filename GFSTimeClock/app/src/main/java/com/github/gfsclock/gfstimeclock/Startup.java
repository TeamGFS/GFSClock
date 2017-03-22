package com.github.gfsclock.gfstimeclock;

/**
 * Created by kentkent on 2/19/17.
 */

import android.app.Application;
import android.content.Context;

import io.realm.Realm;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Startup()
 * This function initializes a new Realm instance.
 */
public class Startup extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();
        Realm.init(this);
    }
}
