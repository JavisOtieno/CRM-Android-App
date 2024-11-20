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

public class IndividualAccountActivity extends AppCompatActivity implements OnSuccessListener {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextView accountName;
    private TextView accountPhone;
    private TextView accountAddress;
    private TextView accountWebsite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_individual_account);
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
        actionBar.setTitle("Account");

        accountAddress = (TextView) findViewById(R.id.accountAddress);
        accountName = (TextView) findViewById(R.id.accountName);
        accountPhone = (TextView) findViewById(R.id.accountPhone);
        accountWebsite = (TextView) findViewById(R.id.accountWebsite);



        String accountId = getIntent().getStringExtra("accountId");

        NetworkUtils.
                fetchDataGet("account/"+accountId,
                        "", IndividualAccountActivity.this,
                        IndividualAccountActivity.this);


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

            if (result != null && result.contains("account")){

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("account") &&
                        jsonObject.get("account") instanceof JSONObject
                ) {

                    JSONObject account = jsonObject.getJSONObject("account");

                    accountName.setText(account.getString("name"));
                    accountAddress.setText(account.getString("address"));
                    accountPhone.setText(account.getString("phone"));
                    accountWebsite.setText(account.getString("website"));


                }
                else if(result.contains("errors")){
                    String message = jsonObject.getString("message");
                    Toasts.toastIconError(IndividualAccountActivity.this,message);
                    //Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
                else{

                    Toasts.toastIconError(IndividualAccountActivity.this,"Error Loading Account. Please Try Again");
//                    FirebaseCrashlytics.getInstance().recordException(new Exception("Add Account Error: "+result));

                }

            }else{

                Toasts.toastIconError(IndividualAccountActivity.this,"Loading Account Failed. Please Try Again");
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