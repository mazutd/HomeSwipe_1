package com.example.homeswipe_1.Chat;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.homeswipe_1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {
    private List<ChatObject> ChatList;
    private Context context;
    private String currentuserUid;
    private static final String TAG = "ChatAdapter";
    DatabaseReference mDatabaseUserInfo;
    public ChatAdapter(List<ChatObject> meddelandeList, Context context){

        this.ChatList = meddelandeList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mDatabaseUserInfo = FirebaseDatabase.getInstance().getReference().child("Users");
        currentuserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, null, false);
        RecyclerView.LayoutParams towp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(towp);
        ChatViewHolder rcv = new ChatViewHolder(layoutView);
        return rcv;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull final ChatViewHolder chatViewHolder, int position) {
        final int nummber=position;
        if (ChatList.get(position).getSkapadav()){

            mDatabaseUserInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String myimageStr = dataSnapshot.child(ChatList.get(nummber).getUserId()).child("profileImageUrl").getValue().toString();
                    Glide.with(context).load(myimageStr).into(chatViewHolder.Imagemy);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            chatViewHolder.mMeddelande.setText(ChatList.get(position).getMeddelande());
            chatViewHolder.mMeddelandeother.setVisibility(View.GONE);
            chatViewHolder.imageOther.setVisibility(View.GONE);
        }else{

            mDatabaseUserInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String myimageStr = dataSnapshot.child(ChatList.get(nummber).getUserId()).child("profileImageUrl").getValue().toString();
                    Glide.with(context).load(myimageStr).into(chatViewHolder.imageOther);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            chatViewHolder.mMeddelandeother.setText(ChatList.get(position).getMeddelande());
            chatViewHolder.Imagemy.setVisibility(View.GONE);
            chatViewHolder.mMeddelande.setVisibility(View.GONE);


        }

    }

    @Override
    public int getItemCount() {
        return ChatList.size();
    }
}
