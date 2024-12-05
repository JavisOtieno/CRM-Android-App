package com.example.crm;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

public class IndividualLeadActivity extends AppCompatActivity implements OnSuccessListener {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextView accountNameTextView;
    private TextView contactNameTextView;
    private TextView contactPhoneTextView;
    private Button editLeadButton;
    private Button convertLeadButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_individual_lead);
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
        actionBar.setTitle("Lead");


        accountNameTextView = (TextView) findViewById(R.id.accountNameTextView);
        contactNameTextView = (TextView) findViewById(R.id.contactNameTextView);
        contactPhoneTextView = (TextView) findViewById(R.id.contactPhoneTextView);
        editLeadButton = (Button) findViewById(R.id.editLeadButton);
        convertLeadButton = (Button) findViewById(R.id.convertLeadButton);

        String leadId = getIntent().getStringExtra("leadId");

        editLeadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IndividualLeadActivity.this,
                        EditLeadActivity.class);
                intent.putExtra("leadId",leadId);
                startActivity(intent);
            }

        });








        NetworkUtils.
                fetchDataGet("lead/"+leadId,
                        "", IndividualLeadActivity.this,
                        IndividualLeadActivity.this);


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

            if (result != null && result.contains("lead")){

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("lead") &&
                        jsonObject.get("lead") instanceof JSONObject
                ) {

                    JSONObject lead = jsonObject.getJSONObject("lead");

                    contactNameTextView.setText(lead.getString("name"));
                    contactPhoneTextView.setText(lead.getString("phone"));
                    accountNameTextView.setText(lead.getString("account_name"));

                    convertLeadButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(IndividualLeadActivity.this,
                                    ConvertLeadActivity.class);
                            intent.putExtra("lead",lead.toString());
                            startActivity(intent);
                        }
                    });


                }
                else if(result.contains("errors")){
                    String message = jsonObject.getString("message");
                    Toasts.toastIconError(IndividualLeadActivity.this,message);
                    //Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
                else{

                    Toasts.toastIconError(IndividualLeadActivity.this,"Error Loading Lead. Please Try Again");
//                    FirebaseCrashlytics.getInstance().recordException(new Exception("Add Lead Error: "+result));

                }

            }else{

                Toasts.toastIconError(IndividualLeadActivity.this,"Loading Lead Failed. Please Try Again");
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

    }
}