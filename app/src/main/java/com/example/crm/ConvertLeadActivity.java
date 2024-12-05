package com.example.crm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.crm.models.Account;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public class ConvertLeadActivity extends AppCompatActivity implements OnSuccessListener {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private Button submitButton;
    private EditText accountNameEditText;
    private EditText accountPhoneEditText;
    private EditText accountWebsiteEditText;
    private EditText accountAddressEditText;
    private EditText contactNameEditText;
    private EditText contactPhoneEditText;
    private Boolean isTaskRunning = false;
    private String mLeadId;
    private String contactName;
    private String contactPhone;
    private String accountName;
    private String accountPhone;
    private String accountWebsite;
    private String accountAddress;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_convert_lead);
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
        actionBar.setTitle("Convert Lead");

        submitButton = (Button) findViewById(R.id.submitButton);
        accountNameEditText = (EditText) findViewById(R.id.accountNameEditText);
        accountPhoneEditText = (EditText) findViewById(R.id.accountPhoneEditText);
        accountWebsiteEditText = (EditText) findViewById(R.id.accountWebsiteEditText);
        accountAddressEditText = (EditText) findViewById(R.id.accountAddressEditText);
        contactNameEditText = (EditText) findViewById(R.id.contactNameEditText);
        contactPhoneEditText = (EditText) findViewById(R.id.contactPhoneEditText);

        String leadString = getIntent().getStringExtra("lead");
        System.out.println("lead: " + leadString);

        try {
            JSONObject lead = new JSONObject(leadString);

            contactNameEditText.setText(lead.getString("name"));
            contactPhoneEditText.setText(lead.getString("phone"));
            accountNameEditText.setText(lead.getString("account_name"));
            mLeadId = lead.getString("id");


            // Use the values as needed
        } catch (Exception e) {
            e.printStackTrace();
        }


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toasts.toastIconError(ConvertLeadActivity.this,"Toast");

                //   longitude = longEditText.getText().toString();
                //   latitude = latEditText.getText().toString();
                if (!isTaskRunning) {
                    isTaskRunning = true;

                    accountName = accountNameEditText.getText().toString();
                    accountPhone = accountPhoneEditText.getText().toString();
                    accountWebsite = accountWebsiteEditText.getText().toString();
                    accountAddress = accountAddressEditText.getText().toString();
                    contactName = contactNameEditText.getText().toString();
                    contactPhone = contactPhoneEditText.getText().toString();

                    JSONObject requestBody = new JSONObject();
                    try {
                        requestBody.put("account_name", accountName);
                        requestBody.put("account_phone", accountPhone);
                        requestBody.put("account_website", accountWebsite);
                        requestBody.put("account_address", accountAddress);
                        requestBody.put("contact_name", contactName);
                        requestBody.put("contact_phone", contactPhone);

//                        Toasts.toastIconError(ConvertLeadActivity.this, accountName);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    NetworkUtils.
                            fetchDataPost("convertlead/" + mLeadId,
                                    "", requestBody, ConvertLeadActivity.this,
                                    ConvertLeadActivity.this);


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
            System.out.println("Result: " + result);

            if (result != null && result.contains("Lead")) {

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("status") &&
                        jsonObject.get("status") instanceof String &&
                        jsonObject.get("status").equals("success")
                ) {

                    String account_id = jsonObject.getString("account_id");
                    Toasts.toastIconSuccess(ConvertLeadActivity.this,
                            jsonObject.getString("message"));
//                    Intent intent = new Intent(ConvertLeadActivity.this, IndividualAccountActivity.class);
                    Intent intent = new Intent(ConvertLeadActivity.this,
                            MainActivity.class);
                    intent.putExtra("fragmentName", "accounts");
                    intent.putExtra("accountId", account_id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } else if (result.contains("errors")) {
                    String message = jsonObject.getString("message");
                    Toasts.toastIconError(ConvertLeadActivity.this, message);
                    //Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                } else {

                    Toasts.toastIconError(ConvertLeadActivity.this,
                            "Error Editing Lead. Please Try Again");
//                    FirebaseCrashlytics.getInstance().recordException(new Exception("Add Deal Error: "+result));

                }

            } else {

                Toasts.toastIconError(ConvertLeadActivity.this, "Edit Lead Failed. Please Try Again");
//                FirebaseCrashlytics.getInstance().recordException(new Exception("Add Deal Error: "+result));

            }


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
}