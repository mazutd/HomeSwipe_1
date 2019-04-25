package com.example.homeswipe_1.Chat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.homeswipe_1.R;
import com.jackandphantom.circularimageview.CircleImage;

public class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mMeddelandeID, mMeddelandeName, mMeddelande,mMeddelandeTid, mMeddelandeother;
    TextView messageText, timeText, nameText;
    ImageView profileImage;
    public LinearLayout mContainer;
    public ImageView mMeddelandeImage, imageOther, Imagemy;
    public TextView mSendChatname;
    public CircleImage mSenderImage, mMyimage;



    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mMeddelande = itemView.findViewById(R.id.MeddelandeMy);
        mMeddelandeother = itemView.findViewById(R.id.meddelandeOther);
        //mContainer  = itemView.findViewById(R.id.chatContainer);
        messageText = (TextView) itemView.findViewById(R.id.text_message_body);
        timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        nameText = (TextView) itemView.findViewById(R.id.text_message_name);

        mSendChatname = (TextView) itemView.findViewById(R.id.chat_send_image);
        imageOther = (CircleImage) itemView.findViewById(R.id.imageView2Sender);
        Imagemy = (CircleImage) itemView.findViewById(R.id.imageView3MyImage);

    }

    @Override
    public void onClick(View view) {

    }
}
