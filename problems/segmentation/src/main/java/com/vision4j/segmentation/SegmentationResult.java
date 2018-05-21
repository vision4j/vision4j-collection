package com.vision4j.segmentation;

import com.vision4j.utils.Categories;
import com.vision4j.utils.Category;

/**
 * A segmentation result is a matrix where for every pixel there is category predicted
 */
public class SegmentationResult {

    private byte[][] bytes;
    private Categories categories;

    public SegmentationResult(byte[][] bytes, Categories categories) {
        this.bytes = bytes;
        this.categories = categories;
    }

    public byte[][] getBytes() {
        return bytes;
    }

    public Category get(int i, int j) {
        return categories.get(bytes[i][j]);
    }

}
