package com.example.crm;



import android.content.Context;
import android.content.Intent;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Navigation {
    public static void setNavigation(Context context, BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
//            Toast.makeText(context, "Item ID: " + itemId, Toast.LENGTH_SHORT).show();
            if (itemId == R.id.navigation_home) {
                // Do something for navigation_1
//                context.startActivity(new Intent(context, MainActivity.class));
                Intent intent = new Intent(context, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
//                return true;
            } else if (itemId == R.id.navigation_accounts) {
                // Do something for navigation_2
                Intent intent = new Intent(context, UNUSEDAccountsActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (itemId == R.id.navigation_deals) {
                // Do something for navigation_2
//                context.startActivity(new Intent(context, DealsActivity.class));
                Intent intent = new Intent(context, DealsActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            } else if (itemId == R.id.navigation_more) {
                // Do something for navigation_2
//                context.startActivity(new Intent(context, AccountsActivity.class));
//                Intent intent = new Intent(context, MoreMenuActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                context.startActivity(intent);
            }
            return true;
        });
    }
}
