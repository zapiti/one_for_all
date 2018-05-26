package com.dev.nathan.testtcc.demos;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.dev.nathan.testtcc.R;
import com.dev.nathan.testtcc.model.AttestantPost;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        firebaseFirestore = FirebaseFirestore.getInstance();

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        CallMaker( mMap);
        // Add a marker in Sydney and move the camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-18.9146, -48.2754), 9));

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
        Query firstQuery = firebaseFirestore.collection("Posts").orderBy("dhUpload", Query.Direction.DESCENDING).limit(3);

        firstQuery.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {


                if (documentSnapshots != null) {
                    if (!documentSnapshots.isEmpty()) {


                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            if (doc.getType() == DocumentChange.Type.ADDED) {


                                AttestantPost id = doc.getDocument().toObject(AttestantPost.class);





                                if(id.danger.equals("Assédio Moral ou Mobbing")) {
                                    getLocationFromAddress(MapsActivity.this, id.address,id,googleMap,R.drawable.ic_loca);
                                }

                                if(id.danger.equals("Assédio Sexual")) {
                                    getLocationFromAddress(MapsActivity.this, id.address,id,googleMap,R.drawable.ic_loca);
                                }

                                if(id.danger.equals("Violência no Trabalho")) {
                                    getLocationFromAddress(MapsActivity.this, id.address,id,googleMap,R.drawable.ic_loca);
                                }
                                else{
                                    getLocationFromAddress(MapsActivity.this, id.address,id,googleMap,R.drawable.ic_loca);
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
