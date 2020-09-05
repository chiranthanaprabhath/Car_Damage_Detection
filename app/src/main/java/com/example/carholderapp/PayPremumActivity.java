package com.example.carholderapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PayPremumActivity extends Activity {
    private DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("users");
    TextView branchName,total,period,vehicalNo,PolicyNo,AnualAmount;
    String PrimeAccount;
    Button pay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payprimume_layout);
        branchName=findViewById(R.id.branch_name);
        total=findViewById(R.id.total);
        period=findViewById(R.id.period_time);
        vehicalNo=findViewById(R.id.vehical_no);
        PolicyNo=findViewById(R.id.PolicyNo);
        AnualAmount=findViewById(R.id.annualAmount);
        pay=findViewById(R.id.pay_premum);
        ImageView backButton=findViewById(R.id.menu);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mDatabase.child(GlobleVariableClass.username).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserDetails data =new UserDetails((String) dataSnapshot.child("AnunalPay").getValue(),(String) dataSnapshot.child("Branch").getValue(),(String) dataSnapshot.child("ChassisNo").getValue(),(String) dataSnapshot.child("Color").getValue(),(String) dataSnapshot.child("ContactNo").getValue(),(String) dataSnapshot.child("FullName").getValue(),(String) dataSnapshot.child("Hire").getValue(),(String) dataSnapshot.child("MakeModel").getValue(),(String) dataSnapshot.child("NIC").getValue(),(String) dataSnapshot.child("PeriodCover").getValue(),(String) dataSnapshot.child("PolicyNo").getValue(),(String) dataSnapshot.child("PrimeAccount").getValue(),(String) dataSnapshot.child("SumInsu").getValue(),(String) dataSnapshot.child("VehicalNo").getValue(),(String) dataSnapshot.child("YearMake").getValue(),(String) dataSnapshot.child("image").getValue());
                        PrimeAccount=data.PrimeAccount;
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
        branchName.setText(data.Branch);
        period.setText("Anual Premium from "+data.PeriodCover);
        PolicyNo.setText(data.PolicyNo);
        total.setText(data.SumInsu);
        vehicalNo.setText(data.VehicalNo);
        AnualAmount.setText("$"+data.AnunalPay);
        final ProgressBar simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        simpleProgressBar.setVisibility(View.INVISIBLE);


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((PrimeAccount).equals("No")){
                    startActivity(new Intent(PayPremumActivity.this, PremumPayActivity.class));
                }
                else {
                    AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(PayPremumActivity.this);
                    myAlertDialog.setTitle("Premium Account");
                    myAlertDialog.setMessage("Premium amount has already been paid for the selected year.");
                    myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            // do something when the Cancel button is clicked
                        }});
                    myAlertDialog.show();
                }

            }
        });
    }
}
