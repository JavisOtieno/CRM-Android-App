package com.example.crm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.crm.models.Phonecall;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public class StatsActivity extends AppCompatActivity implements OnSuccessListener {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextView dealsNumberTextView;
    private TextView accountsNumberTextView;
    private TextView contactsNumberTextView;
    private TextView leadsNumberTextView;

    private TextView tasksNumberTextView;
    private TextView meetingsNumberTextView;
    private TextView callsNumberTextView;
    private TextView visitsNumberTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stats);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        dealsNumberTextView = findViewById(R.id.dealsNumberTextView);
        accountsNumberTextView = findViewById(R.id.accountsNumberTextView);
        contactsNumberTextView = findViewById(R.id.contactsNumberTextView);
        leadsNumberTextView = findViewById(R.id.leadsNumberTextView);
        visitsNumberTextView = findViewById(R.id.visitsNumberTextView);
        tasksNumberTextView = findViewById(R.id.tasksNumberTextView);
        meetingsNumberTextView = findViewById(R.id.meetingsNumberTextView);
        callsNumberTextView = findViewById(R.id.callsNumberTextView);



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Stats");

        NetworkUtils.
                fetchData("GET","stats",
                        null,StatsActivity.this,StatsActivity.this);

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(ResponseBody responseBodyObject) {

        Log.d("API Response", responseBodyObject.toString());

        try {
            // Get the response body as a string
            String responseBody = responseBodyObject.string();

            // Create a JSONObject from the response string
            JSONObject jsonObject = new JSONObject(responseBody);



            String accountsCount = jsonObject.getString("accountsCount");
            String contactsCount = jsonObject.getString("contactsCount");
            String leadsCount = jsonObject.getString("leadsCount");
            String dealsCount = jsonObject.getString("dealsCount");
            String visitsCount = jsonObject.getString("visitsCount");
            String meetingsCount = jsonObject.getString("meetingsCount");
            String tasksCount = jsonObject.getString("tasksCount");
            String callsCount = jsonObject.getString("callsCount");


            dealsNumberTextView.setText(dealsCount);
            accountsNumberTextView.setText(accountsCount);
            contactsNumberTextView.setText(contactsCount);
            leadsNumberTextView .setText(leadsCount);
            visitsNumberTextView.setText(visitsCount);
            tasksNumberTextView.setText(tasksCount);
            meetingsNumberTextView.setText(meetingsCount);
            callsNumberTextView.setText(callsCount);








//                        mAdapter.setOnItemClickListener(new AdapterListAccounts.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(View view, Account obj, int position) {
//
//                                Account account = items.get(position);
//
//                                PreventDoubleClickOnLists.
//                                        showActivityWithoutDoubleClick(
//                                                "account",AccountsActivity.this,
//                                                account, IndividualAccountActivity.class);
//
//
//
//                            }
//                        });


            // Extract values from the JSON object
//                        JSONArray accounts = jsonObject.getJSONArray("calls");
//                        String name = accounts.getJSONObject(0).getString("name");
//                        String message = jsonObject.getString("message");
//                        String name = jsonObject.getString("name");

            // Show a toast with the message
//                        Toasts.toastIconSuccess(AccountsActivity.this, name);

            // Log the values for debugging
//                        Log.d("API Response", "Message: " + name + ", Name: " + name);
        } catch (Exception e) {
            Toasts.toastIconError(StatsActivity.this,"Error Loading");
            Log.e("API Response", "JSON Parsing error: " + e.getMessage());
        }

    }

    @Override
    public void onComplete() {

    }
}