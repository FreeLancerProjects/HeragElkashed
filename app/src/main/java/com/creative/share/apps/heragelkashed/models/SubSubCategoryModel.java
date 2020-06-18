package com.creative.share.apps.heragelkashed.models;

import java.io.Serializable;
import java.util.List;

public class SubSubCategoryModel implements Serializable {
    private int id;
    private String title;
    private int parent_id;
    private List<SubCategories> sub_categories;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getParent_id() {
        return parent_id;
    }

    public List<SubCategories> getSub_categories() {
        return sub_categories;
    }

    public class SubCategories implements Serializable
    {
        private int id;
        private String title;
        private int parent_id;
        private String icon;

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public int getParent_id() {
            return parent_id;
        }

        public String getIcon() {
            return icon;
        }
    }
}
