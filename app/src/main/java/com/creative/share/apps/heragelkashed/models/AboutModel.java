package com.creative.share.apps.heragelkashed.models;

import java.io.Serializable;

public class AboutModel implements Serializable {

    private String phone_number;
    private String mobile_number;
    private String contact_email;
    private String facebook;
    private String twitter;
    private String instagram;
    private String about_us;
    private String terms;
    private String site_link;

    public String getPhone_number() {
        return phone_number;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public String getContact_email() {
        return contact_email;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getAbout_us() {
        return about_us;
    }

    public String getTerms() {
        return terms;
    }

    public String getSite_link() {
        return site_link;
    }
}
