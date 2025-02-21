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
import com.example.crm.models.Contact;

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

public class AddTaskActivity extends AppCompatActivity implements OnSuccessListener  {
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
    private String endpoint;
    private Integer checkedType=-1;


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
        actionBar.setTitle("Add Task");

        endpoint = "accountscontacts";
        NetworkUtils.
                fetchDataGet(endpoint,
                        "",AddTaskActivity.this,
                        AddTaskActivity.this);

        submitButton = (Button)findViewById(R.id.submitButton);

        accountEditText = (EditText)findViewById(R.id.accountEditText);
        contactEditText = (EditText)findViewById(R.id.contactEditText);
        statusEditText = (EditText)findViewById(R.id.statusEditText);
        descriptionEditText = (EditText)findViewById(R.id.descriptionEditText);
        nameEditText = (EditText)findViewById(R.id.nameEditText);

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

                    endpoint = "addtask";
                    NetworkUtils.
                            fetchDataPost(endpoint,
                                    "", requestBody,AddTaskActivity.this,
                                    AddTaskActivity.this);



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

            if (result != null && result.contains("Task") &&
            endpoint.contains( "addtask")){


                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("status") &&
                        jsonObject.get("status") instanceof String &&
                        jsonObject.get("status").equals("success")
                ) {

                    Toasts.toastIconSuccess(AddTaskActivity.this,"Task Added Successfully");
                    Intent intent = new Intent(AddTaskActivity.this, TasksActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
                else if(result.contains("errors")){
                    String message = jsonObject.getString("message");
                    Toasts.toastIconError(AddTaskActivity.this,message);
                    //Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
                else{

                    Toasts.toastIconError(AddTaskActivity.this,"Error Adding Task. Please Try Again");
//                    FirebaseCrashlytics.getInstance().recordException(new Exception("Add Deal Error: "+result));

                }

            }
            else if(result != null && result.contains("\"accounts\":")
            && endpoint.contains("accountscontacts")) {

                try {
                    // Create a JSONObject from the response string
                    JSONArray jsonArray = new JSONArray(result);
                    List<Account> items = new ArrayList<>();
                    List<String> accountNames = new ArrayList<>();
                    List<String> accountIds = new ArrayList<>();
                    List<String> contactNames = new ArrayList<>();
                    List<String> contactIds = new ArrayList<>();
                    Map<String, List<Contact>> contactsMap = new HashMap<>();

                    JSONArray accounts = jsonArray.getJSONObject(0).
                            getJSONArray("accounts");
                    JSONArray contacts = jsonArray.getJSONObject(0).
                            getJSONArray("contacts");
                    System.out.println("Accounts length: "+accounts.length());
                    System.out.println("Contacts length: "+contacts.length());

                    for (int i = 0; i < accounts.length(); i++) {
                        accountNames.add(accounts.getJSONObject(i).getString("name"));
                        accountIds.add(accounts.getJSONObject(i).getString("id"));
                    }

                    for (int i = 0; i < contacts.length(); i++) {
                        contactNames.add(contacts.getJSONObject(i).getString("name"));
                        contactIds.add(contacts.getJSONObject(i).getString("id"));

                        String accountId = contacts.getJSONObject(i).getString("account_id");
                        String contactName = contacts.getJSONObject(i).getString("name");
                        String contactId = contacts.getJSONObject(i).getString("id");

                        // Create a new Contact object
                        Contact contact = new Contact();
                        contact.name = contactName;
                        contact.id = contactId;

                        // If the accountId is not already in the map, create a new list
                        if (!contactsMap.containsKey(accountId)) {
                            contactsMap.put(accountId,new ArrayList<Contact>());
                        }
                        // Add the contact to the appropriate account's list
                        contactsMap.get(accountId).add(contact);

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
                            showContactsDialog(view, contactNamesArray, contactIdsArray,contactsMap,"Contacts");
//                            showAccountDialog(view, contactNamesArray, contactIdsArray,"Contacts");
                        }
                    });


                } catch(Exception e){
                    Toasts.toastIconError(AddTaskActivity.this, "Error Loading");
                    Log.e("API Response", "JSON Parsing error: " + e.getMessage());
                }


            }else{

                Toasts.toastIconError(AddTaskActivity.this,"Add Task Failed. Please Try Again");
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
                    contactEditText.setText("");
                    selectedContactId="0";
                    checkedContact = -1;
                }else{
                    checkedContact= i;
                    selectedContactId = Ids[i];
                }

            }
        });
        builder.show();
    }

    private void showContactsDialog(final View v,String[] contactOptions, String[] Ids, Map<String, List<Contact>> contactsMap,
                                    String title) {
        final String[] array = contactOptions;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        Integer checkedItem;

        checkedItem=checkedContact;


        if(selectedAccountId.equals("0")){
//            Toasts.toastIconError(AddCallActivity.this,selectedAccountId+" out ");
            builder.setSingleChoiceItems(array, checkedItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ((EditText) v).setText(array[i]);
                    dialogInterface.dismiss();
                    checkedContact= i;
                    selectedContactId = Ids[i];
                }
            });

        }else{
//            Toasts.toastIconError(AddCallActivity.this,selectedAccountId+" right here");
            builder.setSingleChoiceItems(getContactNamesForAccount(selectedAccountId,contactsMap), checkedItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Set the selected contact name in the EditText view
                    ((EditText) v).setText(getContactNamesForAccount(selectedAccountId,contactsMap)[i]);

                    // Dismiss the dialog
                    dialogInterface.dismiss();

                    // Update checked contact index and contact ID
                    checkedContact = i;

                    // Get the contact ID from the map using the selected index
                    selectedContactId = getContactIdsForAccount(selectedAccountId,contactsMap).get(i);
                }
            });


        }


        builder.show();
    }

    // Method to get contact names for a specific account
    private String[] getContactNamesForAccount(String accountId,
                                               Map<String, List<Contact>> contactsMap
    ) {
        List<Contact> contactsList = contactsMap.get(accountId);
        if (contactsList == null) {
            return new String[]{};
        }

        String[] contactNames = new String[contactsList.size()];
        for (int i = 0; i < contactsList.size(); i++) {
            contactNames[i] = contactsList.get(i).name;
        }
        return contactNames;
    }

    private List<String> getContactIdsForAccount(String accountId,
                                                 Map<String, List<Contact>> contactsMap) {
        List<Contact> contactsList = contactsMap.get(accountId);
        List<String> contactIds = new ArrayList<>();

        if (contactsList != null) {
            for (Contact contact : contactsList) {
                contactIds.add(contact.id);
            }
        }

        return contactIds;
    }




}