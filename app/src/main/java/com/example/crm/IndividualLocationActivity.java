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

public class IndividualLocationActivity extends AppCompatActivity implements OnSuccessListener{
    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextView locationNameTextView;
    private TextView descriptionTextView;
    private TextView accountNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_individual_location);
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
        actionBar.setTitle("Location");

        locationNameTextView = (TextView) findViewById(R.id.locationTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        accountNameTextView = (TextView) findViewById(R.id.accountNameTextView);

        String locationId = getIntent().getStringExtra("locationId");

        NetworkUtils.
                fetchDataGet("location/"+locationId,
                        "", IndividualLocationActivity.this,
                        IndividualLocationActivity.this);
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

            if (result != null && result.contains("location")){

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("location") &&
                        jsonObject.get("location") instanceof JSONObject
                ) {

                    JSONObject location = jsonObject.getJSONObject("location");

                    locationNameTextView.setText(location.getString("name"));
                    descriptionTextView.setText(location.getString("description"));
                    accountNameTextView.setText(location.getJSONObject("account")
                            .getString("name"));


                }
                else if(result.contains("errors")){
                    String message = jsonObject.getString("message");
                    Toasts.toastIconError(IndividualLocationActivity.this,message);
                    //Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
                else{

                    Toasts.toastIconError(IndividualLocationActivity.this,"Error Loading Account. Please Try Again");
//                    FirebaseCrashlytics.getInstance().recordException(new Exception("Add Account Error: "+result));

                }

            }else{

                Toasts.toastIconError(IndividualLocationActivity.this,"Loading Account Failed. Please Try Again");
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
