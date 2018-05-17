package com.vision4j.segmentation;

/**
 * A segmentation result is a matrix where for every pixel there is category predicted
 */
public class SegmentationResult {

    private byte[][] bytes;

    public SegmentationResult(byte[][] bytes) {
        this.bytes = bytes;
    }

    public byte[][] getBytes() {
        return bytes;
    }

}
