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

public class Admin extends AppCompatActivity {

    private String username;
    private String password;
    private String clockId;

    private EditText usernameField;
    private EditText passwordField;
    private EditText clockIdField;

    //SharedPreferences.Editor settingEditor = settingStore.edit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Get Data from Shared Preferences
        SharedPreferences settingStore = PreferenceManager.getDefaultSharedPreferences(Startup.getContext());
        username = settingStore.getString("username", "");
        password = settingStore.getString("password", "");
        clockId = settingStore.getString("clockId", "");

        // Load Text Fields, and populate them
        usernameField = (EditText) findViewById(R.id.Username);
        usernameField.setText(username, TextView.BufferType.EDITABLE);
        passwordField = (EditText) findViewById(R.id.Password);
        passwordField.setText(password, TextView.BufferType.EDITABLE);
        clockIdField = (EditText) findViewById(R.id.ClockID);
        clockIdField.setText(clockId, TextView.BufferType.EDITABLE);


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
        Intent scanBadge = new Intent(Admin.this, ScanBadge.class);
        startActivity(scanBadge);
    }


    public void saveButtonListener(View view) {
        SharedPreferences settingStore = PreferenceManager.getDefaultSharedPreferences(Startup.getContext());
        SharedPreferences.Editor settingEditor = settingStore.edit();

        username = usernameField.getText().toString();
        password = passwordField.getText().toString();
        clockId = clockIdField.getText().toString();

        settingEditor.putString("username", username);
        settingEditor.putString("password", password);
        settingEditor.putString("clockId", clockId);

        settingEditor.apply();

        Toast.makeText(this, "Settings saved.", Toast.LENGTH_LONG).show();

    }

    public void toScanBadge(View view) {
        Intent next = new Intent (Admin.this, ScanBadge.class);
        startActivity (next);
    }
}
