package com.example.carholderapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.carholderapp.imageclsicifiaction.Classifier;
import com.example.carholderapp.imageclsicifiaction.TensorFlowImageClassifier;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ImageclasificationActiviy extends Activity {
    private static final String MODEL_PATH = "model.tflite";
    private static final boolean QUANT = false;
    private static final String LABEL_PATH = "labels.txt";
    private static final int INPUT_SIZE = 224;
    private Classifier classifier;
    private Executor executor = Executors.newSingleThreadExecutor();
    public ArrayList<Bitmap> byteArrayPicture = new ArrayList<Bitmap>();
    FirebaseStorage storage;
    StorageReference storageReference;
    public Boolean Othertype=false;
    public int OtherImagePosition;
    public Boolean DetectOrNot=false;
    public Boolean DetectOrNotmodel=false;
    public Boolean freeOrNot=false;
    public String urilist="";
    private DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("UserDetails");
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clasifiation_layout);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        byteArrayPicture=GlobleVariableClass.byteArrayPicture;
        backgroundCacification newbackGP = new backgroundCacification(byteArrayPicture);
        newbackGP.start();
        freeOrNot=false;

    }
    class backgroundCacification extends Thread{

        ArrayList<Bitmap> imageset;
        backgroundCacification(ArrayList<Bitmap> imagesend){
            this.imageset=imagesend;
        }
        @Override
        public void run(){
            initTensorFlowAndLoadModel();
            while(true){
            if (DetectOrNotmodel){
                for (int i = 0; i < imageset.size(); i++) {
                    final Bitmap bmpimageDetected = Bitmap.createScaledBitmap(imageset.get(i), INPUT_SIZE, INPUT_SIZE, false);
                    final List<Classifier.Recognition> results = classifier.recognizeImage(bmpimageDetected);
                    if(results.get(0).getTitle().equals("severeDamage")){
                        DetectOrNot = true;
                    }
                    if(results.get(0).getTitle().equals("Other")){
                        OtherImagePosition=i;
                        Othertype = true;
                    }
                    try {
                        // code runs in a thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ImageclasificationActiviy.this, results.get(0).getTitle(), Toast.LENGTH_SHORT).show();


                            }
                        });
                    } catch (final Exception ex) {
                        Log.i("---","Exception in thread");
                    }

                }
                break;
            }
            }
            if(Othertype){
                try {
                    // code runs in a thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final int index = OtherImagePosition;
                            AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(ImageclasificationActiviy.this);
                            myAlertDialog.setTitle("Image Invalid");
                            myAlertDialog.setMessage("Image Position "+String.valueOf(OtherImagePosition)+" is Not Valid Image\n Please Try Again");
                            myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {
                                    finish();
                                }});
                            myAlertDialog.show();


                        }
                    });
                } catch (final Exception ex) {
                    Log.i("---","Exception in thread");
                }

            }
            else if(DetectOrNot){
                for (int i = 0; i < imageset.size(); i++) {
                    uploadImage(imageset.get(i),i);
                }
                while (true){
                if(GlobleVariableClass.byteArrayPicture.size() == stringToList(urilist).size()){
                    GlobleVariableClass.ImageList=urilist;
                    GlobleVariableClass.ClaimNo=Geanrate_Name_Cliame()+Geanrate_Number_Claime();
                    DamageDetails claime = new DamageDetails(GlobleVariableClass.damageType,GlobleVariableClass.ImageList,GlobleVariableClass.Lat+","+GlobleVariableClass.Log,GlobleVariableClass.meterReading,GlobleVariableClass.Status,GlobleVariableClass.Damage,GlobleVariableClass.Estimation,GlobleVariableClass.ClaimNo,getDate(),getTime(),"",GlobleVariableClass.username,"");
                    mDatabase.child("S").child(GlobleVariableClass.username).child(GlobleVariableClass.ClaimNo).setValue(claime);
                    break;
                }}
                startActivity(new Intent(ImageclasificationActiviy.this, DirectToRemoteAcsserConfirm.class));

            }

            else{
                GlobleVariableClass.ClaimNo=Geanrate_Name_Cliame()+Geanrate_Number_Claime();

                for (int i = 0; i < imageset.size(); i++) {
                    uploadImage(imageset.get(i),i);
                }
                while (true){
                if(GlobleVariableClass.byteArrayPicture.size() == stringToList(urilist).size()){
                    GlobleVariableClass.ImageList=urilist;

                    DamageDetails claime = new DamageDetails(GlobleVariableClass.damageType,GlobleVariableClass.ImageList,GlobleVariableClass.location,GlobleVariableClass.meterReading,GlobleVariableClass.Status,GlobleVariableClass.Damage,GlobleVariableClass.Estimation,GlobleVariableClass.ClaimNo,getDate(),getTime(),"",GlobleVariableClass.username,"");
                    mDatabase.child("M").child(GlobleVariableClass.username).child(GlobleVariableClass.ClaimNo).setValue(claime);
                    break;
                }}
                mDatabase.child("CurrentUser").setValue(new currentUser(GlobleVariableClass.username));
                mDatabase.child("CurrentID").setValue(new currentID(GlobleVariableClass.ClaimNo));

                startActivity(new Intent(ImageclasificationActiviy.this, DetectedActivity.class));
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

        private void UploadData(){

        }

        private void initTensorFlowAndLoadModel() {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        classifier = TensorFlowImageClassifier.create(
                                getAssets(),
                                MODEL_PATH,
                                LABEL_PATH,
                                INPUT_SIZE,
                                QUANT);
                        DetectOrNotmodel=true;

                    } catch (final Exception e) {
                        throw new RuntimeException("Error initializing TensorFlow!", e);
                    }
                }
            });
        }
        public String Geanrate_Name() {
            String DATA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghtjklmnopqrstunvzxyz";
            Random RANDOM = new Random();
            StringBuilder sb = new StringBuilder(10);

            for (int i = 0; i < 10; i++) {
                sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
            }

            return sb.toString();
        }
        public String Geanrate_Name_Cliame() {
            String DATA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            Random RANDOM = new Random();
            StringBuilder sb = new StringBuilder(4);

            for (int i = 0; i < 4; i++) {
                sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
            }

            return sb.toString();
        }
        public String Geanrate_Number_Claime() {
            String DATA = "0123456789";
            Random RANDOM = new Random();
            StringBuilder sb = new StringBuilder(4);

            for (int i = 0; i < 4; i++) {
                sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
            }

            return sb.toString();
        }
        private void uploadImage(Bitmap converetdImage, final int i){

            if(converetdImage != null)
            {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                converetdImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                converetdImage.recycle();

                /*final ProgressDialog progressDialog = new ProgressDialog(ImageclasificationActiviy.this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();*/
                final String randomname=Geanrate_Name();
                StorageReference ref = storageReference.child("uploaded/"+randomname+".jpeg");
                ref.putBytes(byteArray)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //progressDialog.dismiss();

                                Geturl(randomname);
                                try {
                                    // code runs in a thread
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(ImageclasificationActiviy.this, "Uploaded Image "+String.valueOf(i+1), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } catch (final Exception ex) {
                                    Log.i("---","Exception in thread");
                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                final Exception xe=e;
                                //progressDialog.dismiss();
                                try {
                                    // code runs in a thread
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(ImageclasificationActiviy.this, "Failed "+xe.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } catch (final Exception ex) {
                                    Log.i("---","Exception in thread");
                                }

                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                /*double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploading Data Please Wait!  "+(int)progress+"%");*/
                            }
                        });
            }
            //deleteImage();
        }
        public void Geturl(String name){
            storageReference.child("uploaded/"+name+".jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url= uri.toString();
                    urilist= urilist + url + "  ";

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });
        }




    }


}
