package com.dev.nathan.testtcc.component;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.dev.nathan.testtcc.R;

import com.dev.nathan.testtcc.model.AttestantPost;
import com.dev.nathan.testtcc.test.MainActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.clustering.ClusterManager;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends BaseGoogleMapsActivity {
    private FirebaseFirestore firebaseFirestore;
    float rainfall [] = {20f,40f,30f,10f,300f};
    private static final int REQUEST_LOCATION = 1;
    String type[] ={"Noticias  ","Ajuda ","Saude ","ONGs ", "Entreterimento"};
    private PieChart chart;
    private Toolbar mainToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseFirestore = FirebaseFirestore.getInstance();


        Button btnGoToMain = findViewById(R.id.goToMainIntro);
        btnGoToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoMenu=  new Intent(MapsActivity.this, MainActivity.class);
                startActivity(gotoMenu);
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setupMap(googleMap);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-18.9146, -48.2754), 9));

        ClusterManager<Person> mClusterManager = new ClusterManager<>(this, googleMap);
        ClusterManager<Person> mClusterManager2 = new ClusterManager<>(this, googleMap);
        ClusterManager<Person> mClusterManager3 = new ClusterManager<>(this, googleMap);
        ClusterManager<Person> mClusterManager4 = new ClusterManager<>(this, googleMap);
        ClusterManager<Person> mClusterManager5 = new ClusterManager<>(this, googleMap);

        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
        googleMap.setOnInfoWindowClickListener(mClusterManager);


        googleMap.setOnCameraIdleListener(mClusterManager2);
        googleMap.setOnMarkerClickListener(mClusterManager2);
        googleMap.setOnInfoWindowClickListener(mClusterManager2);

        googleMap.setOnCameraIdleListener(mClusterManager3);
        googleMap.setOnMarkerClickListener(mClusterManager3);
        googleMap.setOnInfoWindowClickListener(mClusterManager3);

        googleMap.setOnCameraIdleListener(mClusterManager4);
        googleMap.setOnMarkerClickListener(mClusterManager4);
        googleMap.setOnInfoWindowClickListener(mClusterManager4);

        googleMap.setOnCameraIdleListener(mClusterManager5);
        googleMap.setOnMarkerClickListener(mClusterManager5);
        googleMap.setOnInfoWindowClickListener(mClusterManager5);



        mClusterManager.cluster();
        mClusterManager2.cluster();
        mClusterManager3.cluster();
        mClusterManager4.cluster();
        mClusterManager5.cluster();
        CallMaker(googleMap);
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

    protected void createMarker(double latitude, double longitude, String title, String snippet, int iconResID, GoogleMap googleMap) {

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.fromResource(iconResID)));
    }

    public void getLocationFromAddress(Context context,String strAddress, AttestantPost id,GoogleMap googleMap,Integer icon) {

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


    // Todo impoetante chart
    private void setupPieChart() {
        //popular e listar os enfim

        List<PieEntry> pieEntries = new ArrayList<>();
        for(int i = 0; i< rainfall.length; i ++ ){
            pieEntries.add(new PieEntry(rainfall[i],type[i]));
        }
        PieDataSet dataSet = new PieDataSet(pieEntries,null);
        PieData data = new PieData(dataSet);


        dataSet.setColors( Color.parseColor("#12005e"),//noticia
                Color.parseColor("#ac1900"),//ajuda
                Color.parseColor("#1b0000"),//saude
                Color.parseColor("#7f0000"),//ongs
                Color.parseColor("#524c00"));



//        chart.getLegend().setEnabled(false);
        chart.setData(data);

        chart.getDescription().setEnabled(false);
        chart.setDrawEntryLabels(false);


        chart.getData().setValueTextSize(18);
        chart.getLegend().setStackSpace(18);
        chart.getLegend().setFormSize(18);
        chart.getLegend().setXEntrySpace(18);
        chart.getLegend().setTextSize(18);
        chart.getLegend().setWordWrapEnabled(true);
        chart.animateXY(800, 800);

        // pegar o chart

    }
}
