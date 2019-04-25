package com.example.homeswipe_1.Meddelande;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.homeswipe_1.Chat.chatActicity;
import com.example.homeswipe_1.R;
import com.jackandphantom.circularimageview.CircleImage;

public class MeddelandeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mMeddelandeID, mMeddelandeName;
    public ImageView mMeddelandeImage;
    public CircleImage mMeddelandeSenderImage;



    public MeddelandeViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mMeddelandeID = (TextView) itemView.findViewById(R.id.MeddelandeId);
        mMeddelandeName = (TextView) itemView.findViewById(R.id.Meddelandename);
        mMeddelandeImage = (ImageView) itemView.findViewById(R.id.meddelandeImage);
        mMeddelandeSenderImage = (CircleImage) itemView.findViewById(R.id.meddelandeImage);


    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), chatActicity.class);
        Bundle b = new Bundle();
        b.putString("MeddelandeId", mMeddelandeID.getText().toString());
        b.putString("MeddelandeUser", mMeddelandeName.getText().toString());
        b.putString("MeddelandeUserImage", mMeddelandeImage.toString());
        intent.putExtras(b);
        view.getContext().startActivity(intent);
    }
}
