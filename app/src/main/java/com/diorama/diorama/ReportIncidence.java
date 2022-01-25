package com.diorama.diorama;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.amb.bo.IncidenceBo;
import com.amb.bo.PicDetailsBo;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.diorama.diorama.MainActivity.MY_PREFS_NAME;
import static com.diorama.diorama.Utility.dbName;
import static com.diorama.diorama.Utility.dbPassword;
import static com.diorama.diorama.Utility.dbUser;
import static com.diorama.diorama.Utility.hostName;
import static com.diorama.diorama.Utility.userId;
import static com.diorama.diorama.Utility.userName;


public class ReportIncidence extends Activity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, ActivityCompat.OnRequestPermissionsResultCallback, PermissionUtils.PermissionResultCallback {
    EditText DisplayLocation;
    ImageView picIcon,locIcon;
    RadioGroup group;
    RadioButton condition;
    Button submit,back;
    ListView incPic;
    private final static int PLAY_SERVICES_REQUEST = 1000;
    private final static int REQUEST_CHECK_SETTINGS = 2000;
    private Location mLastLocation;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    double latitude;
    double longitude;
    String subLocality;
    IncidenceBo bo=new IncidenceBo();
    // list of permissions
    ArrayList<String> permissions = new ArrayList<>();
    PermissionUtils permissionUtils;
    boolean isPermissionGranted;
    SQLiteDatabase sd = null;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int REQUEST_PERMISSION = 200;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    String inputDataPath = Environment.getExternalStorageDirectory() + "/Android/";
    private Uri fileUri;
    public static ArrayList<Bitmap> imageArray=new ArrayList<>();
    PicDetailsBo bo1=new PicDetailsBo();
    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    private GoogleMap mMap;

    @Override
    public void onMapReady(GoogleMap map) {//todo:map loc
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
     Marker    mCurrLocationMarker;
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
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (LocationListener) this);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_incidence);
        //incPic=(ImageView) findViewById(R.id);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        permissionUtils = new PermissionUtils(ReportIncidence.this);

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.CAMERA);
        permissionUtils.check_permission(permissions, "Need GPS permission for getting your location", 1);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }

        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }
        final SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        user = prefs.getString(userName, null);
        picIcon=(ImageView) findViewById(R.id.fab);
