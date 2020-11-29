package com.example.paint;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.ArrayList;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;



@SuppressLint("MissingPermission")
public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Location mLastLocation;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private ArrayList<LatLng> points; //Creating an array List for the points to which the path will be drawn
    Polyline line; //The polyline in specific.

    Button button;

    public boolean isDrawingButtonPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        checkLocationPermission();

    }

    private void checkLocationPermission() {
        Log.d("permission", "checking");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode != 99){
            return;
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            points = new ArrayList<LatLng>();

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        mLastLocation = location;
                        LatLng realTime = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(realTime));
                    }
                }
            });

            requestLocationUpdates();

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            button = findViewById(R.id.drawing_button);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (!isDrawingButtonPressed){
                        isDrawingButtonPressed = true;
                        button.setText("Stop Drawing");
                    } else {
                        isDrawingButtonPressed = false;
                        button.setText("Start Drawing");
                    }
                }
            });
        }else{
            Intent switch_to_main = new Intent(getApplicationContext(), MainActivity.class);
            startActivityForResult(switch_to_main, 1);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setMyLocationEnabled(true);
    }

    private void redrawLine(){

        mMap.clear();  //clears all Markers and Polylines
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
        }
        line = mMap.addPolyline(options); //add Polyline
    }

    //@Override
    /*public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);

        points.add(latLng);

        redrawLine();

    }*/

    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
        request.setInterval(10000);
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();

                    if (location != null && differentLocation(location)) {
                        mLastLocation = location;
                        LatLng realTime = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(realTime));
                        if(isDrawingButtonPressed == true) {
                            points.add(realTime);
                            redrawLine();
                        }
                    }
                }
            }, null);
        }
    }

    private boolean differentLocation(Location location) {
        return location.getLatitude() != mLastLocation.getLatitude() || location.getLongitude() != mLastLocation.getLongitude();
    }
}