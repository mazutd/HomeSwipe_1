package com.example.homeswipe_1.Lägenhet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.homeswipe_1.Intro.LoginRegiseraActivity;
import com.example.homeswipe_1.R;
import com.example.homeswipe_1.Utils.BottomNavHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class appartment_activity extends AppCompatActivity {
    private TextInputEditText mAdress, mPostnummer, mBeskrivning, mRum,mHyran,mKvm ;
    private Button mAddAppartment;
    private FirebaseAuth myAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthstateListner;
    private FirebaseAuth mAuth;
    private DatabaseReference appartmentDb;
    private Switch mDjur, mRok, mAllergi;
    private ImageView lghImage;
    private String currentUserUid, mLagenhet_typ,mDjurStatuts, mRokStatuts, mAllergiStatuts;
    private FloatingActionButton saveApartmentBtn;
    private Uri resultUri;

    private static final String TAG = "appartment_activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);

        setContentView(R.layout.activity_appartment_activity);
        overridePendingTransition(0, 0);


        myAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUserUid = user.getUid();
        mAdress        = (TextInputEditText) findViewById(R.id.adressStr);
        mPostnummer    = (TextInputEditText) findViewById(R.id.postnummerStr);
        mBeskrivning   = (TextInputEditText) findViewById(R.id.beskrivningStr);
        mRum           = (TextInputEditText) findViewById(R.id.rumStr);
        mHyran         = (TextInputEditText) findViewById(R.id.hyranStr);
        mKvm           = (TextInputEditText) findViewById(R.id.kvmStr);
        appartmentDb   = FirebaseDatabase.getInstance().getReference().child("Lagenheter").child(currentUserUid);
        mDjur          = (Switch) findViewById(R.id.swtDjur);
        mRok           = (Switch) findViewById(R.id.swtRok);
        mAllergi       = (Switch) findViewById(R.id.swtAllergi);
        lghImage       = (ImageView) findViewById(R.id.lghtImage2);
        saveApartmentBtn = findViewById(R.id.bottom_navigation_fab_save);


        //Hämtar Värdens lägenhet
        getApartmentInfo();
        //Lägg till lägenhet bild
        lghImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
        //Sparar ändringar i lägenhet
        saveApartmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveApartmentInfo();
            }
        });

        setupVottomNavView();

    }

    private void getApartmentInfo() {
        appartmentDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("adress")!= null){
                        TextView currentUserName = (TextView) findViewById(R.id.userNameTxt);
                        String name = (String) map.get("adress").toString();
                        mAdress.setText(name);
                    }
                    if (map.get("postnummer")!= null){
                        String name = (String) map.get("postnummer").toString();
                        mPostnummer.setText(name);
                    }
                    if (map.get("hyran")!= null){
                        String name = (String) map.get("hyran").toString();
                        mHyran.setText(name);
                    }
                    if (map.get("kvm")!= null){
                        String name = (String) map.get("kvm").toString();
                        mKvm.setText(name);
                    }
                    mDjur.setChecked(true);

                    if (map.get("antal_rum")!= null){
                        String name = (String) map.get("antal_rum").toString();
                        mRum.setText(name);
                    }
                    if (map.get("beskrivning")!= null){
                        String name = (String) map.get("beskrivning").toString();
                        mBeskrivning.setText(name);
                    }

                    if (map.get("djur")!= null){
                        String djur = (String) map.get("djur").toString();
                            mDjur.setChecked(Boolean.valueOf(djur));
                    }

                    if (map.get("allergi")!= null){
                        String allergi = (String) map.get("allergi").toString();
                            mAllergi.setChecked(Boolean.valueOf(allergi));
                    }

                    if (map.get("rok")!= null){
                        String rok = (String) map.get("rok").toString();
                        mRok.setChecked(Boolean.valueOf(rok));

                    }
                    if (map.get("apartmentImageUrl")!= null){
                        String currentUsertImage = (String) map.get("apartmentImageUrl").toString();
                        Glide.with(getApplication()).load(currentUsertImage).into(lghImage);
                    }else
                    {
                        Glide.with(getApplication()).load(R.mipmap.emptystate).into(lghImage);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void saveApartmentInfo(){
        final Map userInfo = new HashMap();
        userInfo.put("adress",mAdress.getText().toString());
        userInfo.put("postnummer",mPostnummer.getText().toString());
        userInfo.put("antal_rum",mRum.getText().toString());
        userInfo.put("hyran",mHyran.getText().toString());
        userInfo.put("kvm",mKvm.getText().toString());
        userInfo.put("beskrivning", mBeskrivning.getText().toString());
        userInfo.put("rating", "0");
        if (mDjur.isChecked()){mDjurStatuts="true";
            userInfo.put("djur", mDjurStatuts);

        }else{
            mDjurStatuts="false";
            userInfo.put("djur", mDjurStatuts);
        }

        if (mRok.isChecked()){mRokStatuts="true";
            userInfo.put("rok", mRokStatuts);

        }else{
            mRokStatuts="false";
            userInfo.put("rok", mRokStatuts);
        }

        if (mAllergi.isChecked()){mAllergiStatuts="true";
            userInfo.put("allergi", mAllergiStatuts);

        }else{
            mAllergiStatuts="false";
            userInfo.put("allergi", mAllergiStatuts);
        }
        Log.d(TAG, "saveApartmentInfo: dddd"+resultUri);
        if(resultUri !=null){
            final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("ApartmentImages").child(currentUserUid);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask =  filePath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            userInfo.put("apartmentImageUrl", uri.toString());
                            Log.d(TAG, "saveApartmentInfo: "+userInfo.get("apartmentImageUrl").toString());
                            appartmentDb.updateChildren(userInfo);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            userInfo.put("apartmentImageUrl", "https://firebasestorage.googleapis.com/v0/b/homeswipe-76ed8.appspot.com/o/ApartmentImages%2Femptystate.png?alt=media&token=e25cde3f-b081-4409-9ad5-34831b8d6f7e");

                            appartmentDb.updateChildren(userInfo);
                        }
                    });
                }
            });

        }
        appartmentDb.updateChildren(userInfo);

        Intent intent = new Intent(appartment_activity.this,appartment_activity.class);
        startActivity(intent);
        finish();
        return;


    }

    private void setupVottomNavView (){
        Log.d(TAG, "setupVottomNavView: strarta nav setup");
        BottomNavigationViewEx bottomNav = (BottomNavigationViewEx) findViewById(R.id.bnve);
        BottomNavHelper.setupVottomNavView(bottomNav);

        BottomNavHelper.enableNavigation(appartment_activity.this,bottomNav);
    }
    public void logOutUser(View view) {

        mAuth.signOut();
        Intent intent = new Intent(appartment_activity.this, LoginRegiseraActivity.class);
        startActivity(intent);
        finish();
        return;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            lghImage.setImageURI(resultUri);
            //saveApartmentInfo();
        }

    }

}
