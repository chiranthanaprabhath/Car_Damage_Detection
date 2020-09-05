package com.example.carholderapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoreDetailsActivity extends Activity {
    private DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("UserDetails");
    //TextView AnunalPay = findViewById(R.id.blood_group);
    //TextView Branch = findViewById(R.id.blood_group);
    public TextView ACR;
    public TextView AmountPayable;
    public TextView BaldType;
    public TextView Excesses;
    public TextView OtherDeductions;
    public TextView UnderInsurance;
    public TextView SalvageParts;
    public TextView RejectType;
    public List<String> partlist;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_details_layout);
        ImageView backButton=findViewById(R.id.menu);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ACR = findViewById(R.id.acr);
        AmountPayable = findViewById(R.id.amountPayable);
        BaldType = findViewById(R.id.bladtype);
        Excesses = findViewById(R.id.excesses);
        OtherDeductions = findViewById(R.id.otherDetection);
        UnderInsurance = findViewById(R.id.underInsurace);
        SalvageParts = findViewById(R.id.slvageParts);
        RejectType = findViewById(R.id.rejectDetatils);


        mDatabase.child("S").child(GlobleVariableClass.username).child(GlobleVariableClass.ClaimNo).child("Estimation").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Estimation data =new Estimation((String) dataSnapshot.child("ACR").getValue(),(String) dataSnapshot.child("AmountPayable").getValue(),(String) dataSnapshot.child("BaldType").getValue(),(String) dataSnapshot.child("Excesses").getValue(),(String) dataSnapshot.child("OtherDeductions").getValue(),(String) dataSnapshot.child("UnderInsurance").getValue());
                        showdata(data);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        mDatabase.child("S").child(GlobleVariableClass.username).child(GlobleVariableClass.ClaimNo).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String dataparts =((String) dataSnapshot.child("SalvageParts").getValue());
                        showdataParts(dataparts);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        mDatabase.child("S").child(GlobleVariableClass.username).child(GlobleVariableClass.ClaimNo).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String datapartsnew =((String) dataSnapshot.child("RejectionReason").getValue());
                        showdataReject(datapartsnew);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
    public void showdataReject(String data){
        if(!(data==null)){
            final TextView partmain = findViewById(R.id.reject);
            partmain.setVisibility(View.VISIBLE);
            RejectType.setVisibility(View.VISIBLE);
            RejectType.setText(data);
        }

    }
    public void showdata(Estimation data){
        if(!(data==null)){
            if(!(data.ACR==null)){
                ACR.setText(data.ACR);
                AmountPayable.setText("Rs: "+data.AmountPayable);
                BaldType.setText(data.BaldType);
                Excesses.setText(data.Excesses);
                OtherDeductions.setText("Rs: "+data.OtherDeductions);
                UnderInsurance.setText("Rs: "+data.UnderInsurance);
            }

    }}
    public void showdataParts(String data){
        if(data==null){
            SalvageParts.setText("Waiting");
        }
        else{
            partlist=stringToList(data);
            String showparts="";
            for(int x=0;x<partlist.size();x++){
                showparts=showparts+partlist.get(x)+"\n";
            }
            SalvageParts.setText(showparts);
        }

    }
    public List<String> stringToList(String string){
        if(string.isEmpty()){
            return new ArrayList<String>();
        }
        else{
            String str[] = string.split(",");
            List<String> al = new ArrayList<String>();
            al = Arrays.asList(str);
            return al;
        }

    }
}