//        incPic=(ListView) findViewById(R.id.incPic);
        DisplayLocation=(EditText) findViewById(R.id.locationDiplay);
        locIcon=(ImageView) findViewById(R.id.locIcon);
        group=(RadioGroup) findViewById(R.id.condition);
        submit=(Button) findViewById(R.id.incSubmit);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //MapFragment maap=(MapFragment) findViewById(R.id.map);
        back=(Button) findViewById(R.id.incBack);
        initMap();
        LinearLayout imagelay=(LinearLayout) findViewById(R.id.imageLayout);
        ImageView picView1=(ImageView) findViewById(R.id.image_display1);
        ImageView picView2=(ImageView) findViewById(R.id.image_display2);
        ImageView picView3=(ImageView) findViewById(R.id.image_display3);
        ImageView picView4=(ImageView) findViewById(R.id.image_display4);
        ImageView picView5=(ImageView) findViewById(R.id.image_display5);
        ImageView picView6=(ImageView) findViewById(R.id.image_display6);

        if(imageArray.size()!=0) {
//            incPic.setImageBitmap(imageArray.get(0));
//            CustomListImage customAdapter = new CustomListImage(getApplicationContext(), imageArray);
//            incPic.setAdapter(customAdapter);
            if(imageArray.size()==1){
                picView1.setImageBitmap(imageArray.get(0));
            }

            if(imageArray.size()==2){
                picView2.setVisibility(View.VISIBLE);
                picView1.setImageBitmap(imageArray.get(0));
                picView2.setImageBitmap(imageArray.get(1));
            }

            if(imageArray.size()==3){
                picView2.setVisibility(View.VISIBLE);
                picView4.setVisibility(View.VISIBLE);
                imagelay.setVisibility(View.VISIBLE);
                picView1.setImageBitmap(imageArray.get(0));
                picView2.setImageBitmap(imageArray.get(1));
                picView4.setImageBitmap(imageArray.get(2));
            }
            if(imageArray.size()==4){
                imagelay.setVisibility(View.VISIBLE);
                picView2.setVisibility(View.VISIBLE);
                picView4.setVisibility(View.VISIBLE);
                picView5.setVisibility(View.VISIBLE);
                picView1.setImageBitmap(imageArray.get(0));
                picView2.setImageBitmap(imageArray.get(1));
                picView4.setImageBitmap(imageArray.get(2));
                picView5.setImageBitmap(imageArray.get(3));
            }

            if(imageArray.size()==5){
                imagelay.setVisibility(View.VISIBLE);
                picView2.setVisibility(View.VISIBLE);
                picView4.setVisibility(View.VISIBLE);
                picView5.setVisibility(View.VISIBLE);
                picView3.setVisibility(View.VISIBLE);
                picView1.setImageBitmap(imageArray.get(0));
                picView2.setImageBitmap(imageArray.get(1));
               picView4.setImageBitmap(imageArray.get(2));
                picView5.setImageBitmap(imageArray.get(3));
                picView3.setImageBitmap(imageArray.get(4));
            }

            if(imageArray.size()==6){
                imagelay.setVisibility(View.VISIBLE);
                picView2.setVisibility(View.VISIBLE);
                picView4.setVisibility(View.VISIBLE);
                picView5.setVisibility(View.VISIBLE);
                picView3.setVisibility(View.VISIBLE);
                picView6.setVisibility(View.VISIBLE);
                picView1.setImageBitmap(imageArray.get(0));
                picView2.setImageBitmap(imageArray.get(1));
                picView4.setImageBitmap(imageArray.get(2));
                picView5.setImageBitmap(imageArray.get(3));
                picView3.setImageBitmap(imageArray.get(4));
                picView6.setImageBitmap(imageArray.get(5));
            }

        }
//        permissionUtils = new PermissionUtils(ReportIncidence.this);
//
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.CHANGE_NETWORK_STATE);
        permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
        //permissions.add(Manifest.permission.GET_ACCOUNTS);
//        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//


        permissionUtils.check_permission(permissions, "Need GPS permission for getting your location", 1);
//        sd = this.openOrCreateDatabase("MYDB", MODE_PRIVATE, null);
//        sd.execSQL("create table if not exists details (id INTEGER PRIMARY KEY AUTOINCREMENT, userName text, address text,level text,lat text,log text,currdate text)");
//        sd.execSQL("create table if not exists imagedetails (id INTEGER,photo blob);");

      SimpleDateFormat sdf = new SimpleDateFormat("dd+MM+yyyy_HH:mm:ss");

        final String currentDateandTime = sdf.format(new Date());
        Toast.makeText(getApplicationContext(),currentDateandTime,Toast.LENGTH_SHORT).show();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = group.getCheckedRadioButtonId();
//                ArrayList<String>  list=new ArrayList<>();
//                list.add("abc");
//                JSONObject json = new JSONObject();
//                try {
//                    json.put("uniqueArrays", new JSONArray(list));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                String arrayList = json.toString();
//                 for (int i=0;i<imageArray.size();i++) {
                     // find the radiobutton by returned id
                     condition = (RadioButton) findViewById(selectedId);
                     DisplayLocation.getText().toString();
//                     ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                     imageArray.get(i).compress(Bitmap.CompressFormat.PNG, 100, stream);
//                     byteArray = stream.toByteArray();
//                    String id= insertUser(DisplayLocation.getText().toString(), condition.getText().toString(), "" + latitude, "" + longitude, currentDateandTime);

                bo.setCriticallevel(condition.getText().toString());
                bo.setCurrentlocation(DisplayLocation.getText().toString());
                bo.setLatitude(latitude+"");
                bo.setLongitude(longitude+"");
                bo.setUserid(prefs.getString(userId,""));



                ArrayList<byte[]> list=new ArrayList<>();
                for (int i=0;i<imageArray.size();i++) {

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                     imageArray.get(i).compress(Bitmap.CompressFormat.PNG, 100, stream);
                     byteArray = stream.toByteArray();
//                    insertImageData(id,byteArray );
                    list.add(byteArray);
                }
                bo1.setPic(list);
                bo1.setUserid(prefs.getString(userId,""));
                new AsyncCaller().execute();
