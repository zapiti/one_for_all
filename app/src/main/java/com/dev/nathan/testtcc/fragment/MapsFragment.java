package com.dev.nathan.testtcc.fragment;

import android.Manifest;


import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dev.nathan.testtcc.R;

import com.dev.nathan.testtcc.demos.MapsActivity;
import com.dev.nathan.testtcc.model.AttestantPost;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class MapsFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    private FirebaseFirestore firebaseFirestore;
    private Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.location_fragment, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        context = container.getContext();

        mMapView.onResume(); // needed to get the map to display immediately
        firebaseFirestore = FirebaseFirestore.getInstance();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;


               // googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                CallMaker(googleMap);
                LatLng uberlandia = new LatLng(-18.9146, -48.2754);


                // For zooming automatically to the location of the marker


                CameraPosition cameraPosition = new CameraPosition.Builder().target(uberlandia).zoom(9).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        return rootView;
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

    protected void createMarker(double latitude, double longitude, String title, String snippet, int iconResID, GoogleMap googleMap) {

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.fromResource(iconResID)));
    }

    public void getLocationFromAddress(Context context, String strAddress, AttestantPost id, GoogleMap googleMap, Integer icon) {

        Geocoder coder = new Geocoder(context, Locale.getDefault());
        List<Address> address;


        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 1);

            Address location;
            if (  address.size()>0) {
                location = address.get(0);

                createMarker(location.getLatitude(), location.getLongitude() , id.desc, id.address, icon,googleMap);

            }


        } catch (IOException ex) {

            ex.printStackTrace();
        }

    }


    private void CallMaker(final GoogleMap googleMap) {
        Query firstQuery = firebaseFirestore.collection("Posts").orderBy("dhUpload", Query.Direction.DESCENDING);

        firstQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {


                if (documentSnapshots != null) {
                    if (!documentSnapshots.isEmpty()) {


                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            if (doc.getType() == DocumentChange.Type.ADDED) {


                                AttestantPost id = doc.getDocument().toObject(AttestantPost.class);





                                if(id.danger.equals("Assédio Moral ou Mobbing")) {
                                    getLocationFromAddress(context, id.address,id,googleMap,R.drawable.ic_danger_moral);
                                }

                                if(id.danger.equals("Assédio Sexual")) {
                                    getLocationFromAddress(context, id.address,id,googleMap,R.drawable.ic_danger_sexual);
                                }

                                if(id.danger.equals("Violência no Trabalho")) {
                                    getLocationFromAddress(context, id.address,id,googleMap,R.drawable.ic_danger_trabalho);
                                }
                                else{
                                    getLocationFromAddress(context, id.address,id,googleMap,R.drawable.ic_danger_other);
                                }

                            }

                            //    Log.d("Assado",latLng.toString());
                        }



                    }
                }

            }

        });
    }
}

