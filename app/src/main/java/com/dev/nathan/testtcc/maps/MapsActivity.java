package com.dev.nathan.testtcc.maps;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dev.nathan.testtcc.R;
import com.dev.nathan.testtcc.controler.MainActivity;
import com.dev.nathan.testtcc.model.AttestantPost;
import com.dev.nathan.testtcc.model.Url;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MapsActivity extends BaseGoogleMapsActivity {
    private FirebaseFirestore firebaseFirestore;
    float rainfall [] = {20f,40f,30f,10f,300f};
    private static final int REQUEST_LOCATION = 1;
    String type[] ={"Noticias  ","Ajuda ","Saude ","ONGs ", "Entreterimento"};
    private ClusterManager<Person> mClusterManager;
    private ClusterManager<Person> mClusterManager2;
    private ClusterManager<Person> mClusterManager3;
    private ClusterManager<Person> mClusterManager4;
    private ClusterManager<Person> mClusterManager5;
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

        mClusterManager = new ClusterManager<>(this, googleMap);
        mClusterManager2 = new ClusterManager<>(this, googleMap);
        mClusterManager3 = new ClusterManager<>(this, googleMap);
        mClusterManager4 = new ClusterManager<>(this, googleMap);
        mClusterManager5 = new ClusterManager<>(this, googleMap);

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


        addPersonItems(googleMap);
        mClusterManager.cluster();
        mClusterManager2.cluster();
        mClusterManager3.cluster();
        mClusterManager4.cluster();
        mClusterManager5.cluster();
    }

    private void addPersonItems(final GoogleMap googleMap) {

        Query firstQuery = firebaseFirestore.collection("Posts").orderBy("dhUpload", Query.Direction.DESCENDING).limit(3);

        firstQuery.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (documentSnapshots != null) {
                    if (!documentSnapshots.isEmpty()) {


                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            if (doc.getType() == DocumentChange.Type.ADDED) {


                                AttestantPost id = doc.getDocument().toObject(AttestantPost.class);


                                LatLng latLng = getLocationFromAddress(MapsActivity.this, id.address);
                                if(latLng != null) {
                                switch (id.danger) {

                                        case "Assédio Escolar":
                                            mClusterManager3.addItem(new Person(latLng.latitude, latLng.longitude, "PJ3", null));
                                            mClusterManager3.setRenderer(new CustomClusterRenderer(getApplicationContext(), googleMap, mClusterManager, Color.parseColor("#7f0000")));

                                            break;

                                        case "Assédio Moral ou Mobbing":
                                            mClusterManager.addItem(new Person(latLng.latitude, latLng.longitude, "PJ", null));
                                            mClusterManager.setRenderer(new CustomClusterRenderer(getApplicationContext(), googleMap, mClusterManager, Color.parseColor("#1a237e")));


                                            break;

                                        case "Assédio Sexual":
                                            mClusterManager4.addItem(new Person(latLng.latitude, latLng.longitude, "PJ3", null));
                                            mClusterManager4.setRenderer(new CustomClusterRenderer(getApplicationContext(), googleMap, mClusterManager, Color.parseColor("#6a1b9a")));

                                            break;

                                        case "Violência no Trabalho":
                                            mClusterManager2.addItem(new Person(latLng.latitude, latLng.longitude, "PJ2", null));
                                            mClusterManager2.setRenderer(new CustomClusterRenderer(getApplicationContext(), googleMap, mClusterManager, Color.parseColor("#e65100")));
                                            break;


                                        default:
                                            mClusterManager5.addItem(new Person(latLng.latitude, latLng.longitude, "PJ3", null));
                                            mClusterManager5.setRenderer(new CustomClusterRenderer(getApplicationContext(), googleMap, mClusterManager, Color.parseColor("#003300")));

                                    }
                                }

                                    //    Log.d("Assado",latLng.toString());
                            }
                        }


                    }
                }

            }

        });



    }
    public LatLng getLocationFromAddress(Context context,String strAddress) {

        Geocoder coder = new Geocoder(context, Locale.getDefault());
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

        class RenderClusterInfoWindow extends DefaultClusterRenderer<Person> {

        RenderClusterInfoWindow(Context context, GoogleMap map, ClusterManager<Person> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onClusterRendered(Cluster<Person> cluster, Marker marker) {
            super.onClusterRendered(cluster, marker);
        }

        @Override
        protected void onBeforeClusterItemRendered(Person item, MarkerOptions markerOptions) {
            markerOptions.title(item.getName());

            super.onBeforeClusterItemRendered(item, markerOptions);
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
