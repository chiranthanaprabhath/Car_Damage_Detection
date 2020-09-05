package com.example.carholderapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DetectedActivity extends AppCompatActivity {
    private TextView testViewcliame,damagetype,datetime,estimation;
    private Button btnAccept, btnNo;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private DatabaseReference mDatabase;
    public ListView listView;
    public List<String> imageDetectlist;
    public ArrayList<Bitmap> bmp_images;
    public boolean check=false;
    public String dd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.claim_details_layout);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        listView=(ListView)findViewById(R.id.androidlistdetect);
        testViewcliame=findViewById(R.id.claimeNo);
        damagetype=findViewById(R.id.claimetype);
        datetime=findViewById(R.id.Damgetype);
        btnAccept=findViewById(R.id.iddetectaccept);
        btnNo=findViewById(R.id.derecttoweb);
        estimation=findViewById(R.id.Estimationtype);
        testViewcliame.setText("Claim No : "+GlobleVariableClass.ClaimNo +":Details");
        ImageView backButton=findViewById(R.id.menu);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mDatabase.child("UserDetails").child("M").child(GlobleVariableClass.username).child(GlobleVariableClass.ClaimNo).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DamageDetails data =new DamageDetails((String) dataSnapshot.child("DamageType").getValue(),"","","","","",(String) dataSnapshot.child("Estimation").getValue(),"",(String) dataSnapshot.child("Date").getValue(),"",(String) dataSnapshot.child("ImageListDetected").getValue(),GlobleVariableClass.username,"");
                        showdetected(data);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.w("dd", "loadPost:onCancelled", databaseError.toException());
                        // ...
                    }
                });

            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(check){
                    startActivity(new Intent(DetectedActivity.this, AcceptedActivity.class));}
                    else{
                        Toast.makeText(getApplicationContext(),"Wait.......",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(check){
                    DamageDetails claime = new DamageDetails(GlobleVariableClass.damageType,GlobleVariableClass.ImageList,GlobleVariableClass.location,GlobleVariableClass.meterReading,GlobleVariableClass.Status,GlobleVariableClass.Damage,GlobleVariableClass.Estimation,GlobleVariableClass.ClaimNo,getDate(),getTime(),"",GlobleVariableClass.username,"");
                    mDatabase.child("UserDetails").child("S").child(GlobleVariableClass.username).child(GlobleVariableClass.ClaimNo).setValue(claime);
                    startActivity(new Intent(DetectedActivity.this, DirectToRemoteAcsserConfirm.class));}
                    else{
                        Toast.makeText(getApplicationContext(),"Wait.........",Toast.LENGTH_SHORT).show();
                    }
                }

            });

    }

    public String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }
    public String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }
    public class AsyncTaskLoadImage extends AsyncTask<String, Void, ArrayList<Bitmap>> {
        public List<String> images;
        public ArrayList<Bitmap> bmp_images2;
        public AsyncTaskLoadImage(List<String> images) {
            this.images = images;
        }

        @Override
        protected ArrayList<Bitmap> doInBackground(String... params) {
            Bitmap bitmap = null;
            ArrayList<Bitmap> bmp_images2 = new ArrayList<Bitmap>();
            try {
                for (int i=0; i<images.size();i++) {
                    URL url = new URL(this.images.get(i));
                    bitmap = BitmapFactory.decodeStream((InputStream) url.getContent());
                    bmp_images2.add(bitmap);
                }
            } catch (IOException e) {
                Log.e("error", e.getMessage());
            }
            return bmp_images2;
        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> bmp_images2) {

            SlidingImage_Adapter customCountryList = new SlidingImage_Adapter(DetectedActivity.this,bmp_images2);
            listView.setAdapter(customCountryList);
            final ProgressBar simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
            simpleProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(DetectedActivity.this, "Here is the Result", Toast.LENGTH_SHORT).show();
            check=true;

        }
    }
    public void showdetected(DamageDetails data){
        boolean A = !data.Estimation.isEmpty();
        Log.d("sssssssssssssssssss",data.Estimation);
        if (A){
            GlobleVariableClass.Estimation=data.Estimation;
            imageDetectlist=stringToList(data.ImageListDetected);
            damagetype.setText(data.DamageType);
            datetime.setText(data.Date+" : "+data.currentTime);
            estimation.setText("Rs: "+data.Estimation);
            new AsyncTaskLoadImage(imageDetectlist).execute("");

        }


    }

    public List<String> stringToList(String string){
        if(string.isEmpty()){
            return new ArrayList<String>();
        }
        else{
            String str[] = string.split("  ");
            List<String> al = new ArrayList<String>();
            al = Arrays.asList(str);
            return al;
        }

    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed(); // this can go before or after your stuff below
        // do your stuff when the back button is pressed
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        // super.onBackPressed(); calls finish(); for you

        // clear your SharedPreferences
        getSharedPreferences("preferenceName",0).edit().clear().commit();
    }

}
