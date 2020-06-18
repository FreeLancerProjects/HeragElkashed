package com.creative.share.apps.heragelkashed.models;

import java.io.Serializable;
import java.util.List;

public class SubCategoryDataModel implements Serializable {

    private List<SubCategoryModel> data;

    public List<SubCategoryModel> getData() {
        return data;
    }

    public static class SubCategoryModel implements Serializable
    {
        private int id;
        private String title;
        private String icon;
        private int parent_id;
        public SubCategoryModel() {
        }

        public SubCategoryModel(int id, String title) {
            this.id = id;
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getIcon() {
            return icon;
        }

        public int getParent_id() {
            return parent_id;
        }
    }
}
