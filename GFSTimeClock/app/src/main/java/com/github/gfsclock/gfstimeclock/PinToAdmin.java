package com.github.gfsclock.gfstimeclock;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PinToAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_to_admin);

        SharedPreferences prefs = getSharedPreferences("admin_pin", MODE_PRIVATE);
        final String savedPin = prefs.getString("pin", "1234");


        final Button okButton = (Button) findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText pinEditText = (EditText) findViewById(R.id.PIN);
                if(pinEditText.getText().toString().equals(savedPin)){
                    Intent adminScreen = new Intent(PinToAdmin.this, Admin.class);
                    startActivity(adminScreen);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PinToAdmin.this);
                    builder.setTitle(getString(R.string.incorrect_pin));
                    builder.setMessage(getString(R.string.incorrect_pin_message));
                    String positiveText = getString(android.R.string.ok);
                    builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }
}
