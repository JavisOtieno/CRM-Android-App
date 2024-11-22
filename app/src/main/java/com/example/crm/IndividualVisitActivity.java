package com.example.crm;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

public class IndividualVisitActivity extends AppCompatActivity
implements OnSuccessListener{
    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextView accountNameTextView;
    private TextView locationNameTextView;
    private TextView statusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_individual_visit);
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
        actionBar.setTitle("Visit");

        String visitId = getIntent().getStringExtra("visitId");

        accountNameTextView = (TextView) findViewById(R.id.accountNameTextView);
        locationNameTextView = (TextView) findViewById(R.id.locationNameTextView);
        statusTextView = (TextView) findViewById(R.id.statusTextView);

        NetworkUtils.
                fetchDataGet("visit/"+visitId,
                        "", IndividualVisitActivity.this,
                        IndividualVisitActivity.this);




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

            if (result != null && result.contains("visit")){

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("visit") &&
                        jsonObject.get("visit") instanceof JSONObject
                ) {

                    JSONObject visit = jsonObject.getJSONObject("visit");

                    accountNameTextView.setText(visit.getJSONObject("account")
                            .getString("name"));
                    locationNameTextView.setText(visit.getJSONObject("location")
                            .getString("name"));
                    String status = visit.getString("status");
                    statusTextView.setText(status.substring(0, 1).toUpperCase() +
                            status.substring(1).toLowerCase());


                }
                else if(result.contains("errors")){
                    String message = jsonObject.getString("message");
                    Toasts.toastIconError(IndividualVisitActivity.this,message);
                    //Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
                else{

                    Toasts.toastIconError(IndividualVisitActivity.this,"Error Loading Account. Please Try Again");
//                    FirebaseCrashlytics.getInstance().recordException(new Exception("Add Account Error: "+result));

                }

            }else{

                Toasts.toastIconError(IndividualVisitActivity.this,"Loading Account Failed. Please Try Again");
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