package com.example.crmtest;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

//public class BaseActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_base);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
//}

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base); // common layout for all activities

//        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_1) {
                // Do something for navigation_1
                startActivity(new Intent(this, AccountsActivity.class));
//                return true;
            } else if (itemId == R.id.navigation_3) {
                // Do something for navigation_2
                startActivity(new Intent(this, MainActivity.class));

            }
            return true;
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        updateNavigationBarState();
    }

    private void updateNavigationBarState() {
        int actionId = getNavigationMenuItemId();
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(actionId);
    }

    // To be overridden in each activity to specify which menu item should be selected
    protected abstract int getNavigationMenuItemId();
}
