package com.example.carholderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class MainActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        Button btn = findViewById(R.id.lin);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressBar simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
                simpleProgressBar.setVisibility(View.VISIBLE);
                username = (EditText)findViewById(R.id.usrusr);
                password = (EditText)findViewById(R.id.pswrdd);
                String userName= username.getText().toString();
                final String passSet= password.getText().toString();
                if (!userName.equals("") ){
                DocumentReference docRef = db.collection("users").document(username.getText().toString());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            simpleProgressBar.setVisibility(View.INVISIBLE);
                            DocumentSnapshot document = task.getResult();
                            String passfire= document.getString("password");
                            if (document.exists()) {
                                Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                                if(passfire.equals(passSet)){
                                    GlobleVariableClass.username = username.getText().toString();
                                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Wrong Password or UserName Try Again",Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(getApplicationContext(),"Wrong Password or UserName Try Again",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                    }
                });}
                else {
                    Toast.makeText(getApplicationContext(),"Enter UserName and Password",Toast.LENGTH_SHORT).show();
                }
                //
            }
        });
    }
}
