package com.example.crm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.crm.models.Account;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;


public class EditLeadActivity extends AppCompatActivity implements OnSuccessListener {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private EditText leadNameEditText;
    private EditText accountNameEditText;
    private EditText phoneEditText;
    private Button submitButton;
    private String leadName;
    private String accountName;
    private String phone;
    private boolean isTaskRunning = false;
    private String endpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_lead);
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
        actionBar.setTitle("Edit Lead");

        String leadId = getIntent().getStringExtra("leadId");

        endpoint = "lead/"+leadId;

        NetworkUtils.
                fetchDataGet(endpoint,
                        "",EditLeadActivity.this,
                        EditLeadActivity.this);



        submitButton = (Button)findViewById(R.id.submitButton);
        leadNameEditText = (EditText)findViewById(R.id.nameEditText);
        accountNameEditText = (EditText)findViewById(R.id.accountNameEditText);
        phoneEditText = (EditText)findViewById(R.id.phoneEditText);




        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toasts.toastIconError(EditLeadActivity.this,"Toast");

                //   longitude = longEditText.getText().toString();
                //   latitude = latEditText.getText().toString();
                if (!isTaskRunning) {
                    isTaskRunning = true;

                    leadName = leadNameEditText.getText().toString();
                    accountName = accountNameEditText.getText().toString();
                    phone = phoneEditText.getText().toString();

                    JSONObject requestBody = new JSONObject();
                    try {
                        requestBody.put("name", leadName);
                        requestBody.put("account_name", accountName);
                        requestBody.put("phone",phone);
//                        Toasts.toastIconError(EditLeadActivity.this, accountName);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    endpoint = "submiteditlead/"+leadId;
                    NetworkUtils.
                            fetchDataPost(endpoint,
                                    "", requestBody,EditLeadActivity.this,
                                    EditLeadActivity.this);



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

            if (result != null && result.contains("Lead") && endpoint.contains("submiteditlead")){

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("status") &&
                        jsonObject.get("status") instanceof String &&
                        jsonObject.get("status").equals("success")
                ) {

                    Toasts.toastIconSuccess(EditLeadActivity.this,"Lead Edited Successfully");
                    Intent intent = new Intent(EditLeadActivity.this, LeadsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
                else if(result.contains("errors")){
                    String message = jsonObject.getString("message");
                    Toasts.toastIconError(EditLeadActivity.this,message);
                    //Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
                else{

                    Toasts.toastIconError(EditLeadActivity.this,"Error Editing Lead. Please Try Again");
//                    FirebaseCrashlytics.getInstance().recordException(new Exception("Add Deal Error: "+result));

                }

            }
            else if(result != null && result.contains("\"lead\":") &&
                    !endpoint.contains("submiteditlead")){

                try {
                    // Create a JSONObject from the response string
                    JSONObject jsonObject = new JSONObject(result);
                    List<Account> items = new ArrayList<>();
                    List<String> accountNames = new ArrayList<>();
                    List<String> accountIds = new ArrayList<>();

                    JSONObject lead = jsonObject.getJSONObject("lead");
                    leadNameEditText.setText(lead.getString("name"));
                    phoneEditText.setText(lead.getString("phone"));
                    accountNameEditText.setText(lead.getString("account_name"));



                } catch (Exception e) {
                    Toasts.toastIconError(EditLeadActivity.this,"Error Loading");
                    Log.e("API Response", "JSON Parsing error: " + e.getMessage());
                }

            }
            else{

                Toasts.toastIconError(EditLeadActivity.this,"Edit Lead Failed. Please Try Again");
//                FirebaseCrashlytics.getInstance().recordException(new Exception("Add Deal Error: "+result));

            }




        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void onComplete() {
        isTaskRunning = false;
    }
}