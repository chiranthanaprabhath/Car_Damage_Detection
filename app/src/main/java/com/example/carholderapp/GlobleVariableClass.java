package com.example.carholderapp;
import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GlobleVariableClass extends Application{
    //add this variable declaration:
    public static ArrayList<Bitmap> byteArrayPicture;
    public static ArrayList<Uri> FileUriList;
    public static boolean freeThemodel=false;
    public static String username="chira";
    public static String meterReading;
    public static String damageType;
    public static String Lat;
    public static String Log;

    public static String givenAddress;
    public static LatLng CustomLocation;
    public static LatLng CurrentLocation;
    public static String location;
    public static String ImageList;
    public static String Status="pending with assessor";
    public static String Damage="";
    public static String Estimation="";
    public static String ClaimNo;
    public static DamageDetails damageDetails;
    private static GlobleVariableClass singleton;


    public static GlobleVariableClass getInstance() {
        return singleton;
    }
    public static void setVariable(ArrayList<Bitmap> byteArrayPicture){
        singleton.byteArrayPicture = byteArrayPicture;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }
}