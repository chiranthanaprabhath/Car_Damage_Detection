package com.example.carholderapp;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class RoadSideLocation extends FragmentActivity implements GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private GoogleMap mMap;
        double latitude;
        double longitude;
    private int PROXIMITY_RADIUS = 10000;
        GoogleApiClient mGoogleApiClient;
        Location mLastLocation;
        Marker mCurrLocationMarker;
        LocationRequest mLocationRequest;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
        boolean markerClicked;
    private Button buttonGo;
    public LinearLayout remerk;
    public EditText address;
@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roadside_location_layout);
        ImageView backButton=findViewById(R.id.menu);
        backButton.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        finish();
        }
        });
        remerk = findViewById(R.id.remark);
        buttonGo = findViewById(R.id.confirm);
        buttonGo.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        LatLng markedlocatoin = new LatLng(latitude,longitude);
        GlobleVariableClass.CurrentLocation = markedlocatoin;
    AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(RoadSideLocation.this);
    myAlertDialog.setMessage("A support team will contact you soon");
    myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface arg0, int arg1) {
            Intent intent = new Intent(RoadSideLocation.this, HomeActivity.class);
            startActivity(intent);

        }});
    myAlertDialog.show();

        }
        });
        remerk .setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        LatLng markedlocatoin = new LatLng(latitude,longitude);
        GlobleVariableClass.Log=String.valueOf(longitude);
        GlobleVariableClass.Lat=String.valueOf(latitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(markedlocatoin));
        if (markerClicked){
        mMap.addMarker(new MarkerOptions()
        .position(markedlocatoin)
        .draggable(true)
        .title("Drag to the Location")
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        markerClicked = false;
        }
        }
        });
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        checkLocationPermission();
        }

        //Check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {
        Log.d("onCreate", "Finishing test case since Google Play Services are not available");
        finish();
        }
        else {
        Log.d("onCreate","Google Play Services available.");
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //configureCameraIdle();

        }

@Override
public void onMapLongClick(LatLng point) {


        }

@Override
public void onMarkerDrag(Marker marker) {

        }

@Override
public void onMarkerDragEnd(Marker marker) {
        GlobleVariableClass.CustomLocation=marker.getPosition();
        }

@Override
public void onMarkerDragStart(Marker marker) {


        }
private void configureCameraIdle() {
        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
@Override
public void onCameraIdle() {

        LatLng latLng = mMap.getCameraPosition().target;
        Geocoder geocoder = new Geocoder(RoadSideLocation.this);

        try {
        List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        LatLng markedlocatoin = new LatLng(latLng.latitude, latLng.longitude);
        mMap.addMarker(new MarkerOptions()
        .position(markedlocatoin)
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
        .draggable(true));
        if (addressList != null && addressList.size() > 0) {
        String locality = addressList.get(0).getAddressLine(0);
        String country = addressList.get(0).getCountryName();
        if (!locality.isEmpty() && !country.isEmpty())
        Log.d("ssssXXXXXXXXXX",locality + "  " + country);
        }

        } catch (IOException e) {
        e.printStackTrace();
        }

        }
        };
        }


private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
        if(googleAPI.isUserResolvableError(result)) {
        googleAPI.getErrorDialog(this, result,
        0).show();
        }
        return false;
        }
        return true;
        }

public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerDragListener(this);
        markerClicked =true;
        mMap.setOnCameraIdleListener(onCameraIdleListener);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (ContextCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
        }
        }
        else {
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
        }

        // Add a marker in Sydney and move the camera
        }

protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();
        mGoogleApiClient.connect();
        }

@Override
public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        }

private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyATuUiZUkEc_UgHuqsBJa1oqaODI-3mLs0");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
        }

@Override
public void onConnectionSuspended(int i) {

        }

@Override
public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
        mCurrLocationMarker.remove();
        }

        //Place current location marker
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        GlobleVariableClass.Lat=String.valueOf(latitude);
        GlobleVariableClass.Log=String.valueOf(longitude);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        Toast.makeText(RoadSideLocation.this,"Your Current Location", Toast.LENGTH_LONG).show();

        Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f",latitude,longitude));

        //stop location updates
        if (mGoogleApiClient != null) {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        Log.d("onLocationChanged", "Removing Location Updates");
        }
        Log.d("onLocationChanged", "Exit");

        }


@Override
public void onConnectionFailed(ConnectionResult connectionResult) {

        }

public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {

        // Asking user if explanation is needed
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
        Manifest.permission.ACCESS_FINE_LOCATION)) {

        // Show an explanation to the user *asynchronously* -- don't block
        // this thread waiting for the user's response! After the user
        // sees the explanation, try again to request the permission.

        //Prompt the user once explanation has been shown
        ActivityCompat.requestPermissions(this,
        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
        MY_PERMISSIONS_REQUEST_LOCATION);


        } else {
        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(this,
        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
        MY_PERMISSIONS_REQUEST_LOCATION);
        }
        return false;
        } else {
        return true;
        }
        }

@Override
public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        switch (requestCode) {
        case MY_PERMISSIONS_REQUEST_LOCATION: {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        // permission was granted. Do the
        // contacts-related task you need to do.
        if (ContextCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {

        if (mGoogleApiClient == null) {
        buildGoogleApiClient();
        }
        mMap.setMyLocationEnabled(true);
        }

        } else {

        // Permission denied, Disable the functionality that depends on this permission.
        Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
        }
        return;
        }

        // other 'case' lines to check for other permissions this app might request.
        // You can add here other case statements according to your requirement.
        }
        }
        }
