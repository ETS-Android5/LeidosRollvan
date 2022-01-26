package com.example.leidosrollvan.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.leidosrollvan.R;
import com.example.leidosrollvan.dataClasses.BusinessLocation;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
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
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MapActivity extends AppCompatActivity {
    private SupportMapFragment supportMapFragment;
    private AutocompleteSupportFragment autocompleteSupportFragment;
    private FusedLocationProviderClient client;
    private Geocoder geocoder;
    private ArrayList<String> mPostCodes;
    private ArrayList<Marker> searchMarker;
    private DatabaseReference locationRef;
    private DatabaseReference businessRef;
    private String apiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Initialize Map fragment
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

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
        mPostCodes = new ArrayList<>();
        geocoder = new Geocoder(this);

        // Get business locations
        locationRef = FirebaseDatabase.getInstance().getReference("Business Locations");
        locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot locationSnapshot : snapshot.getChildren()){
                        BusinessLocation location = locationSnapshot.getValue(BusinessLocation.class);
                        mPostCodes.add(location.getPostCode());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Location error", "Error retrieving location: ", error.toException().getCause());
            }
        });

        // initialize reference to business table
        businessRef = FirebaseDatabase.getInstance().getReference("Businesses");

        // render the map
        loadMap();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void loadMap() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Location> task = client.getLastLocation();

            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull GoogleMap googleMap) {

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
                                                .title("search")
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                                        if(!searchMarker.isEmpty()){
                                            Marker searchedMarker = searchMarker.get(0);
                                            searchMarker.remove(searchedMarker);
                                            searchedMarker.remove();
                                        }

                                        final Marker marker  = googleMap.addMarker(placeOptions);
                                        searchMarker.add(marker);
                                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(placeLatLng, 10));
                                    }
                                });

                                LatLng myLatLng = new LatLng(location.getLatitude(),
                                        location.getLongitude());

                                // show current location
                                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                    googleMap.setMyLocationEnabled(true);
                                    googleMap.getUiSettings().setZoomControlsEnabled(true);
                                    googleMap.getUiSettings().setCompassEnabled(true);
                                }

                                // show markers for all businesses on database
                                for(String code : mPostCodes){
                                    try{
                                        Address address = geocoder.getFromLocationName(code, 1).get(0);
                                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                                        MarkerOptions options = new MarkerOptions().position(latLng);
                                        googleMap.addMarker(options);
                                    }catch (IOException e){
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 10));
                            }
                        });
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
}