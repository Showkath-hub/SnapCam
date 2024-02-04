package com.example.snapcam;

public class ImageItem {
    private String imageUrl;
    private int position;

    public ImageItem(String imageUrl, int position) {
        this.imageUrl = imageUrl;
        this.position = position;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getPosition() {
        return position;
    }
}
