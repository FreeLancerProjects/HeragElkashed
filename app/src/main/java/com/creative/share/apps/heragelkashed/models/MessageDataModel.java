package com.creative.share.apps.heragelkashed.models;

import java.io.Serializable;
import java.util.List;

public class MessageDataModel implements Serializable {

    private RoomData room;
    private Messages messages;

    public RoomData getRoom() {
        return room;
    }

    public Messages getMessages() {
        return messages;
    }

    public class RoomData implements Serializable
    {
        private int id;
        private int second_user_id;
        private String other_user_name;
        private String other_user_phone_code;
        private String other_user_phone;
        private String other_user_avatar;

        public int getId() {
            return id;
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
    }

    public class Messages implements Serializable
    {
        private int current_page;
        private List<MessageModel> data;

        public int getCurrent_page() {
            return current_page;
        }

        public List<MessageModel> getData() {
            return data;
        }
    }
}
