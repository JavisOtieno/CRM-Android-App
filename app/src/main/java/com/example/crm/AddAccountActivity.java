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


public class AddAccountActivity extends AppCompatActivity implements OnSuccessListener {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private EditText accountNameEditText;
    private EditText websiteEditText;
    private EditText addressEditText;
    private EditText phoneEditText;
    private Button submitButton;
    private String accountName;
    private String website;
    private String address;
    private String phone;
    private boolean isTaskRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_account);
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
        actionBar.setTitle("Add Account");

        submitButton = (Button)findViewById(R.id.submitButton);
        accountNameEditText = (EditText)findViewById(R.id.nameEditText);
        websiteEditText = (EditText)findViewById(R.id.websiteEditText);
        phoneEditText = (EditText)findViewById(R.id.phoneEditText);
        addressEditText = (EditText)findViewById(R.id.addressEditText);



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toasts.toastIconError(AddAccountActivity.this,"Toast");

                //   longitude = longEditText.getText().toString();
                //   latitude = latEditText.getText().toString();
                if (!isTaskRunning) {
                    isTaskRunning = true;

                    accountName = accountNameEditText.getText().toString();
                    website = websiteEditText.getText().toString();
                    phone = phoneEditText.getText().toString();
                    address = addressEditText.getText().toString();

                    JSONObject requestBody = new JSONObject();
                    try {
                        requestBody.put("name", accountName);
                        requestBody.put("website", website);
                        requestBody.put("phone",phone);
                        requestBody.put("address",address);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    NetworkUtils.
                            fetchDataPost("addaccount",
                                    "", requestBody,AddAccountActivity.this,
                                    AddAccountActivity.this);



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

            if (result != null && result.contains("Account")){

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("status") &&
                        jsonObject.get("status") instanceof String &&
                        jsonObject.get("status").equals("success")
                ) {

                    Toasts.toastIconSuccess(AddAccountActivity.this,"Account Added Successfully");
                    Intent intent = new Intent(AddAccountActivity.this, MainActivity.class);
                    intent.putExtra("fragmentName", "accounts");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
                else if(result.contains("errors")){
                    String message = jsonObject.getString("message");
                    Toasts.toastIconError(AddAccountActivity.this,message);
                    //Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
                else{

                    Toasts.toastIconError(AddAccountActivity.this,"Error Adding Account. Please Try Again");
//                    FirebaseCrashlytics.getInstance().recordException(new Exception("Add Account Error: "+result));

                }

            }else{

                Toasts.toastIconError(AddAccountActivity.this,"Add Account Failed. Please Try Again");
//                FirebaseCrashlytics.getInstance().recordException(new Exception("Add Account Error: "+result));

            }

//            Log.d("API Success", "Data: " + jsonObject.toString());


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }
}