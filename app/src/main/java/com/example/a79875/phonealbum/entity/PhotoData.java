package com.example.a79875.phonealbum.entity;

import java.io.Serializable;

public class PhotoData implements Serializable {
    private String photoUrl;

    public PhotoData(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
