package com.example.carholderapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class SlidingImage_Adapter extends ArrayAdapter {
    private ArrayList<Bitmap> bmp_images;
    private Activity context;

    public SlidingImage_Adapter(Activity context, ArrayList<Bitmap> bmp_images) {
        super(context, R.layout.slidingimages_layout,bmp_images);
        this.context = context;
        this.bmp_images=bmp_images;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.slidingimages_layout, null, true);
        ImageView imageFlag = (ImageView) row.findViewById(R.id.imageViewFlag);
        imageFlag.setImageBitmap(bmp_images.get(position));
        return  row;
    }
}