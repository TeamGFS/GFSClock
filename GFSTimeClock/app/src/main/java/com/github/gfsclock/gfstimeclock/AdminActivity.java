package com.github.gfsclock.gfstimeclock;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity {

    private String serverAddress;
    private String employeeAddress;
    private String username;
    private String password;
    private Integer clockId;

    private EditText serverAddressField;
    private EditText employeeAddressField;
    private EditText usernameField;
    private EditText passwordField;
    private EditText clockIdField;

    private Realm realm;

    //SharedPreferences.Editor settingEditor = settingStore.edit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Get Data from Shared Preferences - if not value present display hardcoded string / stub
        SharedPreferences settingStore = PreferenceManager.getDefaultSharedPreferences(Startup.getContext());
        serverAddress = settingStore.getString("serverAddress", null);
        employeeAddress = settingStore.getString("employeeAddress", null);
        username = settingStore.getString("username", null);
        password = settingStore.getString("password", null);
        clockId = settingStore.getInt("clockId", 0); // this is kinda silly

        // Load Text Fields, and populate them
        serverAddressField = (EditText) findViewById(R.id.ServerAddress);
        serverAddressField.setText(serverAddress, TextView.BufferType.EDITABLE);
        employeeAddressField = (EditText) findViewById(R.id.EmployeeServerAddress);
        employeeAddressField.setText(employeeAddress, TextView.BufferType.EDITABLE);
        usernameField = (EditText) findViewById(R.id.Username);
        usernameField.setText(username, TextView.BufferType.EDITABLE);
        passwordField = (EditText) findViewById(R.id.Password);
        passwordField.setText(password, TextView.BufferType.EDITABLE);
        clockIdField = (EditText) findViewById(R.id.ClockID);
        if(clockId == 0){
            clockIdField.setText(clockId.toString(), TextView.BufferType.EDITABLE);
        } else {
            clockIdField.setText(null, TextView.BufferType.EDITABLE);
        }

        final Button button = (Button) findViewById(R.id.Save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                saveButtonListener(view);
            }
        });
    }



    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent scanBadge = new Intent(AdminActivity.this, ScanBadgeActivity.class);
        startActivity(scanBadge);
    }




    public void saveButtonListener(View view) {
        SharedPreferences settingStore = PreferenceManager.getDefaultSharedPreferences(Startup.getContext());
        SharedPreferences.Editor settingEditor = settingStore.edit();

        serverAddress = serverAddressField.getText().toString();
        employeeAddress = employeeAddressField.getText().toString();
        username = usernameField.getText().toString();
        password = passwordField.getText().toString();
        clockId = Integer.parseInt(clockIdField.getText().toString());

        settingEditor.putString("serverAddress", serverAddress);
        settingEditor.putString("employeeAddress", employeeAddress);
        settingEditor.putString("username", username);
        settingEditor.putString("password", password);
        settingEditor.putInt("clockId", clockId);

        settingEditor.apply();

        // Apply API URL Changes
        APIServiceGenerator.changeApiBaseURL(serverAddress);
        InfoServiceGenerator.changeApiBaseURL(employeeAddress);

        Toast.makeText(this, "Settings saved.", Toast.LENGTH_LONG).show();

    }


    public void toScanBadge(View view) {
        Intent next = new Intent (AdminActivity.this, ScanBadgeActivity.class);
        startActivity (next);
    }

    public void apiSync(View view) {
        // Get Cache Contents
        realm = Realm.getDefaultInstance();
        RealmResults<PunchModel> results = realm.where(PunchModel.class).findAll();
        List<PunchModel> output = new ArrayList<>();
        output.addAll(realm.copyFromRealm(results));
        realm.close();

        // invert the list
        List<PunchModel> invertedOutput = output.subList(0, output.size());
        Collections.reverse(invertedOutput);

        for(PunchModel cachedPunch : invertedOutput) {
            SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(Startup.getContext());
            String username = sPref.getString("username", "");
            String password = sPref.getString("password", "");
            PunchQueryService punchClient = APIServiceGenerator.createService(PunchQueryService.class, username, password);
            final PunchModel pAttempt = cachedPunch;
            final List<PunchModel> index = output;
            Call<ResponseBody> call = punchClient.submitPunchesByDate(pAttempt);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    // Remove Punch
                    realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    RealmResults<PunchModel> vList = realm.where(PunchModel.class).findAll();
                    vList.deleteFromRealm(vList.indexOf(pAttempt));
                    realm.commitTransaction();
                    realm.close();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }
}
