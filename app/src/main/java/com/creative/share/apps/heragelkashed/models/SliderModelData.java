package com.creative.share.apps.heragelkashed.models;

import java.io.Serializable;
import java.util.List;

public class SliderModelData implements Serializable {

    private List<SliderModel> data;

    public List<SliderModel> getData() {
        return data;
    }

    public class SliderModel implements Serializable
    {
        private int id;
        private int user_id;
        private int city_id;
        private String title;
        private String description;
        private int like_counts;
        private int report_counts;
        private int favorite_counts;
        private int comment_counts;
        private int is_like_check;
        private int is_favourite_check;
        private int is_report_check;
        private int is_follow_check;
        private String main_image;
        private List<String> image_array;


        public int getId() {
            return id;
        }

        public int getUser_id() {
            return user_id;
        }

        public int getCity_id() {
            return city_id;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public int getLike_counts() {
            return like_counts;
        }

        public int getReport_counts() {
            return report_counts;
        }

        public int getFavorite_counts() {
            return favorite_counts;
        }

        public int getComment_counts() {
            return comment_counts;
        }

        public int getIs_like_check() {
            return is_like_check;
        }

        public int getIs_favourite_check() {
            return is_favourite_check;
        }

        public int getIs_report_check() {
            return is_report_check;
        }

        public int getIs_follow_check() {
            return is_follow_check;
        }

        public String getMain_image() {
            return main_image;
        }

        public List<String> getImage_array() {
            return image_array;
        }
    }
}
