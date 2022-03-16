package com.example.leidosrollvan.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.leidosrollvan.R;
import com.example.leidosrollvan.dataClasses.Business;
import com.example.leidosrollvan.dataClasses.BusinessImage;
import com.example.leidosrollvan.dataClasses.BusinessLocation;
import com.example.leidosrollvan.dataClasses.PolylineData;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.squareup.picasso.Picasso;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements View.OnClickListener,
        OnMapReadyCallback,
        GoogleMap.OnPolylineClickListener {
    private SupportMapFragment supportMapFragment;
    private ImageButton mapHomeButton, reloadButton;
    private AutocompleteSupportFragment autocompleteSupportFragment;
    private FusedLocationProviderClient client;
    private Geocoder geocoder;
    private GoogleMap mMap;
    private HashMap<String, String> mLocationInfo;
    private ArrayList<Marker> searchMarker;
    private ArrayList<Marker> clickedMarker;
    private ArrayList<PolylineData> mPolylinesData;
    private Marker mSelectedMarker = null;
    private Marker mDirectionMarker = null;
    private ArrayList<Marker> tripMarkers;
    private DatabaseReference locationRef;
    private DatabaseReference businessRef;
    private DatabaseReference businessImgRef;
    private String apiKey;
    private GeoApiContext mGeoApiContext = null;
    private LatLng userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapHomeButton = (ImageButton) findViewById(R.id.map_homeButton);
        mapHomeButton.setOnClickListener(this);

        reloadButton = (ImageButton) findViewById(R.id.map_reloadButton);
        reloadButton.setOnClickListener(this);

        // Initialize Map fragment
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);

        if(mGeoApiContext == null){
            mGeoApiContext = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.map_key))
                    .build();
        }

        // initialize Places client
        apiKey = getString(R.string.map_key);
        Places.initialize(this, apiKey);
        PlacesClient placesClient = Places.createClient(this);

        // Initialize AutoComplete search bar and set autocomplete parameters
        autocompleteSupportFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteSupportFragment.setTypeFilter(TypeFilter.ADDRESS);
        autocompleteSupportFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(55.836229, -4.252612),
                new LatLng(55.897463, -4.325364)));
        autocompleteSupportFragment.setCountries("UK");
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // initialize search marker list
        searchMarker = new ArrayList<Marker>();

        // initialize clicked marker list
        clickedMarker = new ArrayList<Marker>();

        // initialzie polylines list
        mPolylinesData = new ArrayList<>();

        // initialize tripMarkers list
        tripMarkers = new ArrayList<>();

        // Initialize client to get user's last location on device
        client = LocationServices.getFusedLocationProviderClient(this);

        // Initialize geocoder to convert business postcodes to latlng coordinates
        mLocationInfo = new HashMap<String, String>();
        geocoder = new Geocoder(this);

        // initialize reference to business and image table
        businessRef = FirebaseDatabase.getInstance().getReference("Businesses");
        businessImgRef = FirebaseDatabase.getInstance().getReference("Business Images");

        // Get business locations
        locationRef = FirebaseDatabase.getInstance().getReference("Business Locations");
        locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot locationSnapshot : snapshot.getChildren()){
                        BusinessLocation location = locationSnapshot.getValue(BusinessLocation.class);
                        String bID = locationSnapshot.getKey();
                        mLocationInfo.put(bID, location.getPostCode());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Location error", "Error retrieving location: ", error.toException().getCause());
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnPolylineClickListener(this);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                ArrayList<String> tags = (ArrayList<String>) marker.getTag();
                resetSelectedMarker();
                mSelectedMarker = marker;
                // Log.d("MARKER", tags.toString());

                if(tags != null && tags.get(0).equals("Business")){
                    String businessID = (String) tags.get(1);
                    String distanceTo = (String) tags.get(2);
                    showBottomSheetDialog(marker, businessID, distanceTo);

                    if (!clickedMarker.isEmpty()) {
                        Marker prevMarker = clickedMarker.get(0);
                        prevMarker.setIcon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_food_truck_map_marker));
                        clickedMarker.remove(prevMarker);
                    }
                    clickedMarker.add(marker);
                    marker.setIcon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_baseline_clicked_restaurant_24));

                }else if(tags != null && tags.get(0).equals("Search")) {
                    showSearchBottomSheet(marker);
                }
                return false;
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                hideSearchBottomSheet();
                hideBottomSheetDialog();
            }
        });
        // render the map
        loadMap();
    }

    private void loadMap() {
        if(mMap != null){
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Task<Location> task = client.getLastLocation();

                task.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null){

                            // autocomplete place search
                            autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                                @Override
                                public void onError(@NonNull Status status) {
                                    Log.i("AutoComplete error", "error status: " + status);
                                }

                                @Override
                                public void onPlaceSelected(@NonNull Place place) {
                                    LatLng placeLatLng = place.getLatLng();

                                    MarkerOptions placeOptions = new MarkerOptions().position(placeLatLng)
                                            .title(place.getName())
                                            .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_baseline_my_location_24));

                                    if(!searchMarker.isEmpty()){
                                        Marker searchedMarker = searchMarker.get(0);
                                        searchMarker.remove(searchedMarker);
                                        searchedMarker.remove();
                                    }

                                    ArrayList<String> tags = new ArrayList<>();
                                    tags.add("Search");
                                    tags.add(placeLatLng.toString());

                                    removePolylines();
                                    resetSelectedMarker();
                                    if(mDirectionMarker != null){
                                        mDirectionMarker.remove();
                                        mDirectionMarker = null;
                                    }
                                    final Marker marker  = mMap.addMarker(placeOptions);
                                    marker.setTag(tags);
                                    searchMarker.add(marker);
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(placeLatLng, 15));
                                }
                            });

                            LatLng myLatLng = new LatLng(location.getLatitude(),
                                    location.getLongitude());
                            userLocation = myLatLng;

                            // show current location
                            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                mMap.setMyLocationEnabled(true);
                                mMap.getUiSettings().setZoomControlsEnabled(true);
                                mMap.getUiSettings().setCompassEnabled(true);
                            }

                            // show markers for all businesses on database
                            for(String businessID : mLocationInfo.keySet()){
                                try{
                                    Address address = geocoder.getFromLocationName(mLocationInfo.get(businessID), 1)
                                            .get(0);
                                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                                    String distance = getDistance(myLatLng, latLng);

                                    MarkerOptions options = new MarkerOptions().position(latLng)
                                            .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_food_truck_map_marker));

                                    ArrayList<String> tags = new ArrayList<>();
                                    tags.add("Business");
                                    tags.add(businessID);
                                    tags.add(distance);

                                    Marker marker = mMap.addMarker(options);
                                    marker.setTag(tags);
                                    marker.showInfoWindow();

                                }catch (IOException e){
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 15));
                        }
                    }
                });
            }else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }
        }

    }

    private ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if(result){
                    finish();
                    startActivity(getIntent());
                }
            }
    );

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResID){
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResID);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.map_homeButton:
                startActivity( new Intent(this, MainActivity.class));
                break;
            case R.id.map_reloadButton:
                resetMap();
                break;
        }
    }

    private void showSearchBottomSheet(Marker marker){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.search_bottom_sheet);

        Button searchDirections = (Button) bottomSheetDialog.findViewById(R.id.searchDirectionButton);
        searchDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                getDirections(marker);
            }
        });

        bottomSheetDialog.show();
    }

    private void hideSearchBottomSheet(){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.search_bottom_sheet);

        bottomSheetDialog.dismiss();
    }

    private void showBottomSheetDialog(Marker marker, String businessID, String distanceTo){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.map_bottom_sheet);

        TextView distance = (TextView) bottomSheetDialog.findViewById(R.id.bottom_distance);
        TextView time = (TextView) bottomSheetDialog.findViewById(R.id.bottom_time);
        TextView header = (TextView) bottomSheetDialog.findViewById(R.id.bottom_header);
        ImageView headerImage = (ImageView) bottomSheetDialog.findViewById(R.id.bottom_image);
        Button directions = (Button) bottomSheetDialog.findViewById(R.id.bottom_directions);
        Button visit = (Button) bottomSheetDialog.findViewById(R.id.bottom_visit_page);

        businessRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(businessID)){
                    Business businessData = snapshot.child(businessID).getValue(Business.class);

                    float totalDist = Float.parseFloat(distanceTo);
                    float timeTo = (totalDist/5)*60;

                    String distanceDisplay = distanceTo + " km away";
                    String timeDisplay = String.format(Locale.UK, "%.2f", timeTo) + " mins";

                    header.setText(businessData.businessName);
                    distance.setText(distanceDisplay);
                    time.setText(timeDisplay);
                    visit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MapActivity.this, BusinessPageActivity.class);
                            intent.putExtra("b_id", businessID);
                            startActivity(intent);
                        }
                    });
                    directions.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.dismiss();
                            getDirections(marker);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Data retrieval error",error.getMessage());
            }
        });



        businessImgRef.child(businessID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String uri = snapshot.getValue(BusinessImage.class).mImageUrl;
                    Picasso.with(headerImage.getContext()).load(uri).into(headerImage);
                } else {
                    headerImage.setImageResource(R.drawable.ic_baseline_image_not_supported_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Image retrieval error", error.getMessage());
            }
        });

        bottomSheetDialog.show();
    }

    private void hideBottomSheetDialog(){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.map_bottom_sheet);

        bottomSheetDialog.dismiss();
    }

    private String getDistance(LatLng currentLocation, LatLng newLocation){
        float[] distance = new float[1];
        Location.distanceBetween(currentLocation.latitude, currentLocation.longitude,
                newLocation.latitude, newLocation.longitude, distance);

        String finalDistance  = String.format(Locale.UK, "%.2f", distance[0]/1000);
        return finalDistance;

    }

    private void getDirections(Marker marker){
        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                marker.getPosition().latitude,
                marker.getPosition().longitude);

        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);

        directions.alternatives(true);
        directions.origin(new com.google.maps.model.LatLng(
                userLocation.latitude,
                userLocation.longitude));

        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                Log.d("LOCATION", "getDirections: routes: " + result.routes[0].toString());
                Log.d("LOCATION", "getDirections: duration: " + result.routes[0].legs[0].duration);
                Log.d("LOCATION", "getDirections: distance: " + result.routes[0].legs[0].distance);
                Log.d("LOCATION", "getDirections: geocodeWayPoints: " + result.geocodedWaypoints[0].toString());

                addPolyLines(result);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e("LOCATION", "getDirections: Failed to get directions: " + e.getMessage());
            }
        });
    }

    private void addPolyLines(final DirectionsResult result){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d("LOCATION", "run: result routes: " + result.routes.length);

                if(mPolylinesData.size() > 0){
                    for(PolylineData polylineData : mPolylinesData){
                        polylineData.getPolyline().remove();
                    }
                    mPolylinesData.clear();
                    mPolylinesData = new ArrayList<>();
                }

                double duration = 9999999;
                for(DirectionsRoute route : result.routes){
                    Log.d("LOCATION", "run: leg: " + route.legs[0].toString());
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                    List<LatLng> newDecodedPath = new ArrayList<>();

                    for(com.google.maps.model.LatLng latLng : decodedPath){
                        newDecodedPath.add(new LatLng(
                                latLng.lat,
                                latLng.lng
                        ));
                    }
                    Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    polyline.setColor(ContextCompat.getColor(getApplicationContext(), R.color.darkGrey));
                    polyline.setClickable(true);
                    mPolylinesData.add(new PolylineData(polyline, route.legs[0]));

                    double tempDuration = route.legs[0].duration.inSeconds;
                    if(tempDuration < duration){
                        duration = tempDuration;
                        onPolylineClick(polyline);
                        zoomRoute(polyline.getPoints());
                    }
                    mSelectedMarker.setVisible(false);
                }
            }
        });
    }

    @Override
    public void onPolylineClick(@NonNull Polyline polyline) {
        for(PolylineData polylineData : mPolylinesData){
            Log.d("LOCATION", "onPolyLineClick: toString: " + polylineData.toString());
            if(polyline.getId().equals(polylineData.getPolyline().getId())){
                polylineData.getPolyline().setColor(ContextCompat.getColor(getApplicationContext(), R.color.quantum_googblue));
                polylineData.getPolyline().setZIndex(1);

                LatLng endLocation = new LatLng(
                        polylineData.getLeg().endLocation.lat,
                        polylineData.getLeg().endLocation.lng);

                if(mDirectionMarker != null){
                    mDirectionMarker.remove();
                    Marker marker = mMap.addMarker(new MarkerOptions().position(endLocation)
                            .title(polylineData.getLeg().endAddress.substring(0, 20) + "...")
                            .snippet("Duration: " + polylineData.getLeg().duration + " | Distance: " + polylineData.getLeg().distance));
                    marker.showInfoWindow();

                    mDirectionMarker = marker;
                    ArrayList<String> markerTags = new ArrayList<>();
                    markerTags.add("Direction");
                    marker.setTag(markerTags);

                    tripMarkers.add(marker);
                }else {
                    Marker marker = mMap.addMarker(new MarkerOptions().position(endLocation)
                            .title(polylineData.getLeg().endAddress.substring(0, 20) + "...")
                            .snippet("Duration: " + polylineData.getLeg().duration + " | Distance: " + polylineData.getLeg().distance));
                    marker.showInfoWindow();

                    mDirectionMarker = marker;
                    ArrayList<String> markerTags = new ArrayList<>();
                    markerTags.add("Direction");
                    marker.setTag(markerTags);

                    tripMarkers.add(marker);
                }
            }else {
                polylineData.getPolyline().setColor(ContextCompat.getColor(getApplicationContext(), R.color.darkGrey));
                polylineData.getPolyline().setZIndex(0);
            }
        }
    }

    private void removeTripMarkers(){
        for(Marker marker : tripMarkers){
            marker.remove();
        }
    }

    private void resetSelectedMarker(){
        if(mSelectedMarker != null){
            mSelectedMarker.setVisible(true);
            mSelectedMarker = null;
            removeTripMarkers();
        }
    }

    private void zoomRoute(List<LatLng> lstLatLngRoute){
        if(mMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for(LatLng latLngPoint : lstLatLngRoute){
            boundsBuilder.include(latLngPoint);
        }

        int routePadding = 120;
        LatLngBounds latLngBounds = boundsBuilder.build();

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding),
                600,
                null);
    }

    private void resetMap(){
        finish();
        overridePendingTransition(0,0);
        startActivity(getIntent());
        overridePendingTransition(0,0);
    }

    private void removePolylines(){
        if(mMap != null){
            if(!mPolylinesData.isEmpty()){
                for(PolylineData polylineData : mPolylinesData){
                    polylineData.getPolyline().remove();
                }
            }
        }
    }
}