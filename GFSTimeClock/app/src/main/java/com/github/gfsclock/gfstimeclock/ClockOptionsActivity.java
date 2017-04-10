package com.github.gfsclock.gfstimeclock;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ClockOptionsActivity extends AppCompatActivity {

    private int employeeID = 0;
    private APIMapper mapper = APIMapper.getInstance();
    private List<PunchModel> punches;
    private TextView employeeIdTextView;
    private ImageView employeePic;
    private static final String TAG = "ClockOptionsActivity";
    private Realm realm;
    private String jobCode;
    private PunchSync punchSync = new PunchSync();


    /**
     * Entry point to the ClockOptionsActivity activity, handles result from barcode intent and
     * makes initial api call to get employee punches.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("ClockOptionsActivity");
        setContentView(R.layout.activity_clock_options);
        Intent intent = getIntent();
        int id = intent.getIntExtra("barcode", 0);
        employeeID = id;
        getEmployeeInfo(employeeID);
        employeePic = (ImageView) findViewById(R.id.userImage);
        employeeIdTextView = (TextView) findViewById(R.id.employeeIdTextView);

        int intID = id;
        getEmployeeInfo(intID);

    }

    @Override
    public void onDestroy() {
        punchSync.cancel(true);
        super.onDestroy();
    }


    /**
     * Obtain initial punch history of a given employee (id).
     * @param id
     */
    private void getPunchesID(int id) {
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(Startup.getContext());
        String username = sPref.getString("username", "");
        String password = sPref.getString("password", "");
        PunchQueryService punchClient = APIServiceGenerator.createService(PunchQueryService.class, username, password);
        String employeeId = Integer.toString(id);
        Date punchDate = new Date();
        Call<List<PunchModel>> call = punchClient.getPunchesByID(new PunchList(employeeId, punchDate));
        call.enqueue(new Callback<List<PunchModel>>() {
            @Override
            public void onResponse(Call<List<PunchModel>> call, Response<List<PunchModel>> response) {
                Log.d(TAG, "response worked!" + response.toString());
                punches = response.body();
                setValidation();
                punchSync.execute();
                // TODO: Disable and enable punches as needed; may need another method
            }

            @Override
            public void onFailure(Call<List<PunchModel>> call, Throwable t) {
                Log.d(TAG, "no punches" + t.toString());
                // No connection, no validation
            }
        });
    }

    /**
     * Clocks employee in on button press.
     *
     * @param view
     */
    public void clockIn(View view) {
        submitPunches(employeeID, "F1", null);
        backToScanBadge();
    }

    private void submitPunches(int id, String docket, String jobCode) {

        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(Startup.getContext());
        String username = sPref.getString("username", "");
        String password = sPref.getString("password", "");
        PunchQueryService punchClient = APIServiceGenerator.createService(PunchQueryService.class, username, password);

        final PunchModel punch = new PunchModel();
        punch.setDocket(docket);
        punch.setpayroll(id);
        punch.setTimestamp(new Date());
        if(docket == "F4"){
            punch.setJobCode(jobCode);
        }
        Call<ResponseBody> call = punchClient.submitPunchesByDate(punch);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "punch worked!?!? !" + response.toString());

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "no punches" + t.toString());
                // TODO: Cache Punches
                realm = Realm.getDefaultInstance();
                realm.beginTransaction();

                final PunchModel cachedPunch = realm.copyToRealm(punch);

                realm.commitTransaction();
                realm.close();
            }
        });
    }

    private void getEmployeeInfo(int id) {
        final int employee = id;
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(Startup.getContext());
        String username = sPref.getString("username", "");
        String password = sPref.getString("password", "");

        EmployeeQueryService infoClient = InfoServiceGenerator.createService(EmployeeQueryService.class, username, password);
        Call<EmployeeAPIContainer> call = infoClient.getData(id);
        call.enqueue(new Callback<EmployeeAPIContainer>() {
            @Override
            public void onResponse(Call<EmployeeAPIContainer> call, Response<EmployeeAPIContainer> response) {
                if (response.isSuccessful()) {
                    Ded dead = response.body().getDed();
                    String pic = response.body().getPictureUrl();
                    employeeIdTextView.setText(dead.getCommonName() +"  "+  dead.getJobCode());
                    Picasso.with(ClockOptionsActivity.this).load(pic).resize(250,250).into(employeePic);
                    getPunchesID(employee);
                    System.out.println("Response Successful");
                    // do things populate employee name and picture
                    Log.d(TAG, response.message());
                    return;
                } else {
                    // error or no connnection
                    System.out.println("Response not successful.\n" + response.toString());
                    Log.d(TAG, response.toString());
                    backToScanBadge();
                }
            }

            public void onFailure(Call<EmployeeAPIContainer> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                // At this point, we can't connect to the employee data server to verify
                // TODO: Don't validate, present entire view ready to use
            }
        });
    }

    /**
     * Rather than transition back to the ManualInputActivity we must override this onBackPressed() method.
     */
    @Override
    public void onBackPressed() {
        backToScanBadge();
    }


    public void setValidation() {
        Button clockinButton = (Button) findViewById(R.id.ClockInButton);
        Button clockOutButton = (Button) findViewById(R.id.ClockOutButton);
        Button breakInButton = (Button) findViewById(R.id.BreakInButton);
        Button breakOutButton = (Button) findViewById(R.id.BreakOutButton);
        Button lunchInButton = (Button) findViewById(R.id.LunchInButton);
        Button lunchOutButton = (Button) findViewById(R.id.LunchOutButton);


        if (punches.size() == 0) {
            // if not punches then only set Clock In to enabled.
            clockinButton.setEnabled(true);
            clockOutButton.setEnabled(false);
            breakInButton.setEnabled(false);
            breakOutButton.setEnabled(false);
            lunchInButton.setEnabled(false);
            lunchOutButton.setEnabled(false);
            return;
        } else {
            PunchModel latest = punches.get(punches.size() - 1);
            String lastPunch = latest.getDocket();

            switch (lastPunch) {
                case "F1":
                    // the most recent punch was start day
                    clockinButton.setEnabled(false);
                    clockOutButton.setEnabled(true);
                    breakInButton.setEnabled(false);
                    breakOutButton.setEnabled(true);
                    lunchInButton.setEnabled(false);
                    lunchOutButton.setEnabled(true);
                    break;
                case "F2":
                    clockinButton.setEnabled(false);
                    clockOutButton.setEnabled(false);
                    breakInButton.setEnabled(true);
                    breakOutButton.setEnabled(false);
                    lunchInButton.setEnabled(false);
                    lunchOutButton.setEnabled(false);
                    break;
                case "F3":
                    clockinButton.setEnabled(false);
                    clockOutButton.setEnabled(false);
                    breakInButton.setEnabled(false);
                    breakOutButton.setEnabled(false);
                    lunchInButton.setEnabled(true);
                    lunchOutButton.setEnabled(false);
                    break;
                case "F5":
                    clockinButton.setEnabled(true);
                    clockOutButton.setEnabled(false);
                    breakInButton.setEnabled(false);
                    breakOutButton.setEnabled(false);
                    lunchInButton.setEnabled(false);
                    lunchOutButton.setEnabled(false);
                    break;
                case "F6":
                    clockinButton.setEnabled(false);
                    clockOutButton.setEnabled(true);
                    breakInButton.setEnabled(false);
                    breakOutButton.setEnabled(true);
                    lunchInButton.setEnabled(false);
                    lunchOutButton.setEnabled(true);
                    break;
                case "F7":
                    clockinButton.setEnabled(false);
                    clockOutButton.setEnabled(true);
                    breakInButton.setEnabled(false);
                    breakOutButton.setEnabled(true);
                    lunchInButton.setEnabled(false);
                    lunchOutButton.setEnabled(true);
                    break;

            }
        }
    }

    /**
     * Shows alert dialog with punch history on button press.
     *
     * @param view
     */
    public void showPunchHistoryDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ClockOptionsActivity.this);
        builder.setTitle(getString(R.string.punch_history));
        // TODO filter history for only last 3 days
        String punchHistory = "";
        for (int i = 0; i < punches.size(); i++) {
            punchHistory += punches.get(i).getDocket() + " " + punches.get(i).getTimestamp() + "\n";
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
     * Clocks employee out for break on button press.
     *
     * @param view
     */
    public void breakOut(View view) {
        submitPunches(employeeID, "F2", null);
        backToScanBadge();
    }

    /**
     * Clocks employee out for lunch on button press.
     *
     * @param view
     */
    public void lunchOut(View view) {
        submitPunches(employeeID, "F3", null);
        backToScanBadge();
    }

    /**
     * Clocks employee out for the day on button press.
     *
     * @param view
     */
    public void clockOut(View view) {
        submitPunches(employeeID, "F5", null);
        backToScanBadge();
    }

    /**
     * Clocks employee in from break on button press.
     *
     * @param view
     */
    public void breakIn(View view) {
        submitPunches(employeeID, "F6", null);
        backToScanBadge();
    }

    /**
     * Clocks employee in from lunch on button press.
     *
     * @param view
     */
    public void lunchIn(View view) {
        submitPunches(employeeID, "F7", null);
        backToScanBadge();
    }

    /**
     * Processes job change on button press.
     *
     * @param view
     */
    public void changeJob(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.enter_job_code);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                jobCode = input.getText().toString();
                Toast.makeText(ClockOptionsActivity.this, R.string.enter_job_code + " " + jobCode,
                        Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        submitPunches(employeeID, "F4", jobCode);
    }

    public void signOut(View view){
        backToScanBadge();
    }

    /**
     * Method called after any activity to return to ScanBadgeActivity activity.
     */
    public void backToScanBadge() {
        Intent backToScanBadge = new Intent(ClockOptionsActivity.this, ScanBadgeActivity.class);
        ClockOptionsActivity.this.startActivity(backToScanBadge);
    }

    private class PunchSync extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {

            // Get Cache Contents
            realm = Realm.getDefaultInstance();
            RealmResults<PunchModel> results = realm.where(PunchModel.class).findAll();
            List<PunchModel> output = new ArrayList<>();
            output.addAll(realm.copyFromRealm(results));
            realm.close();

            // invert the list
            List<PunchModel> invertedOutput = output.subList(0, output.size());
            Collections.reverse(invertedOutput);

            for(PunchModel cachedPunch : invertedOutput) {
                if(isCancelled())
                    break;
                SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(Startup.getContext());
                String username = sPref.getString("username", "");
                String password = sPref.getString("password", "");
                PunchQueryService punchClient = APIServiceGenerator.createService(PunchQueryService.class, username, password);
                final PunchModel pAttempt = cachedPunch;
                final List<PunchModel> index = output;
                Call<ResponseBody> call = punchClient.submitPunchesByDate(pAttempt);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        // Remove Punch
                        realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        RealmResults<PunchModel> vList = realm.where(PunchModel.class).findAll();
                        vList.deleteFromRealm(vList.indexOf(pAttempt));
                        realm.commitTransaction();
                        realm.close();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }

            return null;
        }
    }
}
