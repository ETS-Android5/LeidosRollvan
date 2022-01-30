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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
import com.squareup.picasso.Picasso;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MapActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback{
    private SupportMapFragment supportMapFragment;
    private ImageButton mapHomeButton, reloadButton;
    private AutocompleteSupportFragment autocompleteSupportFragment;
    private FusedLocationProviderClient client;
    private Geocoder geocoder;
    private GoogleMap mMap;
    private ArrayList<String> mPostCodes;
    private HashMap<String, String> mLocationInfo;
    private ArrayList<Marker> searchMarker;
    private DatabaseReference locationRef;
    private DatabaseReference businessRef;
    private DatabaseReference businessImgRef;
    private String apiKey;

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

        // Initialize client to get user's last location on device
        client = LocationServices.getFusedLocationProviderClient(this);

        // Initialize geocoder to convert business postcodes to latlng coordinates
        mLocationInfo = new HashMap<String, String>();
        mPostCodes = new ArrayList<>();
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
                        mPostCodes.add(location.getPostCode());
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
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                if(marker.getTag() != null){
                    String businessID = (String) marker.getTag();
                    showBottomSheetDialog(businessID);
                }
                return true;
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                hideBottomSheetDialog();
            }
        });
        // render the map
        loadMap();
    }

    private void loadMap() {
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
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                                if(!searchMarker.isEmpty()){
                                    Marker searchedMarker = searchMarker.get(0);
                                    searchMarker.remove(searchedMarker);
                                    searchedMarker.remove();
                                }

                                final Marker marker  = mMap.addMarker(placeOptions);
                                searchMarker.add(marker);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(placeLatLng, 15));
                            }
                        });

                        LatLng myLatLng = new LatLng(location.getLatitude(),
                                location.getLongitude());

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

                                MarkerOptions options = new MarkerOptions().position(latLng)
                                        .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_baseline_restaurant_24));

                                mMap.addMarker(options).setTag(businessID);

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

    private ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if(result){
                    loadMap();
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
                finish();
                overridePendingTransition(0,0);
                startActivity(getIntent());
                overridePendingTransition(0,0);
        }

    }

    private void showBottomSheetDialog(String businessID){
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

                    header.setText(businessData.businessName);
                    visit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MapActivity.this, BusinessPageActivity.class);
                            intent.putExtra("b_id", businessID);
                            startActivity(intent);
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

}