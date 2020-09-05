package com.example.carholderapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AcceptedActivity extends Activity {
    public Button ok;
    public TextView show;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credited_account_layout);
        ok=findViewById(R.id.ok);
        show=findViewById(R.id.textViewdmanoygicen4);
        show.setText("Rs."+GlobleVariableClass.Estimation+"has been credited to your \n account for claime No: "+GlobleVariableClass.ClaimNo+".");
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AcceptedActivity.this, HomeActivity.class));
            }
        });


    }
}
