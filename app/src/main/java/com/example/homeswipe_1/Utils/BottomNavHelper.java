package com.example.homeswipe_1.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.view.MenuItem;

import com.example.homeswipe_1.Intro.LoginRegiseraActivity;
import com.example.homeswipe_1.Matches.MatchActivity;
import com.example.homeswipe_1.Meddelande.MeddelandeActivity;
import com.example.homeswipe_1.R;
import com.example.homeswipe_1.Lägenhet.appartment_activity;
import com.example.homeswipe_1.Värd.vard_activity;
import com.google.firebase.auth.FirebaseAuth;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BottomNavHelper {
    private static final String TAG = "BottomNavHelper";
    private FloatingActionButton fltButton;
    public static void setupVottomNavView(BottomNavigationViewEx bottomNavigationViewEx){
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);


    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx view){
        FirebaseAuth mAuth;
        FloatingActionButton fltButton;
        mAuth = FirebaseAuth.getInstance();
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_home_vard:
                        Intent intent1 = new Intent(context, vard_activity.class);
                        context.startActivity(intent1);
                        break;
                    case R.id.navigation_dashboard_vard:
                        Intent intent2 = new Intent(context, MatchActivity.class);
                        context.startActivity(intent2);
                        break;
                    case R.id.navigation_notifications_vard:
                        Intent intent3 = new Intent(context, MeddelandeActivity.class);
                        context.startActivity(intent3);
                        break;
                    case R.id.bottom_navigation_fab:
                        Intent intent5 = new Intent(context, appartment_activity.class);
                        context.startActivity(intent5);
                        break;
                    case R.id.navigation_logout_vard:
                        FirebaseAuth mAuth;
                        mAuth = FirebaseAuth.getInstance();
                        mAuth.signOut();
                        Intent intent4 = new Intent(context, LoginRegiseraActivity.class);
                        context.startActivity(intent4);

                        break;
                }
                return false;
            }
        });
    }

    public static void getCurrentUserInfo(int i, int i1) {
    }
}
