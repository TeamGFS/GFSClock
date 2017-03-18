package com.github.gfsclock.gfstimeclock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;



public class ManualBadgeInput extends AppCompatActivity {

    private String barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_badge_input);
    }


//    final Button okButton = (Button) findViewById(R.id.submitButton);
//    /**
//     * The processScan() is called after a succesful scan is returned from the scanBadge1() method.
//     * This passes the contents of the scanned barcode to the ClockOptions.
//     */
//    public void processScan(){
//        EditText pinEditText = (EditText) findViewById(R.id.editText);
//        Intent optionsScreen = new Intent(ManualBadgeInput.this, ClockOptions.class);
//        optionsScreen.putExtra("barcode", barcode);
//        ManualBadgeInput.this.startActivity(optionsScreen);
//    }
}
