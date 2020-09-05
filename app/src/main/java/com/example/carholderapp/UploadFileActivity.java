package com.example.carholderapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.aditya.filebrowser.Constants;
import com.aditya.filebrowser.FileChooser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.apache.commons.io.FilenameUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class UploadFileActivity extends Activity {
    FirebaseStorage storage;
    StorageReference storageReference;
    private LinearLayout addimage;
    public String urilist="";
    public String urilistfile="";
    ArrayList<Bitmap> bmp_images = new ArrayList<Bitmap>();
    private LinearLayout buttonGo,chooseButton;
    public ArrayList<Bitmap> byteArrayPicture = new ArrayList<Bitmap>();
    private static final int PICK_FILE_REQUEST = 87784;
    private FileChooser data = new FileChooser();
    public ArrayList<Uri> selectedFiles = new ArrayList<Uri>();
    public boolean flag= true;

    private DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("UserDetails");
    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_file_layout);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        ListView listView=(ListView)findViewById(R.id.androidlist);


        // For populating list data
        SlidingImage_Adapter customCountryList = new SlidingImage_Adapter(this,bmp_images);
        listView.setAdapter(customCountryList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final int index = position;
                AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(UploadFileActivity.this);
                myAlertDialog.setTitle("Delete Image");
                myAlertDialog.setMessage("Delete Clicked Image");
                myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(getApplicationContext(),"removed",Toast.LENGTH_SHORT).show();
                        ListView listView=(ListView)findViewById(R.id.androidlist);
                        bmp_images.remove(index);
                        byteArrayPicture.remove(index);
                        SlidingImage_Adapter customCountryList = new SlidingImage_Adapter(UploadFileActivity.this,bmp_images);
                        listView.setAdapter(customCountryList);

                    }});
                myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        // do something when the Cancel button is clicked
                    }});
                myAlertDialog.show();




            }
        });


        addimage = findViewById(R.id.addmoreimage);
        buttonGo = findViewById(R.id.go_location);
        chooseButton=findViewById(R.id.docpdfchoose);
        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(UploadFileActivity.this);
            }
        });
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2 = new Intent(getApplicationContext(), FileChooser.class);
                i2.putExtra(Constants.SELECTION_MODE, Constants.SELECTION_MODES.MULTIPLE_SELECTION.ordinal());
                startActivityForResult(i2,PICK_FILE_REQUEST);
            }
        });
        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(byteArrayPicture.size()>0 || selectedFiles.size()>0){
                    GlobleVariableClass.setVariable(byteArrayPicture);
                    GlobleVariableClass.FileUriList=selectedFiles;
                    flag=true;
                    for (int i = 0; i < selectedFiles.size(); i++) {
                        String type = FilenameUtils.getExtension(selectedFiles.get(i).getPath());
                        if(!(type.equals("pdf")) && !(type.equals("docx"))){
                            flag=false;
                        }
                    }
                    if(flag){
                        final ProgressBar simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
                        simpleProgressBar.setVisibility(View.VISIBLE);
                        for (int i = 0; i < byteArrayPicture.size(); i++) {
                            uploadImage(byteArrayPicture.get(i),i);
                        }
                        if(selectedFiles.size()>0){
                            for (int i = 0; i < selectedFiles.size(); i++) {
                                String type = FilenameUtils.getExtension(selectedFiles.get(i).getPath());
                                uploadFile(selectedFiles.get(i),i,type);
                            }
                        }else{
                            simpleProgressBar.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(UploadFileActivity.this, FileUploadedActivity.class));
                        }


                    }
                    else{
                        Toast.makeText(getApplicationContext(),"You cannot select file type other than .pdf or .docx \n Please Try Again",Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(),"Select Bill's images or Files",Toast.LENGTH_SHORT).show();
                }

            }
        });
        ImageView backButton=findViewById(R.id.menu);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private void uploadFile(Uri file, int i, final String extention) {
        final String randomnamefile=Geanrate_Name();
        final int ifinal = i;
        StorageReference riversRef = storageReference.child("Files/"+randomnamefile+"."+extention);
        riversRef.putFile(file).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Geturlfile(randomnamefile,extention);
                try {
                    // code runs in a thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UploadFileActivity.this, "Uploaded File "+String.valueOf(ifinal+1) + extention, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (final Exception ex) {
                    Log.i("---","Exception in thread");
                }
            }
        });
    }
    public void Geturlfile(String name,String extention){
        storageReference.child("Files/"+name+"."+extention).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url= uri.toString();
                urilistfile= urilistfile + url + "  ";
                if(GlobleVariableClass.FileUriList.size() == stringToList(urilistfile).size()){
                    mDatabase.child("S").child(GlobleVariableClass.username).child(GlobleVariableClass.ClaimNo).child("BillFileList").setValue(urilistfile);
                    final ProgressBar simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
                    simpleProgressBar.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(UploadFileActivity.this, FileUploadedActivity.class));
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }

    private void selectImage(Context context){
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
        if (requestCode == PICK_FILE_REQUEST && data!=null) {
            if (resultCode == RESULT_OK) {
                selectedFiles= data.getParcelableArrayListExtra(Constants.SELECTED_ITEMS);
            }
            Toast.makeText(UploadFileActivity.this, "Selected   "+String.valueOf(selectedFiles.size())+" Files", Toast.LENGTH_SHORT).show();
            TextView textView = findViewById(R.id.Nofiles);
            textView.setText(" You have Selected "+String.valueOf(selectedFiles.size())+" No Of Files");
        }
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
                    break;
            }
        }
        ListView listView=(ListView)findViewById(R.id.androidlist);
        SlidingImage_Adapter customCountryList = new SlidingImage_Adapter(this,bmp_images);
        listView.setAdapter(customCountryList);

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

    private void uploadImage(Bitmap converetdImage, final int i){

        if(converetdImage != null)
        {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            converetdImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            converetdImage.recycle();
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
                                        Toast.makeText(UploadFileActivity.this, "Uploaded Image "+String.valueOf(i+1), Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(UploadFileActivity.this, "Failed "+xe.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void Geturl(String name){
        storageReference.child("uploaded/"+name+".jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url= uri.toString();
                urilist= urilist + url + "  ";
                if(GlobleVariableClass.byteArrayPicture.size() == stringToList(urilist).size()){
                    mDatabase.child("S").child(GlobleVariableClass.username).child(GlobleVariableClass.ClaimNo).child("BillImageList").setValue(urilist);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }


}
