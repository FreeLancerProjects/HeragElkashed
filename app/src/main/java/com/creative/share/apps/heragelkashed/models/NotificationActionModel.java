package com.creative.share.apps.heragelkashed.models;

import java.io.Serializable;

public class NotificationActionModel implements Serializable {

    private int to_id;
    private String title;
    private String content;

    public NotificationActionModel(int to_id, String title, String content) {
        this.to_id = to_id;
        this.title = title;
        this.content = content;
    }

    public int getTo_id() {
        return to_id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
