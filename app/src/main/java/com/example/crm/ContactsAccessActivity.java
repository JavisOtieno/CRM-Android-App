package com.example.crm;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class ContactsAccessActivity extends AppCompatActivity {
    private List<Contact> contactsList = new ArrayList<>();
    private static final int REQUEST_CODE_READ_CONTACTS = 1;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contacts_access);
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
        actionBar.setTitle("Contacts");


        recyclerView= findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        // Check for permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        } else {
            contactsList.add(new Contact("Test", "0722334455"));
            // Permission granted, load contacts
//            loadContacts();
            new LoadContactsTask().execute();
        }
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, load contacts
                new LoadContactsTask().execute();
            } else {
                // Permission denied, show a message
                Toasts.toastIconError(ContactsAccessActivity.this,
                        "Permission not granted. Load Contacts Failed.");
            }
        }
    }

//    private void loadContacts() {
//        Cursor cursor = getContentResolver().query(
//                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                null, null, null, null);
//
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                contactsList.add(new Contact(name, phoneNumber));
//            }
//            cursor.close();
//        }
//
//        // Set up RecyclerView adapter
//        ContactsAdapter adapter = new ContactsAdapter(contactsList);
//        RecyclerView recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setAdapter(adapter);
//    }

    private class LoadContactsTask extends AsyncTask<Void, Void, List<Contact>> {
        private AlertDialog dialog;

        protected void onPreExecute() {
            super.onPreExecute();

            AlertDialog.Builder builder = new AlertDialog.Builder(ContactsAccessActivity.this);
            builder.setView(R.layout.progress_dialog_layout); // Use a custom layout or a simple ProgressBar
            builder.setCancelable(false); // Prevent closing the dialog while loading
            dialog = builder.create();
            dialog.show();

        }
        @Override
        protected List<Contact> doInBackground(Void... voids) {
            List<Contact> contacts = new ArrayList<>();
            // Load contacts in the background
            Cursor cursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contacts.add(new Contact(name, phoneNumber));
                }
                cursor.close();
            }
            return contacts;
        }

        @Override
        protected void onPostExecute(List<Contact> contacts) {
            dialog.dismiss();
            // Update UI with the loaded contacts
            ContactsAdapter adapter = new ContactsAdapter(contacts);
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(new ContactsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, Contact obj, int position) {
                    Toasts.toastIconError(ContactsAccessActivity.this,
                            obj.getName()+" "+obj.getPhoneNumber());

                    Intent intent = new Intent(ContactsAccessActivity.this,
                            AddContactActivity.class);
                    intent.putExtra("contactName",obj.getName());
                    intent.putExtra("contactPhone",obj.getPhoneNumber());
                    startActivity(intent);

                }
            });

        }
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

}