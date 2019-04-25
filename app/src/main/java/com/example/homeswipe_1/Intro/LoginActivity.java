package com.example.homeswipe_1.Intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homeswipe_1.MainActivity;
import com.example.homeswipe_1.R;
import com.example.homeswipe_1.Värd.vard_activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private Button mLogin;
    private EditText mEmail, mPassword;
    private static final String TAG = "LoginActivity";
    private FirebaseAuth myAuth;
    private TextView registeraTxt;
    private FirebaseAuth.AuthStateListener firebaseAuthstateListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_login);

        registeraTxt = findViewById(R.id.registeraTxt);
        myAuth = FirebaseAuth.getInstance();
        firebaseAuthstateListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user !=null)
                {
                    checkUserType ();

                }
            }
        };

        mLogin = (Button) findViewById(R.id.loginBtn);
        mEmail     = (EditText) findViewById(R.id.emailID);
        mPassword  = (EditText) findViewById(R.id.passwordID);
        registeraTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent registeraActivity = new Intent(LoginActivity.this, RegisteringActivity.class);
              startActivity(registeraActivity);
              finish();
            }
        });
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email    = mEmail.getText().toString();
                final String password = mPassword.getText().toString();

                myAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful())
                        {
                            Toast.makeText(LoginActivity.this, "Error Nåt är fel..", Toast.LENGTH_SHORT).show();
                        }else {
                            checkUserType();
                        }
                    }
                });
            }
        });

    }
    public void checkUserType (){
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!= null){
            final DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
            currentUserDb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        Log.d(TAG, "onDataChange: checkar user");
                        if (dataSnapshot.child("typ").getValue().toString() != null){
                            //startActivity(new Intent(MainActivity.this, MainActivity.class));
                            String currentUserTyp = dataSnapshot.child("typ").getValue().toString();
                            Log.d(TAG, "onDataChange: uuuusss"+currentUserTyp);
                            if (currentUserTyp.equals("Hyresvärd")){
                                Log.d(TAG, "onDataChange: uuuusssHH"+currentUserTyp);

                                Intent intent = new Intent(LoginActivity.this, vard_activity.class);
                                startActivity(intent);
                                finish();
                            }else
                            {
                                Log.d(TAG, "onDataChange: uuuusssGG"+currentUserTyp);

                                Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent2);
                                finish();
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
    @Override
    protected void onStart() {

        super.onStart();
        myAuth.addAuthStateListener(firebaseAuthstateListner);
    }
    @Override
    protected void onStop() {
        super.onStop();

        myAuth.removeAuthStateListener(firebaseAuthstateListner);
    }

}
