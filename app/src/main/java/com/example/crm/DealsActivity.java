package com.example.crm;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DealsActivity extends AppCompatActivity{
    private Toolbar toolbar;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.unused_activity_accounts);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Deals");


//        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_deals);
        Navigation.setNavigation(this,bottomNavigationView);


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