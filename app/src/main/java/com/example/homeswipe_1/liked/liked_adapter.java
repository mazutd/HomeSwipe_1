package com.example.homeswipe_1.liked;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.homeswipe_1.R;

import java.util.List;

public class liked_adapter extends RecyclerView.Adapter<likedViewHolder> {
    private List<liked_object> likedList ;
    private Context context;

    public liked_adapter(List<liked_object> likedList, Context context){
        this.likedList = likedList;
        this.context   = context;
    }

    @NonNull
    @Override
    public likedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.liked_ite, null, false);
        likedViewHolder rcv = new likedViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull likedViewHolder likedViewHolder, int i) {
        likedViewHolder.mLikedId.setText(likedList.get(i).getAppartmentId());
        likedViewHolder.mHyran.setText(likedList.get(i).getHyran()+" kr/mån");
        likedViewHolder.mApartmentId.setText(likedList.get(i).getApartmentId());
        Glide.with(context).load(likedList.get(i).getApartmentUrl()).into(likedViewHolder.mApartmentImage);
    }

    @Override
    public int getItemCount() {
        return likedList.size();
    }
}
