package com.example.carholderapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.cardform.view.CardForm;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends Activity {
    private DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("users");
    //TextView AnunalPay = findViewById(R.id.blood_group);
    //TextView Branch = findViewById(R.id.blood_group);
    public TextView ChassisNo;
    public TextView Color;
    public TextView ContactNo;
    public TextView FullName;
    public TextView Hire;
    public TextView NIC;
    public TextView PeriodCover;
    public TextView PolicyNo;
    //TextView PrimeAccount;
    public TextView SumInsu;
    public TextView VehicalNo;
    public TextView YearMake;
    public TextView MakeModel;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        ImageView backButton=findViewById(R.id.menu);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // AnunalPay = findViewById(R.id.blood_group);
        // Branch = findViewById(R.id.blood_group);
        ChassisNo = findViewById(R.id.marriage);
        Color = findViewById(R.id.approved_by);
         ContactNo = findViewById(R.id.blood_group);
         FullName = findViewById(R.id.name);
         Hire = findViewById(R.id.mobileNumber);
         NIC = findViewById(R.id.IdNo);
        PeriodCover = findViewById(R.id.education);
         PolicyNo = findViewById(R.id.PolicyNo);
        // PrimeAccount = findViewById(R.id.blood_group);
         SumInsu = findViewById(R.id.occupation);
         VehicalNo = findViewById(R.id.gender);
         YearMake = findViewById(R.id.email);
        MakeModel=findViewById(R.id.dob);

        mDatabase.child(GlobleVariableClass.username).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserDetails data =new UserDetails((String) dataSnapshot.child("AnunalPay").getValue(),(String) dataSnapshot.child("Branch").getValue(),(String) dataSnapshot.child("ChassisNo").getValue(),(String) dataSnapshot.child("Color").getValue(),(String) dataSnapshot.child("ContactNo").getValue(),(String) dataSnapshot.child("FullName").getValue(),(String) dataSnapshot.child("Hire").getValue(),(String) dataSnapshot.child("MakeModel").getValue(),(String) dataSnapshot.child("NIC").getValue(),(String) dataSnapshot.child("PeriodCover").getValue(),(String) dataSnapshot.child("PolicyNo").getValue(),(String) dataSnapshot.child("PrimeAccount").getValue(),(String) dataSnapshot.child("SumInsu").getValue(),(String) dataSnapshot.child("VehicalNo").getValue(),(String) dataSnapshot.child("YearMake").getValue(),(String) dataSnapshot.child("image").getValue());
                        showdata(data);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.w("dd", "loadPost:onCancelled", databaseError.toException());
                        // ...
                    }
                });

    }
    public void showdata(UserDetails data){
        ChassisNo.setText(data.ChassisNo);
        Color.setText(data.Color);
        ContactNo.setText(data.ContactNo);
        FullName.setText(data.FullName);
        Hire.setText(data.Hire);
        NIC.setText(data.NIC);
        PeriodCover.setText(data.PeriodCover);
        PolicyNo.setText("Policy No: "+data.PolicyNo);
        SumInsu.setText(data.SumInsu);
        VehicalNo.setText(data.VehicalNo);
        YearMake.setText(data.YearMake);
        MakeModel.setText(data.MakeModel);
        ImageView ivBasicImage = (ImageView) findViewById(R.id.person);
        Picasso.get().load(data.Image).into(ivBasicImage);
        final ProgressBar simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        simpleProgressBar.setVisibility(View.INVISIBLE);
    }
}
