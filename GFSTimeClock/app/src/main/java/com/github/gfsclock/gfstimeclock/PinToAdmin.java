package com.github.gfsclock.gfstimeclock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PinToAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_to_admin);

        SharedPreferences prefs = getSharedPreferences("admin_pin", MODE_PRIVATE);
        final String savedPin = prefs.getString("pin", "");

        // TODO figure out how to initalize sharedpref pin because the above line will allow an empty pin
        Button okButton = (Button) findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText pinEditText = (EditText) findViewById(R.id.PIN);
                if(pinEditText.getText().toString().equals(savedPin)){
                    Intent adminScreen = new Intent(PinToAdmin.this, Admin.class);
                    startActivity(adminScreen);
                } else {
                    //TODO make a toast to display try again or press back.
                }
            }
        });
    }
}
