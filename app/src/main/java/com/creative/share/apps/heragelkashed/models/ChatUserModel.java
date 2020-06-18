package com.creative.share.apps.heragelkashed.models;

import java.io.Serializable;

public class ChatUserModel implements Serializable {

    private String name;
    private String image;
    private int id;
    private int room_id;
    private String phone_code;
    private String phone;


    public ChatUserModel(String name, String image, int id, int room_id, String phone_code, String phone) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.room_id = room_id;
        this.phone_code = phone_code;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public String getPhone() {
        return phone;
    }
}
