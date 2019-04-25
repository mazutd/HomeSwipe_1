package com.example.homeswipe_1.Chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.homeswipe_1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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

public class chatActicity extends AppCompatActivity {
    private RecyclerView mRecyler_Chat;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManeger;
    private static final String TAG = "chatActicity";
    private String currentUserID, mMeddelandeID,chatId,mMeddelandeUser,mMeddelandeUserImage,mMeddelandeSenderImage;
    private EditText mMeddelandeText;
    private Button mSkickaKnapp;
    private ImageView mBackbutton;
    private TextView mMeddelandeUserName;
    private CircleImage mTopUserImage;
    DatabaseReference mDatabaseUser,mDatabaseChat,mDatabaseUserInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_acticity);

        //Hämtar Meddelande id
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mMeddelandeID = getIntent().getExtras().getString("MeddelandeId");
        mMeddelandeUser = getIntent().getExtras().getString("MeddelandeUser");

        mTopUserImage  = findViewById(R.id.chatImageUser);
        mMeddelandeUserName = findViewById(R.id.chat_send_image);
        mMeddelandeUserName.setText(mMeddelandeUser);
        mMeddelandeText =  findViewById(R.id.meddelandeText);
        mSkickaKnapp = findViewById(R.id.skickaKnapp);


        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("match").child(mMeddelandeID).child("ChatID");
        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("chat");
        mDatabaseUserInfo = FirebaseDatabase.getInstance().getReference().child("Users");
        mRecyler_Chat = (RecyclerView) findViewById(R.id.RecyklerView_Chat);
        mRecyler_Chat.setNestedScrollingEnabled(false);
        mRecyler_Chat.setHasFixedSize(false);

        mChatLayoutManeger = new LinearLayoutManager(chatActicity.this);
        mRecyler_Chat.setLayoutManager(mChatLayoutManeger);
        mChatAdapter = new ChatAdapter(getDataSeChat(), chatActicity.this);
        mRecyler_Chat.setAdapter(mChatAdapter);
        mDatabaseUserInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String mm = getIntent().getExtras().getString("MeddelandeId");

                String image = dataSnapshot.child(mm).child("profileImageUrl").getValue().toString();

                if (image != null){
                    Glide.with(getApplication()).load(image).into(mTopUserImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        getChatID();
        mBackbutton = findViewById(R.id.chat_backbutton);
        mSkickaKnapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        mBackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }

    private void sendMessage() {

        String skickaMEddelandeText = mMeddelandeText.getText().toString();
        if (!skickaMEddelandeText.isEmpty()){
            DatabaseReference meddelandeDB = mDatabaseChat.push();
            Map nyMeddelande = new HashMap();
            nyMeddelande.put("skapadAv", currentUserID);
            nyMeddelande.put("text", skickaMEddelandeText);

            meddelandeDB.setValue(nyMeddelande);

        }
        mMeddelandeText.setText(null);
    }

    private void getChatID(){

        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    chatId = dataSnapshot.getValue().toString();
                    mDatabaseChat = mDatabaseChat.child(chatId);
                    getChatMeddelande();
                }
            }

            private void getChatMeddelande() {
                mDatabaseChat.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if (dataSnapshot.exists()){
                            String meddelande = null;
                            String skapadAv   = null;

                            if (dataSnapshot.child("text").getValue()!=null){
                                meddelande = dataSnapshot.child("text").getValue().toString();
                            }

                            if (dataSnapshot.child("skapadAv").getValue()!=null){
                                skapadAv = dataSnapshot.child("skapadAv").getValue().toString();
                            }

                            if (meddelande !=null && skapadAv != null){
                                Boolean skapadAvBoolean = false;
                                if (skapadAv.equals(currentUserID)){
                                    skapadAvBoolean = true;
                                }
                                ChatObject nyMeddelande = new ChatObject(meddelande,skapadAvBoolean, skapadAv);
                                resualtChat.add(nyMeddelande);
                                mChatAdapter.notifyDataSetChanged();

                            }
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private ArrayList<ChatObject> resualtChat= new ArrayList<ChatObject>();
    private List<ChatObject> getDataSeChat() {
        return resualtChat;
    }
}
