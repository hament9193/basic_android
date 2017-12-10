package com.droidrank.sample.models;

/**
 * Created by hamentchoudhary on 24/11/17.
 */

public class ImageModel {
    String imageUrl;
    String imageDescription;
    public ImageModel(String imageUrl, String imageDescription) {
        this.imageUrl = imageUrl;
        this.imageDescription = imageDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }
}
