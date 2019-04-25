package com.example.homeswipe_1.Meddelande.Meddelande_gast;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.homeswipe_1.R;

import java.util.List;

public class MeddelandeGastAdapter extends RecyclerView.Adapter<MeddelandeViewGastHolder> {
    private List<MeddelandeGastObject> meddelandeList;
    private Context context;

    public MeddelandeGastAdapter(List<MeddelandeGastObject> meddelandeList, Context context){

        this.meddelandeList = meddelandeList;
        this.context = context;
    }

    @NonNull
    @Override
    public MeddelandeViewGastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.meddelande_item, null, false);
        RecyclerView.LayoutParams towp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(towp);
        MeddelandeViewGastHolder rcv = new MeddelandeViewGastHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull MeddelandeViewGastHolder meddelandeViewHolder, int position) {
            meddelandeViewHolder.mMeddelandeID.setText(meddelandeList.get(position).getUserId());
            meddelandeViewHolder.mMeddelandeName.setText(meddelandeList.get(position).getName());
            Glide.with(context).load(meddelandeList.get(position).getProfileImage()).into(meddelandeViewHolder.mMeddelandeImage);
    }

    @Override
    public int getItemCount() {
        return meddelandeList.size();
    }
}
