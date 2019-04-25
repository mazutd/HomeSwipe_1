package com.example.homeswipe_1;

import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jackandphantom.circularimageview.RoundedImage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class apartment_view extends AppCompatActivity implements OnMapReadyCallback {
    private RoundedImage mLghbild;
    private DatabaseReference appartmentDb;
    private String mApartmentID;
    private TextView mAdress,mHyra,mHyra2,mKvm,mOmrade,mAntalrum;
    private ImageView mImage;
    private GoogleMap mMap;
    public  String finalAdrss;
    private RatingBar mRatingBar;
    private TextView mRatingScale;
    private FirebaseAuth mAuth;
    private String currentUserUid;
    private Button mBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_view);
        mApartmentID = getIntent().getExtras().getString("apartmentID");
        appartmentDb   = FirebaseDatabase.getInstance().getReference().child("Lagenheter").child(mApartmentID);
        mAdress = findViewById(R.id.madress_view);
        mImage  = findViewById(R.id.lghImage_view);
        mHyra  = findViewById(R.id.hyra_view);
        mHyra2  = findViewById(R.id.hyra2_view);
        mKvm  = findViewById(R.id.kvm_view);
        mOmrade  = findViewById(R.id.postnummer_view);
        mAntalrum  = findViewById(R.id.antalrumSsptr2);
        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mRatingScale = (TextView) findViewById(R.id.tvRatingScale);
        mAuth = FirebaseAuth.getInstance();
        mBack = findViewById(R.id.btnSubmit);
        currentUserUid = mAuth.getCurrentUser().getUid();
        appartmentDb   = FirebaseDatabase.getInstance().getReference().child("Lagenheter").child(mApartmentID);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                mRatingScale.setText(String.valueOf(v));
                switch ((int) ratingBar.getRating()) {
                    case 1:
                        mRatingScale.setText("Dålig");
                        setApartmentRate("1",currentUserUid);
                        break;
                    case 2:
                        mRatingScale.setText("Godkänd");
                        setApartmentRate("2",currentUserUid);

                        break;
                    case 3:
                        mRatingScale.setText("Bra");
                        setApartmentRate("3",currentUserUid);

                        break;
                    case 4:
                        mRatingScale.setText("Mycket bra");
                        setApartmentRate("4",currentUserUid);

                        break;
                    case 5:
                        mRatingScale.setText("Helt Suverän!");
                        setApartmentRate("5",currentUserUid);

                        break;
                    default:
                        mRatingScale.setText("");
                }
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        appartmentDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("adress")!= null){
                        TextView currentUserName = (TextView) findViewById(R.id.userNameTxt);
                        String name = (String) map.get("adress").toString();
                        mAdress.setText(name);
                        finalAdrss = name;
                    }
                    if (map.get("postnummer")!= null){
                        String omrade = (String) map.get("postnummer").toString();
                        mOmrade.setText(omrade);
                    }
                    if (map.get("hyran")!= null){
                        String hyran = (String) map.get("hyran").toString();
                        mHyra.setText(hyran+" kr/mån");
                        mHyra2.setText(hyran);
                    }
                    if (map.get("kvm")!= null){
                        String kvm = (String) map.get("kvm").toString();
                        mKvm.setText(kvm);
                    }
                    if (map.get("antal_rum")!= null){
                        String name = (String) map.get("antal_rum").toString();
                        mAntalrum.setText(name);
                    }
                    if (map.get("apartmentImageUrl")!= null){
                        String currentUsertImage = (String) map.get("apartmentImageUrl").toString();
                        Glide.with(getApplication()).load(currentUsertImage).into(mImage);
                    }else
                    {
                        Glide.with(getApplication()).load(R.mipmap.emptystate).into(mImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Toast.makeText(apartment_view.this, getIntent().getExtras().getString("apartmentID"), Toast.LENGTH_SHORT).show();

    }

    private void setApartmentRate(String rate, String userUid) {
        final Map userInfo = new HashMap();
        userInfo.put("rating",rate);
        appartmentDb.updateChildren(userInfo);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }
        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            finalAdrss = getIntent().getExtras().getString("apartmentAdress");

            addresses = geocoder.getFromLocationName(finalAdrss, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses.size() > 0) {
            double latitude= addresses.get(0).getLatitude();
            double longitude= addresses.get(0).getLongitude();
            LatLng sydney = new LatLng(latitude, longitude);
            mMap.setMinZoomPreference(12);
            mMap.addMarker(new MarkerOptions().position(sydney).title(finalAdrss));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }


    }
}