//                 DbOper().insertIncidenceDetails(bo);
//                new DbOper().insertPicDetails(bo1);
//                SocketProg prog=new SocketProg();
//                prog.sendIncidenceDetails(bo);
//                prog.sendPicDetails(bo1);
//
//
// }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),UserHome.class);
                startActivity(intent);
                finish();
            }
        });

        locIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
                if (mLastLocation != null) {
                    latitude = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();
                    getAddress();
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
                    mMap.addMarker(new MarkerOptions().position(latLng).title(subLocality));
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    //move map camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
                    //stop location updates
                    if (mGoogleApiClient != null) {
                        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,  ReportIncidence.this);
                    }
                } else {
                    showToast("Couldn't get the location. Make sure location is enabled on the device");
                }
            }
        });

        picIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
//                    Toast.makeText(getApplicationContext(),"Click",Toast.LENGTH_LONG).show();
//                    captureImage();
//                    if (ContextCompat.checkSelfPermission(ReportIncidence.this, Manifest.permission.CAMERA)
//                            == PackageManager.PERMISSION_DENIED) {
//                       return;
//                    }else {
                        Intent intent = new Intent(getApplicationContext(), CaptureImage.class);
                        startActivity(intent);
                        finish();
//                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }


        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }
    }

//    public String insertUser(String location,String Condition,String Lat,String Lon,String currDateTime){
////        SQLiteDatabase db               =   this.getWritableDatabase();
////
////        String delSql                       =   "DELETE FROM ACCOUNTS";
////        SQLiteStatement delStmt         =   db.compileStatement(delSql);
////        delStmt.execute();
//        sd = this.openOrCreateDatabase("MYDB", MODE_PRIVATE, null);
//        String sql                      =   "INSERT INTO details (userName, address,level,lat,log,currdate) VALUES(?,?,?,?,?,?)";
//        SQLiteStatement insertStmt      =   sd.compileStatement(sql);
//        insertStmt.clearBindings();
//        insertStmt.bindString(1,user);
//        insertStmt.bindString(2, location);
//        insertStmt.bindString(3,Condition);
////        insertStmt.bindString(4, imgData);
//        insertStmt.bindString(4,Lat);
//        insertStmt.bindString(5,Lon);
////        insertStmt.bindString(7,size+"");
//        insertStmt.bindString(6,currDateTime);
//        insertStmt.executeInsert();
//        Cursor c= sd.rawQuery("Select id from details where currdate=?",new String[]{currDateTime});
//        String id = null;
//        if (c != null) {
//            if (c.moveToFirst()) {
//                do {
//                     id = c.getString(0);
//                } while (c.moveToNext());
//            }
//        }
//
//        sd.close();
//        return id;
////        Intent intent=new Intent(getApplicationContext(),UserHome.class);
////        startActivity(intent);
////        finish();
//    }
    public void insertImageData(String id,byte[] data){
        sd = this.openOrCreateDatabase("MYDB", MODE_PRIVATE, null);
        String sql                      =   "INSERT INTO imagedetails (id, photo) VALUES(?,?)";
        SQLiteStatement insertStmt      =   sd.compileStatement(sql);
        insertStmt.clearBindings();
        insertStmt.bindString(1,id);
        insertStmt.bindBlob(2, data);
        insertStmt.executeInsert();
        Intent intent=new Intent(getApplicationContext(),UserHome.class);
        startActivity(intent);
        finish();

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
        else{
            Toast.makeText(this, "Location Permission not granted", Toast.LENGTH_SHORT).show();
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
                DisplayLocation.setText(currentLocation);

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
                            status.startResolutionForResult(ReportIncidence.this, REQUEST_CHECK_SETTINGS);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        getLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        break;
                    default:
                        break;
                }
                break;
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        previewCapturedImage();

                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(getApplicationContext(),
                                "User cancelled image capture", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(),
                                "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                                .show();
                        break;
                }
                break;
        }
    }

