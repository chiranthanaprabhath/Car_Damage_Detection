package com.example.carholderapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.exifinterface.media.ExifInterface;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class TakeImage extends Activity {

    private LinearLayout addimage;
    ArrayList<Bitmap> bmp_images = new ArrayList<Bitmap>();
    private Spinner spinner1;
    private EditText meterReading;
    private LinearLayout buttonGo;
    public Boolean Flage;
    public ArrayList<Bitmap> byteArrayPicture = new ArrayList<Bitmap>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.takeimage_layout);
        ImageView backButton=findViewById(R.id.menu);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        ListView listView=(ListView)findViewById(R.id.androidlist);


        // For populating list data
        SlidingImage_Adapter customCountryList = new SlidingImage_Adapter(this,bmp_images);
        listView.setAdapter(customCountryList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final int index = position;
                AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(TakeImage.this);
                myAlertDialog.setTitle("Delete Image");
                myAlertDialog.setMessage("Delete Clicked Image");
                myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(getApplicationContext(),"removed",Toast.LENGTH_SHORT).show();
                        ListView listView=(ListView)findViewById(R.id.androidlist);
                        bmp_images.remove(index);
                        byteArrayPicture.remove(index);
                        SlidingImage_Adapter customCountryList = new SlidingImage_Adapter(TakeImage.this,bmp_images);
                        listView.setAdapter(customCountryList);

                    }});
                myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        // do something when the Cancel button is clicked
                    }});
                myAlertDialog.show();

            }
        });



        meterReading = findViewById(R.id.meter_reading);
        spinner1 = findViewById(R.id.spinner1);
        addimage = findViewById(R.id.addmoreimage);
        buttonGo = findViewById(R.id.go_location);
        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(TakeImage.this);
            }
        });
       buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(byteArrayPicture.size()>0){
                    GlobleVariableClass.setVariable(byteArrayPicture);
                    if(!meterReading.getText().toString().equals("") && spinner1.getSelectedItem() !=null && spinner1 != null){
                        GlobleVariableClass.meterReading=meterReading.getText().toString();
                        GlobleVariableClass.damageType=spinner1.getSelectedItem().toString();
                        Intent intent = new Intent(TakeImage.this, LocationAccidentActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Fill the Meter Value and Damage Type",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Select Images of Damage Vehical",Toast.LENGTH_SHORT).show();
                }

            }
        });
        addListenerOnSpinnerItemSelection();

    }



    public void addListenerOnSpinnerItemSelection() {
        spinner1 = findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }
    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");

                        //imageView.setImageBitmap(selectedImage);
                        bmp_images.add(selectedImage);
                        Bitmap converetdImage = getResizedBitmap(selectedImage, 500);
                        byteArrayPicture.add(converetdImage);
                        //ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        //converetdImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        //byteArrayPicture.add(stream.toByteArray());
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();
                       try {
                           Flage=true;
                            checkValidity(selectedImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                       if(Flage){
                           String[] filePathColumn = {MediaStore.Images.Media.DATA};
                           if (selectedImage != null) {
                               Cursor cursor = getContentResolver().query(selectedImage,
                                       filePathColumn, null, null, null);
                               if (cursor != null) {
                                   cursor.moveToFirst();

                                   int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                   String picturePath = cursor.getString(columnIndex);
                                   bmp_images.add(BitmapFactory.decodeFile(picturePath));
                                   //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

                                   Bitmap converetdImage = getResizedBitmap(BitmapFactory.decodeFile(picturePath), 500);
                                   byteArrayPicture.add(converetdImage);
                                   //ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                   //converetdImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                   //byteArrayPicture.add(stream.toByteArray());
                                   cursor.close();
                               }
                           }
                       }
                       else{
                           AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(TakeImage.this);
                           myAlertDialog.setTitle("Image Not Valid");
                           myAlertDialog.setMessage("Please upload valid images that are taken within 2 hours of the occurrence of the accident. \n" +
                                   "You can not upload images from your device storage that are received from social media and other sources. \n");
                           myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                               public void onClick(DialogInterface arg0, int arg1) {
                                   // do something when the Cancel button is clicked
                               }});
                           myAlertDialog.show();
                       }


                    }
                    break;
            }
        }
        ListView listView=(ListView)findViewById(R.id.androidlist);
        SlidingImage_Adapter customCountryList = new SlidingImage_Adapter(this,bmp_images);
        listView.setAdapter(customCountryList);

    }
    public void checkValidity(Uri uri) throws IOException {
        ExifInterface exif = new ExifInterface(getContentResolver().openInputStream(uri));
        String date = exif.getAttribute(ExifInterface.TAG_DATETIME);
        if(date==null){
            //Flage = false;
        }
        else{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");

        Date DateImage = null;
        try {
            DateImage = sdf.parse(date);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date TodayTime = Calendar.getInstance().getTime();
        long diffarce = ((TodayTime.getTime() - DateImage.getTime()) / (1000 * 60));
        if (diffarce > 120) {
            //Flage = false;
        }}
    }


}
