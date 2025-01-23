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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;

public class EditMeetingActivity extends AppCompatActivity implements OnSuccessListener  {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private Button submitButton;
    private String selectedAccountId="0";
    private String selectedContactId="0";
    private EditText statusEditText;
    private EditText typeEditText;
    private Boolean isMeetingRunning = false;

    private EditText accountEditText;
    private EditText contactEditText;
    private EditText descriptionEditText;
    private EditText locationEditText;

    private String account;
    private String contact;
    private String status;
    private String dateToBeSaved;
    private String mTime;
    private String mDate;
    private String mMeetingId;

    private Integer checkedStatus=-1;
    private Integer checkedType=-1;
    private Integer checkedAccount=-1;
    private Integer checkedContact=-1;
    private String[] statusArray = new String[]{
            "Pending", "Complete", "Cancelled"
    };
    private String[] typeArray = new String[]{
            "Online", "Physical"
    };
    private String endpoint;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_meeting);
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
        actionBar.setTitle("Edit Meeting");

        endpoint = "accountscontacts";
        NetworkUtils.
                fetchDataGet(endpoint,
                        "",EditMeetingActivity.this,
                        EditMeetingActivity.this);

        submitButton = (Button)findViewById(R.id.submitButton);

        accountEditText = (EditText)findViewById(R.id.accountEditText);
        contactEditText = (EditText)findViewById(R.id.contactEditText);
        statusEditText = (EditText)findViewById(R.id.statusEditText);
        typeEditText = (EditText) findViewById(R.id.typeEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        locationEditText = (EditText) findViewById(R.id.locationEditText);

        String meetingString = getIntent().getStringExtra("meeting");
        System.out.println("meeting: "+meetingString);

        try {
            JSONObject meeting = new JSONObject(meetingString);

            String status = meeting.getString("status");
            mTime = meeting.getString("time");
            mDate = meeting.getString("date");
            mMeetingId = meeting.getString("id");
            status = status.substring(0, 1).toUpperCase() +
                    status.substring(1).toLowerCase();

            checkedStatus = Arrays.asList(statusArray).indexOf(status);
            statusEditText.setText(status);

            String type = meeting.getString("type");
            type = type.substring(0, 1).toUpperCase() +
                    type.substring(1).toLowerCase();
            checkedType = Arrays.asList(typeArray).indexOf(type);
            typeEditText.setText(type);

            accountEditText.setText(meeting.getJSONObject("account").getString("name"));
            contactEditText.setText(meeting.getJSONObject("contact").getString("name"));
            descriptionEditText.setText(meeting.getString("description"));
            locationEditText.setText(meeting.getString("location"));

            selectedAccountId = meeting.getJSONObject("account").getString("id");
            selectedContactId = meeting.getJSONObject("contact").getString("id");

//            Toasts.toastIconError(EditCallActivity.this,checkedAccount+" "+checkedContact);


            // Use the values as needed
        } catch (Exception e) {
            e.printStackTrace();
        }

        statusEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStatusDialog(view);
            }
        });

        typeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTypeDialog(view);
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
        // Display the current date in EditText
        timeInput.setText(mTime);

        timeInput.setOnClickListener(v -> {

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // Display the current date in EditText
            timeInput.setText(mTime);

            LocalTime localTime = LocalTime.parse(mTime);
            int dbHour = localTime.getHour();  // 9
            int dbMinute = localTime.getMinute();

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, selectedHour, selectedMinute) -> {
                        String timeSelected = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
                        timeInput.setText(timeSelected);
                        mTime = timeSelected;
                    }, dbHour, dbMinute, true);

            timePickerDialog.show();
        });

        EditText dateInput = findViewById(R.id.dateInput);

        LocalDate localDate = LocalDate.parse(mDate);
        int day = localDate.getDayOfMonth();
        int month = localDate.getMonthValue();
        int year = localDate.getYear();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = localDate.format(formatter);

        dateInput.setText(formattedDate);

        dateToBeSaved = String.format(Locale.getDefault(), "%04d-%02d-%02d",
                year, month , day);

        dateInput.setOnClickListener(v -> {
            LocalDate localDateUpdated = LocalDate.parse(dateToBeSaved);
            int dayUpdated = localDateUpdated.getDayOfMonth();
            int monthUpdated = localDateUpdated.getMonthValue();
            int yearUpdated = localDateUpdated.getYear();
//            Calendar calendar = Calendar.getInstance();
//            int year = calendar.get(Calendar.YEAR);
//            int month = calendar.get(Calendar.MONTH);
//            int day = calendar.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Set selected date to EditText
                        dateInput.setText(String.format(Locale.getDefault(), "%02d-%02d-%04d",
                                selectedDay, selectedMonth + 1, selectedYear));
                        dateToBeSaved = String.format(Locale.getDefault(), "%04d-%02d-%02d",
                                selectedYear, selectedMonth + 1, selectedDay);

                    }, yearUpdated , monthUpdated-1, dayUpdated);

            datePickerDialog.show();
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toasts.toastIconError(AddAccountActivity.this,"Toast");

                //   longitude = longEditText.getText().toString();
                //   latitude = latEditText.getText().toString();
                if (!isMeetingRunning) {
                    isMeetingRunning = true;


                    status = statusEditText.getText().toString().toLowerCase();
                    String type = typeEditText.getText().toString().toLowerCase();
                    account = selectedAccountId;
                    contact = selectedContactId;
//                    String date = dateInput.getText().toString();
                    String time = timeInput.getText().toString();
                    String description = descriptionEditText.getText().toString();
                    String location = locationEditText.getText().toString();



                    System.out.println("Request Body: "+status+", "+account+", "+contact);

                    JSONObject requestBody = new JSONObject();
                    try {
                        requestBody.put("status", status);
                        requestBody.put("type", type);
                        requestBody.put("contact_id", contact);
                        requestBody.put("account_id",account);
                        requestBody.put("description",description);
                        requestBody.put("location",location);
                        requestBody.put("date",dateToBeSaved);
                        requestBody.put("time",time);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    endpoint = "submiteditmeeting/"+mMeetingId;
                    NetworkUtils.
                            fetchData(endpoint,"PUT",
                                    requestBody,EditMeetingActivity.this,
                                    EditMeetingActivity.this);


//                    new JsonMeeting().execute(Constants.BASE_URL + "api/addf", "POST",
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


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Status");


        builder.setSingleChoiceItems(statusArray, checkedStatus, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(statusArray[i]);
                dialogInterface.dismiss();
                checkedStatus = i;
            }
        });
        builder.show();
    }

    private void showTypeDialog(final View v) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Type");


        builder.setSingleChoiceItems(typeArray, checkedType, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(typeArray[i]);
                dialogInterface.dismiss();
                checkedType = i;
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

            if (result != null && result.contains("Meeting")
                    && endpoint.contains("submiteditmeeting")){

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("status") &&
                        jsonObject.get("status") instanceof String &&
                        jsonObject.get("status").equals("success")
                ) {
                    String message = jsonObject.getString("message");

                    Toasts.toastIconSuccess(EditMeetingActivity.this,message);
                    Intent intent = new Intent(EditMeetingActivity.this, MeetingsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
                else if(result.contains("errors")){
                    String message = jsonObject.getString("message");
                    Toasts.toastIconError(EditMeetingActivity.this,message);


                    //Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
                else{

                    Toasts.toastIconError(EditMeetingActivity.this,"Error Editing Meeting. Please Try Again");
//                    FirebaseCrashlytics.getInstance().recordException(new Exception("Add Deal Error: "+result));

                }

            }
            else if(result != null && result.contains("\"accounts\":")
                    && !endpoint.contains("submiteditmeeting")) {

                try {
                    // Create a JSONObject from the response string
                    JSONArray jsonArray = new JSONArray(result);
                    List<Account> items = new ArrayList<>();
                    List<String> accountNames = new ArrayList<>();
                    List<String> accountIds = new ArrayList<>();
                    List<String> contactNames = new ArrayList<>();
                    List<String> contactIds = new ArrayList<>();

                    JSONArray accounts = jsonArray.getJSONObject(0).
                            getJSONArray("accounts");
                    JSONArray contacts = jsonArray.getJSONObject(0).
                            getJSONArray("contacts");
                    System.out.println("Accounts length: "+accounts.length());
                    System.out.println("Contacts length: "+contacts.length());

                    for (int i = 0; i < accounts.length(); i++) {
                        accountNames.add(accounts.getJSONObject(i).getString("name"));
                        String accountId = accounts.getJSONObject(i).getString("id");
                        accountIds.add(accountId);
                        if(accountId.equals(selectedAccountId)){
                            checkedAccount = i;
                        }
                    }

                    for (int i = 0; i < contacts.length(); i++) {
                        contactNames.add(contacts.getJSONObject(i).getString("name"));
                        String contactId = contacts.getJSONObject(i).getString("id");
                        contactIds.add(contactId);
                        if(contactId.equals(selectedContactId)){
                            checkedContact = i;
                        }

                    }

                    String[] accountNamesArray = accountNames.toArray(new String[0]);
                    String[] accountIdsArray = accountIds.toArray(new String[0]);
                    String[] contactNamesArray = contactNames.toArray(new String[0]);
                    String[] contactIdsArray = contactIds.toArray(new String[0]);

                    accountEditText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showAccountDialog(view, accountNamesArray, accountIdsArray,"Accounts");
                        }
                    });

                    for (int i = 0; i < contacts.length(); i++) {
                        contactNames.add(contacts.getJSONObject(i).getString("name"));
                        contactIds.add(contacts.getJSONObject(i).getString("id"));

                    }

                    contactEditText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showAccountDialog(view, contactNamesArray, contactIdsArray,"Contacts");
                        }
                    });


                } catch(Exception e){
                    Toasts.toastIconError(EditMeetingActivity.this, "Error Loading");
                    Log.e("API Response", "JSON Parsing error: " + e.getMessage());
                }


            }else{

                Toasts.toastIconError(EditMeetingActivity.this,"Edit Meeting Failed. Please Try Again");
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
        isMeetingRunning = false;
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
            checkedItem=checkedContact;
        }
        builder.setSingleChoiceItems(array, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(array[i]);
                dialogInterface.dismiss();
                if(title.equals("Accounts")){
                    checkedAccount = i;
                    selectedAccountId = Ids[i];
                }else{
                    checkedContact= i;
                    selectedContactId = Ids[i];
                }

            }
        });
        builder.show();
    }



}