//        // if the result is capturing Image
//        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                // successfully captured the image
//                // display it in image view
//                previewCapturedImage();
//            } else if (resultCode == RESULT_CANCELED) {
//                // user cancelled Image capture
//                Toast.makeText(getApplicationContext(),
//                        "User cancelled image capture", Toast.LENGTH_SHORT)
//                        .show();
//            } else {
//                // failed to capture image
//                Toast.makeText(getApplicationContext(),
//                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
//                        .show();
//            }
//        }
//    }

    public byte[] byteArray;
    private void previewCapturedImage() {
        try {
            incPic.setVisibility(View.VISIBLE);
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();
            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

//            incPic.setImageBitmap(bitmap);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
             byteArray = stream.toByteArray();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
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


   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // redirects to utils
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }*/


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

    /**
     * Checking device has camera hardware or not
     * */
    private boolean isDeviceSupportCamera() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        boolean res = getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);

        if (result == PackageManager.PERMISSION_GRANTED||res){
            return true;
        } else {
            return false;
        }

//        if (getApplicationContext().getPackageManager().hasSystemFeature(
//                PackageManager.FEATURE_CAMERA)) {
//            // this device has a camera
//            return true;
//        } else {
//            // no camera on this device
//            return false;
//        }
    }
    /**
     * Capturing Camera Image will lauch camera app requrest image capture
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    public static  String DirPath;
    public static String filepath;
    private static File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);
        DirPath=mediaStorageDir.getPath();
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }
        filepath=mediaFile.getAbsolutePath();
        return mediaFile;
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    private class AsyncCaller extends AsyncTask<Void, Void, Integer>
    {
        ProgressDialog pdLoading = new ProgressDialog(ReportIncidence.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //String re="";
            //this method will be running on UI thread
            pdLoading.setMessage("\tReporting, be patient...");
            pdLoading.show();
            //re="longi= "+bo.getLongitude()+" latitude= "+bo.getLatitude();
            //Toast.makeText(ReportIncidence.this, re, Toast.LENGTH_SHORT).show();
        }
        @Override
        protected Integer doInBackground(Void... params) {
            Connection con=null;
            String re="";
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con= DriverManager.getConnection("jdbc:mysql://"+hostName+"/"+dbName+"",dbUser,dbPassword);
                PreparedStatement ps = con.prepareStatement("insert into incidence (currentlocation,criticallevel,userid,latitude,longitude) values (?,?,?,?,?)");
                ps.setString(1, bo.getCurrentlocation());
                ps.setString(2, bo.getCriticallevel());
                ps.setString(3, bo.getUserid());
                ps.setString(4, bo.getLatitude());
                ps.setString(5, bo.getLongitude());
//            ps.setString(4, bo.getIncstatus());
                re="longi= "+bo.getLongitude()+" latitude= "+bo.getLatitude();
                 ps.executeUpdate();
                Log.e("info",re);
                PreparedStatement ps1=con.prepareStatement("Select max(id) from incidence");
                ResultSet rs = ps1.executeQuery();
                int id=0;
                while (rs.next()) {
                    id = rs.getInt(1);
                }
                PreparedStatement pss = con.prepareStatement("insert into detailspic (pic,inc_id,userid) values (?,?,?)");
                for (byte[] col : bo1.getPic()) {
                    pss.setBytes(1, col);
                    pss.setInt(2, id);
                    pss.setString(3, bo.getUserid());
                    pss.executeUpdate();
                }
                return 1;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            } finally {
                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
//            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            if(result!=0) {
                imageArray=new ArrayList<>();
                Intent intent = new Intent(getApplicationContext(), UserHome.class);
                startActivity(intent);
                finish();
                Toast.makeText(getApplicationContext(),"Request Send Successfully...!!!",Toast.LENGTH_LONG).show();
            }
            //this method will be running on UI thread

            pdLoading.dismiss();
        }

    }
}
