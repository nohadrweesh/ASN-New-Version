package com.example.a.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class DriverMapActivity extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLocation;
    LocationRequest mLocationRequest;
    Marker marker;
    List<Marker> neighboursMarkers;

    BitmapDescriptor myCarDescriptor;
    BitmapDescriptor otherCarsDescriptors;
    BitmapDescriptor pbCarsDescriptors;


    LocationUtils mLocationUtils;
    NeighborsUtils mNeighborsUtils;

    HelpUtils mHelpUtils;

    private static final String TAG = "DriverMapActivity";
    double pbLat=-1,pbLong=-1;
    String pbName;

    int currentDriverID;
    ToggleButton trafficToggle;
    ImageView ivLegend;
    MapView mMapView;

    Button btnShowOffers,btnHelp,btnUrgentHelp,btnObd,btnDisplayConnections,btnTrackers,btnHome;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_driver_map,container,false);
btnHome=view.findViewById(R.id.btnHome);
        btnShowOffers=view.findViewById(R.id.btnShowOffers);
        btnHelp=view.findViewById(R.id.btnHelp);
        btnUrgentHelp=view.findViewById(R.id.btnUrgentHelp);
        btnObd=view.findViewById(R.id.btnObd);
        btnDisplayConnections=view.findViewById(R.id.btnDisplayConnections);
        btnTrackers=view.findViewById(R.id.btnTrackers);btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome();
            }
        });

        btnShowOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOffers();
            }
        });
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                help();
            }
        });
        btnUrgentHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urgentHelp();
            }
        });
        btnObd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOBDPanel();
            }
        });

        btnDisplayConnections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayConnectionRequests();
            }
        });
        btnTrackers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToTrackersActivity();
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);


        mLocationUtils=LocationUtils.getInstance(getContext());
        neighboursMarkers=new ArrayList<Marker>();

        mNeighborsUtils=NeighborsUtils.getInstance(getContext());
        neighboursMarkers=new ArrayList<Marker>();

        myCarDescriptor=bitmapDescriptorFromVector(getContext(), R.drawable.my_car);
        otherCarsDescriptors=bitmapDescriptorFromVector(getContext(), R.drawable.other_car);
        pbCarsDescriptors=bitmapDescriptorFromVector(getContext(), R.drawable.pb_car);

        Bundle info=getArguments();
        if(info !=null) {
            pbLat = info.getDouble("lat", -1);
            pbLong = info.getDouble("long", -1);
            pbName = info.getString("name");
            currentDriverID = info.getInt("driverID", 0);
        }
            /*Intent i= getIntent();
            if(i!=null && i.getExtras()!=null){
                //Toast.makeText(this,"i received with data ",Toast.LENGTH_LONG).show();
                pbLat=i.getDoubleExtra("lat",-1);
                pbLong=i.getDoubleExtra("long",-1);
                if(i.hasExtra("name"))
                    pbName=i.getStringExtra("name");
                if(i.hasExtra("driverID"))
                    currentDriverID = i.getIntExtra("driverID",0);
            }*/


        trafficToggle=view.findViewById(R.id.trafficToggle);
        ivLegend=view.findViewById(R.id.ivLegend);
        trafficToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d(TAG, "onCheckedChanged: on");
                    mMap.setTrafficEnabled(false);
                    ivLegend.setVisibility(View.INVISIBLE);
                } else {
                    Log.d(TAG, "onCheckedChanged: off");
                    mMap.setTrafficEnabled(true);
                    ivLegend.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
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
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            Toast.makeText(getContext(),"Accept permssions ",Toast.LENGTH_SHORT).show();
            return;
        }
        mMap.setTrafficEnabled(true);
        Log.d(TAG, "onMapReady: starts ");
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        if(pbLong!=-1&&pbLat!=-1){
            //Toast.makeText(getApplicationContext(),"pbLat !=-1 ",Toast.LENGTH_SHORT).show();
            LatLng pbPerson = new LatLng(pbLat,pbLong);
            mMap.addMarker(new MarkerOptions().position(pbPerson).title(pbName).snippet("I have a problemb")
                    .icon(pbCarsDescriptors)
                    .anchor(0.5f,0.5f));

        }else{
            //Toast.makeText(getApplicationContext(),"pbLat ===-1 ",Toast.LENGTH_SHORT).show();
        }
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient=new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    @Override
    public void onLocationChanged(Location location) {
        // Log.d(TAG, "onLocationChanged: starts with "+location.toString());
        mMap.clear();


        mLocation=location;
        mLocation.setLatitude(round(location.getLatitude(),3));
        mLocation.setLongitude(round(location.getLongitude(),3));

        LatLng latLng=new LatLng(round(location.getLatitude(),3),round(location.getLongitude(),3));
        //LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
        Log.d(TAG, "onLocationChanged: starts with  latLang "+latLng.toString());




        mMap.addMarker(new MarkerOptions().position(latLng).title("My Car ^__^").snippet("I'm Noha ,a software engineer from Egypt")
                .icon(myCarDescriptor)
                .anchor(0.5f,0.5f));
        if(pbLong!=-1&&pbLat!=-1){
            //Toast.makeText(getApplicationContext(),"pbLat !=-1 ",Toast.LENGTH_SHORT).show();
            LatLng pbPerson = new LatLng(pbLat,pbLong);
            mMap.addMarker(new MarkerOptions().position(pbPerson).title(pbName).snippet("I have a problem")
                    .icon(pbCarsDescriptors)
                    .anchor(0.5f,0.5f));

        }else{
            //Toast.makeText(getApplicationContext(),"pbLat ===-1 ",Toast.LENGTH_SHORT).show();
        }

        /*if(marker==null) {
            marker = mMap.addMarker(new MarkerOptions().position(latLng).title("My Car ^__^").snippet("I'm Noha ,a software engineer from Egypt")
                    .icon(myCarDescriptor)
                    .anchor(0.5f,0.5f));
        }else{
            marker.setPosition(latLng);
        }*/

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));



        mLocationUtils.setLocation(mLocation);
        mNeighborsUtils.getNeighbours(mLocation);
        neighboursMarkers.clear();
        int markersSize=neighboursMarkers.size();
        for(int i=0;i<mNeighborsUtils.locations.size();i++){

            LatLng latLng1=new LatLng(mNeighborsUtils.locations.get(i).getLatitude(),mNeighborsUtils.locations.get(i).getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(latLng1)
                    .title(mNeighborsUtils.Names.get(i))
                    .snippet("other cars with id " + String.valueOf(mNeighborsUtils.IDs.get(i)))
                    .icon(otherCarsDescriptors));
            /*if(i<markersSize){
                if(neighboursMarkers.get(i)==null) {
                    neighboursMarkers.add(mMap.addMarker(new MarkerOptions()
                            .position(latLng1)
                            .title(mNeighborsUtils.Names.get(i))
                            .snippet("other cars with id " + String.valueOf(mNeighborsUtils.IDs.get(i)))
                            .icon(otherCarsDescriptors)));
                }else{
                    neighboursMarkers.get(i).setPosition(latLng1);
                }
            }else{
                neighboursMarkers.add(mMap.addMarker(new MarkerOptions()
                        .position(latLng1)
                        .title(mNeighborsUtils.Names.get(i))
                        .snippet("other cars with id " + String.valueOf(mNeighborsUtils.IDs.get(i)))
                        .icon(otherCarsDescriptors)));
            }*/

        }


    }




    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: starts");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            Toast.makeText(getContext(),"Accept permssions ",Toast.LENGTH_SHORT).show();
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: starts");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.d(TAG, "onConnectionFailed: starts");
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId){
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() , vectorDrawable.getIntrinsicHeight() );
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    private  double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: ");
        getMenuInflater().inflate(R.menu.map_menu,menu);
        return true;
    }*/

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.map_logout) {
            Log.d(TAG, "onOptionsItemSelected: ");
            LogoutUtils mLogoutUtils = LogoutUtils.getInstance(this);
            mLogoutUtils.logout();
            startActivity(new Intent(this, MainActivity.class));

            return true;
        } else if (id == R.id.map_settings){

            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return true;
        }else if(id==R.id.map_OBD){
            startActivity(new Intent(this,ObdActivity.class));
            return true;

        }
        else if(id==R.id.map_profile){
            startActivity(new Intent(this,Profile2Activity.class));
            return true;

        }
        return super.onOptionsItemSelected(item);
    }*/

    public void help(){
        startActivity(new Intent(getContext(),HelpActivity.class));

    }

    public void urgentHelp(){
        mHelpUtils=HelpUtils.getInstance(getContext());
        mHelpUtils.help("URGENT","The driver has a serious problem which we have not configure yet" +
                ",please help him/her if you can...","not specified");
        Toast.makeText(getContext(),"We have sent your request ,Your neighbours will help you ASAP ,please don't panic and stop the car" +
                "if you can.If you coud provide us with more info that'll be great  ",Toast.LENGTH_LONG).show();



    }
    public  Location getLocation(){
        return  mLocation;
    }

    public void openOBDPanel() {
        startActivity(new Intent(getContext(),ObdActivity.class));
    }

    public void displayConnectionRequests()
    {
        Intent i= new Intent(getContext(),ConnectionRequestsActivity.class);
        Log.d("DriverMapActivity","displayConnectionRequests current user is "+ currentDriverID);
        Log.d("DriverMapActivity","displayConnectionRequests current user is "+ SharedPrefManager.getInstance(getContext()).getUserId() );
//        i.putExtra("currentDriver",currentDriver);
        i.putExtra("currentDriverID",SharedPrefManager.getInstance(getContext()).getUserId());
        startActivity(i);
    }

    public void goToTrackersActivity()
    {
        Intent i = new Intent(getContext(),TrackersListActivity.class);
        Log.d("DriverMapActivity","goToTrackersActivity current user id is "+ SharedPrefManager.getInstance(getContext()).getUserId());
        i.putExtra("currentUserID",SharedPrefManager.getInstance(getContext()).getUserId());
        startActivity(i);
    }
    public void showOffers() {

        startActivity(new Intent(getContext(),OffersActivity.class));
    }

    public void goHome(){
        startActivity(new Intent(getContext(),MainHomeActivity.class));

    }


}
