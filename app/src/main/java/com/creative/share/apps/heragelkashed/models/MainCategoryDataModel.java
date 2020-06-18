package com.creative.share.apps.heragelkashed.models;

import java.io.Serializable;
import java.util.List;

public class MainCategoryDataModel implements Serializable {

    private List<MainCategoryModel> data;

    public List<MainCategoryModel> getData() {
        return data;
    }

    public class MainCategoryModel implements Serializable
    {
        private int id;
        private String title;
        private String icon;
        private String image;
        List<SubCategoryModel> sub_categories;

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getIcon() {
            return icon;
        }

        public String getImage() {
            return image;
        }

        public List<SubCategoryModel> getSub_categories() {
            return sub_categories;
        }
    }

    public class SubCategoryModel implements Serializable
    {
        private int id;
        private String title;
        private String icon;
        private String image;

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getIcon() {
            return icon;
        }

        public String getImage() {
            return image;
        }
    }
}
