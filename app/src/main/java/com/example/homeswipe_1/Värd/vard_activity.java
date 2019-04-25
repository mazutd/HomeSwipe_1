package com.example.homeswipe_1.Värd;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.homeswipe_1.Lägenhet.appartment_activity;
import com.example.homeswipe_1.R;
import com.example.homeswipe_1.Utils.BottomNavHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.jackandphantom.circularimageview.CircleImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class vard_activity extends AppCompatActivity{
    private FirebaseAuth mAuth;
    private String currentUserUid, currentUserName, currentUsertImage, currentUserPhone;
    private TextView mCurrentUsertelNr;
    private static final String TAG = "vard_activity";
    private CircleImage mVardImageAdd,mVardImage;
    private Button saveBtn;
    private EditText mteleNummer;
    private ImageView userimage;
    private FloatingActionButton homeFltBottomBtn;
    private DatabaseReference mCustomerDataBase,mApartmentDB;
    private Uri resultUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_vard_activity);
        mAuth = FirebaseAuth.getInstance();
        currentUserUid = mAuth.getCurrentUser().getUid();
        mVardImageAdd = (CircleImage) findViewById(R.id.circleImage1);
        mVardImage = (CircleImage) findViewById(R.id.circleImageUser);
        mCurrentUsertelNr  = findViewById(R.id.userTelnrStr);
        mCustomerDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserUid);
        mApartmentDB = FirebaseDatabase.getInstance().getReference().child("Lagenheter").child(currentUserUid);

        getUserInfo();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //Frågar om behörighet för kartan
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Frågar om behörighet för kartan
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        else{
            Log.d(TAG, "onCreate: ");        }
        homeFltBottomBtn = findViewById(R.id.bottom_navigation_fab);
        homeFltBottomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(vard_activity.this, appartment_activity.class);
                startActivity(intent);
                return;
            }

        });


        mVardImageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
        //getPossitTypeUser ();
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
                            Toast.makeText(vard_activity.this, "", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getUserInfo() {
        mApartmentDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int numer1 = (int) dataSnapshot.child("connection").child("like").getChildrenCount();
                int numer2 = (int) dataSnapshot.child("match").getChildrenCount();
                addBadgeAt(1, numer1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mCustomerDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    int MeddelandeCount = (int) dataSnapshot.child("match").getChildrenCount();
                    addBadgeAt(3, MeddelandeCount);

                    TextView currentUserEmail = (TextView) findViewById(R.id.userEmailTxt);
                    TextView currentUserEmail2 = (TextView) findViewById(R.id.userEmail2Txt);
                    currentUserEmail.setText(mAuth.getCurrentUser().getEmail());
                    currentUserEmail2.setText(mAuth.getCurrentUser().getEmail());

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("name")!= null){
                        TextView currentUserName = (TextView) findViewById(R.id.userNameTxt);
                        String name = (String) map.get("name").toString();
                        currentUserName.setText(name);
                    }
                    if (map.get("phone")!= null){
                        TextView PhoneNr = (TextView) findViewById(R.id.userTelnrStr);
                        String name = (String) map.get("phone").toString();
                        PhoneNr.setText(name);
                    }
                    if (map.get("phone") == null){
                        CreateAlertDialog("ss");
                    }
                    if (map.get("profileImageUrl")!= null){
                        String currentUsertImage = (String) map.get("profileImageUrl").toString();
                        Glide.with(getApplication()).load(currentUsertImage).into(mVardImage);
                    }else
                    {
                        Glide.with(getApplication()).load(R.mipmap.nophoto).into(mVardImage);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void saveUserInfo(){
        currentUserPhone = mCurrentUsertelNr.getText().toString();
        Map userInfo = new HashMap();
        userInfo.put("phone",currentUserPhone);
        mCustomerDataBase.updateChildren(userInfo);
        if(resultUri !=null){
            final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("ProfileImages").child(currentUserUid);
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
                            Map newImage = new HashMap();
                            newImage.put("profileImageUrl", uri.toString());
                            mCustomerDataBase.updateChildren(newImage);
                            Intent intent = new Intent(vard_activity.this,vard_activity.class);
                            startActivity(intent);


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                        }
                    });
                }
            });

        }else {
            Intent intent = new Intent(vard_activity.this,vard_activity.class);
            startActivity(intent);
        }


    }
    private void setupVottomNavView (){
        BottomNavigationViewEx bottomNav = (BottomNavigationViewEx) findViewById(R.id.bnve);
        BottomNavHelper.setupVottomNavView(bottomNav);

        BottomNavHelper.enableNavigation(vard_activity.this,bottomNav);
    }
    public void showApartment(View view) {

        Intent intent = new Intent(vard_activity.this, appartment_activity.class);
        startActivity(intent);
        finish();
        return;
    }
    private void CreateAlertDialog (final String type) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        TextView currentUserName = (TextView) findViewById(R.id.userNameTxt);

        alert.setTitle("Information saknas");
        alert.setMessage("Hej "+currentUserName.getText().toString()+"! ditt telefonnummer saknas...fyll i fältet nedan och klicka sedan på spara. ");

        final EditText input = new EditText(this);

        alert.setView(input);

        alert.setPositiveButton("Spara", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                Map userInfo = new HashMap();
                userInfo.put("phone",value);
                mCustomerDataBase.updateChildren(userInfo);
                mCurrentUsertelNr.setText(value);


            }
        });


        alert.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            mVardImage.setImageURI(resultUri);
            saveUserInfo();
        }

    }
}
