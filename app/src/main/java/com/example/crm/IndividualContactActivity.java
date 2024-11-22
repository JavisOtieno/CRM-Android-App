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

public class IndividualContactActivity extends AppCompatActivity implements OnSuccessListener{
    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextView contactNameTextView;
    private TextView phoneTextView;
    private TextView accountNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_individual_contact);
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
        actionBar.setTitle("Contact");

        contactNameTextView = (TextView) findViewById(R.id.contactTextView);
        phoneTextView = (TextView) findViewById(R.id.phoneTextView);
        accountNameTextView = (TextView) findViewById(R.id.accountNameTextView);

        String contactId = getIntent().getStringExtra("contactId");

        NetworkUtils.
                fetchDataGet("contact/"+contactId,
                        "", IndividualContactActivity.this,
                        IndividualContactActivity.this);
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

            if (result != null && result.contains("contact")){

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("contact") &&
                        jsonObject.get("contact") instanceof JSONObject
                ) {

                    JSONObject contact = jsonObject.getJSONObject("contact");

                    contactNameTextView.setText(contact.getString("name"));
                    phoneTextView.setText(contact.getString("phone"));
                    accountNameTextView.setText(contact.getJSONObject("account")
                            .getString("name"));


                }
                else if(result.contains("errors")){
                    String message = jsonObject.getString("message");
                    Toasts.toastIconError(IndividualContactActivity.this,message);
                    //Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
                else{

                    Toasts.toastIconError(IndividualContactActivity.this,"Error Loading Account. Please Try Again");
//                    FirebaseCrashlytics.getInstance().recordException(new Exception("Add Account Error: "+result));

                }

            }else{

                Toasts.toastIconError(IndividualContactActivity.this,"Loading Account Failed. Please Try Again");
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
