package com.github.gfsclock.gfstimeclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;



public class ScanBadge extends AppCompatActivity {

    private String barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_badge);
    }

    /**
     * The scanBadge1() is called when pressing the Scan badge button, the IntentIntegrator
     * provides intent functionality of the zxing camera scanner module.
     *
     * @param view
     */
    public void scanBadge1(View view){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }


    // TODO refactor this into menu overflow / inflator
    /**
     * The admin() method is called when passing intent to the admin configuration screen.
     *
     * @param view
     */
    public void admin(View view){
        Intent pinScreen = new Intent(ScanBadge.this, PinToAdmin.class);
        ScanBadge.this.startActivity(pinScreen);
    }


    /**
     * The onActivityResult function responds to intents that were started in the ScanBadge activity.
     * This is called automatically from the result of the IntentIntegrator and can be matched to
     * either a successful scan or a canceled scan.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("ScanBadge", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("ScanBadge", "Scanned");
                barcode = result.getContents().substring(1);

                Toast.makeText(this, "Scanned: " + barcode, Toast.LENGTH_LONG).show();
                processScan();
//                startActivity(options, scanResults);
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
            Toast.makeText(this, "something goofed", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * The processScan() is called after a succesful scan is returned from the scanBadge1() method.
     * This passes the contents of the scanned barcode to the ClockOptions.
     */
    public void processScan(){
        Intent optionsScreen = new Intent(ScanBadge.this, ClockOptions.class);
        optionsScreen.putExtra("barcode", barcode);
        ScanBadge.this.startActivity(optionsScreen);
    }

}

