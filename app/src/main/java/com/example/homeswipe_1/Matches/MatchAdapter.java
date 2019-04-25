package com.example.homeswipe_1.Matches;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.homeswipe_1.R;

import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchViewHolder> {
    private List<MatchObject> matchList;
    private Context context;


    public MatchAdapter(List<MatchObject> matchList, Context context){
        this.matchList =  matchList;
        this.context   =  context;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.matchitem, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        MatchViewHolder rcv = new MatchViewHolder(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        holder.mMatchId.setText(matchList.get(position).getAppartmentId());
        Glide.with(context).load(matchList.get(position).getProfileImage()).into(holder.mApartmentImage);
    }

    @Override
    public int getItemCount() {
        return matchList.size() ;
    }
}
