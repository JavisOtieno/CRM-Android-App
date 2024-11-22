package com.example.crm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crm.adapters.AdapterListActivities;
import com.example.crm.adapters.AdapterListCalls;
import com.example.crm.models.Phonecall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;

public class CalendarActivity extends AppCompatActivity implements OnSuccessListener {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private CalendarView calendarView;
    private String mAuthToken;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private AdapterListActivities mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calendar);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        emptyView = (TextView) findViewById(R.id.empty_view);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Calendar");

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Trigger your action here
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year; // Month is 0-based
                selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
//                Toast.makeText(CalendarActivity.this, "Selected date: " + selectedDate, Toast.LENGTH_SHORT).show();

                // Call your action method here
//                performActionBasedOnDate(selectedDate);

                JSONObject requestBody = new JSONObject();
                try {
                    requestBody.put("date", selectedDate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                NetworkUtils.
                        fetchDataPost("activitiesdate",
                                "", requestBody,CalendarActivity.this,
                                CalendarActivity.this);
            }
        });

        NetworkUtils.
                fetchDataGet("activities",
                        mAuthToken,CalendarActivity.this,CalendarActivity.this);

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
    public void onSuccess(ResponseBody responseBodyObject) {
        Log.d("API Response", responseBodyObject.toString());

        try {
            // Get the response body as a string
            String responseBody = responseBodyObject.string();

            // Create a JSONObject from the response string
            JSONObject jsonObject = new JSONObject(responseBody);
            List<Map<String, String>> items = new ArrayList<>();

            JSONArray activities = jsonObject.getJSONArray("activities");
            System.out.println("Calls length: "+activities.length());

            for (int i = 0; i < activities.length(); i++) {
                Map<String,String> obj = new HashMap<>();
                obj.put("id", activities.getJSONObject(i).getString("id"));
                //obj.image = drw_arr.getResourceId(i, -1);
//                            obj.name = calls.getJSONObject(i).getString("name");
                JSONObject locationContact;
                if(activities.getJSONObject(i).has("contact")){
                     locationContact = activities.getJSONObject(i).getJSONObject("contact");
                }else{
                    locationContact = activities.getJSONObject(i).getJSONObject("location");
                }

                obj.put("contact", locationContact.
                        getString("name"));
                obj.put("accountName", activities.getJSONObject(i).getJSONObject("account")
                        .getString("name"));
                String status = activities.getJSONObject(i).getString("type");
                obj.put("type",
                        status.substring(0, 1).toUpperCase() +
                                status.substring(1).toLowerCase());


                String createdAt = activities.getJSONObject(i).getString("created_at");
                ZonedDateTime dateTime = Instant.parse(createdAt).atZone(ZoneId.of("Africa/Nairobi"));
                String formatted = dateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy \nHH:mm"));
                String formattedTime = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
                obj.put("time", formattedTime);

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
            mAdapter = new AdapterListActivities(items);
            recyclerView.setAdapter(mAdapter);

            mAdapter.setOnItemClickListener(new AdapterListActivities.OnItemClickListener() {
                @Override
                public void onItemClick(View view, Map<String, String> obj, int position) {

                    Map<String, String> item = items.get(position);

                    if(item.get("type").equals("Call")) {
                        Intent intent = new Intent(CalendarActivity.this, IndividualCallActivity.class);
                        intent.putExtra("callId", item.get("id"));
                        startActivity(intent);
                    }else if(item.get("type").equals("Visit")){
                        Intent intent = new Intent(CalendarActivity.this, IndividualVisitActivity.class);
                        intent.putExtra("visitId", item.get("id"));
                        startActivity(intent);
                    }else if(item.get("type").equals("Meeting")){
                        Intent intent = new Intent(CalendarActivity.this, IndividualMeetingActivity.class);
                        intent.putExtra("meetingId", item.get("id"));
                        startActivity(intent);
                    }else if(item.get("type").equals("Task")){
                        Intent intent = new Intent(CalendarActivity.this, IndividualTaskActivity.class);
                        intent.putExtra("taskId", item.get("id"));
                        startActivity(intent);
                    }else{
                        Toasts.toastIconError(CalendarActivity.this,"Not found"+item.get("type"));
                    }



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
//                        JSONArray accounts = jsonObject.getJSONArray("calls");
//                        String name = accounts.getJSONObject(0).getString("name");
//                        String message = jsonObject.getString("message");
//                        String name = jsonObject.getString("name");

            // Show a toast with the message
//                        Toasts.toastIconSuccess(AccountsActivity.this, name);

            // Log the values for debugging
//                        Log.d("API Response", "Message: " + name + ", Name: " + name);
        } catch (Exception e) {
            Toasts.toastIconError(CalendarActivity.this,"Error Loading");
            Log.e("API Response", "JSON Parsing error: " + e.getMessage());
        }
    }

    @Override
    public void onComplete() {

    }
}