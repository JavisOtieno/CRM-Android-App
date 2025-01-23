package com.example.crm;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.crm.models.Account;
import com.example.crm.models.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;

public class AddVisitActivity extends AppCompatActivity implements OnSuccessListener  {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private Button submitButton;
    private String selectedAccountId="0";
    private String selectedLocationId="0";
    private EditText statusEditText;
    private Boolean isTaskRunning = false;

    private EditText accountEditText;
    private EditText locationEditText;

    private String account;
    private String location;
    private String status;
    private String time;
    private String dateToBeSaved;

    private Integer checkedStatus=-1;
    private Integer checkedAccount=-1;
    private Integer checkedLocation=-1;
    private String endpoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_visit);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Add Visit");

        endpoint ="accountslocations";
        NetworkUtils.
                fetchDataGet(endpoint,
                        "",AddVisitActivity.this,
                        AddVisitActivity.this);

        submitButton = (Button)findViewById(R.id.submitButton);

        accountEditText = (EditText)findViewById(R.id.accountEditText);
        locationEditText = (EditText)findViewById(R.id.locationEditText);
        statusEditText = (EditText)findViewById(R.id.statusEditText);

        statusEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStatusDialog(view);
            }
        });

        EditText timeInput = findViewById(R.id.timeInput);
        Calendar calendar = Calendar.getInstance();
        int hourNow = calendar.get(Calendar.HOUR_OF_DAY);
        int minuteNow = calendar.get(Calendar.MINUTE);
        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH);
        int dayNow = calendar.get(Calendar.DAY_OF_MONTH);

        // Display the current date in EditText
        timeInput.setText(String.format(Locale.getDefault(), "%02d:%02d",
                hourNow, minuteNow));




        timeInput.setOnClickListener(v -> {

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // Display the current date in EditText
            timeInput.setText(String.format(Locale.getDefault(), "%02d:%02d",
                    hour, minute));


            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, selectedHour, selectedMinute) -> {
                        timeInput.setText(String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute));
                    }, hour, minute, true);

            timePickerDialog.show();
        });

        EditText dateInput = findViewById(R.id.dateInput);

        dateInput.setText(String.format(Locale.getDefault(), "%02d-%02d-%04d",
                dayNow, monthNow + 1, yearNow));
        dateToBeSaved=String.format(Locale.getDefault(), "%04d-%02d-%02d",
                yearNow, monthNow + 1,dayNow);

        dateInput.setOnClickListener(v -> {
//            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Set selected date to EditText
                        dateInput.setText(String.format(Locale.getDefault(), "%02d-%02d-%04d",
                                selectedDay, selectedMonth + 1, selectedYear));
                        dateToBeSaved=String.format(Locale.getDefault(), "%04d-%02d-%02d",
                                selectedYear, selectedMonth + 1, selectedDay);
                    }, year, month, day);

            datePickerDialog.show();
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toasts.toastIconError(AddAccountActivity.this,"Toast");

                //   longitude = longEditText.getText().toString();
                //   latitude = latEditText.getText().toString();
                if (!isTaskRunning) {
                    isTaskRunning = true;


                    status = statusEditText.getText().toString().toLowerCase();
                    account = selectedAccountId;
                    location = selectedLocationId;
                    time = timeInput.getText().toString();

                    System.out.println("Request Body: "+status+", "+account+", "+location);

                    JSONObject requestBody = new JSONObject();
                    try {
                        requestBody.put("status", status);
                        requestBody.put("location_id", location);
                        requestBody.put("date",dateToBeSaved);
                        requestBody.put("time",time);
                        requestBody.put("account_id",account);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    endpoint = "addvisit";

                    NetworkUtils.
                            fetchDataPost(endpoint,
                                    "", requestBody,AddVisitActivity.this,
                                    AddVisitActivity.this);



//                    new JsonTask().execute(Constants.BASE_URL + "api/addf", "POST",
//                            userId, outletId, visitId, feedback);
//
                }


            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void showStatusDialog(final View v) {
        final String[] array = new String[]{
                "Pending", "Complete", "Cancelled"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Status");


        builder.setSingleChoiceItems(array, checkedStatus, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(array[i]);
                dialogInterface.dismiss();
                checkedStatus = i;
            }
        });
        builder.show();
    }

    @Override
    public void onSuccess(ResponseBody responseBody) {

        String responseBodyString = null;
        try {
            String result = responseBody.string();
            System.out.println("Result: "+result);

            if (result != null && result.contains("Visit")
                    && endpoint.contains("addvisit")){

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("status") &&
                        jsonObject.get("status") instanceof String &&
                        jsonObject.get("status").equals("success")
                ) {

                    Toasts.toastIconSuccess(AddVisitActivity.this,"Visit Added Successfully");
                    Intent intent = new Intent(AddVisitActivity.this, VisitsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
                else if(result.contains("errors")){
                    String message = jsonObject.getString("message");
                    Toasts.toastIconError(AddVisitActivity.this,message);
                    //Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
                else{

                    Toasts.toastIconError(AddVisitActivity.this,"Error Adding Visit. Please Try Again");
//                    FirebaseCrashlytics.getInstance().recordException(new Exception("Add Deal Error: "+result));

                }

            }
            else if(result != null && result.contains("\"accounts\":")
            && endpoint.contains("accountslocations")) {

                try {
                    // Create a JSONObject from the response string
                    JSONArray jsonArray = new JSONArray(result);
                    List<Account> items = new ArrayList<>();
                    List<String> accountNames = new ArrayList<>();
                    List<String> accountIds = new ArrayList<>();
                    List<String> locationNames = new ArrayList<>();
                    List<String> locationIds = new ArrayList<>();
                    Map<String, List<Location>> locationsMap = new HashMap<>();

                    JSONArray accounts = jsonArray.getJSONObject(0).
                            getJSONArray("accounts");
                    JSONArray locations = jsonArray.getJSONObject(0).
                            getJSONArray("locations");
                    System.out.println("Accounts length: "+accounts.length());
                    System.out.println("Locations length: "+locations.length());

                    for (int i = 0; i < accounts.length(); i++) {
                        accountNames.add(accounts.getJSONObject(i).getString("name"));
                        accountIds.add(accounts.getJSONObject(i).getString("id"));
                    }

                    for (int i = 0; i < locations.length(); i++) {
                        locationNames.add(locations.getJSONObject(i).getString("name"));
                        locationIds.add(locations.getJSONObject(i).getString("id"));

                        String accountId = locations.getJSONObject(i).getString("account_id");
                        String locationName = locations.getJSONObject(i).getString("name");
                        String locationId = locations.getJSONObject(i).getString("id");

                        // Create a new Location object
                        Location location = new Location();
                        location.name = locationName;
                        location.id = locationId;

                        // If the accountId is not already in the map, create a new list
                        if (!locationsMap.containsKey(accountId)) {
                            locationsMap.put(accountId,new ArrayList<Location>());
                        }
                        // Add the location to the appropriate account's list
                        locationsMap.get(accountId).add(location);

                    }

                    String[] accountNamesArray = accountNames.toArray(new String[0]);
                    String[] accountIdsArray = accountIds.toArray(new String[0]);
                    String[] locationNamesArray = locationNames.toArray(new String[0]);
                    String[] locationIdsArray = locationIds.toArray(new String[0]);

                    accountEditText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showAccountDialog(view, accountNamesArray, accountIdsArray,"Accounts");
                        }
                    });

                    for (int i = 0; i < locations.length(); i++) {
                        locationNames.add(locations.getJSONObject(i).getString("name"));
                        locationIds.add(locations.getJSONObject(i).getString("id"));

                    }

                    locationEditText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showLocationsDialog(view, locationNamesArray, locationIdsArray,locationsMap,"Locations");

//                            showAccountDialog(view, locationNamesArray, locationIdsArray,"Locations");
                        }
                    });


                } catch(Exception e){
                    Toasts.toastIconError(AddVisitActivity.this, "Error Loading");
                    Log.e("API Response", "JSON Parsing error: " + e.getMessage());
                }


            }else{

                Toasts.toastIconError(AddVisitActivity.this,"Add Visit Failed. Please Try Again");
//                FirebaseCrashlytics.getInstance().recordException(new Exception("Add Deal Error: "+result));

            }

