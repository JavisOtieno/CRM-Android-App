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

public class EditTaskActivity extends AppCompatActivity implements OnSuccessListener  {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private Button submitButton;
    private String selectedAccountId="0";
    private String selectedContactId="0";
    private EditText statusEditText;
    private Boolean isTaskRunning = false;

    private EditText accountEditText;
    private EditText contactEditText;

    private EditText descriptionEditText;
    private EditText nameEditText;

    private String account;
    private String contact;
    private String status;
    private String time;

    private Integer checkedStatus=-1;
    private Integer checkedAccount=-1;
    private Integer checkedContact=-1;
    private String dateToBeSaved;
    private String mTime;
    private String mDate;
    private String mTaskId;
    private String[] statusArray = new String[]{
            "Pending", "Complete", "Cancelled"
    };
    private String endpoint;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_task);
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
        actionBar.setTitle("Edit Task");

        endpoint = "accountscontacts";
        NetworkUtils.
                fetchDataGet(endpoint,
                        "",EditTaskActivity.this,
                        EditTaskActivity.this);

        submitButton = (Button)findViewById(R.id.submitButton);

        accountEditText = (EditText)findViewById(R.id.accountEditText);
        contactEditText = (EditText)findViewById(R.id.contactEditText);
        statusEditText = (EditText)findViewById(R.id.statusEditText);
        descriptionEditText = (EditText)findViewById(R.id.descriptionEditText);
        nameEditText = (EditText)findViewById(R.id.nameEditText);

        String taskString = getIntent().getStringExtra("task");
        System.out.println("task: "+taskString);

        try {
            JSONObject task = new JSONObject(taskString);

            String status = task.getString("status");
            mTime = task.getString("time");
            mDate = task.getString("date");
            mTaskId = task.getString("id");
            status = status.substring(0, 1).toUpperCase() +
                    status.substring(1).toLowerCase();

            checkedStatus = Arrays.asList(statusArray).indexOf(status);

            statusEditText.setText(status);
            accountEditText.setText(task.getJSONObject("account").getString("name"));
            contactEditText.setText(task.getJSONObject("contact").getString("name"));
            descriptionEditText.setText(task.getString("description"));
            nameEditText.setText(task.getString("name"));
            selectedAccountId = task.getJSONObject("account").getString("id");
            selectedContactId = task.getJSONObject("contact").getString("id");

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


        EditText timeInput = findViewById(R.id.timeInput);
        Calendar calendar = Calendar.getInstance();
        int hourNow = calendar.get(Calendar.HOUR_OF_DAY);
        int minuteNow = calendar.get(Calendar.MINUTE);
        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH);
        int dayNow = calendar.get(Calendar.DAY_OF_MONTH);

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
                if (!isTaskRunning) {
                    isTaskRunning = true;


                    status = statusEditText.getText().toString().toLowerCase();
                    account = selectedAccountId;
                    contact = selectedContactId;
                    String name = nameEditText.getText().toString();
                    String description = descriptionEditText.getText().toString();
                    time = timeInput.getText().toString();

                    System.out.println("Request Body: "+status+", "+account+", "+contact);

                    JSONObject requestBody = new JSONObject();
                    try {
                        requestBody.put("status", status);
                        requestBody.put("name", name);
                        requestBody.put("description",description);
                        requestBody.put("contact_id", contact);
                        requestBody.put("account_id",account);
                        requestBody.put("date",dateToBeSaved);
                        requestBody.put("time",time);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    endpoint = "submitedittask/"+mTaskId;
                    NetworkUtils.
                            fetchData("PUT",endpoint,
                                     requestBody,EditTaskActivity.this,
                                    EditTaskActivity.this);




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

    @Override
    public void onSuccess(ResponseBody responseBody) {

        String responseBodyString = null;
        try {
            String result = responseBody.string();
            System.out.println("Result: "+result);

            if (result != null && result.contains("Task") &&
                    endpoint.contains("submitedittask")){

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("status") &&
                        jsonObject.get("status") instanceof String &&
                        jsonObject.get("status").equals("success")
                ) {
                    String message = jsonObject.getString("message");

                    Toasts.toastIconSuccess(EditTaskActivity.this,message);
                    Intent intent = new Intent(EditTaskActivity.this, TasksActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
                else if(result.contains("errors")){
                    String message = jsonObject.getString("message");
                    Toasts.toastIconError(EditTaskActivity.this,message);
                    //Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
                else{

                    Toasts.toastIconError(EditTaskActivity.this,"Error Adding Task. Please Try Again");
//                    FirebaseCrashlytics.getInstance().recordException(new Exception("Add Deal Error: "+result));

                }

            }
            else if(result != null && result.contains("\"accounts\":")
                    && !endpoint.contains("submitedittask")) {

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
                    Toasts.toastIconError(EditTaskActivity.this, "Error Loading");
                    Log.e("API Response", "JSON Parsing error: " + e.getMessage());
                }


            }else{

                Toasts.toastIconError(EditTaskActivity.this,"Edit Task Failed. Please Try Again");
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