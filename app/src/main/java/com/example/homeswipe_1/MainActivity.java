package com.example.homeswipe_1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homeswipe_1.Lägenhet.arrayAdapter;
import com.example.homeswipe_1.Lägenhet.items;
import com.example.homeswipe_1.Meddelande.Meddelande_gast.MeddelandeGastActivity;
import com.example.homeswipe_1.Utils.BottomNavHelper;
import com.example.homeswipe_1.Utils.BottomNavHelperGast;
import com.google.android.gms.maps.MapView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class MainActivity extends AppCompatActivity {
    private items lgh_data[];
    private ArrayAdapter arrayAdapter;
    private int i;
    private FirebaseAuth mAuth;
    private static final String TAG = "MainActivity";
    private MapView mapView;
    private DatabaseReference appartmentDb,UserDB;
    @BindView(R.id.frame) SwipeFlingAdapterView flingContainer;
    private TextView mTextMessage;
    private String currentUserUid;
    private Button logOutBtn;
    private Button mMapImg;



    ListView listView ;
    List<items> rowItems;
    private final static int FINE_LOCATION = 100;
    private final static int PLACE_PICKER_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        getPossitTypeUser();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Frågar om behörighet för kartan
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        else{
            Log.d(TAG, "onCreate: hahaha");        }


        setContentView(R.layout.activity_main);
        setupVottomNavView();

        appartmentDb = FirebaseDatabase.getInstance().getReference().child("Lagenheter");
        logOutBtn = findViewById(R.id.logOutBtn);

        mAuth = FirebaseAuth.getInstance();
        currentUserUid = mAuth.getCurrentUser().getUid();
        UserDB  = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserUid);

        rowItems = new ArrayList<items>();
        arrayAdapter = new arrayAdapter(this, R.layout.item,rowItems );
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MeddelandeGastActivity.class);
                startActivity(intent);

            }
        });

        UserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int nummer = (int) dataSnapshot.child("match").getChildrenCount();
                addBadgeAt(2,nummer);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {

                items object = (items) dataObject;
                String appartmentId = object.getAppartmentId();
                appartmentDb.child(appartmentId).child("connection").child("unlike").child(currentUserUid).setValue(true);
                Toast.makeText(MainActivity.this, "Left!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRightCardExit(Object dataObject) {
                items object = (items) dataObject;
                String appartmentId = object.getAppartmentId();
                appartmentDb.child(appartmentId).child("connection").child("like").child(currentUserUid).setValue(true);

                //isConnectionMatch(appartmentId);
                Toast.makeText(MainActivity.this, "Right!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here

            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        //  add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                items object = (items) dataObject;
                String appartmentId = object.getAppartmentId();
                String appartmentadress = object.getAppartmentAdress();
                Intent intent1 = new Intent(MainActivity.this, apartment_view.class);
                Bundle b = new Bundle();
                b.putString("apartmentID",appartmentId);
                b.putString("apartmentAdress",appartmentadress);
                intent1.putExtras(b);
                startActivity(intent1);


            }
        });

    }

    private Badge addBadgeAt(int position, int number) {
        BottomNavigationViewEx bnve = findViewById(R.id.bnveMain);
        bnve.getBottomNavigationItemView(1);
        return new QBadgeView(this)
                .setBadgeNumber(number)
                .setBadgeBackground(getResources().getDrawable(R.mipmap.gronknapp))
                .setGravityOffset(22, 2, true)
                .bindTarget(bnve.getBottomNavigationItemView(position))
                .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                    @Override
                    public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                        if (Badge.OnDragStateChangedListener.STATE_SUCCEED == dragState)
                            Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void setupVottomNavView (){
        Log.d(TAG, "setupVottomNavView: strarta nav setup");
        BottomNavigationViewEx bottomNav = (BottomNavigationViewEx) findViewById(R.id.bnveMain);
        BottomNavHelper.setupVottomNavView(bottomNav);

        BottomNavHelperGast.enableNavigation(MainActivity.this,bottomNav);
    }

    public void getPossitTypeUser (){
        DatabaseReference userVardDb = FirebaseDatabase.getInstance().getReference().child("Lagenheter");
        userVardDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()&& !dataSnapshot.child("connection").child("unlike").hasChild(currentUserUid) && !dataSnapshot.child("connection").child("like").hasChild(currentUserUid) && !dataSnapshot.child("match").hasChild(currentUserUid)){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    items Item = new items(dataSnapshot.getKey(),map.get("adress").toString(),map.get("antal_rum").toString(),map.get("beskrivning").toString(),map.get("hyran").toString(),map.get("kvm").toString(),map.get("postnummer").toString(),map.get("apartmentImageUrl").toString(),map.get("djur").toString(),map.get("rok").toString(),map.get("rating").toString());
                    rowItems.add(Item);
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

}
