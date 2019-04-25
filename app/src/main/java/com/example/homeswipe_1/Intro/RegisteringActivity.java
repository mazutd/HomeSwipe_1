package com.example.homeswipe_1.Intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.HashMap;
import java.util.Map;

public class RegisteringActivity extends AppCompatActivity {
    private Button mRegistera;
    private EditText mEmail, mPassword, mName;
    private RadioGroup mRadioGroup;
    private static final String TAG = "RegisteringActivity";
    private String currentUserTyp;
    private FirebaseAuth myAuth;
    private DatabaseReference mCustomerDataBase;

    private FirebaseAuth.AuthStateListener firebaseAuthstateListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registering);

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


        mRegistera  = (Button) findViewById(R.id.registeraBtn);
        mEmail      = (EditText) findViewById(R.id.emailID);
        mName       = (EditText) findViewById(R.id.nameID);
        mPassword   = (EditText) findViewById(R.id.passwordID);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioBtn);

        mRegistera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int select = mRadioGroup.getCheckedRadioButtonId();
                final RadioButton radioButton = (RadioButton) findViewById(select);
                //Ifal användar  typ inte vald
                if (radioButton.getText() == null){
                    return;
                }
                final String email    = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String name     = mName.getText().toString();

                myAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisteringActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful())
                        {
                            Toast.makeText(RegisteringActivity.this, "Registering misslyckades", Toast.LENGTH_SHORT).show();
                        }else{
                            String userID = myAuth.getCurrentUser().getUid();
                                DatabaseReference currentUserUid = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                            Map userinof = new HashMap<>();
                            userinof.put("name", name);
                            userinof.put("typ",radioButton.getText().toString());
                            userinof.put("profileImageUrl", "https://britz.mcmaster.ca/images/nouserimage.gif/image");
                            currentUserUid.updateChildren(userinof);
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
                                    Intent intent = new Intent(RegisteringActivity.this, vard_activity.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case "Hyresgäst":
                                    Intent intent2 = new Intent(RegisteringActivity.this, MainActivity.class);
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
