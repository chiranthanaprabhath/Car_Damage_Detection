package com.example.carholderapp;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowClaimeDetailsActivity extends Activity {

    private TextView testViewcliame,damagetype,damage,status,estimation,claimeamunt;
    public ListView listView;
    public List<String> imageDetectlist;
    public ArrayList<Bitmap> bmp_images;
    public Button clickOk,more;
    public ImageView addFile;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cliamedetails_layout);
        ImageView backButton=findViewById(R.id.menu);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowClaimeDetailsActivity.this, TrackClaimeActivity.class));
            }
        });
        clickOk = findViewById(R.id.okindetails);
        addFile = findViewById(R.id.addfile);
        listView=(ListView)findViewById(R.id.androidlistdetectindetails);

        testViewcliame=findViewById(R.id.topclaimeNo);
        damagetype=findViewById(R.id.claimetypeindetals);
        status=findViewById(R.id.statusindetals);
        estimation=findViewById(R.id.Estimationindetails);
        damage=findViewById(R.id.Damgetypeindetais);
        claimeamunt=findViewById(R.id.clameamount);
        more=findViewById(R.id.moreDetals);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowClaimeDetailsActivity.this, MoreDetailsActivity.class));
            }
        });
        clickOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowClaimeDetailsActivity.this, TrackClaimeActivity.class));
            }
        });
        addFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowClaimeDetailsActivity.this, UploadFileActivity.class));
            }
        });
        showdetected(GlobleVariableClass.damageDetails);

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

            SlidingImage_Adapter customCountryList = new SlidingImage_Adapter(ShowClaimeDetailsActivity.this,bmp_images2);
            listView.setAdapter(customCountryList);
            final ProgressBar simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar2);
            simpleProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(ShowClaimeDetailsActivity.this, "Here is the Result", Toast.LENGTH_SHORT).show();

        }
    }
    public void showdetected(DamageDetails data){
            GlobleVariableClass.ClaimNo=data.ClaimNo;
            imageDetectlist=stringToList(data.ImageList);
            damagetype.setText(data.DamageType);
            status.setText(data.Status);
        testViewcliame.setText("DETAILS OF: "+data.ClaimNo);
            if(!(data.Damage==null)){
                damage.setText(data.Date+" : "+data.currentTime);
            }
        if(!(data.Estimation==null)){
            estimation.setText("Rs: "+data.Estimation);
            claimeamunt.setText("Rs: "+data.ClaimeAmount);
        }
            new ShowClaimeDetailsActivity.AsyncTaskLoadImage(imageDetectlist).execute("");



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
