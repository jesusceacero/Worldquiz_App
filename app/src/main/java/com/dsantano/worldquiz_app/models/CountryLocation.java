package com.dsantano.worldquiz_app.models;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class CountryLocation implements ClusterItem {
    private final String username;
    private final LatLng latLng;
    private final String isoCode;
    private final Bitmap bandera;

    public CountryLocation(String username, LatLng latLng , String isoCode, Bitmap bandera) {
        this.username = username;
        this.latLng = latLng;
        this.isoCode= isoCode;
        this.bandera = bandera;
    }

    @Override
    public LatLng getPosition() {  // 1
        return latLng;
    }

    @Override
    public String getTitle() {  // 2
        return username;
    }

    @Override
    public String getSnippet() {
        return "";
    }

    public String getIsoCode() {return isoCode;
    }

    public Bitmap getBandera() {
        return bandera;
    }
}
