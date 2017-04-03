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

public class AdminActivity extends AppCompatActivity {

    private String serverAddress;
    private String employeeAddress;
    private String username;
    private String password;
    private String clockId;

    private EditText serverAddressField;
    private EditText employeeAddressField;
    private EditText usernameField;
    private EditText passwordField;
    private EditText clockIdField;

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
        clockId = settingStore.getString("clockId", "0"); // this is kinda silly

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
        if(clockId.equals("0")){
            clockIdField.setText(clockId, TextView.BufferType.EDITABLE);
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
        clockId = clockIdField.getText().toString();

        settingEditor.putString("serverAddress", serverAddress);
        settingEditor.putString("employeeAddress", employeeAddress);
        settingEditor.putString("username", username);
        settingEditor.putString("password", password);
        settingEditor.putInt("clockId", Integer.parseInt(clockId));

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
}
