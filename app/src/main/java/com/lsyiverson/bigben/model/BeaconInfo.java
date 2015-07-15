package com.lsyiverson.bigben.model;

import java.io.Serializable;

public class BeaconInfo implements Serializable {
    String uuid;
    String title;
    String content;
    String imageUrl;

    public String getUuid() {
        return uuid;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
