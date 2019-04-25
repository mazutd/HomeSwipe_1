package com.example.homeswipe_1.Lägenhet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.homeswipe_1.R;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<items> {
    private TextView adress,postnummer,image,hyran,antal_rum,kvm,rate;
    Context context;
    private ImageView ciggImg,djurImg;
    private String ciggValue;
    private static final String TAG = "arrayAdapter";
    private TextView mYtan, mAntalrum;
    private RatingBar mRate;
    public arrayAdapter(Context context, int resurceId, List<items> items){
        super(context, resurceId, items);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        items card_item = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        adress    = (TextView) convertView.findViewById(R.id.adressTxt);
        hyran     = (TextView) convertView.findViewById(R.id.adressTxt4);
        djurImg   = (ImageView) convertView.findViewById(R.id.imageView11);
        ciggImg   = (ImageView) convertView.findViewById(R.id.imageView12);
        mRate     = (RatingBar) convertView.findViewById(R.id.ratingBar2);
        ImageView image = (ImageView) convertView.findViewById(R.id.roundedImageLgh);
        adress.setText(card_item.getAppartmentAdress()+" / "+card_item.getPostnummer().toUpperCase());
        mRate.setRating(Integer.parseInt(card_item.getRate()));
        Glide.with(getContext()).load(R.mipmap.emptystate).into(image);

        hyran.setText(card_item.getHyran()+"  kr/mån");
        ciggValue = card_item.getCigg();
        Boolean ciggValue = Boolean.valueOf(card_item.getCigg());
        Boolean djurValue = Boolean.valueOf(card_item.getDjur());

        if (card_item.getApartmentUrl() != null) {
            Glide.with(getContext()).load(card_item.getApartmentUrl()).into(image);
        }else {
            Glide.with(getContext()).load(R.mipmap.emptystate).into(image);
        }

        if(ciggValue.equals(true))
        {
            ciggImg.setVisibility(View.VISIBLE);

        }else
        {            ciggImg.setVisibility(View.INVISIBLE);


        }


        if(djurValue.equals(true))
        {
            djurImg.setVisibility(View.VISIBLE);
        }else
        {            djurImg.setVisibility(View.INVISIBLE);


        }

        return convertView;
    }



}

