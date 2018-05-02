package com.dev.nathan.testtcc.model;


import java.util.Date;

public class AttestantPost extends AttestantPostId {

    public String user_id, image_url, desc, image_thumb,danger,address;
    public Date dhUpload;

    public AttestantPost() {}

    public AttestantPost(String user_id, String image_url, String desc, String image_thumb, Date dhUpload, String danger, String address) {
        this.user_id = user_id;
        this.image_url = image_url;
        this.desc = desc;
        this.image_thumb = image_thumb;
        this.dhUpload= dhUpload;
        this.danger = danger;
        this.address = address;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDanger() {
        return danger;
    }

    public void setDanger(String danger) {
        this.danger = danger;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage_thumb() {
        return image_thumb;
    }

    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }

    public Date getDhUpload() {
        return dhUpload;
    }

    public void setDhUpload(Date dhUpload) {
        this.dhUpload = dhUpload;
    }


}
