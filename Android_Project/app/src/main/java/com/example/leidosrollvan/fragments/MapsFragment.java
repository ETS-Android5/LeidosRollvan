package com.example.leidosrollvan.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.leidosrollvan.R;
import com.example.leidosrollvan.activity.BusinessHomeActivity;
import com.example.leidosrollvan.activity.MainActivity;
import com.example.leidosrollvan.dataClasses.BusinessLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsFragment extends Fragment {
    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient client;
    private Geocoder geocoder;
    private ArrayList<String> mPostCodes;
    private DatabaseReference locationRef;
    private DatabaseReference businessRef;



    public MapsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        client = LocationServices.getFusedLocationProviderClient(requireActivity());

        mPostCodes = new ArrayList<>();

        geocoder = new Geocoder(requireActivity());

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
            }
        });

        businessRef = FirebaseDatabase.getInstance().getReference("Businesses");

        loadMap();
        return view;
    }

    private void loadMap() {
        if (ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Location> task = client.getLastLocation();

            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull GoogleMap googleMap) {
                                LatLng myLatLng = new LatLng(location.getLatitude(),
                                        location.getLongitude());

                                // set current location
                                if (ActivityCompat.checkSelfPermission(requireActivity(),
                                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                    googleMap.setMyLocationEnabled(true);
                                }
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 10));


                                for(String code : mPostCodes){
                                    try{
                                        Address address = geocoder.getFromLocationName(code, 1).get(0);
                                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                                        MarkerOptions options = new MarkerOptions().position(latLng);
                                        googleMap.addMarker(options);
                                    }catch (IOException e){
                                       Toast.makeText(requireActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }

                                }
                            }
                        });
                    }
                }
            });
        }else {
            ActivityCompat.requestPermissions(requireActivity(),
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