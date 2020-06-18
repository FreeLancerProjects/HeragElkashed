package com.creative.share.apps.heragelkashed.activities_fragments.activity_map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.databinding.ActivityMapBinding;
import com.creative.share.apps.heragelkashed.interfaces.Listeners;
import com.creative.share.apps.heragelkashed.language.LanguageHelper;
import com.creative.share.apps.heragelkashed.models.AdLocation;
import com.creative.share.apps.heragelkashed.models.AdModel;
import com.creative.share.apps.heragelkashed.models.ClusterRender;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class MapActivity extends AppCompatActivity implements Listeners.BackListener, OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks,LocationListener {
    private ActivityMapBinding binding;
    private List<AdModel> adModelList;
    private String lang;
    private GoogleMap mMap;
    private final String fineLocation = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int req_loc = 100,gps_req =101;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private String ad_type="";
    private Location myLocation;
    private List<AdLocation> adLocationList;
    private ClusterManager clusterManager;
    private ClusterRender clusterRender;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map);
        getDataFromIntent();

        initView();
    }

    private void getDataFromIntent() {
        adLocationList = new ArrayList<>();
        Intent intent = getIntent();
        if (intent!=null&&intent.hasExtra("adData")&&intent.hasExtra("ad_type"))
        {
            ad_type = intent.getStringExtra("ad_type");
            adModelList = (List<AdModel>) intent.getSerializableExtra("adData");

            for (AdModel adModel :adModelList)
            {
                AdLocation adLocation = new AdLocation(adModel.getPrice()+getString(R.string.sar),adModel.getAddress(),new LatLng(adModel.getLatitude(),adModel.getLongitude()));
                adLocationList.add(adLocation);
            }



        }
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);
        if (adModelList.size()>0)
        {
            binding.setTitle(adModelList.get(0).getCat_title()+" ( "+ad_type+" )");

        }

        setUpFragment();
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.white), PorterDuff.Mode.SRC_IN);

        binding.llMapSatellite.setOnClickListener(view -> {

            if (mMap!=null&&mMap.getMapType()!=GoogleMap.MAP_TYPE_SATELLITE)
            {

                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                binding.satelliteIcon.setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary));
                binding.mapIcon.setColorFilter(ContextCompat.getColor(this,R.color.black));

                String total = getString(R.string.result)+" "+getNearestAds(mMap.getCameraPosition().target.latitude,mMap.getCameraPosition().target.latitude,5000).size()+" "+getString(R.string.from)+" "+this.adModelList.size();
                binding.tvTotal.setText(total);
                mMap.clear();
                clusterManager.clearItems();
                clusterRender = new ClusterRender(this,mMap,clusterManager);
                mMap.setOnCameraIdleListener(clusterManager);
                mMap.setOnMarkerClickListener(clusterManager);
                mMap.setOnInfoWindowClickListener(clusterManager);
                clusterManager.addItems(adLocationList);
                clusterManager.setRenderer(clusterRender);
                clusterManager.cluster();
            }



        });

        binding.llMapNormal.setOnClickListener(view -> {
            if (mMap!=null&&mMap.getMapType()!=GoogleMap.MAP_TYPE_NORMAL)
            {

                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                binding.satelliteIcon.setColorFilter(ContextCompat.getColor(this,R.color.black));
                binding.mapIcon.setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.406045814299713,44.63574629276991),3.8f));

                String total = getString(R.string.result)+" "+getNearestAds(mMap.getCameraPosition().target.latitude,mMap.getCameraPosition().target.latitude,5000).size()+" "+getString(R.string.from)+" "+this.adModelList.size();
                binding.tvTotal.setText(total);

                mMap.clear();
                clusterManager.clearItems();
                clusterRender = new ClusterRender(this,mMap,clusterManager);
                mMap.setOnCameraIdleListener(clusterManager);
                mMap.setOnMarkerClickListener(clusterManager);
                mMap.setOnInfoWindowClickListener(clusterManager);

                clusterManager.addItems(adLocationList);
                clusterManager.setRenderer(clusterRender);
                clusterManager.cluster();
            }
        });

        binding.llMapGps.setOnClickListener(view -> checkPermission());



    }

    private void setUpFragment()
    {
        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (fragment!=null)
        {
            fragment.getMapAsync(this);
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap)
    {

        if (googleMap!=null)
        {
            mMap = googleMap;
            mMap.setTrafficEnabled(false);
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            mMap.setBuildingsEnabled(true);
            mMap.setIndoorEnabled(false);
            mMap.setMinZoomPreference(3.8f);
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.maps));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.406045814299713,44.63574629276991),3.8f));





            String total = getString(R.string.result)+" "+getNearestAds(mMap.getCameraPosition().target.latitude,mMap.getCameraPosition().target.longitude,2000).size()+" "+getString(R.string.from)+" "+this.adModelList.size();
            binding.tvTotal.setText(total);




            clusterManager = new ClusterManager(this,mMap);
            mMap.setOnCameraIdleListener(clusterManager);
            mMap.setOnMarkerClickListener(clusterManager);
            mMap.setOnInfoWindowClickListener(clusterManager);

            clusterRender = new ClusterRender(this,mMap,clusterManager);
            clusterManager.addItems(adLocationList);
            clusterManager.setRenderer(clusterRender);
            clusterManager.cluster();



