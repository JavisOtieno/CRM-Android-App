package com.example.crm;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.crm.adapters.AdapterListCalls;
import com.example.crm.models.Phonecall;
import com.example.crm.utils.EncryptedPrefsUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public class ProfileActivity extends AppCompatActivity implements OnSuccessListener {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private String mAuthToken;
    private TextView nameTextView;
    private TextView dateCreatedTextView;
    private TextView postTextView;
    private TextView emailTextView;
    private TextView phoneTextView;
    private TextView contactsNumberTextView;
    private TextView accountsNumberTextView;
    private TextView dealsNumberTextView;
    private ImageView profileImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
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
        actionBar.setTitle("Profile");

        nameTextView = (TextView) findViewById(R.id.profileNameTextView);
        dateCreatedTextView = (TextView) findViewById(R.id.dateCreatedTextView);
        postTextView = (TextView) findViewById(R.id.postTextView);
        emailTextView = (TextView) findViewById(R.id.emailTextView);
        phoneTextView = (TextView) findViewById(R.id.phoneTextView);
        accountsNumberTextView = (TextView) findViewById(R.id.accountsNumberTextView);
        contactsNumberTextView = (TextView) findViewById(R.id.contactsNumberTextView);
        dealsNumberTextView = (TextView) findViewById(R.id.dealsNumberTextView);
        profileImageView = (ImageView) findViewById(R.id.image);

        mAuthToken = EncryptedPrefsUtil.getString("authToken", "");

        NetworkUtils.
                fetchDataGet("profile",
                        mAuthToken,ProfileActivity.this,ProfileActivity.this);
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
            List<Phonecall> items = new ArrayList<>();

            JSONObject user = jsonObject.getJSONObject("user");
            String accounts = jsonObject.getString("accounts");
            String contacts = jsonObject.getString("contacts");
            String deals = jsonObject.getString("deals");
            String name = user.getString("name");
            nameTextView.setText(name);
            System.out.println("Image Url: "+Constants.STORAGE_URL+user.getString("image"));

            Picasso.get()
                    .load(Constants.STORAGE_URL+user.getString("image"))
//                .placeholder(R.drawable.placeholder) // Optional placeholder image
                    .error(R.drawable.person_edited) // Optional error image
                    .into(profileImageView,new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("Picasso", "Image loaded successfully");
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("Picasso", "Error loading image", e);
                        }
                    });

            String createdAt = user.getString("created_at");
            ZonedDateTime dateTime = Instant.parse(createdAt).atZone(ZoneId.of("Africa/Nairobi"));
            String formatted = dateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy \nHH:mm"));
            String formattedDate = dateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
            String formattedTime = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            dateCreatedTextView.setText(formattedDate);
            String status = user.getString("status");
            status = "Status :"+status.substring(0, 1).toUpperCase() +
                    status.substring(1).toLowerCase();
            postTextView.setText(status);
            emailTextView.setText(user.getString("email"));
            phoneTextView.setText(user.getString("phone"));

            dealsNumberTextView.setText(deals);
            accountsNumberTextView.setText(accounts);
            contactsNumberTextView.setText(contacts);








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
            Toasts.toastIconError(ProfileActivity.this,"Error Loading");
            Log.e("API Response", "JSON Parsing error: " + e.getMessage());
        }

    }
}