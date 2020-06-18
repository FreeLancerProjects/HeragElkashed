package com.creative.share.apps.heragelkashed.models;

import java.io.Serializable;
import java.util.List;

public class CompanyDataModel implements Serializable {

    private int current_page;
    private List<CompanyModel> data;

    public int getCurrent_page() {
        return current_page;
    }

    public List<CompanyModel> getData() {
        return data;
    }
}
