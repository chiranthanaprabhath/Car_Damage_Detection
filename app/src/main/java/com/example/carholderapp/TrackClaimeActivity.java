package com.example.carholderapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TrackClaimeActivity extends Activity {
    private DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("UserDetails");
    public ArrayList<DamageDetails> listData = new ArrayList<DamageDetails>();
    public ListView listView;
    public ArrayList<String> Status = new ArrayList<String>();
    public ArrayList<String> CliameNo = new ArrayList<String>();
    public ArrayList<String> DateTime = new ArrayList<String>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_claimelayout);
        listView= findViewById(R.id.androidlisttrack);
        ImageView backButton=findViewById(R.id.menu);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrackClaimeActivity.this, HomeActivity.class));
            }
        });
        mDatabase.child("S").child(GlobleVariableClass.username).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            String name = ds.getKey();
                            DamageDetails data =new DamageDetails((String) dataSnapshot.child(name).child("DamageType").getValue(),(String) dataSnapshot.child(name).child("ImageList").getValue(),(String) dataSnapshot.child(name).child("Location").getValue(),(String) dataSnapshot.child(name).child("meterReaing").getValue(),(String) dataSnapshot.child(name).child("Status").getValue(),(String) dataSnapshot.child(name).child("Damage").getValue(),(String) dataSnapshot.child(name).child("Estimation").child("AmountPayable").getValue(),(String) dataSnapshot.child(name).child("ClaimNo").getValue(),(String) dataSnapshot.child(name).child("Date").getValue(),(String) dataSnapshot.child(name).child("currentTime").getValue(),(String) dataSnapshot.child(name).child("ImageListDetected").getValue(),GlobleVariableClass.username,(String) dataSnapshot.child(name).child("Estimation").child("LeastAstimation").getValue());
                            listData.add(data);

                        }
                        showdata(listData);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.w("dd", "loadPost:onCancelled", databaseError.toException());
                        // ...
                    }
                });
        //Claime_Adapter customCountryList = new Claime_Adapter(TrackClaimeActivity.this,);
        //listView.setAdapter(customCountryList);
    }
    public void showdata(final List<DamageDetails> datalistnew){
        for(int i=0;i<datalistnew.size();i++){
            Status.add(datalistnew.get(i).Status);
            CliameNo.add(datalistnew.get(i).ClaimNo);
            DateTime.add(datalistnew.get(i).Date);
        }

        Claime_Adapter detailsList = new Claime_Adapter(this,Status,CliameNo,DateTime);
        listView.setAdapter(detailsList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                GlobleVariableClass.damageDetails=datalistnew.get(position);
                startActivity(new Intent(TrackClaimeActivity.this, ShowClaimeDetailsActivity.class));
                //Toast.makeText(getApplicationContext(),"xxxx"+ String.valueOf(position)+" xxx",Toast.LENGTH_SHORT).show();

            }
        });
        final ProgressBar simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar1);
        simpleProgressBar.setVisibility(View.INVISIBLE);


    }

}
