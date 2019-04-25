package com.example.homeswipe_1.Meddelande.Meddelande_gast;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.homeswipe_1.R;
import com.example.homeswipe_1.Utils.BottomNavHelper;
import com.example.homeswipe_1.Utils.BottomNavHelperGast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

public class MeddelandeGastActivity extends AppCompatActivity {
    private RecyclerView mRecyler_medd;
    private RecyclerView.Adapter mMeddelandeAdapter;
    private RecyclerView.LayoutManager mMeddelandeLayoutManeger;
    private static final String TAG = "MeddelandeGastActivity";
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_meddelande_gast);
        overridePendingTransition(0, 0);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        mRecyler_medd = (RecyclerView) findViewById(R.id.RecyklerView_Meddelande);
        mRecyler_medd.setNestedScrollingEnabled(false);
        mRecyler_medd.setHasFixedSize(true);
        mMeddelandeLayoutManeger = new LinearLayoutManager(MeddelandeGastActivity.this);
        mRecyler_medd.setLayoutManager(mMeddelandeLayoutManeger);
        mMeddelandeAdapter = new MeddelandeGastAdapter(getDataSetMeddelande(), MeddelandeGastActivity.this);
        mRecyler_medd.setAdapter(mMeddelandeAdapter);
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        getUserMeddelandeId();

        setupVottomNavView();

    }
    private void setupVottomNavView (){
        Log.d(TAG, "setupVottomNavView: strarta nav setup");
        BottomNavigationViewEx bottomNav = (BottomNavigationViewEx) findViewById(R.id.bnve_meddelande);
        BottomNavHelper.setupVottomNavView(bottomNav);
        BottomNavHelperGast.enableNavigation(MeddelandeGastActivity.this,bottomNav);
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


                    MeddelandeGastObject obk = new MeddelandeGastObject(userID,name,profileImageUrl);
                    resualtMeddelande.add(obk);
                    mMeddelandeAdapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private ArrayList<MeddelandeGastObject> resualtMeddelande= new ArrayList<MeddelandeGastObject>();

    private List<MeddelandeGastObject> getDataSetMeddelande() {
        return resualtMeddelande;
    }



}
