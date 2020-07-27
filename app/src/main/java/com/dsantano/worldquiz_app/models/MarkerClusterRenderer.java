package com.dsantano.worldquiz_app.models;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dsantano.worldquiz_app.CountryDetailActivity;
import com.dsantano.worldquiz_app.Interfaces.ICountryListener;
import com.dsantano.worldquiz_app.R;
import com.dsantano.worldquiz_app.fragments.map.MapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.auth.User;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

public class MarkerClusterRenderer extends DefaultClusterRenderer<CountryLocation> implements ClusterManager.OnClusterClickListener<CountryLocation>, GoogleMap.OnInfoWindowClickListener  {
    private static final int MARKER_DIMENSION = 48;  // 2
    private final IconGenerator iconGenerator;
    private final ImageView markerImageView;
    private final Context ctx;


    public MarkerClusterRenderer(Context context, GoogleMap map, ClusterManager<CountryLocation> clusterManager) {
        super(context, map, clusterManager);
        ctx = context;
        iconGenerator = new IconGenerator(context);  // 3
        markerImageView = new ImageView(context);
        markerImageView.setLayoutParams(new ViewGroup.LayoutParams(MARKER_DIMENSION, MARKER_DIMENSION));
        iconGenerator.setContentView(markerImageView);  // 4
    }

    @Override
    protected void onBeforeClusterItemRendered(CountryLocation item, MarkerOptions markerOptions) { // 5
        Bitmap icon = iconGenerator.makeIcon();  // 7
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(item.getBandera()));  // 8
        markerOptions.title(item.getTitle());
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
    }

    @Override
    protected void onClusterItemRendered(CountryLocation clusterItem, Marker marker) {
        marker.setTag(clusterItem);
    }


    @Override
    public boolean onClusterClick(Cluster<CountryLocation> cluster) {
        return false;
    }
}
