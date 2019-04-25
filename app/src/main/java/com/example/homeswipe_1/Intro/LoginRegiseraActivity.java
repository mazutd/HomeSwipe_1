package com.example.homeswipe_1.Intro;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.example.homeswipe_1.MainActivity;
import com.example.homeswipe_1.R;
import com.example.homeswipe_1.Värd.vard_activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginRegiseraActivity extends AppCompatActivity {
    private String currentUserTyp;
    private Button mLogin,mRegisera;
    private static final String TAG = "LoginRegiseraActivity";
    private VideoView videoBG;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_login_regisera);

        videoBG = (VideoView) findViewById(R.id.videoView);

        Uri uri = Uri.parse("android.resource://"
                + getPackageName()
                + "/"
                + R.raw.aprtment);

        videoBG.setVideoURI(uri);
        videoBG.start();

        videoBG.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mMediaPlayer = mediaPlayer;
                mMediaPlayer.setLooping(true);
                if (mCurrentVideoPosition != 0) {
                    mMediaPlayer.seekTo(mCurrentVideoPosition);
                    mMediaPlayer.start();
                }
            }
        });
        mLogin = (Button) findViewById(R.id.btnLoggain1);
        mRegisera = (Button) findViewById(R.id.btnRegisera);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user!=null){
                checkUserType ();
                } else
                {
                    Intent intent = new Intent(LoginRegiseraActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        });
        mRegisera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user!=null){
                    Log.d(TAG, "onClick: user Finns");
                    checkUserType ();
                } else
                {
                    Intent intent = new Intent(LoginRegiseraActivity.this, RegisteringActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        });

    }
    public void checkUserType (){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!= null){
        DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

            currentUserDb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        if (dataSnapshot.child("typ").getValue().toString() != null){
                            //startActivity(new Intent(MainActivity.this, MainActivity.class));
                            currentUserTyp = dataSnapshot.child("typ").getValue().toString();
                            switch (currentUserTyp){
                                case "Hyresvärd":
                                    Intent intent = new Intent(LoginRegiseraActivity.this, vard_activity.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case "Hyresgäst":
                                    Intent intent2 = new Intent(LoginRegiseraActivity.this, MainActivity.class);
                                    startActivity(intent2);
                                    finish();
                                    break;
                        }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }



        });


    }
}
}
