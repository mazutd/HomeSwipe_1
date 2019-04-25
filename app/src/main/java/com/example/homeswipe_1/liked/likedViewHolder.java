package com.example.homeswipe_1.liked;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.homeswipe_1.R;
import com.example.homeswipe_1.apartment_view;

public class likedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView mLikedId, mHyran,mApartmentId;
    public ImageView mApartmentImage;
    public likedViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mHyran        = (TextView) itemView.findViewById(R.id.sub_text);
        mLikedId        = (TextView) itemView.findViewById(R.id.supporting_text);
        mApartmentImage = (ImageView) itemView.findViewById(R.id.media_image);
        mApartmentId        = (TextView) itemView.findViewById(R.id.apartmentId);
    }

    @Override
    public void onClick(View v) {
        Intent intent1 = new Intent(v.getContext(), apartment_view.class);
        Bundle b = new Bundle();
        b.putString("apartmentID",mApartmentId.getText().toString());
        b.putString("apartmentAdress",mLikedId.getText().toString());
        intent1.putExtras(b);
        v.getContext().startActivity(intent1);
    }
}
