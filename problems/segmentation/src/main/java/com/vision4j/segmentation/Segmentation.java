package com.vision4j.segmentation;

import com.vision4j.utils.Categories;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Basic contract that every segmentation implementations should fulfill
 */
public interface Segmentation {

    Categories getCategories();

    /**
     * Predicts on an image read from input stream. Useful for example for APIs
     * @param inputStream the stream from which the image should be read
     * @return the predicted category
     * @throws IOException if exception happens while reading the image from the stream
     */
    SegmentationResult segment(InputStream inputStream) throws IOException;

    /**
     * Predicts on an image read from file. Useful for testing and debugging
     * @param file the file from which the image should be read
     * @return
     * @throws IOException if exception happens while reading the image from the stream
     */
    SegmentationResult segment(File file) throws IOException;

}
