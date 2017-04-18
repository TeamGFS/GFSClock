package com.github.gfsclock.gfstimeclock;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ManualInputActivity extends AppCompatActivity {

    private int barcode;
    private String barcodeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_badge_input);

        final Button submit = (Button) findViewById(R.id.submitButton);

        final EditText idField = (EditText) findViewById(R.id.editID);

        idField.setFocusableInTouchMode(true);
        idField.requestFocus();

        idField.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                String id = idField.getText().toString();
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    barcodeStr = id.substring(1);
                    barcode = Integer.parseInt(barcodeStr);
                    Intent optionsScreen = new Intent(ManualInputActivity.this, ClockOptionsActivity.class);
                    optionsScreen.putExtra("barcode", barcode);
                    ManualInputActivity.this.startActivity(optionsScreen);
                    Toast.makeText(ManualInputActivity.this, idField.getText(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText idEditText = (EditText) findViewById(R.id.editID);
                String id = idEditText.getText().toString();

                if (id.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ManualInputActivity.this);
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

                    barcode = Integer.parseInt(id);
                    Intent optionsScreen = new Intent(ManualInputActivity.this, ClockOptionsActivity.class);
                    optionsScreen.putExtra("barcode", barcode);
                    ManualInputActivity.this.startActivity(optionsScreen);
                }
            }
        });

    }


}
