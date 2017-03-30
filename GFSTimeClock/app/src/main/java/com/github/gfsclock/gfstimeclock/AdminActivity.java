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
    private String username;
    private String password;
    private int clockId;

    private EditText serverAddressField;
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
        username = settingStore.getString("username", null);
        password = settingStore.getString("password", null);
        clockId = settingStore.getInt("clockId", 0);

        // Load Text Fields, and populate them
        serverAddressField = (EditText) findViewById(R.id.ServerAddress);
        serverAddressField.setText(serverAddress, TextView.BufferType.EDITABLE);
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
        Intent scanBadge = new Intent(AdminActivity.this, ScanBadgeActivity.class);
        startActivity(scanBadge);
    }




    public void saveButtonListener(View view) {
        SharedPreferences settingStore = PreferenceManager.getDefaultSharedPreferences(Startup.getContext());
        SharedPreferences.Editor settingEditor = settingStore.edit();

        serverAddress = serverAddressField.getText().toString();
        username = usernameField.getText().toString();
        password = passwordField.getText().toString();
        clockId = Integer.parseInt(clockIdField.getText().toString());

        settingEditor.putString("serverAddress", serverAddress);
        settingEditor.putString("username", username);
        settingEditor.putString("password", password);
        settingEditor.putInt("clockId", clockId);

        settingEditor.apply();

        Toast.makeText(this, "Settings saved.", Toast.LENGTH_LONG).show();

    }


    public void toScanBadge(View view) {
        Intent next = new Intent (AdminActivity.this, ScanBadgeActivity.class);
        startActivity (next);
    }
}
