package com.example.homeswipe_1.Intresserade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.homeswipe_1.Matches.MatchActivity;
import com.example.homeswipe_1.Matches.MatchObject;
import com.example.homeswipe_1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jackandphantom.circularimageview.CircleImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class intresserade_vard extends AppCompatActivity {
    private RecyclerView mRyclerView;
    private RecyclerView.Adapter mMatchesAdapter ;
    private RecyclerView.LayoutManager mMatchesLayoutManger;
    private static final String TAG = "MatchActivity";
    private String currentuserId, mIntresserId;
    private Context context;
    private CircleImage mIntresseradImage;
    private TextView mIntresseradName;
    private Button mAccept,mError;
    private DatabaseReference mIntresseradDb2,mApartmentDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intresserade_vard);
        overridePendingTransition(0, 0);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        mAccept = findViewById(R.id.acceptBtn);
        mError = findViewById(R.id.errorBtn);
        if (getIntent().getExtras().getString("IntresseradID") != null){
        mIntresserId = getIntent().getExtras().getString("IntresseradID");
        }
        currentuserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mIntresseradDb2 = FirebaseDatabase.getInstance().getReference().child("Users");
        mApartmentDB = FirebaseDatabase.getInstance().getReference().child("Lagenheter");


        if (mIntresserId !=null)
        {
            getIntresseradUser();

        }
        mError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference mApartmentDBv   = mApartmentDB;
                mApartmentDBv.child(currentuserId).child("connection").child("like").child(mIntresserId).removeValue();

                Intent intent = new Intent(v.getContext(), MatchActivity.class);
                Bundle b = new Bundle();
                b.putString("IntresseradID", mIntresserId);
                intent.putExtras(b);
                v.getContext().startActivity(intent);
            }
        });
        mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference intresseUserDb1 = mIntresseradDb2;
                final DatabaseReference mApartmentDB2   = mApartmentDB;
                String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();

                //Lägget till chatt id under den som blev accepterad av värden
                intresseUserDb1.child(mIntresserId).child("match").child(currentuserId).child("ChatID").setValue(key);
                //Lägget till chatt id under värden
                intresseUserDb1.child(currentuserId).child("match").child(mIntresserId).child("ChatID").setValue(key);
                // Tar bort Like (den som är intresserad) från/under LagenhetsID i FB
                mApartmentDB2.child(currentuserId).child("connection").child("like").child(mIntresserId).removeValue();
                //Lägger en match på den som är intresserad under lägenheten oxå(EXTRA)
                mApartmentDB2.child(currentuserId).child("match").child(mIntresserId).setValue(true);



                Map userInfo = new HashMap();
                mIntresseradDb2.child(currentuserId).updateChildren(userInfo);
                mApartmentDB2.child(currentuserId).updateChildren(userInfo);
                Intent intent = new Intent(v.getContext(), MatchActivity.class);
                Bundle b = new Bundle();
                b.putString("IntresseradID", mIntresserId);
                intent.putExtras(b);
                v.getContext().startActivity(intent);
            }
        });
    }

    private void addMatch() {
        mIntresseradDb2.child(mIntresserId);
        Map userInfo = new HashMap();

        userInfo.put("match",mIntresserId);

        finish();


    }

    private void getIntresseradUser() {
        final DatabaseReference intresseUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(mIntresserId);
        intresseUserDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    TextView mIntressersadName = (TextView) findViewById(R.id.IntresseradName);
                     CircleImage mIntresseradImage = (CircleImage) findViewById(R.id.circleImageUserIntresserad);
                    Map<String, Object> intresseMap = (Map<String, Object>) dataSnapshot.getValue();
                    String currentUsertImage = "https://britz.mcmaster.ca/images/nouserimage.gif";
                    mIntressersadName.setText(intresseMap.get("name").toString());
                    String currentUsertImageStr = (String) intresseMap.get("profileImageUrl").toString();
                    Glide.with(getApplication()).load(currentUsertImageStr).into(mIntresseradImage);


                }
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
}
