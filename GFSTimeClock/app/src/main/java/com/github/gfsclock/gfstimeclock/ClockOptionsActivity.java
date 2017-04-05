package com.github.gfsclock.gfstimeclock;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ClockOptionsActivity extends AppCompatActivity {

    private int employeeID = 0;
    private APIMapper mapper = APIMapper.getInstance();
    private List<PunchModel> punches;
    private TextView employeeIdTextView;
    private static final String TAG = "ClockOptionsActivity";


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

        employeeIdTextView = (TextView) findViewById(R.id.employeeIdTextView);
        employeeIdTextView.setText(Integer.toString(id));
        int intID = id;
        getEmployeeInfo(intID);

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
            }

            @Override
            public void onFailure(Call<List<PunchModel>> call, Throwable t) {
                Log.d(TAG, "no punches" + t.toString());
            }
        });
    }

    /**
     * Clocks employee in on button press.
     *
     * @param view
     */
    public void clockIn(View view) {
        submitPunches(employeeID, "F1");
        backToScanBadge();
    }

    private void submitPunches(int id, String docket) {

        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(Startup.getContext());
        String username = sPref.getString("username", "");
        String password = sPref.getString("password", "");
        PunchQueryService punchClient = APIServiceGenerator.createService(PunchQueryService.class, username, password);

        PunchModel punch = new PunchModel();
        punch.setDocket(docket);
        punch.setpayroll(id);
        punch.setTimestamp(new Date());

        Call<ResponseBody> call = punchClient.submitPunchesByDate(punch);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "punch worked!?!? !" + response.toString());

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "no punches" + t.toString());
            }
        });
    }

    private void getEmployeeInfo(int id) {
        final int employee = id;
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(Startup.getContext());
        String username = sPref.getString("username", "");
        String password = sPref.getString("password", "");
        employeeIdTextView = (TextView) findViewById(R.id.employeeIdTextView);
        EmployeeQueryService infoClient = InfoServiceGenerator.createService(EmployeeQueryService.class, username, password);
        Call<EmployeeAPIContainer> call = infoClient.getData(id);
        call.enqueue(new Callback<EmployeeAPIContainer>() {
            @Override
            public void onResponse(Call<EmployeeAPIContainer> call, Response<EmployeeAPIContainer> response) {
                if (response.isSuccessful()) {
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


    public void setValidation(){
        PunchModel latest = punches.get(punches.size() - 1);
        String lastPunch = latest.getDocket();
        // bind buttons
//    Button clockinButton = (Button) findViewById(R.id.ClockInButton);
//    Button clockOutButton = (Button) findViewById(R.id.ClockOutButton);
//    Button breakInButton = (Button) findViewById(R.id.BreakInButton);
//    Button breakOutButton = (Button) findViewById(R.id.BreakOutButton);
//    Button lunchInButton = (Button) findViewById(R.id.LunchInButton);
//    Button lunchOutButton = (Button) findViewById(R.id.LunchOutButton);
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
        submitPunches(employeeID, "F2");
        backToScanBadge();
    }

    /**
     * Clocks employee out for lunch on button press.
     *
     * @param view
     */
    public void lunchOut(View view) {
        submitPunches(employeeID, "F3");
        backToScanBadge();
    }

    /**
     * Clocks employee out for the day on button press.
     *
     * @param view
     */
    public void clockOut(View view) {
        submitPunches(employeeID, "F5");
        backToScanBadge();
    }

    /**
     * Clocks employee in from break on button press.
     *
     * @param view
     */
    public void breakIn(View view) {
        submitPunches(employeeID, "F6");
        backToScanBadge();
    }

    /**
     * Clocks employee in from lunch on button press.
     *
     * @param view
     */
    public void lunchIn(View view) {
        submitPunches(employeeID, "F7");
        backToScanBadge();
    }

    /**
     * Processes job change on button press.
     *
     * @param view
     */
    public void changeJob(View view) {
        backToScanBadge();
        // TODO disabled for now need to get clarification on API functionality
    }

    /**
     * Method called after any activity to return to ScanBadgeActivity activity.
     */
    public void backToScanBadge() {
        Intent backToScanBadge = new Intent(ClockOptionsActivity.this, ScanBadgeActivity.class);
        ClockOptionsActivity.this.startActivity(backToScanBadge);
    }
}
