package com.example.homeswipe_1.Matches;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.homeswipe_1.Intresserade.intresserade_vard;
import com.example.homeswipe_1.R;
import com.jackandphantom.circularimageview.CircleImage;

public class MatchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView mMatchId, mMatchAdress;
    public CircleImage mApartmentImage;
    public MatchViewHolder(@NonNull View itemView) {

        super(itemView);
        itemView.setOnClickListener(this);

        mMatchId = (TextView) itemView.findViewById(R.id.MatchId);
        mMatchAdress = (TextView) itemView.findViewById(R.id.MatchAdress);
        mApartmentImage = (CircleImage) itemView.findViewById(R.id.apartmentImage);


    }
    @Override
    public void onClick(View view){

        Intent intent = new Intent(view.getContext(), intresserade_vard.class);
        Bundle b = new Bundle();
        b.putString("IntresseradID", mMatchId.getText().toString());
        intent.putExtras(b);
        view.getContext().startActivity(intent);
    }
}
