package com.example.crm;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import okhttp3.ResponseBody;

public class IndividualCallActivity extends AppCompatActivity implements OnSuccessListener {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextView accountNameTextView;
    private TextView contactNameTextView;
    private TextView contactPhoneTextView;
    private TextView statusTextView;
    private TextView dateTextView;
    private TextView timeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_individual_call);
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
        actionBar.setTitle("Call");

        String callId = getIntent().getStringExtra("callId");

        accountNameTextView = (TextView) findViewById(R.id.accountNameTextView);
        contactNameTextView = (TextView) findViewById(R.id.contactNameTextView);
        contactPhoneTextView = (TextView) findViewById(R.id.contactPhoneTextView);
        statusTextView = (TextView) findViewById(R.id.statusTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        timeTextView = (TextView) findViewById(R.id.timeTextView);


        NetworkUtils.
                fetchDataGet("call/"+callId,
                        "", IndividualCallActivity.this,
                        IndividualCallActivity.this);
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

            if (result != null && result.contains("call")){

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("call") &&
                        jsonObject.get("call") instanceof JSONObject
                ) {

                    JSONObject call = jsonObject.getJSONObject("call");

                    accountNameTextView.setText(call.getJSONObject("account")
                            .getString("name"));
                    contactNameTextView.setText(call.getJSONObject("contact")
                            .getString("name"));
                    contactPhoneTextView.setText(call.getJSONObject("contact")
                            .getString("phone"));
                    String status = call.getString("status");
                    statusTextView.setText(status.substring(0, 1).toUpperCase() +
                            status.substring(1).toLowerCase());
                    String dateFromDB = call.getString("date");
//                    ZonedDateTime dateTime = Instant.parse(dateFromDB).atZone(ZoneId.of("Africa/Nairobi"));
//                    String formatted = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    LocalDate localDate = LocalDate.parse(dateFromDB, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    String formattedDate = localDate.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"));

                    dateTextView.setText(formattedDate);
                    timeTextView.setText(call.getString("time"));


                }
                else if(result.contains("errors")){
                    String message = jsonObject.getString("message");
                    Toasts.toastIconError(IndividualCallActivity.this,message);
                    //Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
                else{

                    Toasts.toastIconError(IndividualCallActivity.this,"Error Loading Account. Please Try Again");
//                    FirebaseCrashlytics.getInstance().recordException(new Exception("Add Account Error: "+result));

                }

            }else{

                Toasts.toastIconError(IndividualCallActivity.this,"Loading Account Failed. Please Try Again");
//                FirebaseCrashlytics.getInstance().recordException(new Exception("Add Account Error: "+result));

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