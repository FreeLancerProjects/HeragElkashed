package com.creative.share.apps.heragelkashed.models;

import java.io.Serializable;
import java.util.List;

public class AdTypeDataModel implements Serializable {

    private List<AdTypeModel> data;

    public List<AdTypeModel> getData() {
        return data;
    }

    public class AdTypeModel implements Serializable
    {
        private int id;
        private String title;

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }
    }
}
