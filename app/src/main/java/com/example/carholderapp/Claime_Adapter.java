package com.example.carholderapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Claime_Adapter extends ArrayAdapter {

    private ArrayList<String> countryNames = new ArrayList<String>();
    private ArrayList<String> capitalNames = new ArrayList<String>();
    private ArrayList<String> Datetime = new ArrayList<String>();
    private Activity context;

    public Claime_Adapter(Activity context, ArrayList<String> countryNames, ArrayList<String> capitalNames,ArrayList<String> datetime) {
        super(context, R.layout.tracklayout_adapter, countryNames);
        this.context = context;
        this.countryNames = countryNames;
        this.capitalNames = capitalNames;
        this.Datetime=datetime;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.tracklayout_adapter, null, true);
        TextView textViewCountry = (TextView) row.findViewById(R.id.Stautstrack);
        TextView textViewCapital = (TextView) row.findViewById(R.id.ClaimeNotrack);
        TextView textViewdate = (TextView) row.findViewById(R.id.date);

        textViewCountry.setText(countryNames.get(position));
        textViewCapital.setText(capitalNames.get(position));
        textViewdate.setText(Datetime.get(position));
        return  row;
    }
}
