package com.github.gfsclock.gfstimeclock;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class ClockOptions extends AppCompatActivity {

    /**
     * Entry point to the ClockOptions activity, handles result from barcode intent and
     * makes initial api call to get employee punches.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_options);

        Intent intent = getIntent();
        String id = intent.getStringExtra("barcode");
        employeeID =  Integer.parseInt(id.substring(id.length()-5, id.length()));
        punches = mapper.getPunchesID(employeeID);
        employeeIdTextView = (TextView) findViewById(R.id.employeeIdTextView);
        employeeIdTextView.setText(id);
    }



    private int employeeID = 0;
    private APIMapper mapper = APIMapper.getInstance();
    private ArrayList<PunchModel> punches;
    private TextView employeeIdTextView;

    /**
     * Shows alert dialog with punch history on button press.
     * @param view
     */
    public void showPunchHistoryDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(ClockOptions.this);
        builder.setTitle(getString(R.string.punch_history));
        // TODO filter history for only last 3 days
        String punchHistory = "";
        for (int i = 0; i < punches.size(); i++) {
            punchHistory += punches.get(i).getDocket()+ " " + punches.get(i).getTimeStamp() + "\n";
        }
        builder.setMessage(punchHistory);
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

    /**
     * Clocks employee in on button press.
     * @param view
     */
    public void clockIn(View view){
        mapper.punch(employeeID, "F1", new Date());
        backToScanBadge();
    }

    /**
     * Clocks employee out for break on button press.
     * @param view
     */
    public void breakOut(View view){
        mapper.punch(employeeID, "F2", new Date());
        backToScanBadge();
    }

    /**
     * Clocks employee out for lunch on button press.
     * @param view
     */
    public void lunchOut(View view){
        mapper.punch(employeeID, "F3", new Date());
        backToScanBadge();
    }

    /**
     * Clocks employee out for the day on button press.
     * @param view
     */
    public void clockOut(View view){
        mapper.punch(employeeID, "F5", new Date());
        backToScanBadge();
    }

    /**
     * Clocks employee in from break on button press.
     * @param view
     */
    public void breakIn(View view) {
        mapper.punch(employeeID, "F6", new Date());
        backToScanBadge();
    }

    /**
     * Clocks employee in from lunch on button press.
     * @param view
     */
    public void lunchIn(View view){
        mapper.punch(employeeID, "F7", new Date());
        backToScanBadge();
    }

    /**
     * Processes job change on button press.
     * @param view
     */
    public void changeJob(View view){
        backToScanBadge();
        // TODO disabled for now need to get clarification on API functionality
    }

    /**
     * Method called after any activity to return to ScanBadge activity.
     */
    public void backToScanBadge(){
        Intent backToScanBadge = new Intent(ClockOptions.this, ScanBadge.class);
        ClockOptions.this.startActivity(backToScanBadge);
    }
}
