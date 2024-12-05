package com.example.crm;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;


public class AddLeadActivity extends AppCompatActivity implements OnSuccessListener {
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
        actionBar.setTitle("Add Lead");

        submitButton = (Button)findViewById(R.id.submitButton);
        leadNameEditText = (EditText)findViewById(R.id.nameEditText);
        accountNameEditText = (EditText)findViewById(R.id.accountNameEditText);
        phoneEditText = (EditText)findViewById(R.id.phoneEditText);




        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toasts.toastIconError(AddLeadActivity.this,"Toast");

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
//                        Toasts.toastIconError(AddLeadActivity.this, accountName);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    NetworkUtils.
                            fetchDataPost("addlead",
                                    "", requestBody,AddLeadActivity.this,
                                    AddLeadActivity.this);



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

            if (result != null && result.contains("Lead")){

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("status") &&
                        jsonObject.get("status") instanceof String &&
                        jsonObject.get("status").equals("success")
                ) {

                    Toasts.toastIconSuccess(AddLeadActivity.this,"Lead Added Successfully");
                    Intent intent = new Intent(AddLeadActivity.this, LeadsActivity.class);
                    intent.putExtra("fragmentName", "accounts");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
                else if(result.contains("errors")){
                    String message = jsonObject.getString("message");
                    Toasts.toastIconError(AddLeadActivity.this,message);
                    //Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
                else{

                    Toasts.toastIconError(AddLeadActivity.this,"Error Adding Lead. Please Try Again");
//                    FirebaseCrashlytics.getInstance().recordException(new Exception("Add Lead Error: "+result));

                }

            }else{

                Toasts.toastIconError(AddLeadActivity.this,"Add Lead Failed. Please Try Again");
//                FirebaseCrashlytics.getInstance().recordException(new Exception("Add Lead Error: "+result));

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
}