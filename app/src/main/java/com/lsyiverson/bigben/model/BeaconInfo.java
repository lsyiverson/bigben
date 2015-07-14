package com.lsyiverson.bigben.model;

import java.io.Serializable;

public class BeaconInfo implements Serializable {
    String uuid;
    String title;
    String content;

    public String getUuid() {
        return uuid;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
