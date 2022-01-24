package com.example.leidosrollvan.fragments;

import android.Manifest;
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

import java.io.IOException;
import java.util.List;

public class MapsFragment extends Fragment {
    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient client;
    private Geocoder geocoder;
    private String postCode = "G11 6PE";
    private Address mAddress;


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

        geocoder = new Geocoder(requireActivity());
        try {
            List<Address> addresses = geocoder.getFromLocationName(postCode, 1);
            if(addresses != null && !addresses.isEmpty()){
                mAddress = addresses.get(0);
            }else {
                Toast.makeText(requireActivity(), "Unable to get address", Toast.LENGTH_SHORT).show();
            }
        }catch (IOException e ){
            Toast.makeText(requireActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        getCurrentLocation();


//        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(@NonNull GoogleMap googleMap) {
//                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                    @Override
//                    public void onMapClick(@NonNull LatLng latLng) {
//                        MarkerOptions markerOptions = new MarkerOptions();
//                        markerOptions.position(latLng);
//                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
//                        googleMap.clear();
//                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                                latLng, 10
//                        ));
//                        googleMap.addMarker(markerOptions);
//                    }
//                });
//            }
//        });

        return view;
    }

    private void getCurrentLocation() {
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

                                LatLng addressLatLng = new LatLng(mAddress.getLatitude(),
                                        mAddress.getLongitude());

                                MarkerOptions options = new MarkerOptions().position(addressLatLng)
                                        .title("Set location").icon(BitmapDescriptorFactory
                                                .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

                                // set current location
                                if (ActivityCompat.checkSelfPermission(requireActivity(),
                                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                    googleMap.setMyLocationEnabled(true);
                                }
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 10));
                                googleMap.addMarker(options);
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
                    getCurrentLocation();
                }
            }
    );
}