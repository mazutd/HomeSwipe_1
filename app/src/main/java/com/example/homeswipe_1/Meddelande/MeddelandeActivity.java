package com.example.homeswipe_1.Meddelande;

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

public class MeddelandeActivity extends AppCompatActivity {
    private RecyclerView mRecyler_medd;
    private RecyclerView.Adapter mMeddelandeAdapter;
    private RecyclerView.LayoutManager mMeddelandeLayoutManeger;
    private static final String TAG = "MeddelandeGastActivity";
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meddelande);
        overridePendingTransition(0, 0);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        mRecyler_medd = (RecyclerView) findViewById(R.id.RecyklerView_Meddelande);
        mRecyler_medd.setNestedScrollingEnabled(false);
        mRecyler_medd.setHasFixedSize(true);
        mMeddelandeLayoutManeger = new LinearLayoutManager(MeddelandeActivity.this);
        mRecyler_medd.setLayoutManager(mMeddelandeLayoutManeger);
        mMeddelandeAdapter = new MeddelandeAdapter(getDataSetMeddelande(), MeddelandeActivity.this);
        mRecyler_medd.setAdapter(mMeddelandeAdapter);
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userhDB = FirebaseDatabase.getInstance().getReference().child("Lagenheter").child(currentUserID);
        userhDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int numer1 = (int) dataSnapshot.child("connection").child("like").getChildrenCount();
                addBadgeAt(1,numer1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        getUserMeddelandeId();

        setupVottomNavView();

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
                            Toast.makeText(MeddelandeActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void getUserMeddelandeId() {

        DatabaseReference meddelandeDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("match");
        meddelandeDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot meddelande:dataSnapshot.getChildren()){
                        FetchMeddelandeInformation(meddelande.getKey());
                    }
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void FetchMeddelandeInformation(String key) {
        Log.d(TAG, "FetchMeddelandeInformation: namnet är "+key);
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String userID  = dataSnapshot.getKey();
                    String name  = "";
                    String profileImageUrl  = "";

                    if (dataSnapshot.child("name").getValue()!=null){
                        name = dataSnapshot.child("name").getValue().toString();
                    }
                    if (dataSnapshot.child("profileImageUrl").getValue()!=null){
                        profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                    }


                    MeddelandeObject obk = new MeddelandeObject(userID,name,profileImageUrl);
                    resualtMeddelande.add(obk);
                    mMeddelandeAdapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private ArrayList<MeddelandeObject> resualtMeddelande= new ArrayList<MeddelandeObject>();

    private List<MeddelandeObject> getDataSetMeddelande() {
        return resualtMeddelande;
    }

    private void setupVottomNavView (){
        BottomNavigationViewEx bottomNav = (BottomNavigationViewEx) findViewById(R.id.bnve);
        BottomNavHelper.setupVottomNavView(bottomNav);
        BottomNavHelper.enableNavigation(MeddelandeActivity.this,bottomNav);
    }

}
