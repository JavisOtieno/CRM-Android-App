package com.example.crm;

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
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public class AddContactActivity extends AppCompatActivity implements OnSuccessListener  {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private Button submitButton;
    private String selectedAccountId="0";
    private Boolean isTaskRunning = false;
    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText accountEditText;
    private String name;
    private String phone;
    private String account;
    private Integer checkedStatus=-1;
    private Integer checkedAccount=-1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_contact);
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
        actionBar.setTitle("Add Contact");

        NetworkUtils.
                fetchDataGet("accounts",
                        "",AddContactActivity.this,
                        AddContactActivity.this);

        submitButton = (Button)findViewById(R.id.submitButton);
        nameEditText = (EditText)findViewById(R.id.nameEditText);
        phoneEditText = (EditText)findViewById(R.id.phoneEditText);
        accountEditText = (EditText)findViewById(R.id.accountEditText);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toasts.toastIconError(AddAccountActivity.this,"Toast");

                //   longitude = longEditText.getText().toString();
                //   latitude = latEditText.getText().toString();
                if (!isTaskRunning) {
                    isTaskRunning = true;

                    name = nameEditText.getText().toString();
                    phone = phoneEditText.getText().toString();
                    account = selectedAccountId;

                    JSONObject requestBody = new JSONObject();
                    try {
                        requestBody.put("name", name);
                        requestBody.put("phone", phone);
                        requestBody.put("account_id",account);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    NetworkUtils.
                            fetchDataPost("addcontact",
                                    "", requestBody,AddContactActivity.this,
                                    AddContactActivity.this);



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

    @Override
    public void onSuccess(ResponseBody responseBody) {

        String responseBodyString = null;
        try {
            String result = responseBody.string();
            System.out.println("Result: "+result);

            if (result != null && result.contains("Contact")){

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("status") &&
                        jsonObject.get("status") instanceof String &&
                        jsonObject.get("status").equals("success")
                ) {

                    Toasts.toastIconSuccess(AddContactActivity.this,"Contact Added Successfully");
                    Intent intent = new Intent(AddContactActivity.this, ContactsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
                else if(result.contains("errors")){
                    String message = jsonObject.getString("message");
                    Toasts.toastIconError(AddContactActivity.this,message);
                    //Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
                else{

                    Toasts.toastIconError(AddContactActivity.this,"Error Adding Contact. Please Try Again");
//                    FirebaseCrashlytics.getInstance().recordException(new Exception("Add Deal Error: "+result));

                }

            }
            else if(result != null && result.contains("\"accounts\":")){

                try {
                    // Create a JSONObject from the response string
                    JSONObject jsonObject = new JSONObject(result);
                    List<Account> items = new ArrayList<>();
                    List<String> accountNames = new ArrayList<>();
                    List<String> accountIds = new ArrayList<>();


                    JSONArray accounts = jsonObject.getJSONArray("accounts");
//                    System.out.println("Accounts length: "+accounts.length());
                    for (int i = 0; i < accounts.length(); i++) {
                        accountNames.add(accounts.getJSONObject(i).getString("name"));
                        accountIds.add(accounts.getJSONObject(i).getString("id"));
//                        Account obj = new Account();
//                        //obj.image = drw_arr.getResourceId(i, -1);
//                        obj.name = accounts.getJSONObject(i).getString("name");
//                        obj.address = accounts.getJSONObject(i).getString("address");
//                        obj.phone = accounts.getJSONObject(i).getString("phone");
//                        obj.website = accounts.getJSONObject(i).getString("website");
//
//
//                        //obj.imageDrw = drw_arr.getDrawable(obj.image);
//                        items.add(obj);


                    }

                    String[] accountNamesArray = accountNames.toArray(new String[0]);
                    String[] accountIdsArray = accountIds.toArray(new String[0]);

                    accountEditText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showAccountDialog(view,accountNamesArray,accountIdsArray);
                        }
                    });

                } catch (Exception e) {
                    Toasts.toastIconError(AddContactActivity.this,"Error Loading");
                    Log.e("API Response", "JSON Parsing error: " + e.getMessage());
                }

            }
            else{

                Toasts.toastIconError(AddContactActivity.this,"Add Contact Failed. Please Try Again");
//                FirebaseCrashlytics.getInstance().recordException(new Exception("Add Deal Error: "+result));

            }

//            Log.d("API Success", "Data: " + jsonObject.toString());


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    private void showAccountDialog(final View v,String[] accountOptions, String[] accountIds) {
        final String[] array = accountOptions;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Accounts");
        builder.setSingleChoiceItems(array, checkedAccount, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(array[i]);
                dialogInterface.dismiss();
                checkedAccount = i;
                selectedAccountId = accountIds[i];
            }
        });
        builder.show();
    }

}