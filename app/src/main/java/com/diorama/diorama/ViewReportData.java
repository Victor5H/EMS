package com.diorama.diorama;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.diorama.diorama.MainActivity.MY_PREFS_NAME;



public class ViewReportData  extends AppCompatActivity implements OnMapReadyCallback ,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,ActivityCompat.OnRequestPermissionsResultCallback,PermissionUtils.PermissionResultCallback {


    private final static int PLAY_SERVICES_REQUEST = 1000;
    private final static int REQUEST_CHECK_SETTINGS = 2000;
    private Location mLastLocation;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    double latitude;
    double longitude;
    String subLocality;
    ArrayList<String> permissions = new ArrayList<>();
    PermissionUtils permissionUtils;
    boolean isPermissionGranted;
    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
    }
    private GoogleMap mMap;

    @Override
    public void onMapReady(GoogleMap map) {
        mMap=map;
        LatLng latLng;
        if(latitude==0.0||longitude==0.0){
            latLng = new LatLng(18.5074, 73.8077);
        }else {
            latLng = new LatLng(latitude, longitude);
        }
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mCurrLocationMarker = map.addMarker(new MarkerOptions().position(latLng).title(subLocality));
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
    Marker mCurrLocationMarker;
    String user;
    @Override
    public void onLocationChanged(Location location)
    {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (LocationListener) this);
        }
    }

    Button viewResp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report_data);
        initMap();
        ImageView viewImg1=(ImageView) findViewById(R.id.viewImg1);
        ImageView viewImg2=(ImageView) findViewById(R.id.viewImg2);
        ImageView viewImg3=(ImageView) findViewById(R.id.viewImg3);
        ImageView viewImg4=(ImageView) findViewById(R.id.viewImg4);
        ImageView viewImg5=(ImageView) findViewById(R.id.viewImg5);
        ImageView viewImg6=(ImageView) findViewById(R.id.viewImg6);
        TextView viewUser = (TextView) findViewById(R.id.viewUser);
        TextView viewMobile = (TextView) findViewById(R.id.viewMobile);
        final SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final String id = prefs.getString("id", "");
        String User = prefs.getString("viewUser", "");
        String condition = prefs.getString("condition", "");
        String s = condition.replaceAll("\n", " ");
        Button accept=(Button) findViewById(R.id.BTNAccepts);
        Button decline=(Button) findViewById(R.id.BTNDecline);
        final LinearLayout report=(LinearLayout) findViewById(R.id.layDataReport);
        final LinearLayout mapData=(LinearLayout) findViewById(R.id.mapData);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapData.setVisibility(View.VISIBLE);
                report.setVisibility(View.GONE);
                ArrayList<Double> latLon = new DbQuery(getApplicationContext()).getLatLon(id);

                if (latLon.size()!=0){
                latitude=latLon.get(0);
                longitude= latLon.get(1);
                }

                //getLocation();
//                if (mLastLocation != null) {
//                    latitude = mLastLocation.getLatitude();
//                    longitude = mLastLocation.getLongitude();
                  //  getAddress();
                    if (mCurrLocationMarker != null) {
                        mCurrLocationMarker.remove();
                    }
                    //Place current location marker
                    LatLng latLng = new LatLng(latitude, longitude);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(subLocality);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                    mCurrLocationMarker = mMap.addMarker(markerOptions);
//                    mMap.addMarker(new MarkerOptions().position(latLng).title(subLocality));
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    //move map camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                    //stop location updates
                    if (mGoogleApiClient != null) {
                        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,  ViewReportData.this);
                    }
//                } else {
//                   // showToast("Couldn't get the location. Make sure location is enabled on the device");
//                }


            }
        });

        viewUser.setText("User Name : "+User);
        viewMobile.setText("Condition : "+s);

        DbQuery query=new DbQuery(getApplicationContext());
        ArrayList<byte[]> imageData = query.getImageData(id);
        if(imageData.size()==1){
            viewImg1.setVisibility(View.VISIBLE);
//            viewImg2.setVisibility(View.VISIBLE);
//            viewImg3.setVisibility(View.VISIBLE);
//            viewImg4.setVisibility(View.VISIBLE);
//            viewImg5.setVisibility(View.VISIBLE);
//            viewImg6.setVisibility(View.VISIBLE);
            viewImg1.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(0), 0, imageData.get(0).length));
//            viewImg2.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(1), 0, imageData.get(1).length));
//            viewImg3.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(2), 0, imageData.get(2).length));
//            viewImg4.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(3), 0, imageData.get(3).length));
//            viewImg5.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(4), 0, imageData.get(4).length));
//            viewImg6.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(5), 0, imageData.get(5).length));
        }
        if(imageData.size()==2){
            viewImg1.setVisibility(View.VISIBLE);
            viewImg2.setVisibility(View.VISIBLE);
//            viewImg3.setVisibility(View.VISIBLE);
//            viewImg4.setVisibility(View.VISIBLE);
//            viewImg5.setVisibility(View.VISIBLE);
//            viewImg6.setVisibility(View.VISIBLE);
            viewImg1.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(0), 0, imageData.get(0).length));
            viewImg2.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(1), 0, imageData.get(1).length));
