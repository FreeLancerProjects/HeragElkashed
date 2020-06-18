package com.creative.share.apps.heragelkashed.models;

import java.io.Serializable;
import java.util.List;

public class NotificationDataModel implements Serializable {
    private int current_page;
    private List<NotificationModel> data;

    public int getCurrent_page() {
        return current_page;
    }

    public List<NotificationModel> getData() {
        return data;
    }

    public class NotificationModel implements Serializable {
        private int id;
        private String created_at;
        private String notification_message;

        public int getId() {
            return id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getNotification_message() {
            return notification_message;
        }
    }



}
