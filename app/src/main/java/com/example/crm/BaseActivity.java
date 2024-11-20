package com.example.crm;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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



    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        updateNavigationBarState();
//    }

//    private void updateNavigationBarState() {
//        int actionId = getNavigationMenuItemId();
//        Toast.makeText(BaseActivity.this,"this",Toast.LENGTH_SHORT).show();
//        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
//        bottomNavigationView.setSelectedItemId(actionId);
//    }

    // To be overridden in each activity to specify which menu item should be selected
    protected abstract int getNavigationMenuItemId();
}
