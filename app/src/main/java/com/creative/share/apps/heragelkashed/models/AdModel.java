package com.creative.share.apps.heragelkashed.models;

import java.io.Serializable;
import java.util.List;

public class AdModel implements Serializable {

    private int id;
    private String date;
    private int user_id;
    private int city_id;
    private double price;
    private int category_id;
    private int adv_type_id;

    private String company_id;
    private String main_image;
    private String cat_title;
    private String address;
    private double latitude;
    private double longitude;
    private int is_featured;
    private String video;
    private int status;
    private int view_counts;
    private  String user_name;
    private String user_email;
    private String user_phone;
    private String user_avatar;
    private String city_ar_name;
    private String city_en_name;
    private String company_name;
    private String company_phone;
    private String company_email;
    private String company_is_featured;
    private String title;
    private String description;
    private String link_to_share;
    private int follow_counts;
    private int like_counts;
    private int report_counts;
    private int favorite_counts;
    private int comment_counts;
    private int is_like_check;
    private int is_favourite_check;
    private int is_report_check;
    private int is_follow_check;
    private List<AdDetails> advertisement_Details;
    private List<CommentModel> comments;
    private List<String> image_array;


    public AdModel(double price, String address, double latitude, double longitude) {
        this.price = price;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getCity_id() {
        return city_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public int getAdv_type_id() {
        return adv_type_id;
    }

    public double getPrice() {
        return price;
    }

    public String getMain_image() {
        return main_image;
    }

    public String getAddress() {
        return address;
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

    public String getVideo() {
        return video;
    }

    public int getStatus() {
        return status;
    }

    public String getCat_title() {
        return cat_title;
    }

    public String getCompany_id() {
        return company_id;
    }

    public int getView_counts() {
        return view_counts;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public String getCity_ar_name() {
        return city_ar_name;
    }

    public String getCity_en_name() {
        return city_en_name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getCompany_phone() {
        return company_phone;
    }

    public String getCompany_email() {
        return company_email;
    }

    public String getCompany_is_featured() {
        return company_is_featured;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getFollow_counts() {
        return follow_counts;
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

    public String getLink_to_share() {
        return link_to_share;
    }

    public List<AdDetails> getAdvertisementDetails() {
        return advertisement_Details;
    }

    public List<CommentModel> getComments() {
        return comments;
    }

    public void setLike_counts(int like_counts) {
        this.like_counts = like_counts;
    }

    public void setIs_like_check(int is_like_check) {
        this.is_like_check = is_like_check;
    }

    public void setIs_favourite_check(int is_favourite_check) {
        this.is_favourite_check = is_favourite_check;
    }

    public void setIs_report_check(int is_report_check) {
        this.is_report_check = is_report_check;
    }

    public void setIs_follow_check(int is_follow_check) {
        this.is_follow_check = is_follow_check;
    }

    public List<String> getImage_array() {
        return image_array;
    }

    public class AdDetails implements Serializable
    {
        private String dev;
        private String value;

        public String getDev() {
            return dev;
        }

        public String getValue() {
            return value;
        }
    }

    public class CommentModel implements Serializable
    {
        private int id;
        private String content;
        private int status;
        private int advertisement_id;
        private String user_id;
        private String created_at;
        private String user_name;
        private String user_email;
        private String user_phone;
        private String user_avatar;

        public int getId() {
            return id;
        }

        public String getContent() {
            return content;
        }

        public int getStatus() {
            return status;
        }

        public int getAdvertisement_id() {
            return advertisement_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getUser_email() {
            return user_email;
        }

        public String getUser_phone() {
            return user_phone;
        }

        public String getUser_avatar() {
            return user_avatar;
        }
    }
}
