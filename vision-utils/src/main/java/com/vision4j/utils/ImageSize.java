package com.vision4j.utils;

public class ImageSize {

    private int heigth;
    private int width;
    private int channels;

    public ImageSize(int heigth, int width, int channels) {
        this.heigth = heigth;
        this.width = width;
        this.channels = channels;
    }

    public int getHeight() {
        return heigth;
    }

    public int getWidth() {
        return width;
    }

    public int channels() {
        return channels;
    }
}
