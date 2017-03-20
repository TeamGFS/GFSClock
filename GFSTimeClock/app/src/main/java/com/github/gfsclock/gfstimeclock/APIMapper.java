package com.github.gfsclock.gfstimeclock;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit2.Call;


public class APIMapper {
    private static APIMapper mInstance = null;
    private Realm realm;
    private static final String TAG = "APIMAPPER";

    private APIMapper() {};

    public static APIMapper getInstance() {
        if(mInstance == null) {
            synchronized(APIMapper.class) {
                if (mInstance == null) {
                    mInstance = new APIMapper();
                }
            }
        }
        return mInstance;
    }


    /**
     * Initializes the database
     * @return boolean success
     */
    public boolean init_db() {
        realmSetup();

        // Query to see if initialize has already happened
        RealmResults<PunchModel> results = realm.where(PunchModel.class).findAll();

        // if we already have punches in the system
        // init already happened/persisted
        if(results.size() > 0) {
            realmSetdown();
            return true;
        }

        // initialize punches with mocked up data
        realm.beginTransaction();
        PunchModel punch1 = realm.createObject(PunchModel.class);
        punch1.setId(29313);
        punch1.setDocket("F1");
        punch1.setTimeStamp(new Date());
        realm.commitTransaction();

        realmSetdown();
        return true;

    }

    public ArrayList<PunchModel> getPunchesID(int eID) {
        realmSetup();

        RealmQuery query = realm.where(PunchModel.class);
        query.equalTo("id", eID);

        RealmResults<PunchModel> results = realm.where(PunchModel.class).equalTo("id", eID).findAll();


        ArrayList<PunchModel> output = new ArrayList<>();
        output.addAll(realm.copyFromRealm(results));
        realmSetdown();
        return output;
    }

    public void punch(int eID, String docket, Date time) {
        // TODO Fail on Invalid ID not already in DB

        realmSetup();

        realm.beginTransaction();
        PunchModel n = realm.createObject(PunchModel.class);
        n.setId(eID);
        n.setDocket(docket);
        n.setTimeStamp(time);
        realm.commitTransaction();

        realmSetdown();
    }

    private void realmSetup() {
        realm = Realm.getDefaultInstance();
    };

    private void realmSetdown() {
        realm.close();
    }

    private String getAPIURLPreference() {
        SharedPreferences appPref = PreferenceManager.getDefaultSharedPreferences(Startup.getContext());
        return appPref.getString("serverAddress", "nil");
    }

    // Stubs
    private void checkConnection() {
        // check for connection to API
    }

    private void checkAuth() {
        // check to see if user is authorized
    }

    public EmployeeAPIContainer getEmployeeInfo(int idInput) {
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(Startup.getContext());
        String username = sPref.getString("username", "");
        String password = sPref.getString("password", "");
        EmployeeQueryService idClient = InfoServiceGenerator.createService(EmployeeQueryService.class, username, password);
        Call<EmployeeAPIContainer> call = idClient.getData(idInput);
        try {
            EmployeeAPIContainer eData = call.execute().body();
            return eData;
        } catch (IOException e) {
            return new EmployeeAPIContainer();
        }

        // TODO: Parse EmployeeAPIContainer.ded before returning
    }
}
