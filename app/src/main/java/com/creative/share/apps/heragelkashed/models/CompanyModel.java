package com.creative.share.apps.heragelkashed.models;

import java.io.Serializable;

public class CompanyModel implements Serializable {

    private int id;
    private String name;
    private String logo;
    private String phone;
    private String email;
    private String details;
    private double latitude;
    private double longitude;
    private int is_featured;
    private int views_count;
    private int ads_count;


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogo() {
        return logo;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getDetails() {
        return details;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getIs_featured() {
        return is_featured;
    }

    public int getViews_count() {
        return views_count;
    }

    public int getAds_count() {
        return ads_count;
    }
}
