package com.vision4j.segmentation;

import com.vision4j.utils.Categories;
import com.vision4j.utils.Category;

import java.awt.image.BufferedImage;

/**
 * A segmentation result is a matrix where for every pixel there is category predicted
 */
public class SegmentationResult {

    private BufferedImage bufferedImage;
    private Categories categories;

    public SegmentationResult(BufferedImage bufferedImage, Categories categories) {
        this.bufferedImage = bufferedImage;
        this.categories = categories;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public Category get(int i, int j) {
        return categories.get(bufferedImage.getRaster().getSample(i, j, 0));
    }

}
