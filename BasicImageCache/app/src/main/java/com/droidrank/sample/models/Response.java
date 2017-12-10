package com.droidrank.sample.models;

import java.util.ArrayList;

/**
 * Created by hamentchoudhary on 24/11/17.
 */

public class Response {
    ArrayList<ImageModel> images;
    String success;
    String message;

    public Response(ArrayList<ImageModel> images, String success, String message) {
        this.images = images;
        this.success = success;
        this.message = message;
    }

    public ArrayList<ImageModel> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageModel> images) {
        this.images = images;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
