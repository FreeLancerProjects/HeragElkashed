package com.creative.share.apps.heragelkashed.models;

import java.io.Serializable;

public class MessageModel implements Serializable {

    private int id;
    private int room_id;
    private int  from_user_id;
    private int to_user_id;
    private int message_type;
    private int type;
    private String message;
    private String file_link;
    private int date;
    private int is_read;
    private String from_user_name;
    private String from_user_email;
    private String from_user_phone_code;
    private String from_user_phone;
    private String from_user_avatar;
    private String to_user_name;
    private String to_user_email;
    private String to_user_phone_code;
    private String to_user_phone;
    private String to_user_avatar;


    public MessageModel(int id, int room_id, int from_user_id, int to_user_id, int message_type, String message, String file_link, int date, int is_read, String from_user_name, String from_user_email, String from_user_phone_code, String from_user_phone, String from_user_avatar, String to_user_name, String to_user_email, String to_user_phone_code, String to_user_phone, String to_user_avatar) {
        this.id = id;
        this.room_id = room_id;
        this.from_user_id = from_user_id;
        this.to_user_id = to_user_id;
        this.message_type = message_type;
        this.message = message;
        this.file_link = file_link;
        this.date = date;
        this.is_read = is_read;
        this.from_user_name = from_user_name;
        this.from_user_email = from_user_email;
        this.from_user_phone_code = from_user_phone_code;
        this.from_user_phone = from_user_phone;
        this.from_user_avatar = from_user_avatar;
        this.to_user_name = to_user_name;
        this.to_user_email = to_user_email;
        this.to_user_phone_code = to_user_phone_code;
        this.to_user_phone = to_user_phone;
        this.to_user_avatar = to_user_avatar;
    }

    public int getId() {
        return id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public int getFrom_user_id() {
        return from_user_id;
    }

    public int getTo_user_id() {
        return to_user_id;
    }

    public int getMessage_type() {
        return message_type;
    }

    public String getMessage() {
        return message;
    }

    public String getFile_link() {
        return file_link;
    }

    public int getDate() {
        return date;
    }

    public int getIs_read() {
        return is_read;
    }

    public String getFrom_user_name() {
        return from_user_name;
    }

    public String getFrom_user_email() {
        return from_user_email;
    }

    public String getFrom_user_phone_code() {
        return from_user_phone_code;
    }

    public String getFrom_user_phone() {
        return from_user_phone;
    }

    public String getFrom_user_avatar() {
        return from_user_avatar;
    }

    public String getTo_user_name() {
        return to_user_name;
    }

    public String getTo_user_email() {
        return to_user_email;
    }

    public String getTo_user_phone_code() {
        return to_user_phone_code;
    }

    public String getTo_user_phone() {
        return to_user_phone;
    }

    public String getTo_user_avatar() {
        return to_user_avatar;
    }
}
