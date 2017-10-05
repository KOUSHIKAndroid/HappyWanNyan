package com.happywannyan.POJO;

/**
 * Created by su on 10/4/17.
 */

public class SetGetMessageDetailsPojo {
    String sender_id;
    String receiver_id;
    String message_id;
    String message_type;
    String message_type_code;
    String message_info;
    String message_attachment;
    String msg_lat;
    String msg_long;
    String url_location;
    String postedon;

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getMessage_type_code() {
        return message_type_code;
    }

    public void setMessage_type_code(String message_type_code) {
        this.message_type_code = message_type_code;
    }

    public String getMessage_info() {
        return message_info;
    }

    public void setMessage_info(String message_info) {
        this.message_info = message_info;
    }

    public String getMessage_attachment() {
        return message_attachment;
    }

    public void setMessage_attachment(String message_attachment) {
        this.message_attachment = message_attachment;
    }

    public String getMsg_lat() {
        return msg_lat;
    }

    public void setMsg_lat(String msg_lat) {
        this.msg_lat = msg_lat;
    }

    public String getMsg_long() {
        return msg_long;
    }

    public void setMsg_long(String msg_long) {
        this.msg_long = msg_long;
    }

    public String getUrl_location() {
        return url_location;
    }

    public void setUrl_location(String url_location) {
        this.url_location = url_location;
    }

    public String getPostedon() {
        return postedon;
    }

    public void setPostedon(String postedon) {
        this.postedon = postedon;
    }

    public String getUsersname() {
        return usersname;
    }

    public void setUsersname(String usersname) {
        this.usersname = usersname;
    }

    public String getUsersimage() {
        return usersimage;
    }

    public void setUsersimage(String usersimage) {
        this.usersimage = usersimage;
    }

    public String getTime_difference() {
        return time_difference;
    }

    public void setTime_difference(String time_difference) {
        this.time_difference = time_difference;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String usersname;
    String usersimage;
    String time_difference;
    String date;

}
