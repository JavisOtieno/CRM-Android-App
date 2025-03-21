package com.example.crm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crm.adapters.AdapterListContacts;
//import com.example.crm.adapters.AdapterListLeads;
import com.example.crm.models.Contact;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class LeadsActivity extends AppCompatActivity implements OnSuccessListener {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextView mNoAccountsTextView;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private JSONArray leads = null;
    private AdapterListContacts mAdapter;
    private TextView mNoLeadsTextView;
    private String mAuthToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_leads);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        mNoLeadsTextView = (TextView) findViewById(R.id.noLeadsText);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        emptyView = (TextView) findViewById(R.id.empty_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Leads");


//        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
//        bottomNavigationView.setSelectedItemId(R.id.navigation_4);

//        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
//        bottomNavigationView.setSelectedItemId(R.id.navigation_more);
//        Navigation.setNavigation(this,bottomNavigationView);

//        fetchData("fsdwfew");

        NetworkUtils.
                fetchDataGet("leads",
                        mAuthToken,LeadsActivity.this,LeadsActivity.this);



    }

//    private void fetchData(String token) {
//        ApiService apiService = ApiClient.getInstance().getApiService();
//
//        // Call the API
////        Call<MyResponse> call = apiService.getData("application/json", "Bearer " + token);
//        Call<ResponseBody> call = apiService.getData(
//                "leads",
//                "Bearer " + token,"application/json");
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful() && response.body() != null) {
////                    MyResponse myResponse = response.body();
//
//                    // Handle the response data
////                    Toasts.toastIconSuccess(AccountsActivity.this,"we got something");
//                    Log.d("API Response", response.body().toString());
//
//                    try {
//                        // Get the response body as a string
//                        String responseBody = response.body().string();
//
//                        // Create a JSONObject from the response string
//                        JSONObject jsonObject = new JSONObject(responseBody);
//                        List<Lead> items = new ArrayList<>();
//
//                        leads = jsonObject.getJSONArray("leads");
//                        System.out.println("Leads length: "+leads.length());
//
//                        for (int i = 0; i < leads.length(); i++) {
//                            Lead obj = new Lead();
//                            //obj.image = drw_arr.getResourceId(i, -1);
//                            obj.id = leads.getJSONObject(i).getString("id");
//                            obj.name = leads.getJSONObject(i).getString("name");
//                            obj.phone = leads.getJSONObject(i).getString("phone");
//                            obj.account_name = leads.getJSONObject(i).getJSONObject("account")
//                                    .getString("name");
//
//                            String createdAt = leads.getJSONObject(i).getString("created_at");
//                            ZonedDateTime dateTime = Instant.parse(createdAt).atZone(ZoneId.of("Africa/Nairobi"));
//                            String formatted = dateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy \nHH:mm"));
//                            obj.date = formatted;
//                            //obj.imageDrw = drw_arr.getDrawable(obj.image);
//                            items.add(obj);
//
//                        }
//
//                        if (items.isEmpty()) {
//                            recyclerView.setVisibility(View.GONE);
//                            emptyView.setVisibility(View.VISIBLE);
//                        }
//                        else {
//                            recyclerView.setVisibility(View.VISIBLE);
//                            emptyView.setVisibility(View.GONE);
//                        }
//
//
//                        //set data and list adapter
//                        mAdapter = new AdapterListLeads(items);
//                        recyclerView.setAdapter(mAdapter);
//
//                        mAdapter.setOnItemClickListener(new AdapterListLeads.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(View view, Lead obj, int position) {
//
//                                Lead lead = items.get(position);
//
//                                Intent intent = new Intent(LeadsActivity.this,
//                                        IndividualLeadActivity.class);
//                                intent.putExtra("leadId",lead.id);
//                                startActivity(intent);
//
//
//
//                            }
//                        });
//
////                        mAdapter.setOnItemClickListener(new AdapterListAccounts.OnItemClickListener() {
////                            @Override
////                            public void onItemClick(View view, Account obj, int position) {
////
////                                Account account = items.get(position);
////
////                                PreventDoubleClickOnLists.
////                                        showActivityWithoutDoubleClick(
////                                                "account",AccountsActivity.this,
////                                                account, IndividualAccountActivity.class);
////
////
////
////                            }
////                        });
//
//
//                        // Extract values from the JSON object
////                        JSONArray accounts = jsonObject.getJSONArray("leads");
////                        String name = accounts.getJSONObject(0).getString("name");
////                        String message = jsonObject.getString("message");
////                        String name = jsonObject.getString("name");
//
//                        // Show a toast with the message
////                        Toasts.toastIconSuccess(AccountsActivity.this, name);
//
//                        // Log the values for debugging
////                        Log.d("API Response", "Message: " + name + ", Name: " + name);
//                    } catch (Exception e) {
//                        Toasts.toastIconError(LeadsActivity.this,"Error Loading");
//                        Log.e("API Response", "JSON Parsing error: " + e.getMessage());
//                    }
//
//                } else {
//                    // Handle the error
//                    Log.e("API Error", "Response code: " + response.code());
//                    Log.e("API Error", "Response code: " + response.body());
//                    Toasts.toastIconError(LeadsActivity.this,"Error Loading");
//                    // Construct the full URL for the Toast message
//                    String fullUrl = call.request().url().toString(); // Get the URL from the call
////                    Toast.makeText(AccountsActivity.this, "URL: " + fullUrl, Toast.LENGTH_LONG).show(); // Show URL in Toast
//
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                // Handle the failure
//                Log.e("API Failure", t.getMessage());
//                Toasts.toastIconError(LeadsActivity.this,"Error Loading");
//
//                // Check if the throwable is an instance of HttpException
//                if (t instanceof HttpException) {
//                    HttpException httpException = (HttpException) t;
//                    int responseCode = httpException.code();
//
//                    // Log the HTTP status code
//                    Log.e("API Failure", "Response code: " + responseCode);
//
//                    // Try to extract the error body (if present)
//                    try {
//                        Response<?> response = httpException.response();
//                        if (response != null && response.errorBody() != null) {
//                            String errorBody = response.errorBody().string();
//                            Log.e("API Failure", "Error body: " + errorBody);
//                        }
//                    } catch (IOException e) {
//                        Log.e("API Failure", "Error reading error body: " + e.getMessage());
//                    }
//                } else {
//                    // Log the stack trace for other types of exceptions
//                    Log.e("API Failure", "Unknown error: ", t);
//                }
//
//            }
//        });
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            Intent intent = new Intent(LeadsActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        else if (item.getItemId() == R.id.action_add) {
//            Intent intent = new Intent(LeadsActivity.this, AddLeadActivity.class);
            Intent intent = new Intent(LeadsActivity.this, AddLeadActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(ResponseBody responseBodyObject) {

        try {
            // Get the response body as a string
            String responseBody = responseBodyObject.string();

            // Create a JSONObject from the response string
            JSONObject jsonObject = new JSONObject(responseBody);
            List<Contact> items = new ArrayList<>();

            leads = jsonObject.getJSONArray("leads");
            System.out.println("Leads length: "+leads.length());

            for (int i = 0; i < leads.length(); i++) {
                Contact obj = new Contact();
                //obj.image = drw_arr.getResourceId(i, -1);
                obj.id = leads.getJSONObject(i).getString("id");
                obj.account_name = leads.getJSONObject(i).getString("name");
                obj.phone = leads.getJSONObject(i).getJSONArray("contacts")
                        .getJSONObject(0).getString("phone");
                obj.name = leads.getJSONObject(i).getJSONArray("contacts")
                        .getJSONObject(0).getString("name");

                String createdAt = leads.getJSONObject(i).getString("created_at");
                ZonedDateTime dateTime = Instant.parse(createdAt).atZone(ZoneId.of("Africa/Nairobi"));
                String formatted = dateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy \nHH:mm"));
                obj.date = formatted;
                //obj.imageDrw = drw_arr.getDrawable(obj.image);
                items.add(obj);

            }

            if (items.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
            else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }


            //set data and list adapter
            mAdapter = new AdapterListContacts(items);
            recyclerView.setAdapter(mAdapter);

            mAdapter.setOnItemClickListener(new AdapterListContacts.OnItemClickListener() {
                @Override
                public void onItemClick(View view, Contact obj, int position) {

                    Contact lead = items.get(position);
//                    Intent intent = new Intent(LeadsActivity.this,
//                            IndividualLeadActivity.class);
                    Intent intent = new Intent(LeadsActivity.this,
                            IndividualLeadActivity.class);
                    intent.putExtra("leadId",lead.id);
                    startActivity(intent);



                }
            });

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
//                        JSONArray accounts = jsonObject.getJSONArray("leads");
//                        String name = accounts.getJSONObject(0).getString("name");
//                        String message = jsonObject.getString("message");
//                        String name = jsonObject.getString("name");

            // Show a toast with the message
//                        Toasts.toastIconSuccess(AccountsActivity.this, name);

            // Log the values for debugging
//                        Log.d("API Response", "Message: " + name + ", Name: " + name);
        } catch (Exception e) {
            Toasts.toastIconError(LeadsActivity.this,"Error Loading");
            Log.e("API Response", "JSON Parsing error: " + e.getMessage());
        }

    }

    @Override
    public void onComplete() {

    }
}