//            Log.d("API Success", "Data: " + jsonObject.toString());


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onComplete() {
        isTaskRunning = false;
    }

    private void showAccountDialog(final View v,String[] accountOptions, String[] Ids,
                                   String title) {
        final String[] array = accountOptions;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        Integer checkedItem;
        if(title.equals("Accounts")){
            checkedItem=checkedAccount;
        }else{
            checkedItem=checkedLocation;
        }
        builder.setSingleChoiceItems(array, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(array[i]);
                dialogInterface.dismiss();
                if(title.equals("Accounts")){
                    checkedAccount = i;
                    selectedAccountId = Ids[i];
                    locationEditText.setText("");
                    selectedLocationId="0";
                    checkedLocation = -1;
                }else{
                    checkedLocation= i;
                    selectedLocationId = Ids[i];
                }

            }
        });
        builder.show();
    }

    private void showLocationsDialog(final View v,String[] locationOptions, String[] Ids, Map<String, List<Location>> locationsMap,
                                    String title) {
        final String[] array = locationOptions;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        Integer checkedItem;

        checkedItem=checkedLocation;


        if(selectedAccountId.equals("0")){
//            Toasts.toastIconError(AddCallActivity.this,selectedAccountId+" out ");
            builder.setSingleChoiceItems(array, checkedItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ((EditText) v).setText(array[i]);
                    dialogInterface.dismiss();
                    checkedLocation= i;
                    selectedLocationId = Ids[i];
                }
            });

        }else{
//            Toasts.toastIconError(AddCallActivity.this,selectedAccountId+" right here");
            builder.setSingleChoiceItems(getLocationNamesForAccount(selectedAccountId,locationsMap), checkedItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Set the selected location name in the EditText view
                    ((EditText) v).setText(getLocationNamesForAccount(selectedAccountId,locationsMap)[i]);

                    // Dismiss the dialog
                    dialogInterface.dismiss();

                    // Update checked location index and location ID
                    checkedLocation = i;

                    // Get the location ID from the map using the selected index
                    selectedLocationId = getLocationIdsForAccount(selectedAccountId,locationsMap).get(i);
                }
            });


        }


        builder.show();
    }

    // Method to get location names for a specific account
    private String[] getLocationNamesForAccount(String accountId,
                                               Map<String, List<Location>> locationsMap
    ) {
        List<Location> locationsList = locationsMap.get(accountId);
        if (locationsList == null) {
            return new String[]{};
        }

        String[] locationNames = new String[locationsList.size()];
        for (int i = 0; i < locationsList.size(); i++) {
            locationNames[i] = locationsList.get(i).name;
        }
        return locationNames;
    }

    private List<String> getLocationIdsForAccount(String accountId,
                                                 Map<String, List<Location>> locationsMap) {
        List<Location> locationsList = locationsMap.get(accountId);
        List<String> locationIds = new ArrayList<>();

        if (locationsList != null) {
            for (Location location : locationsList) {
                locationIds.add(location.id);
            }
        }

        return locationIds;
    }






}