package com.creative.share.apps.heragelkashed.models;

import java.io.Serializable;

public class AdImageVideoModel implements Serializable {

    private boolean isVideo;
    private String uri;

    public AdImageVideoModel(boolean isVideo, String uri) {
        this.isVideo = isVideo;
        this.uri = uri;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
