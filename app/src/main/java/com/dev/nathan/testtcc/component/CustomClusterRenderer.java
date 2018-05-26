package com.dev.nathan.testtcc.component;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class CustomClusterRenderer extends DefaultClusterRenderer<Person> {

    Integer color;
    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<Person> clusterManager,Integer color) {
        super(context, map, clusterManager);
        this.color = color;
    }

    @Override
    protected int getColor(int clusterSize) {
        // Return any color you want here. You can base it on clusterSize.
        return color;
    }

    /*
    @Override
    protected void onBeforeClusterItemRendered(T item, MarkerOptions markerOptions) {
        // Use this method to set your own icon for the markers
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<T> cluster, MarkerOptions markerOptions) {
        // Use this method to set your own icon for the clusters
    }
    */
}