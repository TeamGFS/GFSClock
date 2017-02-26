package com.github.gfsclock.gfstimeclock;

/**
 * Created by kentkent on 2/19/17.
 */

import android.app.Application;

import io.realm.Realm;

/**
 * Startup()
 * This function initializes a new Realm instance.
 */
public class Startup extends Application{
    @Override
    public void onCreate(){
        super.onCreate();
        Realm.init(this);
    }
}
