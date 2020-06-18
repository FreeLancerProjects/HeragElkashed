package com.creative.share.apps.heragelkashed.models;

import java.io.Serializable;
import java.util.List;

public class UserRoomModelData implements Serializable {

    private List<UserRoomModel> data;
    private int current_page;

    public List<UserRoomModel> getData() {
        return data;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public class UserRoomModel implements Serializable
    {
        private int id;
        private int first_user_id;
        private int second_user_id;
        private String other_user_name;
        private String other_user_phone_code;
        private String other_user_phone;
        private String other_user_avatar;
        private int last_message_type;
        private String last_message;
        private int last_message_date;
        private int my_message_unread_count;


        public int getId() {
            return id;
        }

        public int getFirst_user_id() {
            return first_user_id;
        }

        public int getSecond_user_id() {
            return second_user_id;
        }

        public String getOther_user_name() {
            return other_user_name;
        }

        public String getOther_user_phone_code() {
            return other_user_phone_code;
        }

        public String getOther_user_phone() {
            return other_user_phone;
        }

        public String getOther_user_avatar() {
            return other_user_avatar;
        }

        public int getMy_message_unread_count() {
            return my_message_unread_count;
        }

        public String getLast_message() {
            return last_message;
        }

        public int getLast_message_date() {
            return last_message_date;
        }

        public void setMy_message_unread_count(int my_message_unread_count) {
            this.my_message_unread_count = my_message_unread_count;
        }

        public int getLast_message_type() {
            return last_message_type;
        }
    }


}