//            viewImg3.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(2), 0, imageData.get(2).length));
//            viewImg4.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(3), 0, imageData.get(3).length));
//            viewImg5.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(4), 0, imageData.get(4).length));
//            viewImg6.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(5), 0, imageData.get(5).length));
        }
        if(imageData.size()==3){
            viewImg1.setVisibility(View.VISIBLE);
            viewImg2.setVisibility(View.VISIBLE);
            viewImg3.setVisibility(View.VISIBLE);
//            viewImg4.setVisibility(View.VISIBLE);
//            viewImg5.setVisibility(View.VISIBLE);
//            viewImg6.setVisibility(View.VISIBLE);
            viewImg1.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(0), 0, imageData.get(0).length));
            viewImg2.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(1), 0, imageData.get(1).length));
            viewImg3.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(2), 0, imageData.get(2).length));
//            viewImg4.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(3), 0, imageData.get(3).length));
//            viewImg5.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(4), 0, imageData.get(4).length));
//            viewImg6.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(5), 0, imageData.get(5).length));
        }
        if(imageData.size()==4){
            viewImg1.setVisibility(View.VISIBLE);
            viewImg2.setVisibility(View.VISIBLE);
            viewImg3.setVisibility(View.VISIBLE);
            viewImg4.setVisibility(View.VISIBLE);
//            viewImg5.setVisibility(View.VISIBLE);
//            viewImg6.setVisibility(View.VISIBLE);
            viewImg1.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(0), 0, imageData.get(0).length));
            viewImg2.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(1), 0, imageData.get(1).length));
            viewImg3.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(2), 0, imageData.get(2).length));
            viewImg4.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(3), 0, imageData.get(3).length));
//            viewImg5.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(4), 0, imageData.get(4).length));
//            viewImg6.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(5), 0, imageData.get(5).length));
        }
        if(imageData.size()==5){
            viewImg1.setVisibility(View.VISIBLE);
            viewImg2.setVisibility(View.VISIBLE);
            viewImg3.setVisibility(View.VISIBLE);
            viewImg4.setVisibility(View.VISIBLE);
            viewImg5.setVisibility(View.VISIBLE);
//            viewImg6.setVisibility(View.VISIBLE);
            viewImg1.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(0), 0, imageData.get(0).length));
            viewImg2.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(1), 0, imageData.get(1).length));
            viewImg3.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(2), 0, imageData.get(2).length));
            viewImg4.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(3), 0, imageData.get(3).length));
            viewImg5.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(4), 0, imageData.get(4).length));
//            viewImg6.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(5), 0, imageData.get(5).length));
        }
        if(imageData.size()==6){
            viewImg1.setVisibility(View.VISIBLE);
            viewImg2.setVisibility(View.VISIBLE);
            viewImg3.setVisibility(View.VISIBLE);
            viewImg4.setVisibility(View.VISIBLE);
            viewImg5.setVisibility(View.VISIBLE);
            viewImg6.setVisibility(View.VISIBLE);
           viewImg1.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(0), 0, imageData.get(0).length));
            viewImg2.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(1), 0, imageData.get(1).length));
            viewImg3.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(2), 0, imageData.get(2).length));
            viewImg4.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(3), 0, imageData.get(3).length));
            viewImg5.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(4), 0, imageData.get(4).length));
            viewImg6.setImageBitmap(BitmapFactory.decodeByteArray(imageData.get(5), 0, imageData.get(5).length));
        }

        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }
//        viewResp=(Button) findViewById(R.id.respView);
//        viewResp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
//                startActivity(intent);
//            }
//        });
    }



    private void getLocation() {

        if (isPermissionGranted) {

            try {
                mLastLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient);

            } catch (SecurityException e) {
                e.printStackTrace();
            }

        }

    }

    public Address getAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            return addresses.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public void getAddress() {

        Address locationAddress = getAddress(latitude, longitude);

        if (locationAddress != null) {
            String address = locationAddress.getAddressLine(0);
            String address1 = locationAddress.getAddressLine(1);
            String city = locationAddress.getLocality();
            String state = locationAddress.getAdminArea();
            String country = locationAddress.getCountryName();
            String postalCode = locationAddress.getPostalCode();
            String subAdminArea = locationAddress.getSubAdminArea();
            subLocality = locationAddress.getSubLocality();
            String currentLocation;

            if (!TextUtils.isEmpty(address)) {
                currentLocation = address;

                if (!TextUtils.isEmpty(address1))
                    currentLocation += "\n" + address1;

                if (!TextUtils.isEmpty(city)) {
                    currentLocation += "\n" + city;

                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation += " - " + postalCode;
                } else {
                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation += "\n" + postalCode;
                }

                if (!TextUtils.isEmpty(state))
                    currentLocation += "\n" + state;

                if (!TextUtils.isEmpty(country))
                    currentLocation += "\n" + country;
               // DisplayLocation.setText(currentLocation);

            }

        }

    }

    /**
     * Creating google api client object
     */

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {

                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location requests here
                        getLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(ViewReportData.this, REQUEST_CHECK_SETTINGS);

                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });


    }

    /**
     * Method to verify google play services on the device
     */

    private boolean checkPlayServices() {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this, resultCode,
                        PLAY_SERVICES_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("info", "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        getLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }


    // Permission check functions


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // redirects to utils
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


    @Override
    public void PermissionGranted(int request_code) {
        Log.i("PERMISSION", "GRANTED");
        isPermissionGranted = true;
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
        Log.i("PERMISSION PARTIALLY", "GRANTED");
    }

    @Override
    public void PermissionDenied(int request_code) {
        Log.i("PERMISSION", "DENIED");
    }

    @Override
    public void NeverAskAgain(int request_code) {
        Log.i("PERMISSION", "NEVER ASK AGAIN");
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}