/*
            mMap.setOnCameraMoveListener(() -> {
                binding.progBar.setVisibility(View.VISIBLE);


                String total2 = getString(R.string.result)+" "+getNearestAds(mMap.getCameraPosition().target.latitude,mMap.getCameraPosition().target.longitude,2000).size()+" "+getString(R.string.from)+" "+this.adModelList.size();
                binding.tvTotal.setText(total2);

                clusterManager.addItems(getNearestAds(mMap.getCameraPosition().target.latitude,mMap.getCameraPosition().target.longitude,2000));
                clusterManager.setRenderer(clusterRender);
                clusterManager.cluster();

                binding.progBar.setVisibility(View.GONE);



            });
*/







        }
    }

    private void initGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }
    private void checkPermission()
    {
        if (ContextCompat.checkSelfPermission(this,fineLocation)== PackageManager.PERMISSION_GRANTED)
        {
            initGoogleApiClient();

        }else
            {
                ActivityCompat.requestPermissions(this,new String[]{fineLocation},req_loc);
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == req_loc&&grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            initGoogleApiClient();
        }else
            {
                Toast.makeText(this, "Access location permission denied", Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==gps_req&&resultCode==RESULT_OK)
        {
            startLocationUpdate();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        initLocationRequest();
    }

    private void initLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setFastestInterval(1000);
        locationRequest.setInterval(6000);
        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .setAlwaysShow(false)
                .addLocationRequest(locationRequest)
                .build();
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, request);
        result.setResultCallback(locationSettingsResult -> {
            Status status = locationSettingsResult.getStatus();
            if (status.getStatusCode() == LocationSettingsStatusCodes.SUCCESS)
            {
                startLocationUpdate();
            }else if (status.getStatusCode()==LocationSettingsStatusCodes.RESOLUTION_REQUIRED)
            {
                try {
                    status.startResolutionForResult(this,gps_req);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdate()
    {
        locationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onLocationChanged(locationResult.getLastLocation());
            }
        };

        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient!=null)
        {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        this.myLocation = location;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),4.8f));

        binding.progBar.setVisibility(View.VISIBLE);

        List<AdLocation> adLocationList = getNearestAds(mMap.getCameraPosition().target.latitude,mMap.getCameraPosition().target.longitude,500);
        String total2 = getString(R.string.result)+" "+adLocationList.size()+" "+getString(R.string.from)+" "+this.adModelList.size();
        binding.tvTotal.setText(total2);

        mMap.clear();
        clusterManager.clearItems();

        clusterManager = new ClusterManager(this,mMap);
        clusterRender = new ClusterRender(this,mMap,clusterManager);
        mMap.setOnCameraIdleListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);
        mMap.setOnInfoWindowClickListener(clusterManager);
        clusterManager.addItems(adLocationList);
        clusterManager.setRenderer(clusterRender);
        clusterManager.cluster();

        if (adLocationList.size()==0)
        {
            Toast.makeText(this, getString(R.string.no_adversiment_found), Toast.LENGTH_SHORT).show();
        }

        binding.progBar.setVisibility(View.GONE);

        stopUpdateLocation();
    }


    private List<AdLocation> getNearestAds(double lat,double lng,double distance)
    {

        List<AdLocation> adLocationList = new ArrayList<>();
        for (AdLocation adLocation :this.adLocationList)
        {

            double dis = getDistance(lat,lng,adLocation.getPosition().latitude,adLocation.getPosition().longitude)/1000.0;


            if (dis<=distance)
            {
                adLocationList.add(adLocation);

            }
        }

        return adLocationList;


    }

    private double getDistance(double lat1,double lng1,double lat2,double lng2)
    {
        return SphericalUtil.computeDistanceBetween(new LatLng(lat1,lng1),new LatLng(lat2,lng2));
    }
    @Override
    public void back() {
        finish();
    }


    private  void  stopUpdateLocation(){
        if (googleApiClient!=null)
        {
            googleApiClient.disconnect();
        }

        if (locationCallback!=null)
        {
            LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopUpdateLocation();

    }
}