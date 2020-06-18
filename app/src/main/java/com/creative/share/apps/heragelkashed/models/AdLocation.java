package com.creative.share.apps.heragelkashed.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class AdLocation implements ClusterItem {

    private String price;
    private String address;
    private LatLng latLng;

    public AdLocation(String price, String address, LatLng latLng) {
        this.price = price;
        this.address = address;
        this.latLng = latLng;
    }

    @Override
    public LatLng getPosition() {
        return latLng;
    }

    @Override
    public String getTitle() {
        return price;
    }

    @Override
    public String getSnippet() {
        return address;
    }
}
