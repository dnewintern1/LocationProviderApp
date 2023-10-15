package com.base.androidlocationapp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationRequest;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.base.androidlocationapp.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private static final int FINE_LOCATION_CODE=1000;


    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        prepareLocationServices();
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

        // Add a marker in Sydney and move the camera
        showMeTheCurrentUserLocation();
    }

    private void  giveMePermissionToAccesslocation(){

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == FINE_LOCATION_CODE){

            if(grantResults.length==1 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                showMeTheCurrentUserLocation();

            }else{
                Toast.makeText(this,"the user denied to give us access the location",Toast.LENGTH_SHORT).show();

            }

        }
    }
    private void showMeTheCurrentUserLocation(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            giveMePermissionToAccesslocation();


//        } else {
//            mMap.clear();
//            if (locationRequest == null) {
//                 locationRequest = LocationRequest.create();
//
//                if (locationRequest != null) {
//                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//                    locationRequest.setInterval(5000);
//                    locationRequest.setFastestInterval(1000);
//
//                    LocationCallback locationCallback = new LocationCallback() {
//                        @Override
//                        public void onLocationResult(@NonNull LocationResult locationResult) {
//                            showMeTheCurrentUserLocation();
//                        }
//                    };
//
//                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
//                }
//            }
//        }

            mMap.setMyLocationEnabled(true);

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    Location location = task.getResult();
                    if(location != null){
                        LatLng latLng= new LatLng(location.getLatitude(), location.getLongitude());

                       // mMap.addMarker(new MarkerOptions().position(latLng).title("you current location is here"));

                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f);

                                mMap.moveCamera(cameraUpdate);
                    }else {
                        Toast.makeText(MapsActivity.this,"something went wrong try again", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    private  void prepareLocationServices(){

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }
}