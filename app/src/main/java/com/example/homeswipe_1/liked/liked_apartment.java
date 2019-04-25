package com.example.homeswipe_1.liked;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.homeswipe_1.R;
import com.example.homeswipe_1.Utils.BottomNavHelper;
import com.example.homeswipe_1.Utils.BottomNavHelperGast;
import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

public class liked_apartment extends AppCompatActivity {
    private RecyclerView mRecykleView;
    private RecyclerView.Adapter mLikedAdapter;
    private RecyclerView.LayoutManager mLikedLayoutManger;
    private static final String TAG = "liked_apartment";
    private  String currentUserUid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_apartment);
        setupVottomNavView ();

        currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mRecykleView = (RecyclerView) findViewById(R.id.rv1);
        mRecykleView.setNestedScrollingEnabled(false);
        mRecykleView.setHasFixedSize(true);
        mLikedLayoutManger = new LinearLayoutManager(this);
        mRecykleView.setLayoutManager(mLikedLayoutManger);
        mLikedAdapter = new liked_adapter(getDataMatches(), liked_apartment.this);
        mRecykleView.setAdapter(mLikedAdapter);

        getLikedApartmentId();
        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(mRecykleView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return false;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    DatabaseReference likedDb = FirebaseDatabase.getInstance().getReference().child("Lagenheter");
                                    likedDb.child(resultLiked.get(position).getApartmentId()).child("connection").child("like").child(currentUserUid).removeValue();
                                    likedDb.child(resultLiked.get(position).getApartmentId()).child("connection").child("unlike").child(currentUserUid).setValue(true);
                                    resultLiked.remove(position);
                                    mLikedAdapter.notifyItemRemoved(position);
                                }
                                mLikedAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    resultLiked.remove(position);
                                    mLikedAdapter.notifyItemRemoved(position);
                                }
                                mLikedAdapter.notifyDataSetChanged();
                            }
                        });

        mRecykleView.addOnItemTouchListener(swipeTouchListener);
    }

    private void getLikedApartmentId() {
        DatabaseReference likedDb = FirebaseDatabase.getInstance().getReference().child("Lagenheter");
        likedDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    for (DataSnapshot like : dataSnapshot.getChildren()){
                        FetchLikedApartment(like.getKey());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void FetchLikedApartment(final String key) {
        final DatabaseReference liked_ietmDb = FirebaseDatabase.getInstance().getReference().child("Lagenheter").child(key);
        liked_ietmDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String adress = "";
                    String apartmentImage = "";
                    String hyran = "";
                    if (dataSnapshot.child("connection").child("like").hasChild(currentUserUid)){
                        if (dataSnapshot.child("adress").getValue() != null){
                            adress = dataSnapshot.child("adress").getValue().toString();
                        }
                        if (dataSnapshot.child("apartmentImageUrl").getValue() != null){
                            apartmentImage = dataSnapshot.child("apartmentImageUrl").getValue().toString();
                        }
                        if (dataSnapshot.child("hyran").getValue() != null){
                            hyran = dataSnapshot.child("hyran").getValue().toString();
                        }
                        liked_object object = new liked_object(adress,apartmentImage,hyran, key);
                        resultLiked.add(object);
                        mLikedAdapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<liked_object> resultLiked= new ArrayList<liked_object>();
    private List<liked_object> getDataMatches() {
        return resultLiked;
    }
    private void setupVottomNavView (){

        Log.d(TAG, "setupVottomNavView: strarta nav setup");
        BottomNavigationViewEx bottomNav = (BottomNavigationViewEx) findViewById(R.id.bnve_liked);
        BottomNavHelper.setupVottomNavView(bottomNav);

        BottomNavHelperGast.enableNavigation(liked_apartment.this,bottomNav);
    }
}

