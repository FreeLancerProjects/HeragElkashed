package com.creative.share.apps.heragelkashed.models;

import java.io.Serializable;
import java.util.List;

public class FavoriteDataModel implements Serializable {
    private int current_page;
    private List<AdModel> data;

    public int getCurrent_page() {
        return current_page;
    }

    public List<AdModel> getData() {
        return data;
    }
}
