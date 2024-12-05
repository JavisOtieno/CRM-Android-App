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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crm.adapters.AdapterListContacts;
import com.example.crm.models.Contact;
import com.example.crm.models.Location;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public class IndividualAccountActivity extends AppCompatActivity implements OnSuccessListener {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextView accountName;
    private TextView accountPhone;
    private TextView accountAddress;
    private TextView accountWebsite;
    private Button editAccountButton;

    private RecyclerView recyclerView;
    private TextView emptyView;
    private JSONArray contacts = null;
    private JSONArray locations = null;
    private AdapterListContacts mAdapter;
    private TabLayout tabLayout;

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
        editAccountButton = (Button) findViewById(R.id.editAccountButton);
//        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setHasFixedSize(true);
//        emptyView = (TextView) findViewById(R.id.empty_view);


        tabLayout = findViewById(R.id.tab_layout);






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

                    String accountNameString = account.getString("name");
                    accountName.setText(accountNameString);
                    accountAddress.setText(account.getString("address"));
                    accountPhone.setText(account.getString("phone"));
                    accountWebsite.setText(account.getString("website"));

                    editAccountButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(IndividualAccountActivity.this, EditAccountActivity.class);
                            intent.putExtra("account", account.toString());
                            startActivity(intent);
                        }
                    });

                    List<Contact> items = new ArrayList<>();

                    contacts = account.getJSONArray("contacts");
                    System.out.println("Contacts length: "+contacts.length());

                    for (int i = 0; i < contacts.length(); i++) {
                        Contact obj = new Contact();
                        //obj.image = drw_arr.getResourceId(i, -1);
                        obj.id = contacts.getJSONObject(i).getString("id");
                        obj.name = contacts.getJSONObject(i).getString("name");
                        obj.phone = contacts.getJSONObject(i).getString("phone");
                        obj.account_name = accountNameString;

                        String createdAt = contacts.getJSONObject(i).getString("created_at");
                        ZonedDateTime dateTime = Instant.parse(createdAt).atZone(ZoneId.of("Africa/Nairobi"));
                        String formatted = dateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy \nHH:mm"));
                        obj.date = formatted;
                        //obj.imageDrw = drw_arr.getDrawable(obj.image);
                        items.add(obj);

                    }
                    List<Location> itemLocations = new ArrayList<>();

                    locations = account.getJSONArray("locations");
                    System.out.println("Locations length: "+locations.length());

                    for (int i = 0; i < locations.length(); i++) {
                        Location obj = new Location();
                        //obj.image = drw_arr.getResourceId(i, -1);
                        obj.id = locations.getJSONObject(i).getString("id");
                        obj.name = locations.getJSONObject(i).getString("name");
                        obj.lat = locations.getJSONObject(i).getString("lat");
                        obj.account_name = accountNameString;

                        String createdAt = locations.getJSONObject(i).getString("created_at");
                        ZonedDateTime dateTime = Instant.parse(createdAt).atZone(ZoneId.of("Africa/Nairobi"));
                        String formatted = dateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy \nHH:mm"));
                        obj.date = formatted;
                        //obj.imageDrw = drw_arr.getDrawable(obj.image);
                        itemLocations.add(obj);

                    }
                    // Load default tab (Contacts)
                    // Create the fragment instance
                    ContactsFragment fragment = new ContactsFragment();
                    ArrayList<Contact> contactArrayList = (ArrayList<Contact>) items;

                    // Create a bundle to hold the data
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("contacts", contactArrayList);
                    fragment.setArguments(bundle);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, fragment)
                            .commit();


                    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            Fragment selectedFragment = null;


                            if (tab.getPosition() == 0) {

                                // Create the fragment instance
                                ContactsFragment fragment = new ContactsFragment();
                                ArrayList<Contact> contactArrayList = (ArrayList<Contact>) items;

                                // Create a bundle to hold the data
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("contacts", contactArrayList);
                                fragment.setArguments(bundle);

                                selectedFragment = fragment;

//                                System.out.println("items: "+items.get(0).account_name);
                            } else if (tab.getPosition() == 1) {

                                selectedFragment = LocationsFragment.newInstance(itemLocations);
                            }

                            if (selectedFragment != null) {
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_frame, selectedFragment)
                                        .commit();
                            }

                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {
                            // Optional: Handle unselected tab
                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {
                            // Optional: Handle reselection
                        }
                    });



//                    if (items.isEmpty()) {
//                        recyclerView.setVisibility(View.GONE);
//                        emptyView.setVisibility(View.VISIBLE);
//                    }
//                    else {
//                        recyclerView.setVisibility(View.VISIBLE);
//                        emptyView.setVisibility(View.GONE);
//                    }
//
//
//                    //set data and list adapter
//                    mAdapter = new AdapterListContacts(items);
//                    recyclerView.setAdapter(mAdapter);
//
//                    mAdapter.setOnItemClickListener(new AdapterListContacts.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(View view, com.example.crm.models.Contact obj, int position) {
//
//                            Contact contact = items.get(position);
//
//                            Intent intent = new Intent(IndividualAccountActivity.this,
//                                    IndividualContactActivity.class);
//                            intent.putExtra("contactId",contact.id);
//                            startActivity(intent);
//
//
//
//                        }
//                    });



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

    @Override
    public void onComplete() {

    }
}