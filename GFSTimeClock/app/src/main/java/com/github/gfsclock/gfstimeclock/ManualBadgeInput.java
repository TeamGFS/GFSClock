package com.github.gfsclock.gfstimeclock;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class ManualBadgeInput extends AppCompatActivity {

    private String barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_badge_input);

        final Button submit = (Button) findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText idEditText = (EditText) findViewById(R.id.editID);
                String id = idEditText.getText().toString();
                if (id.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ManualBadgeInput.this);
                    builder.setTitle(getString(R.string.id_empty));
                    builder.setMessage(getString(R.string.id_empty_message));
                    String positiveText = getString(android.R.string.ok);
                    builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    barcode = String.format("%010d", Integer.parseInt(id));
                    Intent optionsScreen = new Intent(ManualBadgeInput.this, ClockOptions.class);
                    optionsScreen.putExtra("barcode", barcode);
                    ManualBadgeInput.this.startActivity(optionsScreen);
                }
            }
        });

    }


}
