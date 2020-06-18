package com.creative.share.apps.heragelkashed.models;

import java.io.Serializable;

public class SliderImageVideoModel implements Serializable {
    private String endPoint;
    private boolean isVideo;

    public SliderImageVideoModel(String endPoint, boolean isVideo) {
        this.endPoint = endPoint;
        this.isVideo = isVideo;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public boolean isVideo() {
        return isVideo;
    }
}
