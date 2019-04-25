package com.example.homeswipe_1.Matches;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.homeswipe_1.R;
import com.example.homeswipe_1.Utils.BottomNavHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class MatchActivity extends AppCompatActivity {
    private RecyclerView mRyclerView;
    private RecyclerView.Adapter mMatchesAdapter ;
    private RecyclerView.LayoutManager mMatchesLayoutManger;
    private static final String TAG = "MatchActivity";
    private String currentuserId;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_match);

        currentuserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRyclerView = findViewById(R.id.recylView);
        mRyclerView.setNestedScrollingEnabled(false);
        mRyclerView.setHasFixedSize(true);
        mMatchesLayoutManger = new LinearLayoutManager(MatchActivity.this);
        mRyclerView.setLayoutManager(mMatchesLayoutManger);
        mMatchesAdapter = new MatchAdapter(getDataMatches(), MatchActivity.this);
        mRyclerView.setAdapter(mMatchesAdapter);
        setupVottomNavView();



        getUserMatchId();
        DatabaseReference userhDB = FirebaseDatabase.getInstance().getReference().child("Users").child(currentuserId);
        userhDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int meddelandeCount = (int) dataSnapshot.child("match").getChildrenCount();
                addBadgeAt(3, meddelandeCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private Badge addBadgeAt(int position, int number) {
        BottomNavigationViewEx bnve = findViewById(R.id.bnve);
        bnve.getBottomNavigationItemView(1);
        return new QBadgeView(this)
                .setBadgeNumber(number)
                .setBadgeBackground(getResources().getDrawable(R.mipmap.gronknapp))
                .setGravityOffset(12, 2, true)
                .bindTarget(bnve.getBottomNavigationItemView(position))
                .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                    @Override
                    public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                        if (Badge.OnDragStateChangedListener.STATE_SUCCEED == dragState)
                            Toast.makeText(MatchActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void getUserMatchId() {

        DatabaseReference matchDB = FirebaseDatabase.getInstance().getReference().child("Lagenheter").child(currentuserId).child("connection").child("like");
        matchDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot match : dataSnapshot.getChildren()){
                        FetchtMatchInformation(match.getKey());
                    }
                }
            }

            private void FetchtMatchInformation(String key) {
                DatabaseReference userhDB = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
                userhDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String userId= dataSnapshot.getKey();
                            String name  = "";
                            String Image = "";

                            if (dataSnapshot.child("name").getKey() != null){

                                name = dataSnapshot.child("name").getValue().toString();
                                Log.d(TAG, "onDataChangsse: "+name);

                            }
                            if (dataSnapshot.child("profileImageUrl").getKey() != null){
                                if(dataSnapshot.child("profileImageUrl").getValue().toString() != null){
                                    Image = dataSnapshot.child("profileImageUrl").getValue().toString();

                                }else {
                                    Image = "https://britz.mcmaster.ca/images/nouserimage.gif/image";
                                }

                            }
                            MatchObject obj = new MatchObject(userId, name, Image);
                            resultMatches.add(obj);
                            mMatchesAdapter.notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private ArrayList<MatchObject> resultMatches = new ArrayList<MatchObject>();

    private List<MatchObject> getDataMatches() {
        return resultMatches;
    }
    private void setupVottomNavView (){
        Log.d(TAG, "setupVottomNavView: strarta nav setup");
        BottomNavigationViewEx bottomNav = (BottomNavigationViewEx) findViewById(R.id.bnve);
        BottomNavHelper.setupVottomNavView(bottomNav);

        BottomNavHelper.enableNavigation(MatchActivity.this,bottomNav);
    }
}
